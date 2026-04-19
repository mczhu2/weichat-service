const fs = require("fs");

const filePath = "D:\\soft\\recruit-agent\\src\\apps\\admin-web\\src\\pages\\massTaskShared.tsx";
let source = fs.readFileSync(filePath, "utf8");

function replaceOnce(pattern, replacement, label) {
  const next = typeof pattern === "string" ? source.replace(pattern, replacement) : source.replace(pattern, replacement);
  if (next === source) {
    throw new Error(`replace failed: ${label}`);
  }
  source = next;
}

replaceOnce(
  `  const [taskSubmitting, setTaskSubmitting] = useState(false);
  const [taskFeedback, setTaskFeedback] = useState<string | null>(null);
  const [contentMode, setContentMode] = useState<"manual" | "template">("manual");`,
  `  const [taskSubmitting, setTaskSubmitting] = useState(false);
  const [taskFeedback, setTaskFeedback] = useState<string | null>(null);
  const [taskManualEditor, setTaskManualEditor] = useState<TemplateEditorState>(createEmptyTemplateEditorState());
  const [taskCompositeItems, setTaskCompositeItems] = useState<TemplateCompositeDraft[]>([]);
  const [taskCompositeAddMsgType, setTaskCompositeAddMsgType] = useState(1);
  const [taskContentUploading, setTaskContentUploading] = useState(false);
  const [contentMode, setContentMode] = useState<"manual" | "template">("manual");`,
  "task states",
);

replaceOnce(
  `    setTaskSubmitting(false);
    setTaskFeedback(null);
    setContentMode("manual");
    setFieldValues({});
    setOptionalExpanded(false);`,
  `    setTaskSubmitting(false);
    setTaskFeedback(null);
    setTaskManualEditor(createEmptyTemplateEditorState());
    setTaskCompositeItems([]);
    setTaskCompositeAddMsgType(1);
    setTaskContentUploading(false);
    setContentMode("manual");
    setFieldValues({});
    setOptionalExpanded(false);`,
  "task reset",
);

replaceOnce(
  `  const requiredFields = selectedSpec?.requiredFields ?? [];
  const optionalFields = selectedSpec?.optionalFields ?? [];
`,
  `  const requiredFields = selectedSpec?.requiredFields ?? [];
  const optionalFields = selectedSpec?.optionalFields ?? [];
  const isStructuredManualMsgType = selectedMsgType >= 0 && selectedMsgType <= 7;
  const manualTaskContent = contentMode === "manual" && isStructuredManualMsgType
    ? (selectedMsgType === 7
      ? serializeTemplateCompositeDrafts(taskCompositeItems)
      : serializeTemplateEditorState(selectedMsgType, taskManualEditor))
    : taskForm.content;
  const manualTaskContentError = contentMode === "manual" && isStructuredManualMsgType
    ? (selectedMsgType === 7
      ? validateTemplateCompositeDrafts(taskCompositeItems)
      : validateTemplateEditorState(selectedMsgType, taskManualEditor, { requireTextContent: true }))
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
`,
  "task derived state",
);

replaceOnce(
  `  function setFieldValue(path: string, value: string) {
    setFieldValues((prev) => ({ ...prev, [path]: value }));
  }

  function buildPayload(): Record<string, unknown> {`,
  `  function setFieldValue(path: string, value: string) {
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

  function buildPayload(): Record<string, unknown> {`,
  "task manual helpers",
);

replaceOnce(
  `  function buildPayload(): Record<string, unknown> {
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
  }`,
  `  function buildPayload(): Record<string, unknown> {
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
  }`,
  "buildPayload",
);

replaceOnce(
  `  function buildCreateBody() {
    const payload = buildPayload();
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
      content: (payload.content as string | undefined) || undefined,
      payload: contentMode === "manual" ? payload : undefined,
    };
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
    return body;
  }`,
  `  function buildCreateBody() {
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
  }`,
  "buildCreateBody",
);

