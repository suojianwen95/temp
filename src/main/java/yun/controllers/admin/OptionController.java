package yun.controllers.admin;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ninja.Context;
import ninja.Result;
import ninja.Results;
import ninja.params.Param;
import ninja.params.PathParam;
import ninja.validation.JSR303Validation;
import ninja.validation.Validation;
import yun.controllers.BaseController;
import yun.dto.OptionDto;
import yun.models.Option;
import yun.service.CommonSdo;
import yun.service.OptionService;
import yun.utils.Page;

import java.util.List;
import java.util.Map;

@Singleton
public class OptionController extends BaseController{

    @Inject
    OptionService optionService;

    public Result index (Context context,
                         @Param("page") Integer p,
                         @Param("limit") Integer limit) {
        if (null == p||p<1){p=1;}
        if (null == limit){limit=10;}

        Page<Option> page = optionService.listPaged(p,limit);
        Page<Map<String,Object>> mapPage = new Page<Map<String, Object>>(page.getCurrent(),page.getMaxResults());
        List<Map<String,Object>> mapList = Lists.newArrayList();

        if (page.getItems().size()>0){
            for ( Option m: page.getItems()){
                Map<String,Object> map = Maps.newHashMap();
                map.put("id",m.getId());
                map.put("name",m.getName());
                map.put("value",m.getValue());
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
        Option option = optionService.find(id);
        if (null == option){
            context.getFlashScope().error("没有找到该参数");
            return Results.redirect("/admin/option");
        }
        return Results.html().render("item",option);
    }

    public Result update(Context context,
                         @PathParam("id") Long id,
                         @JSR303Validation OptionDto dto,
                         Validation validation) {
        Option option = optionService.find(id);
        if (null == option){
            context.getFlashScope().error("没有找到该参数");
            return Results.redirect("/admin/option");
        }

        if(null == dto.getValue() || "".equals(dto.getValue())){
            context.getFlashScope().error("请设置参数值");
            return Results.html().template("/yun/views/admin/OptionController/edit.ftl.html")
                    .render("item",option);
        } else {
            CommonSdo<Option> sdo = optionService.updateOption(option,dto);
            if (sdo.isSuccess()) {
                context.getFlashScope().success(sdo.getMessage());
                return Results.redirect("/admin/option");
            } else {
                context.getFlashScope().error(sdo.getMessage());
                return Results.html().template("/yun/views/admin/OptionController/edit.ftl.html")
                        .render("item",dto);
            }
        }
    }

}
