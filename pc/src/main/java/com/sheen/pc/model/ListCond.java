package com.sheen.pc.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by zxj7044 on 2018-11-7.
 */
@Data
@ApiModel(value = "公共查询参数")
public class ListCond {

    @ApiModelProperty(value = "页长", required = true)
    @NotNull
    protected Integer pageSize;

    @ApiModelProperty(value = "页码", required = true)
    @NotNull
    protected Integer pageNum;


}
