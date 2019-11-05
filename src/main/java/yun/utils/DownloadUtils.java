package yun.utils;

import com.google.common.io.ByteStreams;
import com.google.inject.Inject;
import ninja.Context;
import ninja.Renderable;
import ninja.Result;
import ninja.Results;
import ninja.utils.HttpCacheToolkit;
import ninja.utils.MimeTypes;
import ninja.utils.ResponseStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class DownloadUtils {

    final static Logger logger = LoggerFactory.getLogger(DownloadUtils.class);

    @Inject
    HttpCacheToolkit httpCacheToolkit;

    @Inject
    MimeTypes mimeTypes;

    @Inject
    UploadUtils uploadUtils;


    public Renderable render(String app, String uniqueName, String module){
        DownloadUtils that = this;
        Renderable renderable = new Renderable() {

            public void render(Context context, Result result) {
                final String fileName = uploadUtils.getFilePath(app,module,uniqueName);
                try {
                    URL url = new File(fileName).toURI().toURL();
                    that.streamOutUrlEntity(url, context, result);
                }catch (MalformedURLException ex){
                    logger.error(ex.getMessage());
                }
            }
        };
        return renderable;
    }


    public void streamOutUrlEntity(URL url, Context context, Result result) {
        if (url == null) {
            context.finalizeHeadersWithoutFlashAndSessionCookie(Results.notFound());
        } else {
            try {
                URLConnection e = url.openConnection();
                Long lastModified = Long.valueOf(e.getLastModified());
                this.httpCacheToolkit.addEtag(context, result, lastModified);
                if (result.getStatusCode() == 304) {
                    context.finalizeHeadersWithoutFlashAndSessionCookie(result);
                } else {
                    result.status(200);
                    String mimeType = this.mimeTypes.getContentType(context, url.getFile());
                    if (mimeType != null && !mimeType.isEmpty()) {
                        result.contentType(mimeType);
                    }

                    ResponseStreams responseStreams = context.finalizeHeadersWithoutFlashAndSessionCookie(result);
                    InputStream inputStream = e.getInputStream();
                    Throwable var9 = null;

                    try {
                        OutputStream outputStream = responseStreams.getOutputStream();
                        Throwable var11 = null;

                        try {
                            ByteStreams.copy(inputStream, outputStream);
                        } catch (Throwable var36) {
                            var11 = var36;
                            throw var36;
                        } finally {
                            if (outputStream != null) {
                                if (var11 != null) {
                                    try {
                                        outputStream.close();
                                    } catch (Throwable var35) {
                                        var11.addSuppressed(var35);
                                    }
                                } else {
                                    outputStream.close();
                                }
                            }

                        }
                    } catch (Throwable var38) {
                        var9 = var38;
                        throw var38;
                    } finally {
                        if (inputStream != null) {
                            if (var9 != null) {
                                try {
                                    inputStream.close();
                                } catch (Throwable var34) {
                                    var9.addSuppressed(var34);
                                }
                            } else {
                                inputStream.close();
                            }
                        }

                    }
                }
            } catch (IOException var40) {
                logger.error("error streaming file", var40);
            }
        }
    }
}
