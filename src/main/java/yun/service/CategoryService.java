package yun.service;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import ninja.jpa.UnitOfWork;
import yun.dao.CategoryDao;
import yun.dto.CategoryDto;
import yun.dto.MemberDto;
import yun.models.Category;

import java.util.Date;
import java.util.List;

@Singleton
public class CategoryService extends GenericService<Category>{

    @Inject
    CategoryDao categoryDao;

    @UnitOfWork
    public Category getByName(String name){
        return categoryDao.getByName(name);
    }

    @UnitOfWork
    public List<Category> getCategoryList(){
        return categoryDao.getCategoryList();
    }

    /**
     * 添加
     * @param dto
     * @return
     */
    @Transactional
    public CommonSdo<Category> createCategory(CategoryDto dto) {
        if (null !=categoryDao.getByName(dto.getName())){
            return CommonSdo.error("该软件类别已存在");
        }
        Category category = new Category();
        category.setName(dto.getName());
        categoryDao.create(category);
        return CommonSdo.success(category,"创建成功");
    }

    /**
     * 修改
     * @param category
     * @param dto
     * @return
     */
    @Transactional
    public CommonSdo updateCategory(Category category, CategoryDto dto) {
        Category categoryTemp = categoryDao.getByName(dto.getName());
        if (categoryTemp !=null) {
            return CommonSdo.error("该软件类别已存在");
        }
        category.setName(dto.getName());
        category.setModified(new Date());
        categoryDao.update(category);
        return CommonSdo.success(category,"修改成功");
    }
}
