import { Button } from "@/components/ui/button";
import { Select } from "@/components/ui/input";
import { MassTemplateTypeEditor } from "./MassTemplateTypeEditor";

type TemplateMediaItem = {
  key: string | null;
  url: string;
  filename: string;
  contentType?: string | null;
  size?: number | null;
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

type TemplateMediaRule = {
  label: string;
  accept: string;
  hint: string;
};

type CompositeItem = {
  id: string;
  msgType: number;
  templateContent: string;
  mediaText: string;
  mediaItems: TemplateMediaItem[];
  link: TemplateLinkContent;
  miniApp: TemplateMiniAppContent;
};

export function MassTemplateCompositeEditor({
  items,
  uploading,
  addMsgType,
  messageTypeOptions,
  mediaRules,
  coverAccept,
  onAddMsgTypeChange,
  onAddItem,
  onRemoveItem,
  onMoveItem,
  onChangeItemType,
  onTemplateContentChange,
  onMediaTextChange,
  onSelectMediaFiles,
  onRemoveMediaItem,
  onLinkChange,
  onMiniAppChange,
  onUploadMiniAppCover,
}: {
  items: CompositeItem[];
  uploading: boolean;
  addMsgType: number;
  messageTypeOptions: Array<{ value: number; label: string }>;
  mediaRules: Record<number, TemplateMediaRule>;
  coverAccept: string;
  onAddMsgTypeChange: (msgType: number) => void;
  onAddItem: () => void;
  onRemoveItem: (itemId: string) => void;
  onMoveItem: (itemId: string, direction: -1 | 1) => void;
  onChangeItemType: (itemId: string, msgType: number) => void;
  onTemplateContentChange: (itemId: string, value: string) => void;
  onMediaTextChange: (itemId: string, value: string) => void;
  onSelectMediaFiles: (itemId: string, event: React.ChangeEvent<HTMLInputElement>) => void | Promise<void>;
  onRemoveMediaItem: (itemId: string, index: number) => void;
  onLinkChange: (itemId: string, patch: Partial<TemplateLinkContent>) => void;
  onMiniAppChange: (itemId: string, patch: Partial<TemplateMiniAppContent>) => void;
  onUploadMiniAppCover: (itemId: string, event: React.ChangeEvent<HTMLInputElement>) => void | Promise<void>;
}) {
  return (
    <div className="space-y-4">
      <div className="rounded-2xl border border-border bg-zinc-50/50 p-4">
        <div className="flex flex-col gap-3 md:flex-row md:items-center md:justify-between">
          <div>
            <div className="text-sm font-medium text-foreground">组合消息编排</div>
            <div className="mt-1 text-xs leading-5 text-muted-foreground">
              组合消息会按列表顺序依次发送，每一项的录入方式与单类型模板保持一致。
            </div>
          </div>
          <div className="flex flex-col gap-2 sm:flex-row sm:items-center">
            <Select
              value={String(addMsgType)}
              onChange={(event) => onAddMsgTypeChange(Number(event.target.value))}
              className="min-w-[140px]"
              disabled={uploading}
            >
              {messageTypeOptions.map((option) => (
                <option key={option.value} value={option.value}>{option.label}</option>
              ))}
            </Select>
            <Button
              type="button"
              onClick={onAddItem}
              disabled={uploading}
              className="min-w-[112px] whitespace-nowrap"
            >
              新增组合项
            </Button>
          </div>
        </div>
      </div>

      {items.length === 0 ? (
        <div className="rounded-2xl border border-dashed border-border bg-white px-4 py-10 text-center text-sm text-muted-foreground">
          尚未添加组合项，请先选择消息类型再新增。
        </div>
      ) : (
        items.map((item, index) => (
          <div key={item.id} className="rounded-[24px] border border-border bg-white p-5 shadow-sm">
            <div className="mb-4 flex flex-col gap-3 lg:flex-row lg:items-center lg:justify-between">
              <div>
                <div className="text-sm font-medium text-foreground">第 {index + 1} 项</div>
                <div className="mt-1 text-xs text-muted-foreground">
                  按当前顺序发送，可随时切换消息类型或调整顺序。
                </div>
              </div>
              <div className="flex flex-wrap items-center gap-2">
                <Select
                  value={String(item.msgType)}
                  onChange={(event) => onChangeItemType(item.id, Number(event.target.value))}
                  className="min-w-[140px]"
                  disabled={uploading}
                >
                  {messageTypeOptions.map((option) => (
                    <option key={option.value} value={option.value}>{option.label}</option>
                  ))}
                </Select>
                <Button type="button" variant="secondary" onClick={() => onMoveItem(item.id, -1)} disabled={uploading || index === 0}>
                  上移
                </Button>
                <Button type="button" variant="secondary" onClick={() => onMoveItem(item.id, 1)} disabled={uploading || index === items.length - 1}>
                  下移
                </Button>
                <Button type="button" variant="secondary" onClick={() => onRemoveItem(item.id)} disabled={uploading}>
                  删除
                </Button>
              </div>
            </div>

            <MassTemplateTypeEditor
              selectedMsgType={item.msgType}
              form={item}
              uploading={uploading}
              mediaRule={mediaRules[item.msgType]}
              coverAccept={coverAccept}
              onTemplateContentChange={(value) => onTemplateContentChange(item.id, value)}
              onMediaTextChange={(value) => onMediaTextChange(item.id, value)}
              onSelectMediaFiles={(event) => void onSelectMediaFiles(item.id, event)}
              onRemoveMediaItem={(mediaIndex) => onRemoveMediaItem(item.id, mediaIndex)}
              onLinkChange={(patch) => onLinkChange(item.id, patch)}
              onMiniAppChange={(patch) => onMiniAppChange(item.id, patch)}
              onUploadMiniAppCover={(event) => void onUploadMiniAppCover(item.id, event)}
            />
          </div>
        ))
      )}
    </div>
  );
}
