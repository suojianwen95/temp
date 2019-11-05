package yun.controllers.admin;

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
import yun.dto.MemberDto;
import yun.models.Member;
import yun.service.CommonSdo;
import yun.service.MemberService;
import yun.utils.Page;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Singleton
public class MemberController extends BaseController{

    @Inject
    MemberService memberService;

    public Result index (Context context,
                         @Param("page") Integer p,
                         @Param("limit") Integer limit) {
        if (null == p||p<1){p=1;}
        if (null == limit){limit=10;}

        Page<Member> page = memberService.listPaged(p,limit);
        Page<Map<String,Object>> mapPage = new Page<Map<String, Object>>(page.getCurrent(),page.getMaxResults());
        List<Map<String,Object>> mapList = Lists.newArrayList();

        if (page.getItems().size()>0){
            for (Member m: page.getItems()){
                Map<String,Object> map = Maps.newHashMap();
                map.put("id",m.getId());
                map.put("username",m.getUsername());
                map.put("nickname",m.getNickname());
                map.put("email",m.getEmail());
                map.put("mobile",m.getMobile());
                map.put("created",m.getCreated());
                map.put("modified",m.getMobile());
                map.put("status",m.getStatus());
                map.put("enabled",m.getEnabled());
                if (m.isCustomer()){
                    map.put("type","普通用户");
                }
                if (m.isStaff()){
                    map.put("type","运营人员");
                }
                if (m.isAdmin()){
                    map.put("type","管理员");
                }
                mapList.add(map);
            } 
        }
        mapPage.setItems(mapList);
        mapPage.setAllrecords(page.getAllResults());
        return Results.html().render("page",mapPage);
    }

    public Result add(Context context){
        MemberDto MemberDto = new MemberDto();
        return Results.html().render("item",MemberDto);
    }

    public Result create(Context context,
                         @JSR303Validation MemberDto dto,
                         Validation validation){
        if (validation.hasViolations()){
            context.getFlashScope().error(getErrorMessages(validation));
            return Results.html().template("/yun/views/admin/MemberController/add.ftl.html")
                    .render("item",dto);
        }else {
            CommonSdo<Member> sdo = memberService.createMember(dto);
            if (sdo.isSuccess()){
                context.getFlashScope().success(sdo.getMessage());
                return Results.redirect("/admin/member/show/"+sdo.getItem().getId());
            }else {
                System.out.println(sdo.getMessage());
                context.getFlashScope().error(sdo.getMessage());
                return Results.html().template("/yun/views/admin/MemberController/add.ftl.html")
                        .render("item",dto);
            }
        }
    }

    public Result show(Context context,
                       @PathParam("id") Long id){
        Member member = memberService.find(id);
        if (null == member){
            context.getFlashScope().error("没有找到用户信息");
            return Results.redirect("/admin/member");
        }
        return Results.html().render("item",member);
    }

    public Result edit(Context context,
                       @PathParam("id") Long id){
        Member member = memberService.find(id);
        if (null == member){
            context.getFlashScope().error("没有找到用户信息");
            return Results.redirect("/admin/member");
        }
        return Results.html().render("item",member).render("id",member.getId());
    }

    public Result update(Context context,
                         @PathParam("id") Long id,
                         @JSR303Validation MemberDto dto,
                         Validation validation) throws BadHanyuPinyinOutputFormatCombination {
        Member member = memberService.find(id);
        if (null == member){
            context.getFlashScope().error("没有找到该用户");
            return Results.redirect("/admin/member");
        }

        if (validation.hasViolations()){
            context.getFlashScope().error(getErrorMessages(validation));
            return Results.html().template("/yun/views/admin/MemberController/edit.ftl.html")
                    .render("item",member).render("id",member.getId());
        }else {
            CommonSdo<Member> sdo = memberService.updateMember(member,dto);
            if (sdo.isSuccess()){
                context.getFlashScope().success(sdo.getMessage());
                return Results.redirect("/admin/member/show/"+sdo.getItem().getId());
            }else {
                context.getFlashScope().error(sdo.getMessage());
                return Results.html().template("/yun/views/admin/MemberController/edit.ftl.html")
                        .render("item",dto).render("id",member.getId());
            }
        }
    }

    public Result disable(Context context,
                          @PathParam("id") Long id){
        Member member = memberService.find(id);
        if (null == member){
            context.getFlashScope().error("没有找到该用户");
            return Results.redirect("/admin/member");
        }
        member.setEnabled(false);
        member.setStatus(false);
        member.setModified(new Date());
        memberService.update(member);
        return Results.redirect("/admin/member");
    }

    public Result enabled(Context context,
                          @PathParam("id") Long id){
        Member member = memberService.find(id);
        if (null == member){
            context.getFlashScope().error("没有找到该用户");
            return Results.redirect("/admin/member");
        }
        member.setEnabled(true);
        member.setStatus(true);
        member.setModified(new Date());
        memberService.update(member);
        return Results.redirect("/admin/member");
    }

    public Result delete(Context context,
                          @PathParam("id") Long id){
        Member member = memberService.find(id);
        if (null == member){
            context.getFlashScope().error("没有找到该用户");
            return Results.redirect("/admin/member");
        }
        member.setStatus(false);
        member.setEnabled(false);
        member.setModified(new Date());
        memberService.update(member);
        return Results.redirect("/admin/member");
    }


}