replaceOnce(
  `  const requiredMissing = requiredFields.filter((field) => {
    const value = fieldValues[field.field];
    if (field.field === "payload.content") {
      return !(value?.trim() || taskForm.content.trim());
    }
    return !value?.trim();
  });

  const canMoveStep2 = selectedTargetIds.length > 0;
  const skipsPrecheck = contentMode === "template";
  const canMoveStep3 = contentMode === "template"
    ? !!taskForm.templateId
    : requiredMissing.length === 0 && !(selectedSpec?.storageSupported === false);`,
  `  const requiredMissing = contentMode === "manual" && isStructuredManualMsgType
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
      : requiredMissing.length === 0 && !(selectedSpec?.storageSupported === false);`,
  "step gating",
);

replaceOnce(
  /\{contentMode === "manual" && \([\s\S]*?\n            \)\}\n          <\/section>/,
  `{contentMode === "manual" && (
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
                            placeholder="帮我想一条招聘快递分拣员的文案，突出包吃住"
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
                              <div key={\`\${index}-\${candidate.slice(0, 16)}\`} className="rounded-lg border border-border bg-white p-3 text-sm">
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
                          {optionalExpanded ? "收起可选字段" : \`展开可选字段（\${optionalFields.length}）\`}
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
                          placeholder="帮我想一条招聘快递分拣员的文案，突出包吃住"
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
                            <div key={\`\${index}-\${candidate.slice(0, 16)}\`} className="rounded-lg border border-border bg-white p-3 text-sm">
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
          </section>`,
  "step2 manual section",
);

replaceOnce(
  `            {skipsPrecheck ? (
              <div className="mt-4 rounded-2xl border border-zinc-200 bg-zinc-50 px-4 py-3 text-sm text-muted-foreground">
                褰撳墠浣跨敤妯℃澘锛屽垱寤轰换鍔℃椂璺宠繃棰勬锛屼笉鍐嶆牎楠屾墜鍔ㄥ唴瀹瑰瓧娈点€?
              </div>
            ) : (`,
  `            {skipsPrecheck ? (
              <div className="mt-4 rounded-2xl border border-zinc-200 bg-zinc-50 px-4 py-3 text-sm text-muted-foreground">
                {contentMode === "template" ? "当前使用模板，创建任务时跳过预检。" : "当前消息内容已按模板格式直接写入 content，创建任务时跳过旧版 payload 预检。"}
              </div>
            ) : (`,
  "step4 precheck hint",
);

replaceOnce(
  `            {requiredMissing.length > 0 && contentMode === "manual" && (
              <div className="mt-4 rounded-2xl border border-amber-200 bg-amber-50 px-4 py-3 text-sm text-amber-800">
                浠嶆湁蹇呭～瀛楁鏈畬鎴愶細{requiredMissing.map((field) => field.label).join("銆?)}
              </div>
            )}`,
  `            {requiredMissing.length > 0 && contentMode === "manual" && !isStructuredManualMsgType && (
              <div className="mt-4 rounded-2xl border border-amber-200 bg-amber-50 px-4 py-3 text-sm text-amber-800">
                仍有必填字段未完成：{requiredMissing.map((field) => field.label).join("、")}
              </div>
            )}
            {manualTaskContentError && contentMode === "manual" && isStructuredManualMsgType && (
              <div className="mt-4 rounded-2xl border border-amber-200 bg-amber-50 px-4 py-3 text-sm text-amber-800">
                {manualTaskContentError}
              </div>
            )}`,
  "step4 warnings",
);

replaceOnce(
  `                if (step === 2 && !canMoveStep3) {
                  setTaskFeedback(contentMode === "template" ? "璇烽€夋嫨妯℃澘" : "璇峰厛瀹屾垚蹇呭～瀛楁鎴栬皟鏁存秷鎭被鍨?);
                  return;
                }`,
  `                if (step === 2 && !canMoveStep3) {
                  setTaskFeedback(
                    contentMode === "template"
                      ? "请选择模板"
                      : isStructuredManualMsgType
                        ? (manualTaskContentError ?? "请先完成消息内容")
                        : "请先完成必填字段或调整消息类型",
                  );
                  return;
                }`,
  "next step feedback",
);

fs.writeFileSync(filePath, source, "utf8");
console.log("patched task manual content");
