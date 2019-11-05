package yun.models;


import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import yun.utils.BCrypt;
import yun.utils.CookieUtils;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 用户
 */
@Entity
@Table(name = "yun_member")
public class Member {

    public Member(){
        Date now = new Date();
        this.created = now;
        this.modified = now;
        this.enabled = true;
        this.status = true;
        this.remark = "";
        this.levels = Lists.newArrayList();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String nickname;
    private String password;
    private String email;
    private String mobile;

    private String remark;
    private Date created;
    private Date modified;
    private Boolean status;
    private Boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="yun_member_level",
            joinColumns=
            @JoinColumn(name="member_id", referencedColumnName="id"),
            inverseJoinColumns=
            @JoinColumn(name="level_id", referencedColumnName="id")
    )
    private List<Level> levels;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPlainPassword(String plainPassword){
        String hashed = BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
        this.setPassword(hashed);
    }

    public Boolean validPassword(String pwd){
        return BCrypt.checkpw(pwd,this.getPassword());
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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


    public List<Level> getLevels() {
        return levels;
    }

    public void setLevels(List<Level> levels) {
        this.levels = levels;
    }
    public String getLevelsSecurityString(CookieUtils cookieUtils){
        StringBuilder sb = new StringBuilder();
        for (Level l : this.getLevels()){
            sb.append(cookieUtils.encryptKey(l.getUid())).append("_");
        }
        return sb.toString();
    }

    public void addLevel(Level level){
        if(!this.levels.contains(level)){
            this.levels.add(level);
        }
    }
    public void removeLevel(Level level){
        if(this.levels.contains(level)){
            this.levels.remove(level);
        }
    }

    public String getLevelsString(){
        StringBuilder sb = new StringBuilder();
        for (Level l : this.getLevels()){
            sb.append(l.getUid()).append("_");
        }
        return sb.toString();
    }
    private boolean checkLevel(String uid){
        for(Level l : this.getLevels()){
            if(l.getUid().equals(uid)){return true;}
        }
        return false;
    }

    public boolean isAdmin(){
        return checkLevel(Level.UID_ADMIN);
    }
    public boolean isStaff(){
        return checkLevel(Level.UID_STAFF);
    }
    public boolean isCustomer(){
        return checkLevel(Level.UID_CUSTOMER);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equal(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
