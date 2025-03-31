package com.epoch.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "分页查询结果")
public class PageResult<T> implements Serializable {

    @Schema(description = "总记录数", example = "10")
    private long total; //总记录数

    @Schema(description = "当前页数据集合", example = "class")
    private List<T> records; //当前页数据集合

}