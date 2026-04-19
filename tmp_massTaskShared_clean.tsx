import { useEffect, useMemo, useRef, useState, type ChangeEvent } from "react";
import {
  adminAgentApi,
  massTaskApi,
  type MassExternalContact,
  type MassMessageFieldSpec,
  type MassGroup,
  type MassMessageTypeSpec,
  type MassTask,
  type MassTaskCreateRequest,
  type MassTaskDetail,
  type MassUploadSignatureResult,
  type MessageTemplate,
  type WecomAccount,
} from "../api/client";
import { useAuth } from "@/contexts/AuthContext";
import { Button } from "@/components/ui/button";
import { Dialog, DialogBody, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Input, Select, Textarea } from "@/components/ui/input";
import { MassTemplateStructurePreview } from "@/components/mass-template/MassTemplateStructurePreview";
import { MassTemplateTypeEditor } from "@/components/mass-template/MassTemplateTypeEditor";
import { MassTemplateCompositeEditor } from "@/components/mass-template/MassTemplateCompositeEditor";

export type TargetType = 1 | 2;

export const TARGET_TYPE_OPTIONS: Array<{ value: TargetType; label: string; helper: string }> = [
  { value: 1, label: "外部联系人", helper: "按企微账号 vid 集合查询圈人目标，不选则查全部" },
  { value: 2, label: "群聊", helper: "按企微账号 vid 集合查询圈群目标，不选则查全部" },
];

export const MESSAGE_TYPE_OPTIONS = [
  { value: 0, label: "文本" },
  { value: 1, label: "图片" },
  { value: 2, label: "文件" },
  { value: 3, label: "语音" },
  { value: 4, label: "视频" },
  { value: 5, label: "链接卡片" },
  { value: 6, label: "小程序" },
  { value: 7, label: "组合消息" },
];

const TARGET_PAGE_SIZE = 50;
const TEMPLATE_VARIABLE_OPTIONS = [{ value: "name", label: "name" }] as const;
const NAME_VARIABLE_PLACEHOLDER = "{{name}}";
const MEDIA_TEMPLATE_MSG_TYPES = new Set([1, 2, 3, 4]);

type UploadMediaType = "image" | "file" | "audio" | "video";

export type TemplateMediaItem = {
  key: string | null;
  url: string;
  filename: string;
  contentType?: string | null;
  size?: number | null;
};

type TemplateMediaContent = {
  text: string;
  items: TemplateMediaItem[];
};

type TemplateLinkContent = {
  url: string;
  title: string;
  content: string;
  imgurl: string;
};

type TemplateMiniAppContent = {
  desc: string;
  appName: string;
  title: string;
  weappIconUrl: string;
  coverUrl: string;
  pagepath: string;
  username: string;
  appid: string;
};

type TemplateCompositeItem = {
  msgType: number;
  templateContent: string;
};

type TemplateMediaRule = {
  mediaType: UploadMediaType;
  label: string;
  accept: string;
  hint: string;
  mimeTypes: Set<string>;
  extensions: Set<string>;
};

const TEMPLATE_MEDIA_RULES: Record<number, TemplateMediaRule> = {
  1: {
    mediaType: "image",
    label: "图片",
    accept: ".jpg,.jpeg,.png,.webp,.gif,image/jpeg,image/png,image/webp,image/gif",
    hint: "支持 jpg、png、webp、gif，可批量上传。",
    mimeTypes: new Set(["image/jpeg", "image/png", "image/webp", "image/gif"]),
    extensions: new Set([".jpg", ".jpeg", ".png", ".webp", ".gif"]),
  },
  2: {
    mediaType: "file",
    label: "文件",
    accept: ".pdf,.doc,.docx,.xls,.xlsx,.txt,.zip,application/pdf,application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,text/plain,application/zip",
    hint: "支持 pdf、doc、docx、xls、xlsx、txt、zip，可批量上传。",
    mimeTypes: new Set([
      "application/pdf",
      "application/msword",
      "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
      "application/vnd.ms-excel",
      "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
      "text/plain",
      "application/zip",
    ]),
    extensions: new Set([".pdf", ".doc", ".docx", ".xls", ".xlsx", ".txt", ".zip"]),
  },
  3: {
    mediaType: "audio",
    label: "语音",
    accept: ".mp3,.wav,.m4a,.aac,.amr,audio/mpeg,audio/wav,audio/mp4,audio/aac,audio/amr",
    hint: "支持 mp3、wav、m4a、aac、amr，可批量上传。",
    mimeTypes: new Set(["audio/mpeg", "audio/wav", "audio/mp4", "audio/aac", "audio/amr"]),
    extensions: new Set([".mp3", ".wav", ".m4a", ".aac", ".amr"]),
  },
  4: {
    mediaType: "video",
    label: "视频",
    accept: ".mp4,.mov,.avi,.mkv,video/mp4,video/quicktime,video/x-msvideo,video/x-matroska",
    hint: "支持 mp4、mov、avi、mkv，可批量上传。",
    mimeTypes: new Set(["video/mp4", "video/quicktime", "video/x-msvideo", "video/x-matroska"]),
    extensions: new Set([".mp4", ".mov", ".avi", ".mkv"]),
  },
};

function createEmptyTemplateMediaContent(): TemplateMediaContent {
  return { text: "", items: [] };
}

function createEmptyTemplateLinkContent(): TemplateLinkContent {
  return {
    url: "",
    title: "",
    content: "",
    imgurl: "",
  };
}

function createEmptyTemplateMiniAppContent(): TemplateMiniAppContent {
  return {
    desc: "",
    appName: "",
    title: "",
    weappIconUrl: "",
    coverUrl: "",
    pagepath: "",
    username: "",
    appid: "",
  };
}

function normalizeTemplateVariables(value?: string | null) {
  if (!value) return [];
  try {
    const parsed = JSON.parse(value);
    if (Array.isArray(parsed)) {
      return parsed.map((item) => String(item).trim()).filter(Boolean);
    }
  } catch {
    return value
      .split(/[\n,，]/)
      .map((item) => item.trim())
      .filter(Boolean);
  }
  return [];
}

export function formatTemplateVariables(value?: string | null) {
  const variables = normalizeTemplateVariables(value);
  if (variables.length === 0) return "--";
  return variables.join("、");
}

function isMediaTemplateMsgType(msgType?: number | null) {
  return MEDIA_TEMPLATE_MSG_TYPES.has(Number(msgType));
}

function supportsTemplateNameVariable(msgType?: number | null) {
  const value = Number(msgType ?? 0);
  return value !== 5 && value !== 6 && value !== 7;
}

function getTemplateMediaRule(msgType?: number | null) {
  return TEMPLATE_MEDIA_RULES[Number(msgType)];
}

function getFileExtension(filename: string) {
  const index = filename.lastIndexOf(".");
  return index >= 0 ? filename.slice(index).toLowerCase() : "";
}

function validateTemplateUploadFile(file: File, msgType: number) {
  const rule = getTemplateMediaRule(msgType);
  if (!rule) return null;
  const extension = getFileExtension(file.name);
  const contentType = (file.type || "").toLowerCase();
  if (rule.mimeTypes.has(contentType) || rule.extensions.has(extension)) {
    return null;
  }
  return `${rule.label}类型校验失败，当前文件 ${file.name} 不在允许范围内。`;
}

function parseTemplateMediaContent(raw?: string | null): TemplateMediaContent {
  if (!raw) return createEmptyTemplateMediaContent();
  try {
    const parsed = JSON.parse(raw) as {
      text?: unknown;
      items?: unknown;
      mediaList?: unknown;
    };
    const sourceItems = Array.isArray(parsed.items)
      ? parsed.items
      : Array.isArray(parsed.mediaList)
        ? parsed.mediaList
        : [];
    return {
      text: typeof parsed.text === "string" ? parsed.text : "",
      items: sourceItems
        .map<TemplateMediaItem | null>((item) => {
          if (!item || typeof item !== "object") return null;
          const candidate = item as Record<string, unknown>;
          const url = typeof candidate.url === "string" ? candidate.url.trim() : "";
          const filename = typeof candidate.filename === "string" ? candidate.filename.trim() : "";
          if (!url || !filename) return null;
          return {
            key: typeof candidate.key === "string" ? candidate.key : null,
            url,
            filename,
            contentType: typeof candidate.contentType === "string" ? candidate.contentType : null,
            size: typeof candidate.size === "number" ? candidate.size : null,
          } satisfies TemplateMediaItem;
        })
        .filter((item): item is TemplateMediaItem => !!item),
    };
  } catch {
    return { text: raw, items: [] };
  }
}

function parseTemplateLinkContent(raw?: string | null): TemplateLinkContent {
  if (!raw) return createEmptyTemplateLinkContent();
  try {
    const parsed = JSON.parse(raw) as Record<string, unknown>;
    return {
      url: typeof parsed.url === "string" ? parsed.url : "",
      title: typeof parsed.title === "string" ? parsed.title : "",
      content: typeof parsed.content === "string" ? parsed.content : "",
      imgurl: typeof parsed.imgurl === "string" ? parsed.imgurl : "",
    };
  } catch {
    return {
      ...createEmptyTemplateLinkContent(),
      content: raw,
    };
  }
}

function parseTemplateMiniAppContent(raw?: string | null): TemplateMiniAppContent {
  if (!raw) return createEmptyTemplateMiniAppContent();
  try {
    const parsed = JSON.parse(raw) as Record<string, unknown>;
    return {
      desc: typeof parsed.desc === "string" ? parsed.desc : "",
      appName: typeof parsed.appName === "string" ? parsed.appName : "",
      title: typeof parsed.title === "string" ? parsed.title : "",
      weappIconUrl: typeof parsed.weappIconUrl === "string" ? parsed.weappIconUrl : "",
      coverUrl: typeof parsed.coverUrl === "string" ? parsed.coverUrl : "",
      pagepath: typeof parsed.pagepath === "string" ? parsed.pagepath : "",
      username: typeof parsed.username === "string" ? parsed.username : "",
      appid: typeof parsed.appid === "string" ? parsed.appid : "",
    };
  } catch {
    return {
      ...createEmptyTemplateMiniAppContent(),
      desc: raw,
    };
  }
}

function parseTemplateCompositeContent(raw?: string | null): TemplateCompositeItem[] {
  if (!raw) return [];
  try {
    const parsed = JSON.parse(raw);
    if (!Array.isArray(parsed)) return [];
    return parsed
      .map<TemplateCompositeItem | null>((item) => {
        if (!item || typeof item !== "object") return null;
        const candidate = item as Record<string, unknown>;
        const msgType = Number(candidate.msgType);
        const templateContent = typeof candidate.templateContent === "string" ? candidate.templateContent : "";
        if (!Number.isFinite(msgType)) return null;
        return {
          msgType,
          templateContent,
        };
      })
      .filter((item): item is TemplateCompositeItem => !!item);
  } catch {
    return [];
  }
}

function validateTemplateCompositeContent(raw: string) {
  const trimmed = raw.trim();
  if (!trimmed) return "组合消息模板内容不能为空";

  let parsed: unknown;
  try {
    parsed = JSON.parse(trimmed);
  } catch {
    return "组合消息模板内容必须是合法 JSON 数组";
  }

  if (!Array.isArray(parsed) || parsed.length === 0) {
    return "组合消息模板内容必须是非空数组";
  }

  for (let index = 0; index < parsed.length; index += 1) {
    const item = parsed[index];
    if (!item || typeof item !== "object") {
      return `第 ${index + 1} 项必须是对象`;
    }
    const candidate = item as Record<string, unknown>;
    const msgType = Number(candidate.msgType);
    if (!Number.isInteger(msgType) || msgType < 0 || msgType > 6) {
      return `第 ${index + 1} 项的 msgType 仅支持 0-6`;
    }
    const templateContent = typeof candidate.templateContent === "string" ? candidate.templateContent.trim() : "";
    if (!templateContent) {
      return `第 ${index + 1} 项的 templateContent 不能为空`;
    }
  }

  return null;
}

