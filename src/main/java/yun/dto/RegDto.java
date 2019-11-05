package yun.dto;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RegDto {
    @Pattern(regexp = "[a-zA-Z0-9_]*", message = "用户名请使用26英文字符,数字和下划线")
    @Size(min = 5, max = 30, message = "用户名需要使用5到30个字符")
    private String username;

    @NotBlank(message = "请填写真实姓名")
    private String nickname;

    @NotBlank(message = "请填写邮箱地址")
    @Email(message = "请输入正确的邮箱")
    private String email;
    private String mobile;
    @Size(min = 6, max = 255, message = "请输入6位以上密码")
    private String password;


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}