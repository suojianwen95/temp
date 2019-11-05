package yun.models;

import com.google.common.base.Objects;

import javax.persistence.*;
import java.util.Date;

/*******
 * 软件信息
 */
@Entity
@Table(name = "yun_soft")
public class Soft {

    public Soft(){
        this.created = new Date();
        this.modified= this.created;
        this.status  = true;
        this.enabled = true;
        this.remark  = "";
        this.module   = "soft";
        this.sequence = 0;
        this.starNumber = 0;
        this.score = 0;
        this.attach = "";
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 软件类别
     */
    @ManyToOne
    @JoinColumn(name="category_id",nullable = false)
    private Category category;

    @Column(name="name",length = 255,nullable = false)
    private String name; // 软件名称
    @Column(name="summary",length = 255,nullable = false)
    private String summary; // 软件摘要

    private String content; // 软件简介

    private String attach; // 软件缩略图

    @Column(name="module",length = 100,nullable = false)
    private String module; //upload module

    private String url; // 软件测试地址

    private String developer; // 软件开发商

    private String site; // 软件官网

    private String version; // 软件版本

    private String platform; // 技术平台

    private Integer starNumber; // 星级

    private Integer score; // 评分

    private String contact; // 联系人

    private String telphone; // 电话

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    private Integer sequence;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Integer getStarNumber() {
        return starNumber;
    }

    public void setStarNumber(Integer starNumber) {
        this.starNumber = starNumber;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
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

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Soft soft = (Soft) o;
        return Objects.equal(id, soft.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Soft{" +
                "id=" + id +
                ", category=" + category +
                ", name='" + name + '\'' +
                ", summary='" + summary + '\'' +
                ", content='" + content + '\'' +
                ", attach='" + attach + '\'' +
                ", module='" + module + '\'' +
                ", url='" + url + '\'' +
                ", developer='" + developer + '\'' +
                ", site='" + site + '\'' +
                ", version='" + version + '\'' +
                ", platform='" + platform + '\'' +
                ", starNumber=" + starNumber +
                ", score=" + score +
                ", contact='" + contact + '\'' +
                ", telphone='" + telphone + '\'' +
                ", sequence=" + sequence +
                ", created=" + created +
                ", modified=" + modified +
                ", status=" + status +
                ", enabled=" + enabled +
                ", remark='" + remark + '\'' +
                '}';
    }
}