function normalizeTemplateCompositeContent(raw: string) {
  return JSON.stringify(JSON.parse(raw.trim()));
}

function serializeTemplateContent(msgType: number, text: string, items: TemplateMediaItem[]) {
  if (!isMediaTemplateMsgType(msgType)) {
    return text;
  }
  return JSON.stringify({
    text,
    items: items.map((item) => ({
      key: item.key ?? undefined,
      url: item.url,
      filename: item.filename,
      contentType: item.contentType ?? undefined,
      size: item.size ?? undefined,
    })),
  });
}

function serializeTemplateLinkContent(link: TemplateLinkContent) {
  return JSON.stringify({
    url: link.url.trim(),
    title: link.title.trim(),
    content: link.content.trim(),
    imgurl: link.imgurl.trim(),
  });
}

function serializeTemplateMiniAppContent(app: TemplateMiniAppContent) {
  return JSON.stringify({
    desc: app.desc.trim(),
    appName: app.appName.trim(),
    title: app.title.trim(),
    weappIconUrl: app.weappIconUrl.trim(),
    coverUrl: app.coverUrl.trim(),
    pagepath: app.pagepath.trim(),
    username: app.username.trim(),
    appid: app.appid.trim(),
  });
}



export function formatTemplateContentPreview(template: MessageTemplate) {
  const msgType = Number(template.msgType ?? 0);
  if (msgType === 5) {
    const link = parseTemplateLinkContent(template.templateContent);
    return [link.title, link.content, link.url].filter(Boolean).join(" · ") || "链接卡片未配置内容";
  }
  if (msgType === 6) {
    const app = parseTemplateMiniAppContent(template.templateContent);
    return [app.title, app.appName, app.pagepath].filter(Boolean).join(" · ") || "小程序模板未配置内容";
  }
  if (msgType === 7) {
    const items = parseTemplateCompositeContent(template.templateContent);
    if (items.length === 0) {
      return "组合消息模板未配置内容";
    }
    const labels = items.slice(0, 3).map((item) => getMessageTypeLabel(item.msgType));
    const suffix = items.length > 3 ? ` 等 ${items.length} 项` : ` 共 ${items.length} 项`;
    return `组合消息：${labels.join("、")}${suffix}`;
  }
  if (!isMediaTemplateMsgType(msgType)) {
    return template.templateContent || "暂无内容";
  }
  const parsed = parseTemplateMediaContent(template.templateContent);
  const summary = `${getMessageTypeLabel(msgType)}素材 ${parsed.items.length} 个`;
  if (parsed.text.trim()) {
    return `${parsed.text.trim()} · ${summary}`;
  }
  if (parsed.items.length > 0) {
    return `${summary} · ${parsed.items.map((item) => item.filename).slice(0, 2).join("、")}`;
  }
  return `${getMessageTypeLabel(msgType)}模板未配置素材`;
}

function hasNamePlaceholder(content: string) {
  return content.includes(NAME_VARIABLE_PLACEHOLDER);
}

function appendNamePlaceholder(content: string) {
  if (hasNamePlaceholder(content)) return content;
  const trimmed = content.trim();
  if (!trimmed) return NAME_VARIABLE_PLACEHOLDER;
  return `${content}${content.endsWith("\n") ? "" : "\n"}${NAME_VARIABLE_PLACEHOLDER}`;
}

function hasLinkNamePlaceholder(link: TemplateLinkContent) {
  return [link.title, link.content].some(hasNamePlaceholder);
}

function appendLinkNamePlaceholder(link: TemplateLinkContent) {
  if (hasLinkNamePlaceholder(link)) return link;
  return {
    ...link,
    content: appendNamePlaceholder(link.content),
  };
}

function hasMiniAppNamePlaceholder(app: TemplateMiniAppContent) {
  return [app.desc, app.appName, app.title].some(hasNamePlaceholder);
}

function appendMiniAppNamePlaceholder(app: TemplateMiniAppContent) {
  if (hasMiniAppNamePlaceholder(app)) return app;
  return {
    ...app,
    desc: appendNamePlaceholder(app.desc),
  };
}

async function uploadFileBySignature(signature: MassUploadSignatureResult, file: File) {
  const response = await fetch(signature.uploadUrl, {
    method: signature.method || "PUT",
    headers: signature.headers ?? { "Content-Type": signature.contentType },
    body: file,
  });
  if (!response.ok) {
    throw new Error(`OSS 上传失败（HTTP ${response.status}）`);
  }
}

async function uploadImageToOss(file: File) {
  const signatureRes = await massTaskApi.getUploadSignature({
    mediaType: "image",
    filename: file.name,
    contentType: file.type || undefined,
    expiresIn: 600,
  });
  if (!signatureRes.ok || !signatureRes.data) {
    throw new Error(signatureRes.error ?? "获取图片上传签名失败");
  }
  await uploadFileBySignature(signatureRes.data, file);
  return signatureRes.data.url;
}

export const TASK_STATUS_LABELS: Record<number, string> = {
  0: "待发送",
  1: "发送中",
  2: "已完成",
  3: "已取消",
};

const DETAIL_STATUS_LABELS: Record<number, string> = {
  0: "失败",
  1: "成功",
};

type TaskFormState = {
  taskName: string;
  msgType: string;
  templateId: string;
  creator: string;
  sendTime: string;
  remark: string;
  content: string;
  imageMediaId: string;
  fileMediaId: string;
  audioMediaId: string;
  videoMediaId: string;
};

function createEmptyTaskForm(): TaskFormState {
  return {
    taskName: "",
    msgType: "0",
    templateId: "",
    creator: "",
    sendTime: "",
    remark: "",
    content: "",
    imageMediaId: "",
    fileMediaId: "",
    audioMediaId: "",
    videoMediaId: "",
  };
}

export function formatDateTime(value?: string | null) {
  if (!value) return "--";
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return value;
  return new Intl.DateTimeFormat("zh-CN", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
  }).format(date);
}

export function getMessageTypeLabel(msgType?: number | null) {
  return MESSAGE_TYPE_OPTIONS.find((item) => item.value === msgType)?.label ?? `类型 ${msgType ?? "--"}`;
}

export function getMessageTypeLabelFromSpecs(msgType: number | null | undefined, specs?: MassMessageTypeSpec[]) {
  if (msgType === null || msgType === undefined) return "--";
  const matched = specs?.find((item) => item.msgType === msgType);
  return matched?.label ?? getMessageTypeLabel(msgType);
}

export function getTaskTypeLabel(taskType?: number | null) {
  if (taskType === 1) return "外部联系人";
  if (taskType === 2) return "群聊";
  return "--";
}

function getTargetLabel(target: MassExternalContact | MassGroup, targetType: TargetType) {
  if (targetType === 1) {
    return String(target.nickname ?? target.realname ?? target.userId ?? target.id);
  }
  return String(target.nickname ?? target.roomId ?? target.id);
}

function getTargetMeta(target: MassExternalContact | MassGroup, targetType: TargetType) {
  if (targetType === 1) {
    return [target.userId, target.realname, target.ownerUserId, target.corpId].filter(Boolean).join(" / ");
  }
  return [target.roomId, target.createUserId, target.corpId].filter(Boolean).join(" / ");
}

function getAccountFilterOptions(accounts: WecomAccount[]) {
  const seen = new Set<string>();
  return accounts
    .map((account) => {
      const vid = account.vid?.trim();
      if (account.loginState !== "online" || !vid || seen.has(vid)) return null;
      seen.add(vid);
      return {
        value: vid,
        label: `${account.nickname || account.wxName || account.agentNo || "未命名账号"} · ${vid}`,
      };
    })
    .filter((item): item is { value: string; label: string } => !!item);
}

function mergeTargets(
  current: Array<MassExternalContact | MassGroup>,
  incoming: Array<MassExternalContact | MassGroup>,
) {
  const seen = new Set(current.map((item) => Number(item.id)));
  const merged = [...current];
  for (const item of incoming) {
    const id = Number(item.id);
    if (seen.has(id)) continue;
    seen.add(id);
    merged.push(item);
  }
  return merged;
}

function setNestedValue(target: Record<string, unknown>, path: string, value: unknown) {
  const segments = path.split(".").filter(Boolean);
  if (segments.length === 0) return;
  let cursor: Record<string, unknown> = target;
  for (let index = 0; index < segments.length - 1; index += 1) {
    const segment = segments[index];
    const existing = cursor[segment];
    if (!existing || typeof existing !== "object" || Array.isArray(existing)) {
      cursor[segment] = {};
    }
    cursor = cursor[segment] as Record<string, unknown>;
  }
  cursor[segments[segments.length - 1]] = value;
}

export function StatusBadge({
  label,
  tone,
}: {
  label: string;
  tone: "default" | "success" | "warning" | "danger";
}) {
  const toneClass =
    tone === "success"
      ? "bg-emerald-50 text-emerald-700"
      : tone === "warning"
        ? "bg-amber-50 text-amber-700"
        : tone === "danger"
          ? "bg-red-50 text-red-700"
          : "bg-zinc-100 text-zinc-700";
  return (
    <span className={`inline-flex items-center rounded-full px-2.5 py-1 text-[11px] font-medium ${toneClass}`}>
      {label}
    </span>
  );
}

function FileUploadInput({
  value,
  onChange,
  accept = "*/*",
  placeholder = "点击选择文件",
}: {
  value: string;
  onChange: (base64: string) => void;
  accept?: string;
  placeholder?: string;
}) {
  const [preview, setPreview] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const fileInputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    if (value && value.startsWith("data:image")) {
      setPreview(value);
    } else {
      setPreview(null);
    }
  }, [value]);

  function handleFileChange(event: React.ChangeEvent<HTMLInputElement>) {
    const file = event.target.files?.[0];
    if (!file) return;
    setLoading(true);
    const reader = new FileReader();
    reader.onload = (e) => {
      const result = e.target?.result as string;
      onChange(result);
      setLoading(false);
    };
    reader.onerror = () => setLoading(false);
    reader.readAsDataURL(file);
    if (fileInputRef.current) fileInputRef.current.value = "";
  }

  const isImage = accept.includes("image");

  return (
    <div className="space-y-2">
      <input
        ref={fileInputRef}
        type="file"
        accept={accept}
        onChange={handleFileChange}
        className="hidden"
      />
      <div
        className="flex cursor-pointer items-center justify-center rounded-xl border-2 border-dashed border-border bg-zinc-50/50 p-6 transition-colors hover:border-muted-foreground/40"
        onClick={() => fileInputRef.current?.click()}
      >
        {loading ? (
          <span className="text-sm text-muted-foreground">处理中...</span>
        ) : preview ? (
          isImage ? (
            <img src={preview} alt="预览" className="max-h-32 max-w-full rounded-lg object-contain" />
          ) : (
            <span className="text-sm text-muted-foreground">文件已选择</span>
          )
        ) : (
          <span className="text-sm text-muted-foreground">{placeholder}</span>
        )}
      </div>
      {preview && (
        <button
          type="button"
          onClick={(e) => { e.stopPropagation(); onChange(""); setPreview(null); }}
          className="text-xs text-muted-foreground underline underline-offset-4 hover:text-foreground"
        >
          清除文件
        </button>
      )}
    </div>
  );
}

