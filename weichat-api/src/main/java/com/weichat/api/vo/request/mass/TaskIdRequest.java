package com.weichat.api.vo.request.mass;

import com.weichat.api.vo.request.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 群发任务ID请求
 *
 * @author weichat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "群发任务ID请求参数")
public class TaskIdRequest extends BaseRequest {

    @ApiModelProperty(value = "群发任务ID", required = true, example = "task_id_xxx")
    private String taskId;
}
