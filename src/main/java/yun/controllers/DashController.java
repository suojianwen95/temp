package yun.controllers;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ninja.Context;
import ninja.Result;
import ninja.Results;
import ninja.params.Param;
import ninja.servlet.NinjaServletContext;
import ninja.uploads.DiskFileItemProvider;
import ninja.uploads.FileItem;
import ninja.uploads.FileProvider;
import org.apache.commons.lang.StringUtils;
import yun.utils.UploadUtils;

import java.util.List;

@Singleton
public class DashController extends BaseController{

    @Inject
    UploadUtils uploadUtils;

    public Result index(Context context){
        return Results.html();
    }

    @FileProvider(DiskFileItemProvider.class)
    public Result upload(Context context,
                         @Param("module") String module,
                         @Param("attach") FileItem attach) {

        String CKEditorFuncNum = ((NinjaServletContext) context).getHttpServletRequest().getParameter("CKEditorFuncNum");
        if (context.getParameterFileItems().isEmpty()) {
            return Results.json().render("未找到图片流")
                    .render("notice","error");
        }
        if(Strings.isNullOrEmpty(module)){
            return Results.json().render("请传入module参数").render("notice","error");
        }

        List<FileItem> fileItems = context.getParameterFileItems().values().iterator().next();
        if (null==fileItems) {
            return Results.json().render("请上传图片")
                    .render("notice","error");
        }
        try {
            String name = uploadUtils.upload("yun",module,fileItems.get(0));
            if (StringUtils.isBlank(CKEditorFuncNum)) {
                return Results.json().render("module",module)
                        .render("name",name)
                        .render("notice","success");
            } else{
                return Results.html()
                        .template("/yun/views/system/ckeditorUpload.ftl.html")
                        .render("callback", CKEditorFuncNum)
                        .render("module",module)
                        .render("name",name)
                        .render("notice","success");
            }
        } catch (Exception e) {
            return Results.json().render("message","不支持的文件类型")
                    .render("notice","error");
        }
        finally {
            fileItems.forEach(fileItem -> fileItem.cleanup());
        }
    }
}