export function TaskDetailDialog({
  taskId,
  onClose,
}: {
  taskId: number | null;
  onClose: () => void;
}) {
  const [task, setTask] = useState<MassTask | null>(null);
  const [details, setDetails] = useState<MassTaskDetail[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!taskId) return;
    let active = true;
    setLoading(true);
    setError(null);
    Promise.all([massTaskApi.getTask(taskId), massTaskApi.getTaskDetails(taskId)])
      .then(([taskRes, detailRes]) => {
        if (!active) return;
        if (!taskRes.ok) {
          setError(taskRes.error ?? "加载任务失败");
          return;
        }
        setTask(taskRes.data ?? null);
        setDetails(detailRes.ok ? detailRes.data ?? [] : []);
        if (!detailRes.ok) {
          setError(detailRes.error ?? "加载明细失败");
        }
      })
      .catch((err) => {
        if (!active) return;
        setError(err instanceof Error ? err.message : "加载任务失败");
      })
      .finally(() => {
        if (active) setLoading(false);
      });
    return () => {
      active = false;
    };
  }, [taskId]);

  return (
    <Dialog open={!!taskId} onClose={onClose} className="max-w-5xl rounded-[28px] border border-border/70">
      <DialogHeader>
        <DialogTitle>任务详情</DialogTitle>
      </DialogHeader>
      <DialogBody className="space-y-6">
        {loading ? (
          <div className="py-12 text-center text-sm text-muted-foreground">加载中...</div>
        ) : error ? (
          <div className="rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">{error}</div>
        ) : task ? (
          <>
            <div className="grid gap-4 md:grid-cols-4">
              <div className="rounded-2xl border border-border bg-white px-4 py-4">
                <div className="text-xs uppercase tracking-[0.18em] text-muted-foreground">任务名称</div>
                <div className="mt-2 text-sm font-medium text-foreground">{task.taskName || "--"}</div>
              </div>
              <div className="rounded-2xl border border-border bg-white px-4 py-4">
                <div className="text-xs uppercase tracking-[0.18em] text-muted-foreground">发送状态</div>
                <div className="mt-2 text-sm font-medium text-foreground">{TASK_STATUS_LABELS[task.sendStatus ?? 0] ?? "--"}</div>
              </div>
              <div className="rounded-2xl border border-border bg-white px-4 py-4">
                <div className="text-xs uppercase tracking-[0.18em] text-muted-foreground">发送进度</div>
                <div className="mt-2 text-sm font-medium text-foreground">
                  {task.successCount ?? 0} / {task.totalCount ?? 0}
                </div>
              </div>
              <div className="rounded-2xl border border-border bg-white px-4 py-4">
                <div className="text-xs uppercase tracking-[0.18em] text-muted-foreground">计划时间</div>
                <div className="mt-2 text-sm font-medium text-foreground">{formatDateTime(task.sendTime)}</div>
              </div>
            </div>

            <div className="grid gap-6 lg:grid-cols-[1.4fr_1fr]">
              <div className="rounded-2xl border border-border bg-white">
                <div className="border-b border-border px-5 py-4">
                  <h3 className="text-sm font-semibold text-foreground">接收明细</h3>
                </div>
                <div className="max-h-[360px] overflow-auto">
                  <table className="w-full text-sm">
                    <thead>
                      <tr className="border-b border-border bg-zinc-50/70 text-left text-xs uppercase tracking-[0.14em] text-muted-foreground">
                        <th className="px-4 py-3">对象</th>
                        <th className="px-4 py-3">状态</th>
                        <th className="px-4 py-3">发送时间</th>
                        <th className="px-4 py-3">结果</th>
                      </tr>
                    </thead>
                    <tbody>
                      {details.length === 0 ? (
                        <tr>
                          <td colSpan={4} className="px-4 py-8 text-center text-muted-foreground">暂无明细</td>
                        </tr>
                      ) : (
                        details.map((detail) => (
                          <tr key={detail.id} className="border-b border-border/70 last:border-b-0">
                            <td className="px-4 py-3 text-foreground">{detail.receiverName || detail.receiverId || "--"}</td>
                            <td className="px-4 py-3">
                              <StatusBadge
                                label={detail.isSent ? (DETAIL_STATUS_LABELS[detail.sendStatus ?? 0] ?? "已发送") : "未发送"}
                                tone={detail.isSent ? (detail.sendStatus === 1 ? "success" : "danger") : "default"}
                              />
                            </td>
                            <td className="px-4 py-3 text-muted-foreground">{formatDateTime(detail.sendTime)}</td>
                            <td className="px-4 py-3 text-muted-foreground">{detail.sendResult || "--"}</td>
                          </tr>
                        ))
                      )}
                    </tbody>
                  </table>
                </div>
              </div>

              <div className="rounded-2xl border border-border bg-white p-5">
                <h3 className="text-sm font-semibold text-foreground">任务摘要</h3>
                <div className="mt-4 space-y-3 text-sm">
                  <div className="flex items-center justify-between gap-4">
                    <span className="text-muted-foreground">目标类型</span>
                    <span className="text-foreground">{getTaskTypeLabel(task.taskType)}</span>
                  </div>
                  <div className="flex items-center justify-between gap-4">
                    <span className="text-muted-foreground">消息类型</span>
                    <span className="text-foreground">{getMessageTypeLabel(task.msgType)}</span>
                  </div>
                  <div className="flex items-center justify-between gap-4">
                    <span className="text-muted-foreground">创建人</span>
                    <span className="text-foreground">{task.creator || "--"}</span>
                  </div>
                  <div className="flex items-center justify-between gap-4">
                    <span className="text-muted-foreground">模板 ID</span>
                    <span className="text-foreground">{task.templateId ?? "--"}</span>
                  </div>
                  <div className="flex items-center justify-between gap-4">
                    <span className="text-muted-foreground">已发送</span>
                    <span className="text-foreground">{task.sentCount ?? 0}</span>
                  </div>
                  <div className="flex items-center justify-between gap-4">
                    <span className="text-muted-foreground">成功数</span>
                    <span className="text-foreground">{task.successCount ?? 0}</span>
                  </div>
                </div>
                <div className="mt-5 rounded-2xl bg-zinc-50 px-4 py-4 text-sm text-muted-foreground">
                  <div className="font-medium text-foreground">任务内容</div>
                  <div className="mt-2 whitespace-pre-wrap break-words">{task.content || task.remark || "暂无文本内容"}</div>
                </div>
              </div>
            </div>
          </>
        ) : null}
      </DialogBody>
      <DialogFooter>
        <Button variant="secondary" onClick={onClose}>关闭</Button>
      </DialogFooter>
    </Dialog>
  );
}

// COMPOSITE_TEMPLATE_HELPERS_START
type TemplateEditorState = {
  templateContent: string;
  mediaText: string;
  mediaItems: TemplateMediaItem[];
  link: TemplateLinkContent;
  miniApp: TemplateMiniAppContent;
};

type TemplateCompositeDraft = TemplateEditorState & {
  id: string;
  msgType: number;
};

const COMPOSITE_MESSAGE_TYPE_OPTIONS = MESSAGE_TYPE_OPTIONS.filter((option) => option.value >= 0 && option.value <= 6);

let templateCompositeDraftSequence = 0;

function nextTemplateCompositeDraftId() {
  templateCompositeDraftSequence += 1;
  return `composite-${Date.now()}-${templateCompositeDraftSequence}`;
}

function createEmptyTemplateEditorState(): TemplateEditorState {
  return {
    templateContent: "",
    mediaText: "",
    mediaItems: [],
    link: createEmptyTemplateLinkContent(),
    miniApp: createEmptyTemplateMiniAppContent(),
  };
}

function createTemplateCompositeDraft(msgType = 0, templateContent = ""): TemplateCompositeDraft {
  const state = createEmptyTemplateEditorState();
  if (isMediaTemplateMsgType(msgType)) {
    const parsed = parseTemplateMediaContent(templateContent);
    state.mediaText = parsed.text;
    state.mediaItems = parsed.items;
  } else if (msgType === 5) {
    state.link = parseTemplateLinkContent(templateContent);
  } else if (msgType === 6) {
    state.miniApp = parseTemplateMiniAppContent(templateContent);
  } else {
    state.templateContent = templateContent;
  }
  return {
    id: nextTemplateCompositeDraftId(),
    msgType,
    ...state,
  };
}

function parseTemplateCompositeDrafts(raw?: string | null) {
  return parseTemplateCompositeContent(raw).map((item) => createTemplateCompositeDraft(item.msgType, item.templateContent));
}

function serializeTemplateEditorState(msgType: number, state: TemplateEditorState) {
  if (msgType === 5) {
    return serializeTemplateLinkContent(state.link);
  }
  if (msgType === 6) {
    return serializeTemplateMiniAppContent(state.miniApp);
  }
  return serializeTemplateContent(
    msgType,
    isMediaTemplateMsgType(msgType) ? state.mediaText : state.templateContent,
    state.mediaItems,
  );
}

function validateTemplateEditorState(
  msgType: number,
  state: TemplateEditorState,
  options?: { requireTextContent?: boolean },
) {
  if (msgType === 5) {
    if (!state.link.url.trim() || !state.link.title.trim() || !state.link.content.trim() || !state.link.imgurl.trim()) {
      return "链接卡片必须完整填写网站地址、网站名称、描述内容和卡片封面";
    }
    return null;
  }
  if (msgType === 6) {
    const app = state.miniApp;
    if (
      !app.desc.trim() ||
      !app.appName.trim() ||
      !app.title.trim() ||
      !app.weappIconUrl.trim() ||
      !app.coverUrl.trim() ||
      !app.pagepath.trim() ||
      !app.username.trim() ||
      !app.appid.trim()
    ) {
      return "小程序模板必须完整填写内容、名称、标题、头像、封面图、跳转地址、原始 ID 和 AppID";
    }
    return null;
  }
  if (isMediaTemplateMsgType(msgType) && state.mediaItems.length === 0) {
    return "当前消息类型至少需要上传 1 个素材";
  }
  if ((options?.requireTextContent ?? false) && msgType === 0 && !state.templateContent.trim()) {
    return "文本消息内容不能为空";
  }
  return null;
}

function buildTemplateCompositePreview(items: TemplateCompositeDraft[]) {
  return items.map((item) => ({
    msgType: item.msgType,
    templateContent: serializeTemplateEditorState(item.msgType, item),
  }));
}

function validateTemplateCompositeDrafts(items: TemplateCompositeDraft[]) {
  if (items.length === 0) {
    return "组合消息模板至少添加 1 项";
  }
  for (let index = 0; index < items.length; index += 1) {
    const item = items[index];
    if (!Number.isInteger(item.msgType) || item.msgType < 0 || item.msgType > 6) {
      return `第 ${index + 1} 项的消息类型仅支持 0-6`;
    }
    const validationError = validateTemplateEditorState(item.msgType, item, { requireTextContent: true });
    if (validationError) {
      return `第 ${index + 1} 项：${validationError}`;
    }
    if (!serializeTemplateEditorState(item.msgType, item).trim()) {
      return `第 ${index + 1} 项内容不能为空`;
    }
  }
  return null;
}

function serializeTemplateCompositeDrafts(items: TemplateCompositeDraft[]) {
  return JSON.stringify(buildTemplateCompositePreview(items));
}
// COMPOSITE_TEMPLATE_HELPERS_END

function ensureTaskMessageTypeSpecs(specs: MassMessageTypeSpec[]) {
  if (specs.some((item) => item.msgType === 7)) {
    return [...specs].sort((a, b) => a.msgType - b.msgType);
  }
  return [
    ...specs,
    {
      msgType: 7,
      code: "composite",
      label: getMessageTypeLabel(7),
      storageSupported: true,
      executionSupported: true,
      requiredFields: [],
      optionalFields: [],
      notes: ["组合消息手动内容与模板内容均按 template_content 结构直接写入 content。"],
    },
  ].sort((a, b) => a.msgType - b.msgType);
}

