package yun.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import yun.models.Category;
import yun.models.Member;
import yun.models.Soft;
import yun.utils.Page;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class SoftDao extends GenericDao<Soft>{

    @Inject
    Provider<EntityManager> entityManagerProvider;

    public SoftDao() {
        super(TypeLiteral.get(Soft.class));
    }

    /**
     *
     * @param num
     * @param categoryId
     * @param sortname
     * @param sortorder
     * @return
     */
    public List<Soft> getNumByCategoryId(int num, Long categoryId, String sortname, String sortorder) {
        final StringBuffer queryString = new StringBuffer(
                "SELECT o from Soft o where o.category.id = :categoryId and o.status = 'true' and o.enabled = 'true' order by o."+sortname+" "+sortorder);
        final Query q = this.provider.get().createQuery(queryString.toString());
        q.setParameter("categoryId", categoryId);
        q.setFirstResult(0);
        q.setMaxResults(num);
        List<Soft> l = q.getResultList();
        return l;
    }

    public List<Soft> getNumByBest(int num, String sortname, String sortorder) {
        final StringBuffer queryString = new StringBuffer(
                "SELECT o from Soft o where  o.status = 'true' and o.enabled = 'true' order by o."+sortname+" "+sortorder);
        final Query q = this.provider.get().createQuery(queryString.toString());
        q.setFirstResult(0);
        q.setMaxResults(num);
        List<Soft> l = q.getResultList();
        return l;
    }

    /**
     * 通过名称查询
     * @param name
     * @return
     */
    public Soft getByName(String name) {
        EntityManager em = entityManagerProvider.get();
        TypedQuery<Soft> q = em.createQuery(
                "select x from Soft as x where x.name = :name", Soft.class
        );
        q.setParameter("name", name);
        q.setFirstResult(0).setMaxResults(1);
        List<Soft> softs = q.getResultList();
        if (softs.size() == 1) {
            return softs.get(0);
        }
        return null;
    }

    public long countPaged(Long memberId) {
        final StringBuffer queryString = new StringBuffer("SELECT count(o) from Soft o where " +
                "o.member.id = :memberId");
        final Query query = this.provider.get().createQuery(queryString.toString());
        query.setParameter("memberId", memberId);
        Long count =  (Long) query.getSingleResult();
        return count;
    }

    public Page<Soft> getPaged(int p, int limit, Long memberId, String sortname, String sortorder){
        Page<Soft> page = new Page<Soft>(p,limit);
        final StringBuffer queryString = new StringBuffer(
                "SELECT o from Soft o where o.member.id = :memberId  order by o."+sortname+" "+sortorder);
        final Query q = this.provider.get().createQuery(queryString.toString());
        q.setParameter("memberId", memberId);
        page.setAllrecords(this.countPaged(memberId));
        q.setFirstResult(page.getFirstResult());
        q.setMaxResults(page.getMaxResults());
        List<Soft> l = q.getResultList();
        page.setItems(l);
        return page;
    }


    public long countByCategoryId(Long categoryId) {
        final StringBuffer queryString = new StringBuffer("SELECT count(o) from Soft o where " +
                "o.category.id = :categoryId  and o.status = 'true' and o.enabled = 'true'");
        final Query query = this.provider.get().createQuery(queryString.toString());
        query.setParameter("categoryId", categoryId);
        Long count =  (Long) query.getSingleResult();
        return count;
    }

    public Page<Soft> listPagedByCategoryId(int p, int limit, Long categoryId, String sortname, String sortorder){
        Page<Soft> page = new Page<Soft>(p,limit);
        final StringBuffer queryString = new StringBuffer(
                "SELECT o from Soft o where o.category.id = :categoryId  and  o.status = 'true' and o.enabled = 'true' order by o."+sortname+" "+sortorder);
        final Query q = this.provider.get().createQuery(queryString.toString());
        q.setParameter("categoryId", categoryId);
        page.setAllrecords(this.countByCategoryId(categoryId));
        q.setFirstResult(page.getFirstResult());
        q.setMaxResults(page.getMaxResults());
        List<Soft> l = q.getResultList();
        page.setItems(l);
        return page;
    }

    public long countByCondition(Long categoryId, String name) {
        String hql = " where 1=1 ";
        if(categoryId != null ) {
            hql += " and  o.category.id = :categoryId ";
        }
        if( name != null && !"".equals(name.trim())) {
            hql += " and  o.name like :name ";
        }
        final StringBuffer queryString = new StringBuffer("SELECT count(o) from Soft o  " + hql);
        final Query query = this.provider.get().createQuery(queryString.toString());

        if( categoryId != null ) {
            query.setParameter("categoryId", categoryId);
        }
        if( name != null && !"".equals(name.trim())) {
            query.setParameter("name", "%"+name+"%");
        }

        Long count =  (Long) query.getSingleResult();
        return count;
    }

    public Page<Soft> listPagedByCondition(int p, int limit, Long categoryId, String name, String sortname, String sortorder){
        Page<Soft> page = new Page<Soft>(p,limit);

        String hql = " where 1=1 ";
        if(categoryId != null ) {
            hql += " and  o.category.id = :categoryId ";
        }
        if( name != null && !"".equals(name.trim())) {
            hql += " and  o.name like :name ";
        }

        final StringBuffer queryString = new StringBuffer(
                "SELECT o from Soft o "+ hql + "  order by o."+sortname+" "+sortorder);
        final Query q = this.provider.get().createQuery(queryString.toString());

        page.setAllrecords(this.countByCondition(categoryId , name));

        if(categoryId != null ) {
            q.setParameter("categoryId", categoryId);
        }
        if( name != null && !"".equals(name.trim())) {
            q.setParameter("name", "%"+name+"%");
        }
        q.setFirstResult(page.getFirstResult());
        q.setMaxResults(page.getMaxResults());
        List<Soft> l = q.getResultList();
        page.setItems(l);
        return page;
    }

    public long countByCondition(Member member,Long categoryId, String name) {
        String hql = " where 1=1 and o.member.id = :memberId ";
        if(categoryId != null ) {
            hql += " and  o.category.id = :categoryId ";
        }
        if( name != null && !"".equals(name.trim())) {
            hql += " and  o.name like :name ";
        }
        final StringBuffer queryString = new StringBuffer("SELECT count(o) from Soft o  " + hql);
        final Query query = this.provider.get().createQuery(queryString.toString());

        if( categoryId != null ) {
            query.setParameter("categoryId", categoryId);
        }
        if( name != null && !"".equals(name.trim())) {
            query.setParameter("name", "%"+name+"%");
        }
        query.setParameter("memberId", member.getId());
        Long count =  (Long) query.getSingleResult();
        return count;
    }

    public Page<Soft> listPagedByConditionMember(int p, int limit, Member member,Long categoryId, String name, String sortname, String sortorder){
        Page<Soft> page = new Page<Soft>(p,limit);

        String hql = " where 1=1 and o.member.id = :memberId ";

        if(categoryId != null ) {
            hql += " and  o.category.id = :categoryId ";
        }
        if( name != null && !"".equals(name.trim())) {
            hql += " and  o.name like :name ";
        }

        final StringBuffer queryString = new StringBuffer(
                "SELECT o from Soft o "+ hql + "  order by o."+sortname+" "+sortorder);
        final Query q = this.provider.get().createQuery(queryString.toString());

        page.setAllrecords(this.countByCondition(member,categoryId , name));

        if(categoryId != null ) {
            q.setParameter("categoryId", categoryId);
        }
        if( name != null && !"".equals(name.trim())) {
            q.setParameter("name", "%"+name+"%");
        }
        q.setParameter("memberId", member.getId());
        q.setFirstResult(page.getFirstResult());
        q.setMaxResults(page.getMaxResults());
        List<Soft> l = q.getResultList();
        page.setItems(l);
        return page;
    }

}
