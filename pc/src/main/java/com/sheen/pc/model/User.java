package com.sheen.pc.model;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by zxj7044 on 2018-10-19.
 */
@Data
@ApiModel(value = "用户类", description = "用户查询条件")
public class User implements Serializable{
    @ApiModelProperty(value = "用户id")
    public Integer id;
    @ApiModelProperty(value = "姓名")
    public String name;
    @ApiModelProperty(value = "年龄")
    public Integer age;
    @ApiModelProperty(value = "性别")
    public Integer gender;
    @ApiModelProperty(value = "角色")
    public Integer role;
    @ApiModelProperty(value = "微信id")
    public String wxid;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", role=" + role +
                ", wxid='" + wxid + '\'' +
                '}';
    }
}
