package yun.controllers.staff;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ninja.Context;
import ninja.Result;
import ninja.Results;
import ninja.params.Param;
import ninja.params.PathParam;
import ninja.uploads.DiskFileItemProvider;
import ninja.uploads.FileItem;
import ninja.uploads.FileProvider;
import yun.controllers.BaseController;
import yun.dto.SoftDto;
import yun.models.Category;
import yun.models.Member;
import yun.models.Soft;
import yun.service.CategoryService;
import yun.service.CommonSdo;
import yun.service.SoftService;
import yun.utils.Page;
import yun.utils.UploadUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Singleton
public class SoftController extends BaseController {

    @Inject
    SoftService softService;
    @Inject
    CategoryService categoryService;
    @Inject
    UploadUtils uploadUtils;

    public Result index (Context context,
                         @Param("page") Integer p,
                         @Param("limit") Integer limit,
                         @Param("name") String name,
                         @Param("categoryId") Long categoryId) {
        if (null == p||p<1){p=1;}
        if (null == limit){limit=10;}
        List<Category> categoryList = categoryService.getCategoryList();
        Page<Soft> page = softService.listPagedByCondition(p,limit, null, categoryId, name,"sequence","DESC");
        Page<Map<String,Object>> mapPage = new Page<Map<String, Object>>(p,page.getMaxResults());
        List<Map<String,Object>> mapList = Lists.newArrayList();
        if (page.getItems().size()>0){
            for ( Soft m: page.getItems()){
                Map<String,Object> map = Maps.newHashMap();
                map.put("id",m.getId());
                map.put("name",m.getName());
                map.put("attach",m.getAttach());
                map.put("categoryName",m.getCategory().getName());
                map.put("summ",m.getCategory().getName());
                map.put("url",m.getUrl());
                map.put("developer",m.getDeveloper());
                map.put("site",m.getSite());
                map.put("version",m.getVersion());
                map.put("platform",m.getPlatform());
                map.put("starNumber",m.getStarNumber());
                map.put("score",m.getScore());
                map.put("contact",m.getContact());
                map.put("telphone",m.getTelphone());
                map.put("sequence",m.getSequence());
                map.put("created",m.getCreated());
                map.put("modified",m.getModified());
                map.put("status",m.getStatus());
                map.put("enabled",m.getEnabled());
                mapList.add(map);
            }
        }
        mapPage.setItems(mapList);
        mapPage.setAllrecords(page.getAllResults());
        return Results.html().render("page",mapPage).render("categoryList",categoryList).render("name",name).render("categoryId",categoryId);
    }

    public Result add(Context context){
        SoftDto softDto = new SoftDto();
        List<Category> categoryList = categoryService.getCategoryList();
        return Results.html().render("item",softDto).render("categoryList",categoryList);
    }

    @FileProvider(DiskFileItemProvider.class)
    public Result create(Context context, @Param("attach") FileItem fileItem) {
        List<Category> categoryList = categoryService.getCategoryList();
        // 获取参数
        String categoryId = context.getParameter("categoryId");
        String name = context.getParameter("name");
        String summary = context.getParameter("summary");
        String content = context.getParameter("content");
        String url = context.getParameter("url");
        String developer = context.getParameter("developer");
        String site = context.getParameter("site");
        String version = context.getParameter("version");
        String platform = context.getParameter("platform");
        String starNumber = context.getParameter("starNumber");
        String score = context.getParameter("score");
        String contact = context.getParameter("contact");
        String telphone = context.getParameter("telphone");
        String sequence = context.getParameter("sequence");
        // 设置 dto
        SoftDto dto = new SoftDto();
        dto.setCategoryId(Long.parseLong(categoryId));
        dto.setName(name);
        dto.setSummary(summary);
        dto.setContent(content);
        dto.setUrl(url);
        dto.setDeveloper(developer);
        dto.setSite(site);
        dto.setVersion(version);
        dto.setPlatform(platform);
        dto.setStarNumber(starNumber==null ?0:Integer.parseInt(starNumber));
        dto.setScore(score==null ?0:Integer.parseInt(score));
        dto.setContact(contact);
        dto.setTelphone(telphone);
        dto.setSequence(sequence==null ?0:Integer.parseInt(sequence));
        if (fileItem == null || fileItem.getFile() == null || "".equals(fileItem.getFileName())){
            context.getFlashScope().error("请上传缩略图");
            return Results.html().template("/yun/views/staff/SoftController/add.ftl.html")
                    .render("item",dto).render("categoryList",categoryList);
        }
        String attach = uploadUtils.upload("yun", "soft", fileItem);
        dto.setAttach(attach);
        if (summary!=null && summary.length()>200){
            context.getFlashScope().error("摘要不能超过200个字符，一个中文汉子为2个字符");
            return Results.html().template("/yun/views/staff/SoftController/add.ftl.html")
                    .render("item",dto).render("categoryList",categoryList);
        }
        Member member = this.getCurrent(context);
        CommonSdo<Soft> sdo = softService.createSoft(dto,member);
        if (sdo.isSuccess()){
            context.getFlashScope().success(sdo.getMessage());
            return Results.redirect("/staff/soft/show/"+sdo.getItem().getId());
        }else {
            context.getFlashScope().error(sdo.getMessage());
            return Results.html().template("/yun/views/staff/SoftController/add.ftl.html").render("item",dto).render("categoryList",categoryList);
        }
    }

