package conf.routes;

import com.google.inject.Singleton;
import ninja.Router;
import ninja.application.ApplicationRoutes;
import yun.controllers.staff.*;
import yun.models.Soft;

@Singleton
public class StaffRoutes implements ApplicationRoutes {

    @Override
    public void init(Router router) {

        router.GET().route(url("")).with(StaffController.class,"index");
        // 软件类别
        router.GET().route(url("/category")).with(CategoryController::index);
        router.GET().route(url("/category/add")).with(CategoryController::add);
        router.POST().route(url("/category/create")).with(CategoryController::create);
        router.GET().route(url("/category/show/{id}")).with(CategoryController::show);
        router.GET().route(url("/category/edit/{id}")).with(CategoryController::edit);
        router.POST().route(url("/category/update/{id}")).with(CategoryController::update);
        router.GET().route(url("/category/disable/{id}")).with(CategoryController::disable);
        router.GET().route(url("/category/enabled/{id}")).with(CategoryController::enabled);
        router.GET().route(url("/category/delete/{id}")).with(CategoryController::delete);

        // 软件信息
        router.GET().route(url("/soft")).with(SoftController::index);
        router.GET().route(url("/soft/add")).with(SoftController::add);
        router.POST().route(url("/soft/create")).with(SoftController::create);
        router.GET().route(url("/soft/show/{id}")).with(SoftController::show);
        router.GET().route(url("/soft/edit/{id}")).with(SoftController::edit);
        router.POST().route(url("/soft/update/{id}")).with(SoftController::update);
        router.GET().route(url("/soft/disable/{id}")).with(SoftController::disable);
        router.GET().route(url("/soft/enabled/{id}")).with(SoftController::enabled);
        router.GET().route(url("/soft/delete/{id}")).with(SoftController::delete);

        // 评分管理
        router.GET().route(url("/score")).with(ScoreController::index);
        router.GET().route(url("/score/edit/{id}")).with(ScoreController::edit);
        router.POST().route(url("/score/update/{id}")).with(ScoreController::update);
        router.GET().route(url("/score/show/{id}")).with(ScoreController::show);

    }
    protected String url(String url){
        return new StringBuffer("/staff").append(url).toString();
    }


}