export function TemplateDialog({
  open,
  template,
  onClose,
  onSubmit,
  saving,
}: {
  open: boolean;
  template: MessageTemplate | null;
  onClose: () => void;
  onSubmit: (body: Partial<MessageTemplate>) => Promise<void>;
  saving: boolean;
}) {
  const { user } = useAuth();
  const [form, setForm] = useState({
    templateName: "",
    templateContent: "",
    mediaText: "",
    mediaItems: [] as TemplateMediaItem[],
    link: createEmptyTemplateLinkContent(),
    miniApp: createEmptyTemplateMiniAppContent(),
    useNameVariable: true,
    msgType: "0",
  });
  const [compositeItems, setCompositeItems] = useState<TemplateCompositeDraft[]>([]);
  const [compositeAddMsgType, setCompositeAddMsgType] = useState(1);
  const [formError, setFormError] = useState<string | null>(null);
  const [uploading, setUploading] = useState(false);

  useEffect(() => {
    const msgType = Number(template?.msgType ?? 0);
    const supportsNameVariable = supportsTemplateNameVariable(msgType);
    const variables = normalizeTemplateVariables(template?.variables);
    const useNameVariable = supportsNameVariable && (variables.length === 0 ? !template : variables.includes("name"));
    const parsedMediaContent = parseTemplateMediaContent(template?.templateContent);
    const parsedLinkContent = parseTemplateLinkContent(template?.templateContent);
    const parsedMiniAppContent = parseTemplateMiniAppContent(template?.templateContent);
    const contentText = isMediaTemplateMsgType(msgType)
      ? parsedMediaContent.text
      : (template?.templateContent ?? "");

    setForm({
      templateName: template?.templateName ?? "",
      templateContent: !isMediaTemplateMsgType(msgType) && supportsNameVariable && useNameVariable
        ? appendNamePlaceholder(contentText)
        : !isMediaTemplateMsgType(msgType)
          ? contentText
          : "",
      mediaText: isMediaTemplateMsgType(msgType) && supportsNameVariable && useNameVariable
        ? appendNamePlaceholder(contentText)
        : isMediaTemplateMsgType(msgType)
          ? contentText
          : "",
      mediaItems: isMediaTemplateMsgType(msgType) ? parsedMediaContent.items : [],
      link: parsedLinkContent,
      miniApp: parsedMiniAppContent,
      useNameVariable,
      msgType: String(msgType),
    });
    setCompositeItems(msgType === 7 ? parseTemplateCompositeDrafts(template?.templateContent) : []);
    setCompositeAddMsgType(1);
    setFormError(null);
    setUploading(false);
  }, [template, open]);

  function updateCompositeItem(itemId: string, updater: (item: TemplateCompositeDraft) => TemplateCompositeDraft) {
    setCompositeItems((prev) => prev.map((item) => (item.id === itemId ? updater(item) : item)));
  }

  function createReplacedCompositeItem(itemId: string, msgType: number) {
    const nextItem = createTemplateCompositeDraft(msgType);
    return { ...nextItem, id: itemId };
  }

  async function uploadTemplateMediaFiles(msgType: number, files: File[]) {
    const rule = getTemplateMediaRule(msgType);
    if (!rule || files.length === 0) return [] as TemplateMediaItem[];

    const validationError = files.map((file) => validateTemplateUploadFile(file, msgType)).find(Boolean);
    if (validationError) {
      throw new Error(validationError);
    }

    const uploadedItems: TemplateMediaItem[] = [];
    for (const file of files) {
      const signatureRes = await massTaskApi.getUploadSignature({
        mediaType: rule.mediaType,
        filename: file.name,
        contentType: file.type || undefined,
        expiresIn: 600,
      });
      if (!signatureRes.ok || !signatureRes.data) {
        throw new Error(signatureRes.error ?? "获取上传签名失败");
      }
      await uploadFileBySignature(signatureRes.data, file);
      uploadedItems.push({
        key: signatureRes.data.key,
        url: signatureRes.data.url,
        filename: signatureRes.data.filename || file.name,
        contentType: signatureRes.data.contentType || file.type || null,
        size: file.size,
      });
    }
    return uploadedItems;
  }

  async function uploadTemplateCoverFile(file: File) {
    const validationError = validateTemplateUploadFile(file, 1);
    if (validationError) {
      throw new Error(validationError);
    }
    return uploadImageToOss(file);
  }

  async function handleSubmit() {
    const msgType = Number(form.msgType);
    const supportsNameVariable = supportsTemplateNameVariable(msgType);
    const content = isMediaTemplateMsgType(msgType) ? form.mediaText : form.templateContent;

    if (msgType === 7) {
      const compositeError = validateTemplateCompositeDrafts(compositeItems);
      if (compositeError) {
        setFormError(compositeError);
        return;
      }
      setFormError(null);
      await onSubmit({
        templateName: form.templateName,
        templateContent: serializeTemplateCompositeDrafts(compositeItems),
        variables: "",
        msgType,
        creator: user?.name ?? template?.creator ?? "",
      });
      return;
    }

    if (msgType === 5) {
      const link = form.link;
      if (!link.url.trim() || !link.title.trim() || !link.content.trim() || !link.imgurl.trim()) {
        setFormError("链接卡片必须完整填写网站地址、网站名称、描述内容和卡片封面");
        return;
      }
      setFormError(null);
      await onSubmit({
        templateName: form.templateName,
        templateContent: serializeTemplateLinkContent(link),
        variables: "",
        msgType,
        creator: user?.name ?? template?.creator ?? "",
      });
      return;
    }

    if (msgType === 6) {
      const app = form.miniApp;
      if (
        !app.desc.trim() ||
        !app.appName.trim() ||
        !app.title.trim() ||
        !app.weappIconUrl.trim() ||
        !app.coverUrl.trim() ||
        !app.pagepath.trim() ||
        !app.username.trim() ||
        !app.appid.trim()
      ) {
        setFormError("小程序模板必须完整填写内容、名称、标题、头像、封面图、跳转地址、原始 ID 和 AppID");
        return;
      }
      setFormError(null);
      await onSubmit({
        templateName: form.templateName,
        templateContent: serializeTemplateMiniAppContent(app),
        variables: "",
        msgType,
        creator: user?.name ?? template?.creator ?? "",
      });
      return;
    }

    if (supportsNameVariable && form.useNameVariable && !hasNamePlaceholder(content)) {
      setFormError("已选择变量 name，模板内容必须包含完整的 {{name}} 占位符");
      return;
    }
    if (supportsNameVariable && !form.useNameVariable && hasNamePlaceholder(content)) {
      setFormError("未选择变量 name 时，模板内容中不能包含 {{name}}");
      return;
    }
    if (isMediaTemplateMsgType(msgType) && form.mediaItems.length === 0) {
      setFormError("当前消息类型至少需要上传 1 个素材");
      return;
    }

    setFormError(null);
    await onSubmit({
      templateName: form.templateName,
      templateContent: serializeTemplateContent(msgType, content, form.mediaItems),
      variables: supportsNameVariable && form.useNameVariable ? JSON.stringify(["name"]) : "",
      msgType,
      creator: user?.name ?? template?.creator ?? "",
    });
  }

  async function handleSelectMediaFiles(event: ChangeEvent<HTMLInputElement>) {
    const msgType = Number(form.msgType);
    const files = Array.from(event.target.files ?? []);
    event.target.value = "";
    if (files.length === 0) return;

    setUploading(true);
    setFormError(null);
    try {
      const uploadedItems = await uploadTemplateMediaFiles(msgType, files);
      setForm((prev) => ({ ...prev, mediaItems: [...prev.mediaItems, ...uploadedItems] }));
    } catch (error) {
      setFormError(error instanceof Error ? error.message : "素材上传失败");
    } finally {
      setUploading(false);
    }
  }

  async function handleSelectCompositeMediaFiles(itemId: string, event: ChangeEvent<HTMLInputElement>) {
    const item = compositeItems.find((candidate) => candidate.id === itemId);
    const files = Array.from(event.target.files ?? []);
    event.target.value = "";
    if (!item || files.length === 0) return;

    setUploading(true);
    setFormError(null);
    try {
      const uploadedItems = await uploadTemplateMediaFiles(item.msgType, files);
      updateCompositeItem(itemId, (current) => ({
        ...current,
        mediaItems: [...current.mediaItems, ...uploadedItems],
      }));
    } catch (error) {
      setFormError(error instanceof Error ? error.message : "素材上传失败");
    } finally {
      setUploading(false);
    }
  }

  async function handleUploadMiniAppCover(event: ChangeEvent<HTMLInputElement>) {
    const file = event.target.files?.[0];
    event.target.value = "";
    if (!file) return;

    setUploading(true);
    setFormError(null);
    try {
      const url = await uploadTemplateCoverFile(file);
      setForm((prev) => ({
        ...prev,
        miniApp: {
          ...prev.miniApp,
          coverUrl: url,
        },
      }));
    } catch (error) {
      setFormError(error instanceof Error ? error.message : "封面上传失败");
    } finally {
      setUploading(false);
    }
  }

  async function handleUploadCompositeMiniAppCover(itemId: string, event: ChangeEvent<HTMLInputElement>) {
    const file = event.target.files?.[0];
    event.target.value = "";
    if (!file) return;

    setUploading(true);
    setFormError(null);
    try {
      const url = await uploadTemplateCoverFile(file);
      updateCompositeItem(itemId, (current) => ({
        ...current,
        miniApp: {
          ...current.miniApp,
          coverUrl: url,
        },
      }));
    } catch (error) {
      setFormError(error instanceof Error ? error.message : "封面上传失败");
    } finally {
      setUploading(false);
    }
  }

  const selectedMsgType = Number(form.msgType);
  const supportsNameVariable = supportsTemplateNameVariable(selectedMsgType);
  const mediaRule = getTemplateMediaRule(selectedMsgType);
  const currentStructurePreview = isMediaTemplateMsgType(selectedMsgType)
    ? { text: form.mediaText, items: form.mediaItems }
    : selectedMsgType === 5
      ? form.link
      : selectedMsgType === 6
        ? form.miniApp
        : selectedMsgType === 7
          ? buildTemplateCompositePreview(compositeItems)
          : form.templateContent;

  return (
    <Dialog open={open} onClose={onClose} className="max-w-2xl rounded-[28px] border border-border/70">
      <DialogHeader>
        <DialogTitle>{template ? "编辑模板" : "新建模板"}</DialogTitle>
      </DialogHeader>
      <DialogBody className="space-y-4">
        <div className="grid gap-4 md:grid-cols-2">
          <div>
            <label className="mb-1.5 block text-sm font-medium text-foreground">模板名称</label>
            <Input
              value={form.templateName}
              onChange={(event) => setForm((prev) => ({ ...prev, templateName: event.target.value }))}
              placeholder="例如：入群活动通知"
            />
          </div>
          <div>
            <label className="mb-1.5 block text-sm font-medium text-foreground">消息类型</label>
            <Select
              value={form.msgType}
              onChange={(event) => {
                const nextMsgType = Number(event.target.value);
                setForm((prev) => ({
                  ...prev,
                  msgType: String(nextMsgType),
                  useNameVariable: supportsTemplateNameVariable(nextMsgType) ? prev.useNameVariable : false,
                  templateContent: isMediaTemplateMsgType(nextMsgType) ? prev.templateContent : (prev.templateContent || prev.mediaText),
                  mediaText: isMediaTemplateMsgType(nextMsgType) ? (prev.mediaText || prev.templateContent) : prev.mediaText,
                }));
                setFormError(null);
              }}
              className="w-full"
            >
              {MESSAGE_TYPE_OPTIONS.map((option) => (
                <option key={option.value} value={option.value}>{option.label}</option>
              ))}
            </Select>
          </div>
        </div>

        {selectedMsgType === 7 ? (
          <MassTemplateCompositeEditor
            items={compositeItems}
            uploading={uploading}
            addMsgType={compositeAddMsgType}
            messageTypeOptions={COMPOSITE_MESSAGE_TYPE_OPTIONS}
            mediaRules={TEMPLATE_MEDIA_RULES}
            coverAccept={TEMPLATE_MEDIA_RULES[1].accept}
            onAddMsgTypeChange={setCompositeAddMsgType}
            onAddItem={() => {
              setCompositeItems((prev) => [...prev, createTemplateCompositeDraft(compositeAddMsgType)]);
              setFormError(null);
            }}
            onRemoveItem={(itemId) => {
              setCompositeItems((prev) => prev.filter((item) => item.id !== itemId));
              setFormError(null);
            }}
            onMoveItem={(itemId, direction) => {
              setCompositeItems((prev) => {
                const index = prev.findIndex((item) => item.id === itemId);
                if (index < 0) return prev;
                const nextIndex = index + direction;
                if (nextIndex < 0 || nextIndex >= prev.length) return prev;
                const next = [...prev];
                const [moved] = next.splice(index, 1);
                next.splice(nextIndex, 0, moved);
                return next;
              });
              setFormError(null);
            }}
            onChangeItemType={(itemId, msgType) => {
              setCompositeItems((prev) => prev.map((item) => (
                item.id === itemId ? createReplacedCompositeItem(itemId, msgType) : item
              )));
              setFormError(null);
            }}
            onTemplateContentChange={(itemId, value) => {
              updateCompositeItem(itemId, (current) => ({ ...current, templateContent: value }));
              if (formError) setFormError(null);
            }}
            onMediaTextChange={(itemId, value) => {
              updateCompositeItem(itemId, (current) => ({ ...current, mediaText: value }));
              if (formError) setFormError(null);
            }}
            onSelectMediaFiles={(itemId, event) => void handleSelectCompositeMediaFiles(itemId, event)}
            onRemoveMediaItem={(itemId, index) => {
              updateCompositeItem(itemId, (current) => ({
                ...current,
                mediaItems: current.mediaItems.filter((_, itemIndex) => itemIndex !== index),
              }));
              if (formError) setFormError(null);
            }}
            onLinkChange={(itemId, patch) => {
              updateCompositeItem(itemId, (current) => ({
                ...current,
                link: { ...current.link, ...patch },
              }));
              if (formError) setFormError(null);
            }}
            onMiniAppChange={(itemId, patch) => {
              updateCompositeItem(itemId, (current) => ({
                ...current,
                miniApp: { ...current.miniApp, ...patch },
              }));
              if (formError) setFormError(null);
            }}
            onUploadMiniAppCover={(itemId, event) => void handleUploadCompositeMiniAppCover(itemId, event)}
          />
        ) : (
          <MassTemplateTypeEditor
            selectedMsgType={selectedMsgType}
            form={form}
            uploading={uploading}
            mediaRule={mediaRule}
            coverAccept={TEMPLATE_MEDIA_RULES[1].accept}
            onMediaTextChange={(value) => {
              setForm((prev) => ({ ...prev, mediaText: value }));
              if (formError) setFormError(null);
            }}
            onTemplateContentChange={(value) => {
              setForm((prev) => ({ ...prev, templateContent: value }));
              if (formError) setFormError(null);
            }}
            onSelectMediaFiles={(event) => void handleSelectMediaFiles(event)}
            onRemoveMediaItem={(index) => {
              setForm((prev) => ({
                ...prev,
                mediaItems: prev.mediaItems.filter((_, itemIndex) => itemIndex !== index),
              }));
              if (formError) setFormError(null);
            }}
            onLinkChange={(patch) => {
              setForm((prev) => ({ ...prev, link: { ...prev.link, ...patch } }));
              if (formError) setFormError(null);
            }}
            onMiniAppChange={(patch) => {
              setForm((prev) => ({ ...prev, miniApp: { ...prev.miniApp, ...patch } }));
              if (formError) setFormError(null);
            }}
            onUploadMiniAppCover={(event) => void handleUploadMiniAppCover(event)}
          />
        )}

        <div className={`grid gap-4 ${supportsNameVariable ? "md:grid-cols-2" : "md:grid-cols-1"}`}>
          {supportsNameVariable && (
            <div>
              <label className="mb-1.5 block text-sm font-medium text-foreground">变量定义</label>
              <div className="rounded-2xl border border-border bg-zinc-50/50 p-4">
                <label className="flex cursor-pointer items-start gap-3">
                  <input
                    type="checkbox"
                    className="mt-0.5 h-4 w-4 accent-black"
                    checked={form.useNameVariable}
                    onChange={(event) => {
                      const checked = event.target.checked;
                      setForm((prev) => ({
                        ...prev,
                        useNameVariable: checked,
                        templateContent: checked ? appendNamePlaceholder(prev.templateContent) : prev.templateContent,
                        mediaText: checked ? appendNamePlaceholder(prev.mediaText) : prev.mediaText,
                        link: checked ? appendLinkNamePlaceholder(prev.link) : prev.link,
                        miniApp: checked ? appendMiniAppNamePlaceholder(prev.miniApp) : prev.miniApp,
                      }));
                      if (formError) setFormError(null);
                    }}
                  />
                  <div className="space-y-1">
                    <div className="text-sm font-medium text-foreground">使用变量 {TEMPLATE_VARIABLE_OPTIONS[0].label}</div>
                    <div className="text-xs leading-5 text-muted-foreground">
                      变量定义固定为枚举值 `name`。勾选后模板内容必须包含完整的 <code>{"{{name}}"}</code>；不勾选时模板内容不能出现它。
                    </div>
                  </div>
                </label>
              </div>
            </div>
          )}
          <div>
            <label className="mb-1.5 block text-sm font-medium text-foreground">创建人</label>
            <div className="flex h-10 items-center rounded-lg border border-border bg-zinc-50 px-3 text-sm text-muted-foreground">
              {user?.name || template?.creator || "--"}
            </div>
          </div>
        </div>

        {(isMediaTemplateMsgType(selectedMsgType) || selectedMsgType === 5 || selectedMsgType === 6 || selectedMsgType === 7) && (
          <MassTemplateStructurePreview value={currentStructurePreview} />
        )}
        {formError && <div className="rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">{formError}</div>}
      </DialogBody>
      <DialogFooter>
        <Button variant="secondary" onClick={onClose} disabled={saving || uploading}>取消</Button>
        <Button
          onClick={() => void handleSubmit()}
          disabled={saving || uploading || !form.templateName.trim()}
        >
          {saving ? "提交中..." : uploading ? "上传中..." : template ? "保存修改" : "创建模板"}
        </Button>
      </DialogFooter>
    </Dialog>
  );
}

