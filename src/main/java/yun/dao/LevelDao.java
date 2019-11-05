package yun.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import yun.models.Level;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;


public class LevelDao extends GenericDao<Level> {

    @Inject
    Provider<EntityManager> provider;

    public LevelDao() {
        super(TypeLiteral.get(Level.class));
    }

    public Level getByUid(String uid) {
        EntityManager em = provider.get();
        TypedQuery<Level> q = em.createQuery("select x from Level as x where x.uid = :uid", Level.class);
        q.setParameter("uid", uid);
        q.setFirstResult(0).setMaxResults(1);
        List<Level> levels = q.getResultList();
        if (levels.size() == 1) {
            return levels.get(0);
        }
        return null;
    }

    /**
     * 查询所有启动的level
     * @return
     */
    public List<Level> levels (){
        EntityManager em = provider.get();
        TypedQuery<Level> l = em.createQuery("select x from Level as x where x.status =:status",Level.class);
        l.setParameter("status",true);
        return l.getResultList();
    }


}
