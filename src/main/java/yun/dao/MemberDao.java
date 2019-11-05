package yun.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import ninja.jpa.UnitOfWork;
import yun.models.Member;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class MemberDao extends GenericDao<Member>{
    
    @Inject
    Provider<EntityManager> entityManagerProvider;

    public MemberDao() {
        super(TypeLiteral.get(Member.class));
    }


    /**
     * 通过用户名查找用户
     * 用户是否注册
     * @param username
     * @return
     */
    public Member getByUsername(String username) {
        EntityManager em = entityManagerProvider.get();
        TypedQuery<Member> q = em.createQuery(
                "select x from Member as x where x.username = :username", Member.class
        );
        q.setParameter("username", username);
        q.setFirstResult(0).setMaxResults(1);
        List<Member> members = q.getResultList();
        if (members.size() == 1) {
            return members.get(0);
        }
        return null;
    }

    /*邮箱是否注册*/
    public Member getByEmail(String email){
        EntityManager em = provider.get();
        TypedQuery<Member> query = em.createQuery("select x from Member as x where x.email = :email",Member.class);
        query.setFirstResult(0);
        query.setMaxResults(1);
        query.setParameter("email",email);
        List<Member> members = query.getResultList();
        if(members.size()>0){
            return members.get(0);
        }else{
            return null;
        }
    }
    /*s手机是否注册*/
    public Member getByMobile(String mobile){
        EntityManager em = provider.get();
        TypedQuery<Member> query = em.createQuery("select x from Member as x where x.mobile = :mobile",Member.class);
        query.setFirstResult(0);
        query.setMaxResults(1);
        query.setParameter("mobile",mobile);
        List<Member> members = query.getResultList();
        if(members.size()>0){
            return members.get(0);
        }else{
            return null;
        }
    }

    /**
     * 查询所有主管
     * @return
     */
    public List<Member> getHr(){
        EntityManager em = (EntityManager) this.provider.get();
        TypedQuery<Member> query = em.createQuery(
                "select m from Member as m inner join m.levels as l where l.uid =:uid and m.status =:status",Member.class);
        query.setParameter("uid","9532f584-7100-456b-b8b4-6fc8003072ef");
        query.setParameter("status",true);
        return query.getResultList();
    }

    /**
     * 查询主管
     * @return
     */
    public Member getManage(){
        EntityManager em = (EntityManager) this.provider.get();
        TypedQuery<Member> query = em.createQuery(
                "select m from Member as m inner join m.levels as l where l.uid =:uid and m.status =:status",Member.class);
        query.setParameter("uid","9532f584-7100-456b-b8b4-6fc8003072ef");
        query.setParameter("status",true);
        List<Member> members = query.getResultList();
        if(members.size()>0){
            return members.get(0);
        }else{
            return null;
        }
    }

    @UnitOfWork
    public boolean isUserAndPasswordValid(String username, String password) {
        
        if (username != null && password != null) {
            EntityManager entityManager = entityManagerProvider.get();
            TypedQuery<Member> q = entityManager.createQuery("SELECT x FROM Member x WHERE username = :usernameParam", Member.class);
            Member user = getSingleResult(q.setParameter("usernameParam", username));
            if (user != null) {
            }
        }
        return false;
 
    }

    private static <T> T getSingleResult(TypedQuery<T> query) {
        query.setMaxResults(1);
        List<T> list = query.getResultList();
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

}
