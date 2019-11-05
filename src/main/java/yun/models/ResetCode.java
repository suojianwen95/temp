package yun.models;

import com.google.common.base.Objects;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="yun_reset_code")
public class ResetCode implements Serializable{

    public ResetCode(){
        this.created = new Date();
        this.status = true;
        this.valid = false;
        this.processed = false;
        this.token = "";
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    private String code;
    private Date created;
    private Boolean status;
    private Boolean valid;//是否valid，如果是valid才能够进入修改密码的页面
    private Boolean processed;//是否processed，如果是processed才能够处理修改密码的表单
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getProcessed() {
        return processed;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResetCode resetCode = (ResetCode) o;
        return Objects.equal(id, resetCode.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
