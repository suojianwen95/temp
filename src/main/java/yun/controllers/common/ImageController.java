package yun.controllers.common;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ninja.Context;
import ninja.Renderable;
import ninja.Result;
import ninja.Results;
import ninja.params.Param;
import ninja.params.PathParam;
import yun.controllers.BaseController;
import yun.service.CommonSdo;
import yun.utils.DownloadUtils;
import yun.utils.ImageUtils;

import java.awt.image.BufferedImage;

@Singleton
public class ImageController  {

    @Inject
    ImageUtils imageUtils;

    @Inject
    DownloadUtils downloadUtils;

    public Result show_img(Context context,
                           @Param("attach") String attach,
                           @Param("module") String module) {
        if (null == attach || "".equals(attach) || "null".equals(attach)){
            return Results.json().render(CommonSdo.error("未找到缩略图"));
        }
        if (null == module || "".equals(module)){
            return Results.json().render(CommonSdo.error("未找到缩略图"));
        }
        Renderable renderable = downloadUtils.render("yun",attach,module);
        return Results.ok().render(renderable);
    }

}
