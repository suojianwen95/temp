package yun.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import ninja.jpa.UnitOfWork;
import yun.dao.CategoryDao;
import yun.dao.SoftDao;
import yun.dto.SoftDto;
import yun.models.Category;
import yun.models.Member;
import yun.models.Soft;
import yun.utils.Page;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;

@Singleton
public class SoftService extends GenericService<Soft>{
    @Inject
    SoftDao softDao;
    @Inject
    CategoryDao categoryDao;

    @UnitOfWork
    public Soft getByName(String name){
        return softDao.getByName(name);
    }

    /***
     * 查询用户软件信息列表
     * @param p
     * @param limit
     * @param member
     * @param sortname
     * @param sortorder
     * @return
     */
    @UnitOfWork
    public Page<Soft> getPaged(int p, int limit, Member member, String sortname, String sortorder){
        return softDao.getPaged(p, limit, member.getId() ,sortname,sortorder);
    }

    @UnitOfWork
    public List<Soft> getNumByCategoryId(int num, Category category, String sortname, String sortorder){
        return softDao.getNumByCategoryId(num, category.getId() ,sortname,sortorder);
    }

    @UnitOfWork
    public List<Soft> getNumByBest(int num, String sortname, String sortorder){
        return softDao.getNumByBest(num ,sortname,sortorder);
    }

    /***
     * 依据分类查询软件信息
     * @param p
     * @param limit
     * @param category
     * @param sortname
     * @param sortorder
     * @return
     */
    @UnitOfWork
    public Page<Soft> listPagedByCategoryId(int p, int limit, Category category, String sortname, String sortorder){
        return softDao.listPagedByCategoryId(p, limit, category.getId(), sortname, sortorder);
    }

    @UnitOfWork
    public Page<Soft> listPagedByCondition(int p, int limit,Member member, Long categoryId, String name, String sortname, String sortorder){
        if( member != null ) {
            return softDao.listPagedByConditionMember(p,limit,member,categoryId,name,sortname,sortorder);
        }
        return softDao.listPagedByCondition(p,limit,categoryId,name,sortname,sortorder);
    }

    /**
     * 添加
     * @param dto
     * @return
     */
    @Transactional
    public CommonSdo<Soft> createSoft(SoftDto dto, Member member) {
        if (null !=softDao.getByName(dto.getName())){
            return CommonSdo.error("软件名称不能重复");
        }
        Category category = categoryDao.find(dto.getCategoryId());
        if (null == category){
            return CommonSdo.error("没有该软件分类");
        }
        if (null == dto.getName() || "".equals(dto.getName().trim())){
            return CommonSdo.error("请输入软件名称");
        } else if (dto.getName().length()>200){
            return CommonSdo.error("软件名称不能超过200个字符");
        }
        if (null == dto.getUrl() || "".equals(dto.getUrl().trim())){
            return CommonSdo.error("请输入软件测试地址");
        } else if (dto.getUrl().length()>200){
            return CommonSdo.error("软件测试地址不能超过200个字符");
        }
        if (null == dto.getSite()|| "".equals(dto.getSite().trim())){
            return CommonSdo.error("请输入官方地址");
        } else if (dto.getSite().length()>200){
            return CommonSdo.error("官方地址不能超过200个字符");
        }
        if (null == dto.getDeveloper()|| "".equals(dto.getDeveloper().trim())){
            return CommonSdo.error("请输入开发商信息");
        } else if (dto.getDeveloper().length()>200){
            return CommonSdo.error("开发商信息不能超过200个字符");
        }
        if (null == dto.getVersion()|| "".equals(dto.getVersion().trim())){
            return CommonSdo.error("请输入版本信息");
        } else if (dto.getVersion().length()>50){
            return CommonSdo.error("版本信息不能超过50个字符");
        }
        if (null == dto.getPlatform()|| "".equals(dto.getPlatform().trim())){
            return CommonSdo.error("请输入技术平台");
        } else if (dto.getPlatform().length()>100){
            return CommonSdo.error("技术平台不能超过100个字符");
        }
        if (null == dto.getContact()|| "".equals(dto.getContact().trim())){
            return CommonSdo.error("请输入联系人");
        } else if (dto.getContact().length()>50){
            return CommonSdo.error("联系人不能超过50个字符");
        }
        if (null == dto.getTelphone()|| "".equals(dto.getTelphone().trim())){
            return CommonSdo.error("请输入联系电话");
        } else if (dto.getTelphone().length()>50){
            return CommonSdo.error("联系电话不能超过50个字符");
        }
        Soft soft = new Soft();
        soft.setMember(member);
        soft.setName(dto.getName());
        soft.setSummary(dto.getSummary());
        soft.setContent(dto.getContent());
        soft.setAttach(dto.getAttach());
        soft.setUrl(dto.getUrl());
        soft.setDeveloper(dto.getDeveloper());
        soft.setSite(dto.getSite());
        soft.setVersion(dto.getVersion());
        soft.setPlatform(dto.getPlatform());
        soft.setStarNumber(dto.getStarNumber());
        soft.setScore(dto.getScore());
        soft.setContact(dto.getContact());
        soft.setTelphone(dto.getTelphone());
        soft.setSequence(dto.getSequence());
        soft.setCategory(category);
        softDao.create(soft);
        return CommonSdo.success(soft,"创建成功");
    }

