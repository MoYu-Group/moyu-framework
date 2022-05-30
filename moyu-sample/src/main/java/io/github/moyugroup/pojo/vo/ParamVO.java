package io.github.moyugroup.pojo.vo;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 入参
 * <p>
 * Created by fanfan on 2022/05/30.
 */
@Data
public class ParamVO {

    @NotBlank
    private String name;

    @NotBlank
    private String age;

    @Max(5)
    @Min(2)
    private String address;

    @NotNull
    private String gender;
}
