package io.github.moyugroup.pojo.vo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

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
