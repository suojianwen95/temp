package yun.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import yun.models.Member;
import yun.models.ResetCode;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Peng on 2017-1-25.
 */
public class ResetCodeDao extends GenericDao<ResetCode> {
    public ResetCodeDao() {
        super(TypeLiteral.get(ResetCode.class));
    }

    @Inject
    Provider<EntityManager> provider;

    public ResetCode get(String code) {
        EntityManager em = provider.get();
        Query q = em.createQuery("select x from ResetCode as x where x.status = true and x.code = :code and x.created>:dt order by x.id desc");
        q.setParameter("code", code);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -1);
        Date date = calendar.getTime();
        q.setParameter("dt", date);
        List<ResetCode> list = q.getResultList();
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public ResetCode get(Member member) {
        EntityManager em = provider.get();
        Query q = em.createQuery("select x from ResetCode as x where x.status = true and x.member = :mb and x.created > :dt order by x.id desc");
        q.setParameter("mb", member);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -1);
        Date date = calendar.getTime();
        q.setParameter("dt", date);
        List<ResetCode> list = q.getResultList();
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
