param(
    [Parameter(Mandatory = $true)]
    [string]$PropertiesPath,

    [Parameter(Mandatory = $true)]
    [string]$SqlPath
)

$ErrorActionPreference = 'Stop'

function Resolve-RepoPath {
    param([string]$Path)
    $resolved = Resolve-Path -LiteralPath $Path
    return $resolved.Path
}

function Read-Properties {
    param([string]$Path)
    $map = @{}
    foreach ($line in Get-Content -LiteralPath $Path) {
        $trimmed = $line.Trim()
        if (-not $trimmed -or $trimmed.StartsWith('#') -or $trimmed.StartsWith('!')) {
            continue
        }
        $idx = $trimmed.IndexOf('=')
        if ($idx -lt 0) {
            continue
        }
        $key = $trimmed.Substring(0, $idx).Trim()
        $value = $trimmed.Substring($idx + 1).Trim()
        $map[$key] = $value
    }
    return $map
}

$repoRoot = (Resolve-RepoPath (Join-Path $PSScriptRoot '..'))
$propertiesFile = Resolve-RepoPath $PropertiesPath
$sqlFile = Resolve-RepoPath $SqlPath

$props = Read-Properties -Path $propertiesFile
$jdbcUrl = $props['spring.datasource.url']
$jdbcUser = $props['spring.datasource.username']
$jdbcPassword = $props['spring.datasource.password']

if (-not $jdbcUrl -or -not $jdbcUser) {
    throw "Datasource settings not found in $propertiesFile"
}

$mysqlJar = Get-ChildItem -LiteralPath (Join-Path $repoRoot '.m2') -Recurse -Filter 'mysql-connector-java-*.jar' |
    Sort-Object FullName -Descending |
    Select-Object -First 1 -ExpandProperty FullName

if (-not $mysqlJar) {
    throw 'MySQL JDBC jar not found under .m2'
}

$tempDir = Join-Path $env:TEMP ('db-sync-' + [guid]::NewGuid().ToString('N'))
New-Item -ItemType Directory -Path $tempDir | Out-Null
$javaFile = Join-Path $tempDir 'SchemaSyncRunner.java'

$javaSource = @'
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SchemaSyncRunner {
    public static void main(String[] args) throws Exception {
        if (args.length != 4) {
            throw new IllegalArgumentException("usage: <jdbcUrl> <jdbcUser> <jdbcPassword> <sqlPath>");
        }
        String jdbcUrl = args[0];
        String jdbcUser = args[1];
        String jdbcPassword = args[2];
        String sql = Files.readString(Path.of(args[3]), StandardCharsets.UTF_8);

        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword)) {
            connection.setAutoCommit(true);
            for (String statementSql : splitStatements(sql)) {
                String normalized = statementSql.trim();
                if (normalized.isEmpty()) {
                    continue;
                }
                try (Statement statement = connection.createStatement()) {
                    statement.execute(normalized);
                    System.out.println("EXECUTED: " + preview(normalized));
                }
            }
        }
    }

    private static List<String> splitStatements(String sql) {
        List<String> statements = new ArrayList<>();
        String delimiter = ";";
        StringBuilder current = new StringBuilder();
        for (String rawLine : sql.split("\\R", -1)) {
            String line = rawLine;
            String trimmed = line.trim();
            if (trimmed.startsWith("--") || trimmed.startsWith("#")) {
                continue;
            }
            if (trimmed.regionMatches(true, 0, "DELIMITER ", 0, 10)) {
                String pending = current.toString().trim();
                if (!pending.isEmpty()) {
                    statements.add(pending);
                    current.setLength(0);
                }
                delimiter = trimmed.substring(10).trim();
                continue;
            }
            current.append(line).append('\n');
            if (endsWithDelimiter(current, delimiter)) {
                int end = delimiterStart(current, delimiter);
                String statement = current.substring(0, end).trim();
                if (!statement.isEmpty()) {
                    statements.add(statement);
                }
                current.setLength(0);
            }
        }
        String pending = current.toString().trim();
        if (!pending.isEmpty()) {
            statements.add(pending);
        }
        return statements;
    }

    private static boolean endsWithDelimiter(StringBuilder current, String delimiter) {
        return delimiterStart(current, delimiter) >= 0;
    }

    private static int delimiterStart(StringBuilder current, String delimiter) {
        int index = current.length() - 1;
        while (index >= 0 && Character.isWhitespace(current.charAt(index))) {
            index--;
        }
        if (index + 1 < delimiter.length()) {
            return -1;
        }
        int start = index + 1 - delimiter.length();
        for (int i = 0; i < delimiter.length(); i++) {
            if (current.charAt(start + i) != delimiter.charAt(i)) {
                return -1;
            }
        }
        return start;
    }

    private static String preview(String sql) {
        String oneLine = sql.replaceAll("\\s+", " ").trim();
        return oneLine.length() <= 120 ? oneLine : oneLine.substring(0, 117) + "...";
    }
}
'@

$utf8NoBom = New-Object System.Text.UTF8Encoding($false)
[System.IO.File]::WriteAllText($javaFile, $javaSource, $utf8NoBom)

try {
    & javac -encoding UTF-8 -cp $mysqlJar $javaFile
    if ($LASTEXITCODE -ne 0) {
        throw "javac failed with exit code $LASTEXITCODE"
    }

    & java -cp "$mysqlJar;$tempDir" SchemaSyncRunner $jdbcUrl $jdbcUser $jdbcPassword $sqlFile
    if ($LASTEXITCODE -ne 0) {
        throw "java failed with exit code $LASTEXITCODE"
    }
}
finally {
    if (Test-Path -LiteralPath $tempDir) {
        Remove-Item -LiteralPath $tempDir -Recurse -Force
    }
}
