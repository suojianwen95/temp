package conf.routes;

import com.google.inject.Singleton;
import ninja.Router;
import ninja.application.ApplicationRoutes;
import yun.controllers.customer.SoftController;

@Singleton
public class CustomerRoutes implements ApplicationRoutes {

    @Override
    public void init(Router router) {

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

    }
    protected String url(String url){
        return new StringBuffer("/customer").append(url).toString();
    }


}
