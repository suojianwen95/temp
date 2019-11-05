package yun.service;


import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import ninja.jpa.UnitOfWork;
import yun.dao.GenericDao;
import yun.utils.Page;

import java.util.List;

public class GenericService<T>
{
    @Inject
    GenericDao<T> dao;

    @Transactional
    public T create(T t){
        return dao.create(t);
    }

    @Transactional
    public void delete(T t){
        dao.delete(t);
    }

    @UnitOfWork
    public T find(final Long id){
        return dao.find(id);
    }

    @Transactional
    public T update(T t){
        return dao.update(t);
    }

    @UnitOfWork
    public long count() {
        return dao.count();
    }

    @UnitOfWork
    public Page<T> listPaged(int p, int limit){
        return dao.listPaged(p,limit);
    }

    @UnitOfWork
    public Page<T> listPaged(int p, int limit, String sortname, String sortorder){
        return dao.listPaged(p,limit,sortname,sortorder);
    }

    @UnitOfWork
    public Page<T> listPagedEnabled(int p, int limit, String sortname, String sortorder){
        return dao.listPagedEnabled(p,limit,sortname,sortorder);
    }

    @UnitOfWork
    public List<T> findAll(){
        return dao.findAll();
    }

    @Transactional
    public Integer deleteAll() {
        return dao.deleteAll();
    }
}
