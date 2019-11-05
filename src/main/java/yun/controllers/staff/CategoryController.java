package yun.controllers.staff;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import ninja.Context;
import ninja.Result;
import ninja.Results;
import ninja.params.Param;
import ninja.params.PathParam;
import ninja.validation.JSR303Validation;
import ninja.validation.Validation;
import yun.controllers.BaseController;
import yun.dto.CategoryDto;
import yun.models.Category;
import yun.service.CategoryService;
import yun.service.CommonSdo;
import yun.utils.Page;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Singleton
public class CategoryController extends BaseController{

    @Inject
    CategoryService categoryService;

    public Result index (Context context,
                         @Param("page") Integer p,
                         @Param("limit") Integer limit) {
        if (null == p||p<1){p=1;}
        if (null == limit){limit=10;}

        Page<Category> page = categoryService.listPaged(p,limit);
        Page<Map<String,Object>> mapPage = new Page<Map<String, Object>>(page.getCurrent(),page.getMaxResults());
        List<Map<String,Object>> mapList = Lists.newArrayList();

        if (page.getItems().size()>0){
            for ( Category m: page.getItems()){
                Map<String,Object> map = Maps.newHashMap();
                map.put("id",m.getId());
                map.put("name",m.getName());
                map.put("created",m.getCreated());
                map.put("modified",m.getModified());
                map.put("status",m.getStatus());
                map.put("enabled",m.getEnabled());
                mapList.add(map);
            }
        }
        mapPage.setItems(mapList);
        mapPage.setAllrecords(page.getAllResults());
        return Results.html().render("page",mapPage);
    }

    public Result add(Context context){
        CategoryDto categoryDto = new CategoryDto();
        return Results.html().render("item",categoryDto);
    }

    public Result create(Context context, @JSR303Validation CategoryDto dto,
                         Validation validation){
        if (validation.hasViolations()){
            context.getFlashScope().error(getErrorMessages(validation));
            return Results.html().template("/yun/views/staff/CategoryController/add.ftl.html").render("item",dto);
        } else {
            CommonSdo<Category> sdo = categoryService.createCategory(dto);
            if (sdo.isSuccess()){
                context.getFlashScope().success(sdo.getMessage());
                return Results.redirect("/staff/category/show/"+sdo.getItem().getId());
            }else {
                context.getFlashScope().error(sdo.getMessage());
                return Results.html().template("/yun/views/staff/CategoryController/add.ftl.html").render("item",dto);
            }
        }
    }

    public Result show(Context context, @PathParam("id") Long id){
        Category category = categoryService.find(id);
        if (null == category){
            context.getFlashScope().error("没有找到该软件类别信息");
            return Results.redirect("/staff/category");
        }
        return Results.html().render("item",category);
    }

    public Result edit(Context context, @PathParam("id") Long id){
        Category category = categoryService.find(id);
        if (null == category){
            context.getFlashScope().error("没有找到该软件类别信息");
            return Results.redirect("/staff/category");
        }
        return Results.html().render("item",category).render("id",category.getId());
    }

    public Result update(Context context,
                         @PathParam("id") Long id,
                         @JSR303Validation CategoryDto dto,
                         Validation validation) {
        Category category = categoryService.find(id);
        if (null == category){
            context.getFlashScope().error("没有找到该软件类别信息");
            return Results.redirect("/staff/category");
        }

        if (validation.hasViolations()){
            context.getFlashScope().error(getErrorMessages(validation));
            return Results.html().template("/yun/views/staff/CategoryController/edit.ftl.html")
                    .render("item",category).render("id",category.getId());
        }else {
            CommonSdo<Category> sdo = categoryService.updateCategory(category,dto);
            if (sdo.isSuccess()) {
                context.getFlashScope().success(sdo.getMessage());
                return Results.redirect("/staff/category/show/"+sdo.getItem().getId());
            } else {
                context.getFlashScope().error(sdo.getMessage());
                return Results.html().template("/yun/views/staff/CategoryController/edit.ftl.html")
                        .render("item",dto).render("id",category.getId());
            }
        }
    }

    public Result disable(Context context, @PathParam("id") Long id){
        Category category = categoryService.find(id);
        if (null == category){
            context.getFlashScope().error("没有找到该软件类别信息");
            return Results.redirect("/staff/category");
        }
        category.setEnabled(false);
        category.setStatus(false);
        category.setModified(new Date());
        categoryService.update(category);
        return Results.redirect("/staff/category");
    }

    public Result enabled(Context context, @PathParam("id") Long id){
        Category category = categoryService.find(id);
        if (null == category){
            context.getFlashScope().error("没有找到该软件类别信息");
            return Results.redirect("/staff/category");
        }
        category.setEnabled(true);
        category.setStatus(true);
        category.setModified(new Date());
        categoryService.update(category);
        return Results.redirect("/staff/category");
    }

    public Result delete(Context context, @PathParam("id") Long id){
        Category category = categoryService.find(id);
        if (null == category){
            context.getFlashScope().error("没有找到该软件类别信息");
            return Results.redirect("/staff/category");
        }
        category.setStatus(false);
        category.setEnabled(false);
        category.setModified(new Date());
        categoryService.update(category);
        return Results.redirect("/staff/category");
    }

}
