package yun.models;

import com.google.common.base.Objects;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Peng on 2017-1-18.
 * 用户级别,作为权限控制
 */
@Entity
@Table(name = "yun_level")
public class Level {

    public static final String UID_ADMIN="64757d1a-d818-43c1-88a5-71751b4ce505"; // 系统管理员

    public static final String UID_STAFF="5ad9d884-5c3d-4624-9473-247b0e61121a"; // 运营人员

    public static final String UID_CUSTOMER="7ad9d884-7c3d-4624-8473-147b0e61121a"; // 普通用户

    public Level(){
        Date now = new Date();
        this.created = now;
        this.modified = now;
        this.enabled = true;
        this.status = true;
        this.uid = UUID.randomUUID().toString();
        this.members = Collections.EMPTY_LIST;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String uid;
    private Date created;
    private Date modified;
    private Boolean status;
    private Boolean enabled;

    @ManyToMany(mappedBy = "levels",fetch = FetchType.LAZY)
    private Collection<Member> members;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public Collection<Member> getMembers() {
        return members;
    }

    public void setMembers(Collection<Member> members) {
        this.members = members;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Level level = (Level) o;
        return Objects.equal(id, level.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