    public Result edit(Context context, @PathParam("id") Long id){
        List<Category> categoryList = categoryService.getCategoryList();
        Soft soft = softService.find(id);
        if (null == soft){
            context.getFlashScope().error("没有找到该软件");
            return Results.redirect("/staff/soft");
        }
        return Results.html().render("item",soft).render("id",soft.getId()).render("categoryList",categoryList);
    }

    @FileProvider(DiskFileItemProvider.class)
    public Result update(Context context,
                         @PathParam("id") Long id,
                         @Param("attach") FileItem fileItem) {

        Soft soft = softService.find(id);
        if (null == soft){
            context.getFlashScope().error("没有找到该软件");
            return Results.redirect("/staff/soft");
        }
        List<Category> categoryList = categoryService.getCategoryList();
        // 获取参数
        String categoryId = context.getParameter("categoryId");
        String name = context.getParameter("name");
        String summary = context.getParameter("summary");
        String content = context.getParameter("content");
        String url = context.getParameter("url");
        String developer = context.getParameter("developer");
        String site = context.getParameter("site");
        String version = context.getParameter("version");
        String platform = context.getParameter("platform");
        String starNumber = context.getParameter("starNumber");
        String score = context.getParameter("score");
        String contact = context.getParameter("contact");
        String telphone = context.getParameter("telphone");
        String sequence = context.getParameter("sequence");
        // 设置 dto
        SoftDto dto = new SoftDto();
        dto.setCategoryId(Long.parseLong(categoryId));
        dto.setName(name);
        dto.setSummary(summary);
        dto.setContent(content);
        dto.setUrl(url);
        dto.setDeveloper(developer);
        dto.setSite(site);
        dto.setVersion(version);
        dto.setPlatform(platform);
        dto.setStarNumber(starNumber==null ?0:Integer.parseInt(starNumber));
        dto.setScore(score==null ?0:Integer.parseInt(score));
        dto.setContact(contact);
        dto.setTelphone(telphone);
        dto.setSequence(sequence==null ?0:Integer.parseInt(sequence));
        if (fileItem == null || fileItem.getFile() == null || "".equals(fileItem.getFileName())){
            dto.setAttach(soft.getAttach());
        } else {
            String attach = uploadUtils.upload("yun", "soft", fileItem);
            dto.setAttach(attach);
        }
        if (summary!=null && summary.length()>200){
            context.getFlashScope().error("摘要不能超过200个字符，一个中文汉子为2个字符");
            return Results.html().template("/yun/views/staff/SoftController/edit.ftl.html")
                    .render("item",dto).render("categoryList",categoryList);
        }

        CommonSdo<Soft> sdo = softService.updateSoft(soft,dto,id);
        if (sdo.isSuccess()){
            context.getFlashScope().success(sdo.getMessage());
            return Results.redirect("/staff/soft/show/"+sdo.getItem().getId());
        }else {
            context.getFlashScope().error(sdo.getMessage());
            return Results.html().template("/yun/views/staff/SoftController/edit.ftl.html").render("item",dto).render("id",soft.getId()).render("categoryList",categoryList);
        }
    }

    public Result show(Context context, @PathParam("id") Long id){
        Soft soft = softService.find(id);
        if (null == soft){
            context.getFlashScope().error("没有找到该软件的信息");
            return Results.redirect("/staff/soft");
        }
        return Results.html().render("item",soft);
    }

    public Result disable(Context context, @PathParam("id") Long id){
        Soft soft = softService.find(id);
        if (null == soft){
            context.getFlashScope().error("没有找到该软件的信息");
            return Results.redirect("/staff/soft");
        }
        soft.setEnabled(false);
        soft.setStatus(true);
        soft.setModified(new Date());
        softService.update(soft);
        return Results.redirect("/staff/soft");
    }

    public Result enabled(Context context, @PathParam("id") Long id){
        Soft soft = softService.find(id);
        if (null == soft){
            context.getFlashScope().error("没有找到该软件的信息");
            return Results.redirect("/staff/soft");
        }
        soft.setEnabled(true);
        soft.setStatus(true);
        soft.setModified(new Date());
        softService.update(soft);
        return Results.redirect("/staff/soft");
    }

    public Result delete(Context context, @PathParam("id") Long id){
        Soft soft = softService.find(id);
        if (null == soft){
            context.getFlashScope().error("没有找到该软件的信息");
            return Results.redirect("/staff/soft");
        }
        soft.setStatus(false);
        soft.setEnabled(false);
        soft.setModified(new Date());
        softService.update(soft);
        return Results.redirect("/staff/soft");
    }
}
