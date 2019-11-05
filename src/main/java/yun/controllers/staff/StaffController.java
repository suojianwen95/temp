package yun.controllers.staff;


import com.google.inject.Singleton;
import ninja.Context;
import ninja.Result;
import ninja.Results;
import yun.controllers.BaseController;

@Singleton
public class StaffController extends BaseController{

    public Result index(Context context){
        return Results.html();
    }

}
