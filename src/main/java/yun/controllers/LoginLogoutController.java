package yun.controllers;


import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ninja.Context;
import ninja.Result;
import ninja.Results;
import ninja.Router;
import ninja.params.Param;
import ninja.validation.FieldViolation;
import ninja.validation.JSR303Validation;
import ninja.validation.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yun.controllers.admin.MemberController;
import yun.controllers.customer.SoftController;
import yun.controllers.staff.StaffController;
import yun.dto.RegDto;
import yun.models.Level;
import yun.models.Member;
import yun.models.ResetCode;
import yun.service.LevelService;
import yun.service.MemberService;
import yun.utils.CookieUtils;
import yun.utils.MailUtils;

import java.io.UnsupportedEncodingException;

@Singleton
public class LoginLogoutController {

    final private static Logger logger = LoggerFactory.getLogger(LoginLogoutController.class);

    @Inject
    Router router;
    @Inject
    MemberService memberService;
    @Inject
    MailUtils mailUtils;
    @Inject
    CookieUtils cookieUtils;
    @Inject
    LevelService levelService;


    public Result login(Context context){
        return Results.html();
    }

    public Result doLogin(Context context,
                          @Param("username") String username,
                          @Param("password") String password) {

        Member member = memberService.getByUsername(username);
        if (null ==member){
            context.getFlashScope().error("该用户不存在");
            return Results.redirect("/login");
        }
        if (!member.getStatus()){
            context.getFlashScope().error("该用户已被删除");
            return Results.redirect("/login");
        }
        if(!member.validPassword(password)){
            context.getFlashScope().error("密码输入错误，请重试");
            return Results.redirect("/login");
        } else {
            try {
                cookieUtils.setCookie(context,"member_id",String.valueOf(member.getId()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                cookieUtils.setCookie(context,"member_levels",member.getLevelsString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            cookieUtils.setPlainCookie(context,"levels",member.getLevelsSecurityString(cookieUtils));
            cookieUtils.setPlainCookie(context,"member",member.getNickname());
        }

        if (member.isAdmin() ) {
            return Results.redirect(router.getReverseRoute(MemberController.class,"index"));
        } else if (member.isStaff()) {
            return Results.redirect(router.getReverseRoute(StaffController.class,"index"));
        } else if (member.isCustomer()) {
            return Results.redirect(router.getReverseRoute(SoftController.class,"index"));
        } else {
            return Results.redirect(router.getReverseRoute(ApplicationController.class,"index"));
        }
    }

    public Result logout(Context context){
        cookieUtils.clearCookie(context);
        return Results.redirect(router.getReverseRoute(ApplicationController.class,"index"));
    }

    //修改密码
    public Result updatePassword(Context context){
        return Results.html();
    }
    //确认修改
    public Result postPassword(Context context,
                               @Param("oldpassword") String old,
                               @Param("password") String password,
                               @Param("confirmPassword") String confirmPassword) {
        String memberId = cookieUtils.getCookie(context, "member_id");
        Member member = null;
        if (null == memberId||"".equals(memberId)) {
            context.getFlashScope().error("请重新登陆后修改密码");
            return Results.redirect("/login");
        } else {
            member = memberService.find(Long.valueOf(memberId));
            if(!member.validPassword(old)){
                context.getFlashScope().error("原密码输入错误");
                return Results.redirect("/password");
            }
            if(password.length()<6){
                context.getFlashScope().error("请使用6位以及上的新密码");
                logger.info("情况是请使用6位以上的密码");
                return Results.redirect("/password");
            }
            if (!password.equals(confirmPassword)){
                context.getFlashScope().error("两次密码输入不一致");
                return Results.redirect("/password");
            }
            member.setPlainPassword(password);
            memberService.update(member);
        }
        context.getFlashScope().success("密码修改成功，请重新登录");
        cookieUtils.clearCookie(context);
        return Results.redirect("/login");
    }
    /**
     * 注册
     */
    public Result join(Context context){
        cookieUtils.clearCookie(context);
        RegDto dto = new RegDto();
        return Results.html().render("dto",dto);
    }
    /**
     * 注册
     * @param context
     * @param dto
     * @param validation
     * @return
     */
    public Result reg(Context context,
                      @JSR303Validation RegDto dto,
                      Validation validation) {
        cookieUtils.clearCookie(context);
        if(validation.hasViolations()){
            context.getFlashScope().error(getErrorMessages(validation));
            return Results.html().template("/yun/views/LoginLogoutController/join.ftl.html")
                    .render("dto",dto);
        }
        if(null!=memberService.getByUsername(dto.getUsername())){
            context.getFlashScope().error("用户名已注册");
            return Results.html().template("/yun/views/LoginLogoutController/join.ftl.html")
                    .render("dto",dto);
        }
        if(null!=memberService.getByEmail(dto.getEmail())){
            context.getFlashScope().error("邮箱已注册");
            return Results.html().template("/yun/views/LoginLogoutController/join.ftl.html")
                    .render("dto",dto);
        }
        if(!Strings.isNullOrEmpty(dto.getMobile())&&null!=memberService.getByMobile(dto.getMobile())){
            context.getFlashScope().error("手机号已注册");
            return Results.html().template("/yun/views/LoginLogoutController/join.ftl.html")
                    .render("dto",dto);
        }
        Member member = new Member();
        //新用户注册成功就是普通用户
        Level level = levelService.getLevel("7ad9d884-7c3d-4624-8473-147b0e61121a");
        member.setUsername(dto.getUsername());
        member.setNickname(dto.getNickname());
        member.setEmail(dto.getEmail());
        member.setMobile(dto.getMobile());
        member.setPlainPassword(dto.getPassword());
        member.addLevel(level);
        memberService.create(member);
        context.getFlashScope().success("注册成功！");
        return Results.html().template("/yun/views/LoginLogoutController/join.ftl.html")
                .render("dto",dto);
    }



    /**
     * 找回密码
     * @param context
     * @return
     */
    public Result reset(Context context){
        context.getSession().clear();
        return Results.html();
    }

    public Result resetAction(Context context,
                              @Param("username") String username){
        if(null==username||"".equals(username)){
            context.getFlashScope().error("请输入用户名");
            return Results.redirect("/reset");
        }
        Member member = memberService.getByUsername(username);
        if(null==member||!member.isAdmin()||!member.isStaff()){
            context.getFlashScope().error("用户名不存在");
            return Results.redirect("/reset");
        }
        ResetCode resetCode = memberService.resetCode(member);
        mailUtils.send(resetCode.getMember().getEmail(),"重置密码验证码",resetCode.getCode());
        return Results.redirect("/reset/code?id="+resetCode.getId());
    }

    public Result resetCode(Context context,
                            @Param("id") String id){
        return Results.html().render("id",id);
    }

    public Result resetCodeAction(Context context,
                                  @Param("id") Long id,
                                  @Param("code") String code){
        ResetCode resetCode = memberService.getResetCode(id);
        if(null==resetCode||!resetCode.getStatus()){
            context.getFlashScope().error("验证码已过期，请重新尝试找回密码");
            return Results.redirect("/reset/code?id="+id);
        }
        if(resetCode.getCode().equals(code)){
            context.getFlashScope().success("请输入新密码");
            memberService.validResetCode(resetCode);
            return Results.redirect("/reset/password?id="+resetCode.getId());
        }else{
            context.getFlashScope().error("验证码输入错误，请重新输入");
            return Results.html().render("id",id)
                    .template("/yun/views/LoginLogoutController/resetCode.ftl.html");
        }
    }

    public Result resetPassword(Context context,
                                @Param("id") Long id) throws UnsupportedEncodingException {
        ResetCode resetCode = memberService.getResetCode(id);
        if(null==resetCode||!resetCode.getValid()||!resetCode.getStatus()){
            context.getFlashScope().error("请先输入验证码");
            return Results.redirect("/reset");
        }
        memberService.cleanResetCode(resetCode);
        resetCode = memberService.processResetCode(resetCode);
        cookieUtils.setCookie(context,"resetting",resetCode.getToken());
        return Results.html().render("id",id).render("member",resetCode.getMember());
    }

    public Result resetPasswordAction(Context context,
                                      @Param("id") Long id,
                                      @Param("password") String password,
                                      @Param("confirmPassword") String confirmPassword){

        ResetCode resetCode = memberService.getResetCode(id);
        String token = cookieUtils.getCookie(context,"resetting");
        if(null==resetCode||resetCode.getStatus()||!resetCode.getProcessed()||null==token||!token.equals(resetCode.getToken())){
            context.getFlashScope().error("请先输入最新的验证码");
            return Results.redirect("/reset");
        }
        Member member = resetCode.getMember();
        if(!password.equals(confirmPassword)){
            context.getFlashScope().error("两次输入的新密码不匹配");
            return Results.html().render("id",id)
                    .template("/yun/views/LoginLogoutController/resetPassword.ftl.html");
        }

        if(password.length()<6){
            context.getFlashScope().error("请使用6位以及上的密码");
            logger.info("情况是请使用6位以上的密码");
            return Results.redirect("/reset/password?id="+id);
        }
        member.setPlainPassword(password);
        memberService.update(member);
        context.getFlashScope().success("密码修改成功，请重新登录");
        return Results.redirect("/login");
    }

    protected String getErrorMessages(Validation validation){
        String errorFieldMessage = null;
        for(FieldViolation fv :validation.getBeanViolations()){
            errorFieldMessage = fv.constraintViolation.getMessageKey().toString();
            break;
        }

        return errorFieldMessage;
    }
}
