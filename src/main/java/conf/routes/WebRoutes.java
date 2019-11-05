package conf.routes;


import com.google.inject.Singleton;
import ninja.Router;
import ninja.application.ApplicationRoutes;
import yun.controllers.ApplicationController;
import yun.controllers.LoginLogoutController;

@Singleton
public class WebRoutes implements ApplicationRoutes{

    @Override
    public void init(Router router) {
         /////////////////////////////////////////////////////////////////////
//         Login / Logout
        /////////////////////////////////////////////////////////////////////
        router.GET().route("/").with(ApplicationController.class,"index");
        router.GET().route("/listByCategory").with(ApplicationController.class,"listByCategory");
        router.GET().route("/detail").with(ApplicationController.class,"detail");
        // 首页获取数据
        router.GET().route("/api/softList").with(ApplicationController.class, "getAllSoft");

        router.GET().route("/setup").with(ApplicationController.class,"setup");

        router.GET().route("/login").with(LoginLogoutController.class,"login");
        router.POST().route("/login").with(LoginLogoutController.class,"doLogin");

        router.GET().route("/join").with(LoginLogoutController::join);
        router.POST().route("/reg").with(LoginLogoutController.class,"reg");

        //修改密码
        router.GET().route("/password").with(LoginLogoutController.class,"updatePassword");
        router.POST().route("/update/password").with(LoginLogoutController.class,"postPassword");

        //找回密码
        router.GET().route("/reset").with(LoginLogoutController.class,"reset");
        router.POST().route("/reset").with(LoginLogoutController.class,"resetAction");
        router.GET().route("/reset/code").with(LoginLogoutController.class,"resetCode");
        router.POST().route("/reset/code").with(LoginLogoutController.class,"resetCodeAction");
        router.GET().route("/reset/password").with(LoginLogoutController.class,"resetPassword");
        router.POST().route("/reset/password").with(LoginLogoutController.class,"resetPasswordAction");

        router.GET().route("/logout").with(LoginLogoutController.class,"logout");
    }
}
