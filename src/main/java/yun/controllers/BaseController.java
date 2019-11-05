package yun.controllers;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ninja.Context;
import ninja.FilterWith;
import ninja.validation.FieldViolation;
import ninja.validation.Validation;
import yun.filters.SecurityFilter;
import yun.models.Member;
import yun.service.MemberService;
import yun.utils.CookieUtils;


@Singleton
@FilterWith(value = {
        SecurityFilter.class
})
public abstract class BaseController {

    @Inject
    CookieUtils cookieUtils;
    @Inject
    MemberService memberService;
    /**
     * 判断当前是否有登录用户
     * @return Boolean
     */
    protected boolean isLogin(Context context){
        String memberId = cookieUtils.getCookie(context,"member_id");
        return (null!=memberId&&!"".equals(memberId));
    }

    protected Member getCurrent(Context context){
        String memberId = cookieUtils.getCookie(context,"member_id");
        Member member = null;
        if(null!=memberId&&!"".equals(memberId)){
            member = memberService.find(Long.valueOf(memberId));
        }
        return member;
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
