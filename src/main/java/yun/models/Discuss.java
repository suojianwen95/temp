package yun.models;

import com.google.common.base.Objects;

import javax.persistence.*;
import java.util.Date;

/*******
 * 评论
 */
@Entity
@Table(name = "yun_discuss")
public class Discuss {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 只有Member(会员)能够发起评论
     */
    @ManyToOne
    @JoinColumn(name="member_id",nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name="soft_id",nullable = false)
    private Soft soft; // 评论的软件

    @Column(name="content", nullable = false)
    private String content; // 评论内容

    private Date created;
    private Date modified;
    private Boolean status;
    private Boolean enabled;
    private String remark;

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

    public Soft getSoft() {
        return soft;
    }

    public void setSoft(Soft soft) {
        this.soft = soft;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Discuss discuss = (Discuss) o;
        return Objects.equal(id, discuss.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Discuss{" +
                "id=" + id +
                ", member=" + member +
                ", soft=" + soft +
                ", content='" + content + '\'' +
                ", created=" + created +
                ", modified=" + modified +
                ", status=" + status +
                ", enabled=" + enabled +
                ", remark='" + remark + '\'' +
                '}';
    }
}
