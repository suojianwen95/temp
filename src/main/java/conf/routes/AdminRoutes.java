package conf.routes;

import com.google.inject.Singleton;
import ninja.Router;
import ninja.application.ApplicationRoutes;
import yun.controllers.admin.MemberController;
import yun.controllers.admin.OptionController;

@Singleton
public class AdminRoutes implements ApplicationRoutes {

    @Override
    public void init(Router router) {

        router.GET().route(url("")).with(MemberController.class,"index");
        // 用户管理
        router.GET().route(url("/member")).with(MemberController::index);
        router.GET().route(url("/member/add")).with(MemberController::add);
        router.POST().route(url("/member/create")).with(MemberController::create);
        router.GET().route(url("/member/show/{id}")).with(MemberController::show);
        router.GET().route(url("/member/edit/{id}")).with(MemberController::edit);
        router.POST().route(url("/member/update/{id}")).with(MemberController::update);
        router.GET().route(url("/member/disable/{id}")).with(MemberController::disable);
        router.GET().route(url("/member/enabled/{id}")).with(MemberController::enabled);
        router.GET().route(url("/member/delete/{id}")).with(MemberController::delete);

        // 系统参数
        router.GET().route(url("/option")).with(OptionController::index);
        router.GET().route(url("/option/edit/{id}")).with(OptionController::edit);
        router.POST().route(url("/option/update/{id}")).with(OptionController::update);

    }
    protected String url(String url){
        return new StringBuffer("/admin").append(url).toString();
    }


}
