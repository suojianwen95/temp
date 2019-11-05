package yun.controllers.api.staff;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ninja.Context;
import ninja.Result;
import ninja.Results;
import ninja.params.Param;
import yun.controllers.BaseController;
import yun.utils.Page;

@Singleton
public class SoftController extends BaseController {

    public Result index (Context context,
                         @Param("page") Integer p,
                         @Param("limit") Integer limit) {

        if (null == p||p<1){p=1;}
        if (null == limit){limit=10;}
        return Results.json();
    }

}
