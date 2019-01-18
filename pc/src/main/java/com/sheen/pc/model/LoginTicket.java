package com.sheen.pc.model;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * Created by zxj7044 on 2018-11-9.
 */
@Data
@ApiModel(value = "登录ticket", description = "登录ticket")
public class LoginTicket implements Serializable{

    @ApiModelProperty(value = "ID")
    private Integer id;
    @ApiModelProperty(value = "用户ID")
    private Integer userid;
    @ApiModelProperty(value = "过期时间")
    private Date expired;
    @ApiModelProperty(value = "身份状态")
    private Integer status;
    @ApiModelProperty(value = "ticket")
    private String ticket;
}
