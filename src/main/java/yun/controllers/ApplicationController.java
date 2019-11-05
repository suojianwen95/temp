

package yun.controllers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ninja.Context;
import ninja.Result;
import ninja.Results;
import ninja.params.Param;
import yun.models.Category;
import yun.models.Soft;
import yun.service.CategoryService;
import yun.service.SoftService;
import yun.utils.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Singleton
public class ApplicationController {

    @Inject
    CategoryService categoryService;

    @Inject
    SoftService softService;

    public Result setup() {
        return Results.ok();
    }

    public Result index(Context context) {

        int num = 8;
        List<Category> categoryList = categoryService.findAll();
        List<Soft> softList = softService.getNumByBest(10,"sequence","desc");
        return Results.html().render("categoryList",categoryList).render("softList",softList);
    }

    public Result getAllSoft(Context context) {
        int num = 6;
        List<Category> categoryList = categoryService.findAll();
        List<List<Map<String,Object>>> allSoft = new ArrayList<>();
        for(Category e:categoryList) {
            List<Soft> softList = softService.getNumByCategoryId(num,e,"sequence", "DESC");
            List<Map<String,Object>> mapList = Lists.newArrayList();
            for ( Soft m: softList) {
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
                    map.put("summary",m.getSummary());
                    map.put("content",m.getContent());
                    mapList.add(map);
                }
            allSoft.add(mapList);
        }
        return Results.json().render("allSoft",allSoft);
    }

    /***
     * 依据分类查询软件类别
     * @param context
     * @param p
     * @param limit
     * @param categoryId
     * @return
     */
    public Result listByCategory(Context context,
                                 @Param("page") Integer p,
                                 @Param("limit") Integer limit,
                                 @Param("categoryId") Long categoryId) {
        List<Category> categoryList = categoryService.findAll();
        if (null == p||p<1){p=1;}
        if (null == limit){limit=10;}
        Page<Soft> page = null;
        Category category = null;
        if (null == categoryId){
            page = softService.listPagedEnabled(p,limit,"sequence","DESC");
        } else {
            category = categoryService.find(categoryId);
            page = softService.listPagedByCategoryId(p,limit, category,"sequence","DESC");
        }
        return Results.html().render("page",page).render("category",category).render("categoryList",categoryList).render("categoryId",categoryId);
    }

    /***
     * 软件详情
     * @param context
     * @param softId
     * @return
     */
    public Result detail(Context context, @Param("softId") Long softId) {
        List<Category> categoryList = categoryService.findAll();
        Soft soft = softService.find(softId);
        if (null == soft){
            context.getFlashScope().error("没有找到该软件信息");
            return Results.redirect("/");
        }
        List<Soft> softList = softService.getNumByBest(4,"sequence","desc");
        return Results.html().render("item",soft).render("categoryList",categoryList).render("softList",softList);
    }
}
