package yun.dto;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class MemberDto {

    @Pattern(regexp = "[a-zA-Z0-9_]*",message = "用户名请使用26英文字符,数字和下划线")
    @Size(min = 5,max = 30,message = "用户名需要使用5到30个字符")
    private String username;

    @Size(min = 2,max = 15,message = "姓名需要使用2到15个字符")
    private String nickname;

    @Email(message = "请使用合法的电子信箱地址")
    private String email;

    @Size(min = 11,max = 11,message = "请使用11位手机号")
    private String mobile;

    @Size(max=255,message = "密码不能超过255位")
    private String password;

    private String remark;

    private String type;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
