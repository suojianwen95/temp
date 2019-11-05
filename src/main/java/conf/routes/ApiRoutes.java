package conf.routes;

import com.google.inject.Singleton;
import ninja.Router;
import ninja.application.ApplicationRoutes;
import yun.controllers.ApplicationController;

@Singleton
public class ApiRoutes implements ApplicationRoutes {

    @Override
    public void init(Router router) {

    }
    protected String url(String url){
        return new StringBuffer("/api").append(url).toString();
    }


}
