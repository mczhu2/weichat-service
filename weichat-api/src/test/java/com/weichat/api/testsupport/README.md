# Customer Reply Manual Runner

Location:
- `weichat-api/src/test/java/com/weichat/api/testsupport/CustomerReplyServiceManualRunner.java`
- `weichat-api/src/test/java/com/weichat/api/testsupport/run-manual-runner.ps1`
- `weichat-api/src/test/java/com/weichat/api/testsupport/DownstreamMessageContentManualRunner.java`
- `weichat-api/src/test/java/com/weichat/api/testsupport/run-downstream-media-check.ps1`

Purpose:
- Manual validation for `CustomerReplyService.sendReplyToCustomer`
- Manual validation for base64 voice upload and send flow
- Manual validation for `DownstreamMessageContentService.resolveCallbackPayload`
- Keep test-only runner outside `src/main/java` so it does not affect Spring Boot startup

Runtime prerequisites:
- JDK with `javac` and `java`
- Windows PowerShell
- Existing project build output under `weichat-api/target`
- `weichat-api/target/runtime-classpath.txt` must exist

Available modes:
- `all`: text + image + remote voice URL
- `text`: text only
- `image`: image only
- `voice`: remote voice URL only
- `voice_base64`: base64 voice through formal `sendReplyToCustomer`
- `voice_base64_temp`: base64 voice -> temp mp3 -> curl upload -> direct `SendCDNVoiceMsg`

Recommended usage:
```powershell
powershell -ExecutionPolicy Bypass -File .\weichat-api\src\test\java\com\weichat\api\testsupport\run-manual-runner.ps1 -Mode voice_base64
```

Pass a custom base64 payload:
```powershell
powershell -ExecutionPolicy Bypass -File .\weichat-api\src\test\java\com\weichat\api\testsupport\run-manual-runner.ps1 -Mode voice_base64_temp -Base64File .\runlogs\sample-tone.base64.txt
```

Check whether media URLs can be resolved for image/video callback records:
```powershell
powershell -ExecutionPolicy Bypass -File .\weichat-api\src\test\java\com\weichat\api\testsupport\run-downstream-media-check.ps1
```

Pass explicit `wx_message_info.id` values:
```powershell
powershell -ExecutionPolicy Bypass -File .\weichat-api\src\test\java\com\weichat\api\testsupport\run-downstream-media-check.ps1 -MessageIds 510,512
```

Notes:
- The runner uses test record `wx_message_info.id=502`
- The runner reuses receiver context from the database
- The downstream media checker defaults to `wx_message_info.id=510` and `512`
- If scheduled tasks print unrelated SQL errors during startup, they do not necessarily mean the manual reply flow failed
