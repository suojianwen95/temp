package yun.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import yun.models.Option;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class OptionDao extends GenericDao<Option>{

    @Inject
    Provider<EntityManager> entityManagerProvider;

    public OptionDao() {
        super(TypeLiteral.get(Option.class));
    }

    /**
     * 通过类别名称查询
     * @param name
     * @return
     */
    public Option getByName(String name) {
        EntityManager em = entityManagerProvider.get();
        TypedQuery<Option> q = em.createQuery(
                "select x from Option as x where x.name = :name", Option.class
        );
        q.setParameter("name", name);
        q.setFirstResult(0).setMaxResults(1);
        List<Option> options = q.getResultList();
        if (options.size() == 1) {
            return options.get(0);
        }
        return null;
    }

}
