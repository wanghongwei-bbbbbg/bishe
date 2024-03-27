package com.southwind.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * 校验用的注册表单，进行非空校验；前端用的是required
 */
@Data
public class UserRegisterForm {
    @NotEmpty(message = "登录名不能为空")
    private String loginName;
    @NotEmpty(message = "用户名不能为空")
    private String userName;
    @NotEmpty(message = "密码不能为空")
    private String password;
    @NotNull(message = "性别不能为空")
    private Integer gender;
    @NotEmpty(message = "邮箱不能为空")
    private String email;
    @NotEmpty(message = "手机不能为空")
    private String mobile;
}
