package yun.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ninja.jpa.UnitOfWork;
import yun.dao.LevelDao;
import yun.models.Level;

import java.util.List;


@Singleton
public class LevelService extends GenericService<Level>{

    @Inject
    LevelDao dao;

    @UnitOfWork
    public List<Level> levels(){
        return dao.levels();
    }
    @UnitOfWork
    public Level getLevel(String uid){
        return dao.getByUid(uid);
    }
}
