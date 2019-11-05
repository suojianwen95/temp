package yun.utils;

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteStreams;
import com.google.inject.Inject;
import ninja.Context;
import ninja.Renderable;
import ninja.Result;
import ninja.Results;
import ninja.utils.HttpCacheToolkit;
import ninja.utils.MimeTypes;
import ninja.utils.ResponseStreams;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

public class ImageUtils {

    final static Logger logger = LoggerFactory.getLogger(ImageUtils.class);

    @Inject
    HttpCacheToolkit httpCacheToolkit;

    @Inject
    MimeTypes mimeTypes;

    @Inject
    UploadUtils uploadUtils;

    public BufferedImage thumbnail(BufferedImage image,int width, int height, Boolean upscale){
        return image;
    }

    /**
     * Transforms 50x40 to 32x32, while cropping the width
     * upscale==true:a smaller image will be expanded to the requested
     * upscale==false:a smaller image will be left as it
     */
    public BufferedImage outbound(BufferedImage image,int width,Boolean upscale){
        return inset(image,width,width,upscale);
    }

    /**
     * Transforms 50x40 to 32x26, no cropping
     * upscale==true:a smaller image will be expanded to the requested
     * upscale==false:a smaller image will be left as it
     */
    public BufferedImage inset(BufferedImage image,int width,int height,Boolean upscale){
        Image scaledImage = new ImageIcon(image.getScaledInstance(width,height, BufferedImage.SCALE_SMOOTH)).getImage();
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            int transparency = Transparency.OPAQUE;
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                    scaledImage.getWidth(null), scaledImage.getHeight(null), transparency);
        } catch (HeadlessException e) {
        }

        if (bimage == null) {
            int type = BufferedImage.TYPE_INT_RGB;
            bimage = new BufferedImage(scaledImage.getWidth(null), scaledImage.getHeight(null), type);
        }
        Graphics g = bimage.createGraphics();
        g.drawImage(scaledImage, 0, 0, null);
        g.dispose();
        return bimage;
    }

    /**
     *   relative_heighten: { heighten: 60 } # Transforms 50x40 to 75x60
     */
    public BufferedImage relativeHeight(BufferedImage image,int height){
        int h = image.getHeight();
        int w = image.getWidth();

        BigDecimal bw = BigDecimal.valueOf(height).multiply(BigDecimal.valueOf(w))
                .divide(BigDecimal.valueOf(h),BigDecimal.ROUND_UP);

        logger.info("picture new width is {}",bw.intValue());

        return inset(image, bw.intValue() ,height,true);
    }

    /**
     *   relative_width: { widen: 32 }    # Transforms 50x40 to 32x26
     */
    public BufferedImage relativeWidth(BufferedImage image,int width){
        return image;
    }
    /**
     *   relative_increase: { increase: 10 } # Transforms 50x40 to 60x50
     */
    public BufferedImage relativeIncrease(BufferedImage image,int increase){
        return image;
    }

    /**
     *   relative_scale: { scale: 2.5 }   # Transforms 50x40 to 125x100
     */
    public BufferedImage relativeScale(BufferedImage image,int scale){
        return image;
    }

    /**
     * performs an upscale transformation on your image to increase its size to the given dimensions
     * upscale: { min: [800, 600] }
     */
    public BufferedImage upscale(BufferedImage image,int width,int height){
        return image;
    }

    /**
     * performs a downscale transformation on your image to reduce its size to the given dimensions
     * downscale: { max: [1980, 1280] }
     */
    public BufferedImage downscale(BufferedImage image,int width,int height){
        return image;
    }

    public BufferedImage crop(BufferedImage image,int x, int y,int width,int height){
        return image.getSubimage(x,y,width,height);
    }

    /**
     * quality=80,60,75
     */
    public BufferedImage quality(BufferedImage image,int quality){
        //quality = quality/100;
        return image;
    }

    /**
     * #335577
     */
    public BufferedImage background(BufferedImage image, String color){
        return image;
    }

    /**
     * Position: One of topleft,top,topright,left,center,right,bottomleft,bottom,bottomright
     * Size of the watermark relative to the origin img size
     */
    public BufferedImage watermark(BufferedImage image,BufferedImage watermark,int size,String position){
        return image;
    }

    public BufferedImage load(String assets){
        BufferedImage image = null;
        try {
            String path = new StringBuilder(System.getProperty("user.dir"))
                    .append(File.separator)
                    .append("src")
                    .append(File.separator)
                    .append("main")
                    .append(File.separator)
                    .append("java")
                    .append(File.separator)
                    .append("assets")
                    .append(File.separator)
                    .append(assets)
                    .toString();
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return image;
    }

    public BufferedImage load(String app,String module,String uniqueName){
        BufferedImage image = null;
        String path = uploadUtils.getFilePath(app,module,uniqueName);
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return image;
    }

    public String getCacheRoot(){
        return new StringBuilder(System.getProperty("user.dir"))
                .append(File.separator)
                .append("cache")
                .append(File.separator)
                .append("img")
                .append(File.separator)
                .toString();
    }

    public String getCacheKey(String module,String uniqueName){
        Hasher hasher = Hashing.md5().newHasher();
        hasher.putString(module+uniqueName, Charset.forName("UTF8"));
        return hasher.hash().toString();
    }


    public String getCachePath(String app,String module,String cid,String uniqueName){
        StringBuilder sb = new StringBuilder(getCacheRoot())
                .append(app).append(File.separator)
                .append(module).append(File.separator)
                .append(cid).append(File.separator);

        String key = getCacheKey(module,uniqueName);
        for(int i = 1;i<=6;i++){
            sb.append(key.substring(0,i)).append(File.separator);
        }
        sb.append(key)
        .append(".")
        .append(FilenameUtils.getExtension(uniqueName));
        return sb.toString();
    }

    public Boolean cached(String app,String cid, String module,String uniqueName){
        return new File(getCachePath(app,module,cid,uniqueName)).exists();
    }

    public Renderable render(String app, String cid, String module, String uniqueName, BufferedImage image){
        ImageUtils that = this;

        String cachePath = getCachePath(app,module,cid,uniqueName);

        Renderable renderable = new Renderable() {
            @Override
            public void render(Context context, Result result) {
                if(null!=image){
                    new File(FilenameUtils.getFullPath(cachePath)).mkdirs();
                    File cachedFile = new File(cachePath);
                    try {
                        ImageIO.write(image, FilenameUtils.getExtension(uniqueName), cachedFile);
                    }catch (IOException ex){
                        logger.error(ex.getMessage());
                    }
                }

                try {
                    URL url = new File(cachePath).toURI().toURL();
                    that.streamOutUrlEntity(url, context, result);
                }catch (MalformedURLException ex){
                    logger.error("未找到{}:{}:{}:{}的缓存",app,cid,module,uniqueName);
                }
            }
        };
        return  renderable;
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