export function TaskCreateDialog({
  open,
  accounts,
  templates,
  specs,
  onClose,
  onCreated,
}: {
  open: boolean;
  accounts: WecomAccount[];
  templates: MessageTemplate[];
  specs: MassMessageTypeSpec[];
  onClose: () => void;
  onCreated: (taskId: number | null) => Promise<void> | void;
}) {
  const [step, setStep] = useState<1 | 2 | 3 | 4>(1);
  const [targetType, setTargetType] = useState<TargetType>(1);
  const [selectedUserIds, setSelectedUserIds] = useState<string[]>([]);
  const [targets, setTargets] = useState<Array<MassExternalContact | MassGroup>>([]);
  const [targetsLoading, setTargetsLoading] = useState(false);
  const [targetError, setTargetError] = useState<string | null>(null);
  const [targetPageNum, setTargetPageNum] = useState(0);
  const [targetHasMore, setTargetHasMore] = useState(false);
  const [targetSearched, setTargetSearched] = useState(false);
  const [selectedTargetIds, setSelectedTargetIds] = useState<number[]>([]);
  const [taskForm, setTaskForm] = useState<TaskFormState>(createEmptyTaskForm());
  const [taskSubmitting, setTaskSubmitting] = useState(false);
  const [taskFeedback, setTaskFeedback] = useState<string | null>(null);
  const [taskManualEditor, setTaskManualEditor] = useState<TemplateEditorState>(createEmptyTemplateEditorState());
  const [taskCompositeItems, setTaskCompositeItems] = useState<TemplateCompositeDraft[]>([]);
  const [taskCompositeAddMsgType, setTaskCompositeAddMsgType] = useState(1);
  const [taskContentUploading, setTaskContentUploading] = useState(false);
  const [contentMode, setContentMode] = useState<"manual" | "template">("manual");
  const [fieldValues, setFieldValues] = useState<Record<string, string>>({});
  const [optionalExpanded, setOptionalExpanded] = useState(false);
  const [autoTrigger, setAutoTrigger] = useState(false);
  const [precheckErrors, setPrecheckErrors] = useState<string[]>([]);
  const [prechecking, setPrechecking] = useState(false);

  const [llmPrompt, setLlmPrompt] = useState("");
  const [llmLoading, setLlmLoading] = useState(false);
  const [llmError, setLlmError] = useState<string | null>(null);
  const [llmCandidates, setLlmCandidates] = useState<string[]>([]);

  const targetSearchInFlightRef = useRef(false);

  useEffect(() => {
    if (!open) return;
    setStep(1);
    setTargetType(1);
    setSelectedUserIds([]);
    setTargets([]);
    setTargetsLoading(false);
    setTargetError(null);
    setTargetPageNum(0);
    setTargetHasMore(false);
    setTargetSearched(false);
    setSelectedTargetIds([]);
    setTaskForm(createEmptyTaskForm());
    setTaskSubmitting(false);
    setTaskFeedback(null);
    setTaskManualEditor(createEmptyTemplateEditorState());
    setTaskCompositeItems([]);
    setTaskCompositeAddMsgType(1);
    setTaskContentUploading(false);
    setContentMode("manual");
    setFieldValues({});
    setOptionalExpanded(false);
    setAutoTrigger(false);
    setPrecheckErrors([]);
    setPrechecking(false);
    setLlmPrompt("");
    setLlmLoading(false);
    setLlmError(null);
    setLlmCandidates([]);
  }, [open]);

  const sortedSpecs = useMemo(() => ensureTaskMessageTypeSpecs(specs), [specs]);

  useEffect(() => {
    if (!open || sortedSpecs.length === 0) return;
    setTaskForm((prev) => {
      if (sortedSpecs.some((item) => String(item.msgType) === prev.msgType)) return prev;
      return { ...prev, msgType: String(sortedSpecs[0].msgType) };
    });
  }, [open, sortedSpecs]);

  const selectedMsgType = Number(taskForm.msgType);
  const selectedSpec = useMemo(
    () => sortedSpecs.find((item) => item.msgType === selectedMsgType),
    [sortedSpecs, selectedMsgType],
  );
  const requiredFields = selectedSpec?.requiredFields ?? [];
  const optionalFields = selectedSpec?.optionalFields ?? [];
  const isStructuredManualMsgType = selectedMsgType >= 0 && selectedMsgType <= 7;
  const manualTaskContent = contentMode === "manual" && isStructuredManualMsgType
    ? (
        selectedMsgType === 7
          ? serializeTemplateCompositeDrafts(taskCompositeItems)
          : serializeTemplateEditorState(selectedMsgType, taskManualEditor)
      )
    : taskForm.content;
  const manualTaskContentError = contentMode === "manual" && isStructuredManualMsgType
    ? (
        selectedMsgType === 7
          ? validateTemplateCompositeDrafts(taskCompositeItems)
          : validateTemplateEditorState(selectedMsgType, taskManualEditor, { requireTextContent: true })
      )
    : null;
  const manualTaskStructurePreview = selectedMsgType === 7
    ? buildTemplateCompositePreview(taskCompositeItems)
    : isMediaTemplateMsgType(selectedMsgType)
      ? { text: taskManualEditor.mediaText, items: taskManualEditor.mediaItems }
      : selectedMsgType === 5
        ? taskManualEditor.link
        : selectedMsgType === 6
          ? taskManualEditor.miniApp
          : taskManualEditor.templateContent;

  useEffect(() => {
    if (contentMode !== "manual" || !isStructuredManualMsgType) return;
    setTaskForm((prev) => (prev.content === manualTaskContent ? prev : { ...prev, content: manualTaskContent }));
  }, [contentMode, isStructuredManualMsgType, manualTaskContent]);

  const accountFilterOptions = useMemo(() => {
    const seen = new Set<string>();
    const options = accounts
      .map((account) => {
        const vid = account.vid?.trim();
        if (!vid || seen.has(vid)) return null;
        seen.add(vid);
        return {
          value: vid,
          label: `${account.nickname || account.wxName || account.agentNo || "未命名账号"} · ${vid}`,
          loginState: account.loginState,
        };
      });
    return options.filter((item): item is NonNullable<(typeof options)[number]> => !!item);
  }, [accounts]);

  async function fetchTargets(pageNum: number, append: boolean) {
    if (targetSearchInFlightRef.current) return;
    targetSearchInFlightRef.current = true;
    setTargetsLoading(true);
    setTargetError(null);
    try {
      const params = {
        userIdList: selectedUserIds.length > 0 ? selectedUserIds : undefined,
        pageNum,
        pageSize: TARGET_PAGE_SIZE,
      };
      const result = targetType === 1 ? await massTaskApi.listExternalContacts(params) : await massTaskApi.listGroups(params);
      if (!result.ok) throw new Error(result.error ?? "查询目标失败");
      const nextTargets = result.data ?? [];
      setTargets((prev) => (append ? mergeTargets(prev, nextTargets) : nextTargets));
      setTargetPageNum(pageNum);
      setTargetHasMore(nextTargets.length >= TARGET_PAGE_SIZE);
      setTargetSearched(true);
    } catch (error) {
      setTargetError(error instanceof Error ? error.message : "查询目标失败");
      if (!append) {
        setTargets([]);
        setTargetPageNum(0);
        setTargetHasMore(false);
      }
    } finally {
      targetSearchInFlightRef.current = false;
      setTargetsLoading(false);
    }
  }

  useEffect(() => {
    if (!open) return;
    setSelectedTargetIds([]);
    setTargets([]);
    setTargetPageNum(0);
    setTargetHasMore(false);
    setTargetSearched(false);
    const timer = setTimeout(() => {
      void fetchTargets(1, false);
    }, 280);
    return () => clearTimeout(timer);
  }, [open, targetType, selectedUserIds]);

  async function handleLoadMoreTargets() {
    if (!targetSearched || !targetHasMore || targetSearchInFlightRef.current) return;
    await fetchTargets(targetPageNum + 1, true);
  }

  function setFieldValue(path: string, value: string) {
    setFieldValues((prev) => ({ ...prev, [path]: value }));
  }

  function updateTaskCompositeItem(itemId: string, updater: (item: TemplateCompositeDraft) => TemplateCompositeDraft) {
    setTaskCompositeItems((prev) => prev.map((item) => (item.id === itemId ? updater(item) : item)));
  }

  function createReplacedTaskCompositeItem(itemId: string, msgType: number) {
    const nextItem = createTemplateCompositeDraft(msgType);
    return { ...nextItem, id: itemId };
  }

  async function uploadTaskManualMediaFiles(msgType: number, files: File[]) {
    const rule = getTemplateMediaRule(msgType);
    if (!rule || files.length === 0) return [] as TemplateMediaItem[];

    const validationError = files.map((file) => validateTemplateUploadFile(file, msgType)).find(Boolean);
    if (validationError) {
      throw new Error(validationError);
    }

    const uploadedItems: TemplateMediaItem[] = [];
    for (const file of files) {
      const signatureRes = await massTaskApi.getUploadSignature({
        mediaType: rule.mediaType,
        filename: file.name,
        contentType: file.type || undefined,
        expiresIn: 600,
      });
      if (!signatureRes.ok || !signatureRes.data) {
        throw new Error(signatureRes.error ?? "获取上传签名失败");
      }
      await uploadFileBySignature(signatureRes.data, file);
      uploadedItems.push({
        key: signatureRes.data.key,
        url: signatureRes.data.url,
        filename: signatureRes.data.filename || file.name,
        contentType: signatureRes.data.contentType || file.type || null,
        size: file.size,
      });
    }
    return uploadedItems;
  }

  async function uploadTaskManualCoverFile(file: File) {
    const validationError = validateTemplateUploadFile(file, 1);
    if (validationError) {
      throw new Error(validationError);
    }
    return uploadImageToOss(file);
  }

  async function handleSelectTaskManualMediaFiles(event: ChangeEvent<HTMLInputElement>) {
    const files = Array.from(event.target.files ?? []);
    event.target.value = "";
    if (files.length === 0) return;

    setTaskContentUploading(true);
    setTaskFeedback(null);
    try {
      const uploadedItems = await uploadTaskManualMediaFiles(selectedMsgType, files);
      setTaskManualEditor((prev) => ({ ...prev, mediaItems: [...prev.mediaItems, ...uploadedItems] }));
    } catch (error) {
      setTaskFeedback(error instanceof Error ? error.message : "素材上传失败");
    } finally {
      setTaskContentUploading(false);
    }
  }

  async function handleSelectTaskCompositeMediaFiles(itemId: string, event: ChangeEvent<HTMLInputElement>) {
    const item = taskCompositeItems.find((candidate) => candidate.id === itemId);
    const files = Array.from(event.target.files ?? []);
    event.target.value = "";
    if (!item || files.length === 0) return;

    setTaskContentUploading(true);
    setTaskFeedback(null);
    try {
      const uploadedItems = await uploadTaskManualMediaFiles(item.msgType, files);
      updateTaskCompositeItem(itemId, (current) => ({
        ...current,
        mediaItems: [...current.mediaItems, ...uploadedItems],
      }));
    } catch (error) {
      setTaskFeedback(error instanceof Error ? error.message : "素材上传失败");
    } finally {
      setTaskContentUploading(false);
    }
  }

  async function handleUploadTaskManualCover(event: ChangeEvent<HTMLInputElement>) {
    const file = event.target.files?.[0];
    event.target.value = "";
    if (!file) return;

    setTaskContentUploading(true);
    setTaskFeedback(null);
    try {
      const url = await uploadTaskManualCoverFile(file);
      setTaskManualEditor((prev) => ({
        ...prev,
        miniApp: {
          ...prev.miniApp,
          coverUrl: url,
        },
      }));
    } catch (error) {
      setTaskFeedback(error instanceof Error ? error.message : "封面上传失败");
    } finally {
      setTaskContentUploading(false);
    }
  }

  async function handleUploadTaskCompositeCover(itemId: string, event: ChangeEvent<HTMLInputElement>) {
    const file = event.target.files?.[0];
    event.target.value = "";
    if (!file) return;

    setTaskContentUploading(true);
    setTaskFeedback(null);
    try {
      const url = await uploadTaskManualCoverFile(file);
      updateTaskCompositeItem(itemId, (current) => ({
        ...current,
        miniApp: {
          ...current.miniApp,
          coverUrl: url,
        },
      }));
    } catch (error) {
      setTaskFeedback(error instanceof Error ? error.message : "封面上传失败");
    } finally {
      setTaskContentUploading(false);
    }
  }

  function buildPayload(): Record<string, unknown> {
    if (contentMode === "manual" && isStructuredManualMsgType) {
      return {};
    }
    const payload: Record<string, unknown> = {};
    const allFields = [...requiredFields, ...optionalFields];
    const typeByField = new Map(allFields.map((field) => [field.field, field.fieldType]));
    Object.entries(fieldValues).forEach(([path, rawValue]) => {
      const value = rawValue?.trim();
      if (!value) return;
      const fieldType = typeByField.get(path) ?? "string";
      const normalized = /number|int|float/.test(fieldType) ? Number(value) : value;
      setNestedValue(payload, path, normalized);
    });
    if (!payload.content && taskForm.content.trim()) {
      payload.content = taskForm.content.trim();
    }
    return payload;
  }

  function buildValidationBody(): MassTaskCreateRequest {
    return {
      taskName: taskForm.taskName.trim() || undefined,
      taskType: targetType,
      receiverType: targetType,
      receiverIds: selectedTargetIds,
      msgType: selectedMsgType,
      creator: taskForm.creator.trim() || undefined,
      sendTime: taskForm.sendTime || undefined,
      remark: taskForm.remark.trim() || undefined,
      templateId: contentMode === "template" && taskForm.templateId ? Number(taskForm.templateId) : undefined,
      payload: buildPayload(),
    };
  }

  function buildCreateBody() {
    const payload = buildPayload();
    const isStructuredManualContent = contentMode === "manual" && isStructuredManualMsgType;
    const mediaFromPayload = (payload.media as Record<string, unknown> | undefined)?.url as string | undefined;
    const body: Partial<MassTask> & { receiverType?: number; receiverIds?: number[]; payload?: Record<string, unknown> } = {
      taskName: taskForm.taskName.trim() || undefined,
      taskType: targetType,
      receiverType: targetType,
      receiverIds: selectedTargetIds,
      msgType: selectedMsgType,
      creator: taskForm.creator.trim() || undefined,
      sendTime: taskForm.sendTime || undefined,
      remark: taskForm.remark.trim() || undefined,
      templateId: contentMode === "template" && taskForm.templateId ? Number(taskForm.templateId) : undefined,
      content: isStructuredManualContent ? (manualTaskContent.trim() || undefined) : (payload.content as string | undefined) || undefined,
      payload: contentMode === "manual" && !isStructuredManualContent ? payload : undefined,
    };
    if (!isStructuredManualContent) {
      if (selectedMsgType === 1) body.imageMediaId = mediaFromPayload;
      if (selectedMsgType === 2) body.fileMediaId = mediaFromPayload;
      if (selectedMsgType === 3) body.audioMediaId = mediaFromPayload;
      if (selectedMsgType === 4) body.videoMediaId = mediaFromPayload;
      if (selectedMsgType === 5) {
        const link = payload.link as Record<string, unknown> | undefined;
        body.content = [link?.title, link?.content, link?.url].filter(Boolean).join("\\n");
      }
      if (selectedMsgType === 6) {
        const app = payload.app as Record<string, unknown> | undefined;
        body.content = [app?.title, app?.pagepath].filter(Boolean).join("\\n");
      }
    }
    return body;
  }

  const requiredMissing = contentMode === "manual" && isStructuredManualMsgType
    ? []
    : requiredFields.filter((field) => {
        const value = fieldValues[field.field];
        if (field.field === "payload.content") {
          return !(value?.trim() || taskForm.content.trim());
        }
        return !value?.trim();
      });

  const canMoveStep2 = selectedTargetIds.length > 0;
  const skipsPrecheck = contentMode === "template" || (contentMode === "manual" && isStructuredManualMsgType);
  const canMoveStep3 = contentMode === "template"
    ? !!taskForm.templateId
    : contentMode === "manual" && isStructuredManualMsgType
      ? !manualTaskContentError
      : requiredMissing.length === 0 && !(selectedSpec?.storageSupported === false);

  useEffect(() => {
    if (!skipsPrecheck) return;
    setPrecheckErrors([]);
  }, [skipsPrecheck]);

  async function handlePrecheck() {
    if (skipsPrecheck) {
      setPrecheckErrors([]);
      return true;
    }
    const request = buildValidationBody();
    setPrechecking(true);
    setPrecheckErrors([]);
    try {
      const result = await massTaskApi.validateTask(request);
      if (!result.ok) {
        setPrecheckErrors([result.error ?? "预检失败"]);
        return false;
      }
      const errors = result.data?.errors ?? [];
      setPrecheckErrors(errors);
      return errors.length === 0;
    } finally {
      setPrechecking(false);
    }
  }

  async function handleGenerateCopy() {
    if (!llmPrompt.trim()) {
      setLlmError("请输入文案需求");
      return;
    }
    setLlmLoading(true);
    setLlmError(null);
    setLlmCandidates([]);
    const msgLabel = getMessageTypeLabelFromSpecs(selectedMsgType, sortedSpecs);
    const prompt = [
      "你是招聘运营群发文案助手。请生成 3 条可直接发送的中文文案。",
      `消息类型：${msgLabel}`,
      `需求描述：${llmPrompt}`,
      "输出要求：仅输出 JSON 数组字符串，例如 [\\\"文案1\\\",\\\"文案2\\\",\\\"文案3\\\"]，不要额外说明。",
    ].join("\n");

    const result = await adminAgentApi.turn({ message: prompt });
    setLlmLoading(false);

    if (!result.ok || !result.data?.reply) {
      setLlmError(result.error ?? "文案生成失败");
      return;
    }

    const reply = result.data.reply.trim();
    const firstBracket = reply.indexOf("[");
    const lastBracket = reply.lastIndexOf("]");
    try {
      const jsonText = firstBracket >= 0 && lastBracket > firstBracket ? reply.slice(firstBracket, lastBracket + 1) : reply;
      const parsed = JSON.parse(jsonText);
      if (Array.isArray(parsed)) {
        const candidates = parsed.map((item) => String(item).trim()).filter(Boolean).slice(0, 5);
        if (candidates.length > 0) {
          setLlmCandidates(candidates);
          return;
        }
      }
      throw new Error("empty");
    } catch {
      setLlmCandidates([reply]);
    }
  }

  async function handleCreateTask() {
    if (selectedTargetIds.length === 0) {
      setTaskFeedback("请先选择至少一个发送目标");
      setStep(1);
      return;
    }
    const valid = skipsPrecheck ? true : await handlePrecheck();
    if (!valid) {
      setTaskFeedback("预检未通过，请先修正后再创建");
      setStep(4);
      return;
    }

    setTaskSubmitting(true);
    setTaskFeedback(null);
    const result = await massTaskApi.createTask(buildCreateBody(), {
      receiverIds: selectedTargetIds,
      receiverType: targetType,
    });
    setTaskSubmitting(false);

    if (!result.ok) {
      setTaskFeedback(result.error ?? "创建任务失败");
      return;
    }

    if (autoTrigger && result.data) {
      const triggerRes = await massTaskApi.triggerTask(result.data);
      if (!triggerRes.ok) {
        setTaskFeedback(`任务已创建（ID: ${result.data}），但触发失败：${triggerRes.error ?? "未知错误"}`);
      }
    }

    await onCreated(result.data ?? null);
    onClose();
  }

  function renderFieldInput(field: MassMessageFieldSpec) {
    const value = fieldValues[field.field] ?? "";
    const isLongText = field.fieldType === "textarea" || field.field.includes("content") || field.field.includes("desc");

    if (field.fieldType === "base64" || field.field.includes("base64") || field.field === "media.base64") {
      return (
        <div key={field.field}>
          <label className="mb-1.5 block text-sm font-medium text-foreground">{field.label}</label>
          <FileUploadInput
            value={value}
            onChange={(base64Data) => setFieldValue(field.field, base64Data)}
            accept={field.label?.includes("图片") ? "image/*" : field.label?.includes("语音") ? "audio/*" : "*/*"}
            placeholder={field.description || "点击选择文件，或拖拽文件到此处"}
          />
        </div>
      );
    }

    return (
      <div key={field.field}>
        <label className="mb-1.5 block text-sm font-medium text-foreground">{field.label}</label>
        {isLongText ? (
          <Textarea
            value={value}
            onChange={(event) => setFieldValue(field.field, event.target.value)}
            placeholder={field.description || field.field}
            className="min-h-[100px]"
          />
        ) : (
          <Input
            value={value}
            onChange={(event) => setFieldValue(field.field, event.target.value)}
            placeholder={field.description || field.field}
          />
        )}
        {field.description && <p className="mt-1 text-xs text-muted-foreground">{field.description}</p>}
      </div>
    );
  }

  function handleDialogClose() {
    if (taskSubmitting) return;
    onClose();
  }

  return (
    <Dialog open={open} onClose={handleDialogClose} className="max-w-6xl rounded-[28px] border border-border/70">
      <DialogHeader>
        <DialogTitle>创建群发任务</DialogTitle>
      </DialogHeader>

      <DialogBody className="space-y-5">
        <div className="flex flex-wrap items-center gap-2">
          {[
            [1, "目标与范围"],
            [2, "消息内容"],
            [3, "发送设置"],
            [4, "预检确认"],
          ].map(([index, label]) => (
            <button
              key={index}
              type="button"
              onClick={() => setStep(index as 1 | 2 | 3 | 4)}
              className={`rounded-full border px-3 py-1.5 text-xs ${step === index ? "border-black bg-black text-white" : "border-border text-muted-foreground"}`}
            >
              {index}. {label}
            </button>
          ))}
        </div>

        {step === 1 && (
          <section className="rounded-[24px] border border-border bg-zinc-50/40 p-5">
            <div className="grid gap-4 md:grid-cols-2">
              <div>
                <label className="mb-1.5 block text-sm font-medium text-foreground">目标类型</label>
                <Select className="w-full" value={String(targetType)} onChange={(event) => setTargetType(Number(event.target.value) as TargetType)}>
                  {TARGET_TYPE_OPTIONS.map((option) => (
                    <option key={option.value} value={option.value}>{option.label}</option>
                  ))}
                </Select>
              </div>
              <div>
                <label className="mb-1.5 block text-sm font-medium text-foreground">发送账号筛选（可不选）</label>
                <Select className="h-32 w-full py-2" multiple value={selectedUserIds} onChange={(event) => setSelectedUserIds(Array.from(event.target.selectedOptions, (option) => option.value))}>
                  {accountFilterOptions.map((account) => (
                    <option key={account.value} value={account.value}>{account.label}{account.loginState ? ` (${account.loginState})` : ""}</option>
                  ))}
                </Select>
                <p className="mt-1 text-xs text-muted-foreground">不选择账号时，会查询该目标类型下的全部可用对象。</p>
              </div>
            </div>

            {targetError && <div className="mt-4 rounded-2xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">{targetError}</div>}

            <div className="mt-4 flex items-center justify-between text-sm text-muted-foreground">
              <span>已选择 <span className="font-semibold text-foreground">{selectedTargetIds.length}</span> 个目标</span>
              <div className="flex items-center gap-3">
                <button type="button" className="text-xs underline underline-offset-4" onClick={() => setSelectedTargetIds([])}>清空</button>
                <button type="button" className="text-xs underline underline-offset-4" onClick={() => setSelectedTargetIds(targets.map((item) => Number(item.id)))}>全选当前结果</button>
              </div>
            </div>

            <div
              className="mt-4 max-h-[380px] overflow-auto rounded-[20px] border border-border bg-white"
              onScroll={(event) => {
                const element = event.currentTarget;
                if (element.scrollTop + element.clientHeight >= element.scrollHeight - 48) {
                  void handleLoadMoreTargets();
                }
              }}
            >
              {targets.length === 0 ? (
                <div className="px-6 py-12 text-center text-sm text-muted-foreground">{targetsLoading ? "自动查询中..." : "暂无目标，请调整筛选条件"}</div>
              ) : (
                <div className="divide-y divide-border">
                  {targets.map((target) => {
                    const id = Number(target.id);
                    const checked = selectedTargetIds.includes(id);
                    return (
                      <label key={`${targetType}-${id}`} className={`flex cursor-pointer items-start gap-4 px-5 py-4 ${checked ? "bg-zinc-50" : "hover:bg-zinc-50/70"}`}>
                        <input
                          type="checkbox"
                          className="mt-1 h-4 w-4 accent-black"
                          checked={checked}
                          onChange={(event) => {
                            if (event.target.checked) setSelectedTargetIds((prev) => (prev.includes(id) ? prev : [...prev, id]));
                            else setSelectedTargetIds((prev) => prev.filter((item) => item !== id));
                          }}
                        />
                        <div className="min-w-0 flex-1">
                          <div className="truncate text-sm font-medium text-foreground">{getTargetLabel(target, targetType)}</div>
                          <div className="mt-1 truncate text-xs text-muted-foreground">{getTargetMeta(target, targetType) || "无补充信息"}</div>
                        </div>
                        <div className="text-xs text-muted-foreground">#{id}</div>
                      </label>
                    );
                  })}
                  {targetHasMore && <div className="px-5 py-3 text-center text-xs text-muted-foreground">{targetsLoading ? "加载更多中..." : "滚动到底部自动加载"}</div>}
                </div>
              )}
            </div>
          </section>
        )}

        {step === 2 && (
          <section className="rounded-[24px] border border-border bg-white p-5">
            <div className="flex items-center gap-3">
              <button type="button" onClick={() => setContentMode("manual")} className={`rounded-full border px-3 py-1 text-xs ${contentMode === "manual" ? "border-black bg-black text-white" : "border-border text-muted-foreground"}`}>手动内容（默认）</button>
              <button type="button" onClick={() => setContentMode("template")} className={`rounded-full border px-3 py-1 text-xs ${contentMode === "template" ? "border-black bg-black text-white" : "border-border text-muted-foreground"}`}>使用模板</button>
            </div>

            <div className="mt-5 grid gap-4 md:grid-cols-2">
              <div>
                <label className="mb-1.5 block text-sm font-medium text-foreground">消息类型</label>
                <Select
                  className="w-full"
                  value={taskForm.msgType}
                  onChange={(event) => {
                    const nextMsgType = Number(event.target.value);
                    setTaskForm((prev) => ({ ...prev, msgType: event.target.value }));
                    setTaskManualEditor((prev) => ({
                      ...prev,
                      templateContent: isMediaTemplateMsgType(nextMsgType) ? (prev.templateContent || prev.mediaText) : prev.templateContent,
                      mediaText: isMediaTemplateMsgType(nextMsgType) ? (prev.mediaText || prev.templateContent) : prev.mediaText,
                    }));
                    setTaskFeedback(null);
                  }}
                >
                  {sortedSpecs.map((option) => (
                    <option key={option.msgType} value={option.msgType}>{option.label}</option>
                  ))}
                </Select>
              </div>
              {contentMode === "template" && (
                <div>
                  <label className="mb-1.5 block text-sm font-medium text-foreground">模板</label>
                  <Select className="w-full" value={taskForm.templateId} onChange={(event) => setTaskForm((prev) => ({ ...prev, templateId: event.target.value }))}>
                    <option value="">请选择模板</option>
                    {templates.map((template) => (
                      <option key={template.id} value={template.id}>{template.templateName || `模板 ${template.id}`}</option>
                    ))}
                  </Select>
                </div>
              )}
            </div>

            {contentMode === "manual" && (
              <>
                {isStructuredManualMsgType ? (
                  <div className="mt-5 space-y-4">
                    {selectedMsgType === 7 ? (
                      <MassTemplateCompositeEditor
                        items={taskCompositeItems}
                        uploading={taskContentUploading}
                        addMsgType={taskCompositeAddMsgType}
                        messageTypeOptions={COMPOSITE_MESSAGE_TYPE_OPTIONS}
                        mediaRules={TEMPLATE_MEDIA_RULES}
                        coverAccept={TEMPLATE_MEDIA_RULES[1].accept}
                        onAddMsgTypeChange={setTaskCompositeAddMsgType}
                        onAddItem={() => {
                          setTaskCompositeItems((prev) => [...prev, createTemplateCompositeDraft(taskCompositeAddMsgType)]);
                          setTaskFeedback(null);
                        }}
                        onRemoveItem={(itemId) => {
                          setTaskCompositeItems((prev) => prev.filter((item) => item.id !== itemId));
                          setTaskFeedback(null);
                        }}
                        onMoveItem={(itemId, direction) => {
                          setTaskCompositeItems((prev) => {
                            const index = prev.findIndex((item) => item.id === itemId);
                            if (index < 0) return prev;
                            const nextIndex = index + direction;
                            if (nextIndex < 0 || nextIndex >= prev.length) return prev;
                            const next = [...prev];
                            const [moved] = next.splice(index, 1);
                            next.splice(nextIndex, 0, moved);
                            return next;
                          });
                          setTaskFeedback(null);
                        }}
                        onChangeItemType={(itemId, msgType) => {
                          setTaskCompositeItems((prev) => prev.map((item) => (
                            item.id === itemId ? createReplacedTaskCompositeItem(itemId, msgType) : item
                          )));
                          setTaskFeedback(null);
                        }}
                        onTemplateContentChange={(itemId, value) => {
                          updateTaskCompositeItem(itemId, (current) => ({ ...current, templateContent: value }));
                          setTaskFeedback(null);
                        }}
                        onMediaTextChange={(itemId, value) => {
                          updateTaskCompositeItem(itemId, (current) => ({ ...current, mediaText: value }));
                          setTaskFeedback(null);
                        }}
                        onSelectMediaFiles={(itemId, event) => void handleSelectTaskCompositeMediaFiles(itemId, event)}
                        onRemoveMediaItem={(itemId, index) => {
                          updateTaskCompositeItem(itemId, (current) => ({
                            ...current,
                            mediaItems: current.mediaItems.filter((_, itemIndex) => itemIndex !== index),
                          }));
                          setTaskFeedback(null);
                        }}
                        onLinkChange={(itemId, patch) => {
                          updateTaskCompositeItem(itemId, (current) => ({
                            ...current,
                            link: { ...current.link, ...patch },
                          }));
                          setTaskFeedback(null);
                        }}
                        onMiniAppChange={(itemId, patch) => {
                          updateTaskCompositeItem(itemId, (current) => ({
                            ...current,
                            miniApp: { ...current.miniApp, ...patch },
                          }));
                          setTaskFeedback(null);
                        }}
                        onUploadMiniAppCover={(itemId, event) => void handleUploadTaskCompositeCover(itemId, event)}
                      />
                    ) : (
                      <MassTemplateTypeEditor
                        selectedMsgType={selectedMsgType}
                        form={taskManualEditor}
                        uploading={taskContentUploading}
                        mediaRule={getTemplateMediaRule(selectedMsgType)}
                        coverAccept={TEMPLATE_MEDIA_RULES[1].accept}
                        onMediaTextChange={(value) => {
                          setTaskManualEditor((prev) => ({ ...prev, mediaText: value }));
                          setTaskFeedback(null);
                        }}
                        onTemplateContentChange={(value) => {
                          setTaskManualEditor((prev) => ({ ...prev, templateContent: value }));
                          setTaskFeedback(null);
                        }}
                        onSelectMediaFiles={(event) => void handleSelectTaskManualMediaFiles(event)}
                        onRemoveMediaItem={(index) => {
                          setTaskManualEditor((prev) => ({
                            ...prev,
                            mediaItems: prev.mediaItems.filter((_, itemIndex) => itemIndex !== index),
                          }));
                          setTaskFeedback(null);
                        }}
                        onLinkChange={(patch) => {
                          setTaskManualEditor((prev) => ({
                            ...prev,
                            link: { ...prev.link, ...patch },
                          }));
                          setTaskFeedback(null);
                        }}
                        onMiniAppChange={(patch) => {
                          setTaskManualEditor((prev) => ({
                            ...prev,
                            miniApp: { ...prev.miniApp, ...patch },
                          }));
                          setTaskFeedback(null);
                        }}
                        onUploadMiniAppCover={(event) => void handleUploadTaskManualCover(event)}
                      />
                    )}

                    {selectedMsgType === 0 && (
                      <div className="rounded-[20px] border border-border bg-zinc-50/40 p-4">
                        <div className="text-sm font-medium text-foreground">AI 文案助手</div>
                        <div className="mt-3">
                          <Textarea
                            value={llmPrompt}
                            onChange={(event) => setLlmPrompt(event.target.value)}
                            placeholder="帮我想一条招聘群发文案，突出薪资和福利"
                            className="min-h-[80px]"
                          />
                        </div>
                        <div className="mt-3 flex items-center gap-3">
                          <Button size="sm" variant="secondary" onClick={handleGenerateCopy} disabled={llmLoading}>{llmLoading ? "生成中..." : "生成文案"}</Button>
                          {llmError && <span className="text-xs text-red-600">{llmError}</span>}
                        </div>
                        {llmCandidates.length > 0 && (
                          <div className="mt-3 space-y-2">
                            {llmCandidates.map((candidate, index) => (
                              <div key={`${index}-${candidate.slice(0, 16)}`} className="rounded-lg border border-border bg-white p-3 text-sm">
                                <div className="whitespace-pre-wrap break-words text-foreground">{candidate}</div>
                                <div className="mt-2">
                                  <button
                                    type="button"
                                    className="text-xs text-muted-foreground underline underline-offset-4"
                                    onClick={() => {
                                      setTaskManualEditor((prev) => ({ ...prev, templateContent: candidate }));
                                      setTaskFeedback(null);
                                    }}
                                  >
                                    使用这条文案
                                  </button>
                                </div>
                              </div>
                            ))}
                          </div>
                        )}
                      </div>
                    )}

                    <MassTemplateStructurePreview value={manualTaskStructurePreview} />
                  </div>
                ) : (
                  <>
                    <div className="mt-5 grid gap-4 md:grid-cols-2">
                      {requiredFields.map((field) => renderFieldInput(field))}
                    </div>

                    {optionalFields.length > 0 && (
                      <div className="mt-4">
                        <button type="button" className="text-xs text-muted-foreground underline underline-offset-4" onClick={() => setOptionalExpanded((prev) => !prev)}>
                          {optionalExpanded ? "收起可选字段" : `展开可选字段（${optionalFields.length}）`}
                        </button>
                        {optionalExpanded && <div className="mt-3 grid gap-4 md:grid-cols-2">{optionalFields.map((field) => renderFieldInput(field))}</div>}
                      </div>
                    )}

                    <div className="mt-6 rounded-[20px] border border-border bg-zinc-50/40 p-4">
                      <div className="text-sm font-medium text-foreground">AI 文案助手</div>
                      <div className="mt-3">
                        <Textarea
                          value={llmPrompt}
                          onChange={(event) => setLlmPrompt(event.target.value)}
                          placeholder="帮我想一条招募快递分拣员的文案，突出包吃住"
                          className="min-h-[80px]"
                        />
                      </div>
                      <div className="mt-3 flex items-center gap-3">
                        <Button size="sm" variant="secondary" onClick={handleGenerateCopy} disabled={llmLoading}>{llmLoading ? "生成中..." : "生成文案"}</Button>
                        {llmError && <span className="text-xs text-red-600">{llmError}</span>}
                      </div>
                      {llmCandidates.length > 0 && (
                        <div className="mt-3 space-y-2">
                          {llmCandidates.map((candidate, index) => (
                            <div key={`${index}-${candidate.slice(0, 16)}`} className="rounded-lg border border-border bg-white p-3 text-sm">
                              <div className="whitespace-pre-wrap break-words text-foreground">{candidate}</div>
                              <div className="mt-2">
                                <button type="button" className="text-xs text-muted-foreground underline underline-offset-4" onClick={() => {
                                  setFieldValue("payload.content", candidate);
                                  setTaskForm((prev) => ({ ...prev, content: candidate }));
                                }}>使用这条文案</button>
                              </div>
                            </div>
                          ))}
                        </div>
                      )}
                    </div>
                  </>
                )}
              </>
            )}
          </section>
        )}

        {step === 3 && (
          <section className="rounded-[24px] border border-border bg-white p-5">
            <div className="grid gap-4 md:grid-cols-2">
              <div>
                <label className="mb-1.5 block text-sm font-medium text-foreground">任务名称（可选）</label>
                <Input value={taskForm.taskName} onChange={(event) => setTaskForm((prev) => ({ ...prev, taskName: event.target.value }))} placeholder="例如：周一邀约批次" />
              </div>
              <div>
                <label className="mb-1.5 block text-sm font-medium text-foreground">创建人（可选）</label>
                <Input value={taskForm.creator} onChange={(event) => setTaskForm((prev) => ({ ...prev, creator: event.target.value }))} placeholder="例如：运营一组" />
              </div>
              <div>
                <label className="mb-1.5 block text-sm font-medium text-foreground">发送时间（可选）</label>
                <Input type="datetime-local" value={taskForm.sendTime} onChange={(event) => setTaskForm((prev) => ({ ...prev, sendTime: event.target.value }))} />
                <p className="mt-1 text-xs text-muted-foreground">留空默认为立即发送（按后端策略执行）。</p>
              </div>
              <div>
                <label className="mb-1.5 block text-sm font-medium text-foreground">备注（可选）</label>
                <Input value={taskForm.remark} onChange={(event) => setTaskForm((prev) => ({ ...prev, remark: event.target.value }))} placeholder="给协作同学的备注" />
              </div>
            </div>
            <label className="mt-4 flex items-center gap-2 text-sm text-foreground">
              <input type="checkbox" className="h-4 w-4 accent-black" checked={autoTrigger} onChange={(event) => setAutoTrigger(event.target.checked)} />
              创建后自动触发发送
            </label>
          </section>
        )}

        {step === 4 && (
          <section className="rounded-[24px] border border-border bg-white p-5">
            <div className="grid gap-3 text-sm md:grid-cols-2">
              <div className="rounded-xl bg-zinc-50 px-4 py-3">目标类型：{getTaskTypeLabel(targetType)}，目标数：<span className="font-semibold text-foreground">{selectedTargetIds.length}</span></div>
              <div className="rounded-xl bg-zinc-50 px-4 py-3">消息类型：{getMessageTypeLabelFromSpecs(selectedMsgType, sortedSpecs)}</div>
              <div className="rounded-xl bg-zinc-50 px-4 py-3">内容模式：{contentMode === "manual" ? "手动内容" : "模板"}</div>
              <div className="rounded-xl bg-zinc-50 px-4 py-3">自动触发：{autoTrigger ? "是" : "否"}</div>
            </div>

            {skipsPrecheck ? (
              <div className="mt-4 rounded-2xl border border-zinc-200 bg-zinc-50 px-4 py-3 text-sm text-muted-foreground">
                {contentMode === "template"
                  ? "当前使用模板，创建任务时跳过预检。"
                  : "当前手动内容已按模板结构直接写入 content，创建任务时跳过旧版 payload 预检。"}
              </div>
            ) : (
              <div className="mt-4 flex items-center gap-3">
              <Button variant="secondary" size="sm" onClick={() => void handlePrecheck()} disabled={prechecking}>{prechecking ? "预检中..." : "执行预检"}</Button>
              <span className="text-xs text-muted-foreground">预检会调用后端 validateTask，提前暴露字段问题。</span>
              </div>
            )}

            {!skipsPrecheck && precheckErrors.length > 0 && (
              <div className="mt-4 rounded-2xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
                <div className="font-medium">预检未通过</div>
                <ul className="mt-2 list-disc space-y-1 pl-4">
                  {precheckErrors.map((error, index) => <li key={`${error}-${index}`}>{error}</li>)}
                </ul>
              </div>
            )}

            {requiredMissing.length > 0 && contentMode === "manual" && !isStructuredManualMsgType && (
              <div className="mt-4 rounded-2xl border border-amber-200 bg-amber-50 px-4 py-3 text-sm text-amber-800">
                仍有必填字段未完成：{requiredMissing.map((field) => field.label).join("、")}
              </div>
            )}
            {manualTaskContentError && contentMode === "manual" && isStructuredManualMsgType && (
              <div className="mt-4 rounded-2xl border border-amber-200 bg-amber-50 px-4 py-3 text-sm text-amber-800">
                {manualTaskContentError}
              </div>
            )}
          </section>
        )}

        {taskFeedback && <div className="rounded-2xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">{taskFeedback}</div>}
      </DialogBody>

      <DialogFooter className="items-center justify-between">
        <div className="text-xs text-muted-foreground">消息类型优先来自 message-type-specs，手动内容补充支持本地组合消息 7。</div>
        <div className="flex items-center gap-3">
          <Button variant="secondary" onClick={handleDialogClose} disabled={taskSubmitting}>取消</Button>
          {step > 1 && <Button variant="secondary" onClick={() => setStep((prev) => (prev - 1) as 1 | 2 | 3 | 4)} disabled={taskSubmitting}>上一步</Button>}
          {step < 4 && (
            <Button
              onClick={() => {
                if (step === 1 && !canMoveStep2) {
                  setTaskFeedback("请至少选择 1 个发送目标");
                  return;
                }
                if (step === 2 && !canMoveStep3) {
                  setTaskFeedback(
                    contentMode === "template"
                      ? "请选择模板"
                      : isStructuredManualMsgType
                        ? (manualTaskContentError ?? "请先完成消息内容")
                        : "请先完成必填字段或调整消息类型",
                  );
                  return;
                }
                setTaskFeedback(null);
                setStep((prev) => (prev + 1) as 1 | 2 | 3 | 4);
              }}
            >下一步</Button>
          )}
          {step === 4 && (
            <Button onClick={() => void handleCreateTask()} disabled={taskSubmitting || prechecking}>{taskSubmitting ? "创建中..." : "创建任务"}</Button>
          )}
        </div>
      </DialogFooter>
    </Dialog>
  );
}

