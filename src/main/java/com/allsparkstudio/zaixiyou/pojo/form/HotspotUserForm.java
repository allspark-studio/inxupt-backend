package com.allsparkstudio.zaixiyou.pojo.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wzr
 * @date 2023/7/19
 */
@Data
@ApiModel("热点用户表单")
public class HotspotUserForm {
    @ApiModelProperty("当前页面")
    private Integer pageNum;

    @ApiModelProperty("最大容量")
    private Integer pageSize;

    @ApiModelProperty("排序方式")
    private Integer sortedBy;
}
