package yun.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import yun.models.Category;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class CategoryDao extends GenericDao<Category>{

    @Inject
    Provider<EntityManager> entityManagerProvider;

    public CategoryDao() {
        super(TypeLiteral.get(Category.class));
    }

    /**
     * 通过类别名称查询
     * @param name
     * @return
     */
    public Category getByName(String name) {
        EntityManager em = entityManagerProvider.get();
        TypedQuery<Category> q = em.createQuery(
                "select x from Category as x where x.name = :name", Category.class
        );
        q.setParameter("name", name);
        q.setFirstResult(0).setMaxResults(1);
        List<Category> categories = q.getResultList();
        if (categories.size() == 1) {
            return categories.get(0);
        }
        return null;
    }

    public List<Category> getCategoryList(){
        EntityManager em = entityManagerProvider.get();
        TypedQuery<Category> q = em.createQuery(
                "select x from Category as x where x.status = 'true' and x.enabled = 'true'", Category.class
        );
        List<Category> categories = q.getResultList();
        return categories;
    }

}
