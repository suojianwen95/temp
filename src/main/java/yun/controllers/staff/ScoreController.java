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
import ninja.uploads.FileItem;
import yun.controllers.BaseController;
import yun.dto.SoftDto;
import yun.models.Category;
import yun.models.Soft;
import yun.service.CategoryService;
import yun.service.CommonSdo;
import yun.service.SoftService;
import yun.utils.Page;

import java.util.List;
import java.util.Map;

@Singleton
public class ScoreController extends BaseController {

    @Inject
    SoftService softService;
    @Inject
    CategoryService categoryService;

    public Result index (Context context,
                         @Param("page") Integer p,
                         @Param("limit") Integer limit) {
        if (null == p||p<1){p=1;}
        if (null == limit){limit=10;}

        Page<Soft> page = softService.listPaged(p,limit);
        Page<Map<String,Object>> mapPage = new Page<Map<String, Object>>(page.getCurrent(),page.getMaxResults());
        List<Map<String,Object>> mapList = Lists.newArrayList();

        if (page.getItems().size()>0){
            for ( Soft m: page.getItems()){
                Map<String,Object> map = Maps.newHashMap();
                map.put("id",m.getId());
                map.put("name",m.getName());
                map.put("attach",m.getAttach());
                map.put("categoryName",m.getCategory().getName());
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
        return Results.html().render("page",mapPage);
    }

    public Result edit(Context context, @PathParam("id") Long id){
        List<Category> categoryList = categoryService.getCategoryList();
        Soft soft = softService.find(id);
        if (null == soft){
            context.getFlashScope().error("没有找到该软件");
            return Results.redirect("/staff/score");
        }
        return Results.html().render("item",soft).render("id",soft.getId()).render("categoryList",categoryList);
    }

    public Result update(Context context,
                         @PathParam("id") Long id) {
        List<Category> categoryList = categoryService.getCategoryList();
        Soft soft = softService.find(id);
        if (null == soft){
            context.getFlashScope().error("没有找到该软件");
            return Results.redirect("/staff/soft");
        }
        String score = context.getParameter("score");
        CommonSdo<Soft> sdo = softService.updateSoftScore(soft,score== null?0:Integer.parseInt(score));
        if (sdo.isSuccess()){
            context.getFlashScope().success(sdo.getMessage());
            return Results.redirect("/staff/score/show/"+sdo.getItem().getId());
        }else {
            context.getFlashScope().error(sdo.getMessage());
            return Results.html().template("/yun/views/staff/ScoreController/edit.ftl.html").render("item",soft).render("id",soft.getId()).render("categoryList",categoryList);
        }
    }

    public Result show(Context context, @PathParam("id") Long id){
        Soft soft = softService.find(id);
        if (null == soft){
            context.getFlashScope().error("没有找到该软件的信息");
            return Results.redirect("/staff/score");
        }
        return Results.html().render("item",soft);
    }
}
