package yun.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import yun.utils.Page;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class GenericDao<T>
{

    protected final Class< T > type;

    @Inject
    Provider<EntityManager> provider;

    @Inject
    public GenericDao(TypeLiteral<T> type)
    {
        this.type = (Class<T>)type.getRawType();
    }

    public T create(T t)
    {
        EntityManager em = provider.get();
        em.persist(t);
        em.flush();
        return t;
    }

    public void delete(T t)
    {
        t = this.provider.get().merge(t);
        this.provider.get().remove(t);
    }

    public T find(final Long id)
    {
        return this.provider.get().find(type, id);
    }

    public T update(T t)
    {
        EntityManager em = provider.get();
        em.merge(t);
        em.flush();
        return t;
    }

    public long count() {
        final StringBuffer queryString = new StringBuffer(
                "SELECT count(o) from ");
        queryString.append(type.getSimpleName()).append(" o ");
        final Query query = this.provider.get().createQuery(queryString.toString());

        Long count =  (Long) query.getSingleResult();
        return count;
    }

    public Page<T> listPaged(int p, int limit){
        Page<T> page = new Page<T>(p,limit);
        final StringBuffer queryString = new StringBuffer(
                "SELECT o from ");
        queryString.append(type.getSimpleName()).append(" o order by o.id desc");
        final Query q = this.provider.get().createQuery(queryString.toString());
        q.setFirstResult(page.getFirstResult());
        q.setMaxResults(page.getMaxResults());
        List<T> l = q.getResultList();
        page.setAllrecords(this.count());
        page.setItems(l);
        return page;
    }

    public Page<T> listPaged(int p, int limit, String sortname, String sortorder){
        Page<T> page = new Page<T>(p,limit);
        final StringBuffer queryString = new StringBuffer(
                "SELECT o from ");
        queryString.append(type.getSimpleName()).append(" o order by o."+sortname+" "+sortorder);
        final Query q = this.provider.get().createQuery(queryString.toString());
        page.setAllrecords(this.count());
        q.setFirstResult(page.getFirstResult());
        q.setMaxResults(page.getMaxResults());
        List<T> l = q.getResultList();
        page.setItems(l);
        return page;
    }

    public long countEnabled() {
        final StringBuffer queryString = new StringBuffer(
                "SELECT count(o) from ");
        queryString.append(type.getSimpleName()).append(" o where o.status = 'true' and o.enabled = 'true'");
        final Query query = this.provider.get().createQuery(queryString.toString());

        Long count =  (Long) query.getSingleResult();
        return count;
    }

    public Page<T> listPagedEnabled(int p, int limit, String sortname, String sortorder){
        Page<T> page = new Page<T>(p,limit);
        final StringBuffer queryString = new StringBuffer(
                "SELECT o from ");
        queryString.append(type.getSimpleName()).append(" o where o.status = 'true' and o.enabled = 'true' order by o."+sortname+" "+sortorder);
        final Query q = this.provider.get().createQuery(queryString.toString());
        page.setAllrecords(this.countEnabled());
        q.setFirstResult(page.getFirstResult());
        q.setMaxResults(page.getMaxResults());
        List<T> l = q.getResultList();
        page.setItems(l);
        return page;
    }

    public Page<T> findByPaged(T example, int currentPage, int limit)
    {
        Page<T> page = new Page<T>(currentPage, limit);
        Example ex = Example.create(example);
        final Session session = (Session)this.provider.get().getDelegate();
        final Criteria criteria = session
                .createCriteria(example.getClass())
                .addOrder(Order.desc("id"))
                .add(ex);
        criteria.setFirstResult(page.getFirstResult());
        criteria.setMaxResults(page.getMaxResults());
        page.setItems(criteria.list());
        int total = this.findBy(example).size();
        page.setAllrecords(total);
        return page;
    }


    public List<T> findBy(T example) {
        Example ex = Example.create(example);
        final Session session = (Session)this.provider.get().getDelegate();
        final Criteria criteria = session.createCriteria(example.getClass()).add(ex);
        return criteria.list();
    }

    public List<T> findLike(T example) {
        Example ex = Example.create(example);
        final Session session = (Session)this.provider.get().getDelegate();
        final Criteria criteria = session.createCriteria(example.getClass()).add(ex);
        return criteria.list();
    }

    public List<T> findAll(){
        final StringBuffer queryString = new StringBuffer(
                "SELECT o from ");
        queryString.append(type.getSimpleName()).append(" o where o.status = 'true' and o.enabled = 'true' order by o.id desc");
        final Query query = this.provider.get().createQuery(queryString.toString());
        return query.getResultList();
    }

    public Integer deleteAll() {
        final StringBuffer queryString = new StringBuffer(
                "DELETE  from ");
        queryString.append(type.getSimpleName()).append(" o ");
        final Query query = this.provider.get().createQuery(queryString.toString());

        Integer count =  (Integer) query.executeUpdate();
        return count;
    }
}