    /**
     * 修改
     * @param soft
     * @param dto
     * @return
     */
    @Transactional
    public CommonSdo updateSoft(Soft soft, SoftDto dto,Long id) {
        Soft softTemp = softDao.getByName(dto.getName());
        if (softTemp !=null && (!softTemp.getId().equals(id))) {
            return CommonSdo.error("软件名称不能重复");
        }
        Category category = categoryDao.find(dto.getCategoryId());
        if (null == category){
            return CommonSdo.error("没有该软件分类");
        }
        if (null == dto.getName() || "".equals(dto.getName().trim())){
            return CommonSdo.error("请输入软件名称");
        } else if (dto.getName().length()>200){
            return CommonSdo.error("软件名称不能超过200个字符");
        }
        if (null == dto.getUrl() || "".equals(dto.getUrl().trim())){
            return CommonSdo.error("请输入软件测试地址");
        } else if (dto.getUrl().length()>200){
            return CommonSdo.error("软件测试地址不能超过200个字符");
        }
        if (null == dto.getSite()|| "".equals(dto.getSite().trim())){
            return CommonSdo.error("请输入官方地址");
        } else if (dto.getSite().length()>200){
            return CommonSdo.error("官方地址不能超过200个字符");
        }
        if (null == dto.getDeveloper()|| "".equals(dto.getDeveloper().trim())){
            return CommonSdo.error("请输入开发商信息");
        } else if (dto.getDeveloper().length()>200){
            return CommonSdo.error("开发商信息不能超过200个字符");
        }
        if (null == dto.getVersion()|| "".equals(dto.getVersion().trim())){
            return CommonSdo.error("请输入版本信息");
        } else if (dto.getVersion().length()>50){
            return CommonSdo.error("版本信息不能超过50个字符");
        }
        if (null == dto.getPlatform()|| "".equals(dto.getPlatform().trim())){
            return CommonSdo.error("请输入技术平台");
        } else if (dto.getPlatform().length()>100){
            return CommonSdo.error("技术平台不能超过100个字符");
        }
        if (null == dto.getContact()|| "".equals(dto.getContact().trim())){
            return CommonSdo.error("请输入联系人");
        } else if (dto.getContact().length()>50){
            return CommonSdo.error("联系人不能超过50个字符");
        }
        if (null == dto.getTelphone()|| "".equals(dto.getTelphone().trim())){
            return CommonSdo.error("请输入联系电话");
        } else if (dto.getTelphone().length()>50){
            return CommonSdo.error("联系电话不能超过50个字符");
        }
        soft.setName(dto.getName());
        soft.setSummary(dto.getSummary());
        soft.setContent(dto.getContent());
        soft.setAttach(dto.getAttach());
        soft.setUrl(dto.getUrl());
        soft.setDeveloper(dto.getDeveloper());
        soft.setSite(dto.getSite());
        soft.setVersion(dto.getVersion());
        soft.setPlatform(dto.getPlatform());
        soft.setStarNumber(dto.getStarNumber());
        soft.setScore(dto.getScore());
        soft.setContact(dto.getContact());
        soft.setTelphone(dto.getTelphone());
        soft.setSequence(dto.getSequence());
        soft.setCategory(category);
        soft.setModified(new Date());
        softDao.update(soft);
        return CommonSdo.success(soft,"修改成功");
    }

    @Transactional
    public CommonSdo updateSoftScore(Soft soft, Integer score) {
        soft.setScore(score);
        soft.setModified(new Date());
        softDao.update(soft);
        return CommonSdo.success(soft,"修改成功");
    }
}
