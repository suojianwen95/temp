package yun.filters;


import com.google.common.base.Strings;
import com.google.inject.Inject;
import ninja.*;
import org.slf4j.Logger;
import yun.models.Level;
import yun.utils.CookieUtils;

public class SecurityFilter implements Filter {

    @Inject
    Logger logger;
    @Inject
    CookieUtils cookieUtils;


    @Override
    public Result filter(FilterChain filterChain, Context context) {

        String path = context.getRequestPath();
        String memberLevels = cookieUtils.getCookie(context,"member_levels");
        String nickname = cookieUtils.getPlainCookie(context,"member");
        logger.info("用户名{}通过ip地址{}访问后台管理:{}",nickname,context.getRemoteAddr(),context.getRequestPath());
        if(Strings.isNullOrEmpty(memberLevels)){
            logger.info("用户名{}通过ip地址{}访问后台管理:{},登录超时，需要重新登录",nickname,context.getRemoteAddr(),context.getRequestPath());
            return Results.redirect("/logout");
        }
        if(path.startsWith("/staff")){
            if(!memberLevels.contains(Level.UID_STAFF)) {
                logger.info("用户 {} 尝试访问 /staff 权限失败",nickname);
                return Results.redirect("/logout");
            }
        }
        return filterChain.next(context);
    }
}
