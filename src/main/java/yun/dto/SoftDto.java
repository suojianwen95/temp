package yun.dto;


import javax.validation.constraints.Size;

public class SoftDto {

    private Long categoryId; // 类别id

    @Size(min = 1,max = 255,message = "软件名称需要1-255个字符组成")
    private String name; // 软件名称

    @Size(min = 1,max = 255,message = "软件摘要需要1-255个字符组成")
    private String summary; // 软件摘要

    private String content; // 软件简介

    private String attach; // 软件缩略图

    @Size(min = 1,max = 255,message = "软件测试地址需要1-255个字符组成")
    private String url; // 软件测试地址

    private String developer; // 软件开发商
    @Size(min = 1,max = 255,message = "软件官网需要1-255个字符组成")
    private String site; // 软件官网

    private String version; // 软件版本

    private String platform; // 技术平台

    private Integer starNumber; // 星级

    private Integer score; // 评分

    private String contact; // 联系人

    private String telphone; // 电话

    private Integer sequence;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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

    @Override
    public String toString() {
        return "SoftDto{" +
                "categoryId=" + categoryId +
                ", name='" + name + '\'' +
                ", summary='" + summary + '\'' +
                ", content='" + content + '\'' +
                ", attach='" + attach + '\'' +
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
                '}';
    }
}
