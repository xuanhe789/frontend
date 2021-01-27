package com.xuanhe.prize.commons.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("响应报文")
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> implements Serializable {
    private static final long serialVersionUID=1L;
    @ApiModelProperty(value = "状态码（-1=异常，0=业务不成功，1=业务成功）",required = true)
    private Integer code;
    @ApiModelProperty(value = "信息",required = true)
    private String message;
    @ApiModelProperty(value = "数据")
    private T data;
    @ApiModelProperty(value = "服务器时间")
    @DateTimeFormat(pattern="yyyy/MM/dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date now = new Date();


    public Result(int i, String s, T o) {
        this.code=i;
        this.message=s;
        this.data=o;
    }
}
