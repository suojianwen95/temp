package yun.utils;

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import ninja.uploads.FileItem;
import ninja.utils.MimeTypes;
import org.apache.commons.fileupload.FileItemHeaders;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Properties;
import java.util.UUID;

public class UploadUtils {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public FileItem getDiskFileItem(String path, String root){
        String filePath = root+path;
        File file = new File(filePath);
        if(!file.exists()){
            return null;
        }
        String filename = file.getName();
        return new FileItem() {
            @Override
            public String getFileName() {
                return filename;
            }

            @Override
            public InputStream getInputStream() {
                try {
                    return new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    logger.error(e.getMessage());
                    throw new IllegalArgumentException("没找到这个文章");
                }
            }

            @Override
            public File getFile() {
                return file;
            }

            @Override
            public String getContentType() {
                try {
                    return file.toURI().toURL().openConnection().getContentType();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                    throw new IllegalArgumentException("没找到这个文章");
                }
            }

            @Override
            public FileItemHeaders getHeaders() {
                return null;
            }

            @Override
            public void cleanup() {

            }
        };
    }

    public FileItem getDiskFileItem(String path){
        String root = new StringBuilder(System.getProperty("user.dir"))
                .append(File.separator)
                .append("src")
                .append(File.separator)
                .append("main")
                .append(File.separator)
                .append("java")
                .append(File.separator)
                .append("assets")
                .append(File.separator)
                .toString();
        return getDiskFileItem(path,root);
    }

    public String upload(String app,String module,FileItem fileItem) {

        String extension = getExtension(fileItem.getContentType());
        if(null == extension){
            throw new UnsupportedFileException();
        }
        String uniqueName = getUniqueName(module)+"."+extension;// .+.docx
        String filePath = getFilePath(app,module,uniqueName);
        InputStream inputStream = fileItem.getInputStream();
        OutputStream outputStream = null;
        try {
            outputStream =
                    new FileOutputStream(new File(filePath));

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }

            }
        }
        return uniqueName;
    }

    public String getExtension (String contentType){
        if(contentType.equals("image/jpeg")){return "jpg";}
        Properties properties = new Properties();
        InputStream is = MimeTypes.class.getResourceAsStream("mime-types.properties");
        try {
            properties.load(is);
        }catch(IOException ex){
            logger.error("Exception for UploadService, can not find mime-types.properties");
        }
        for(String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            if(value.equals(contentType)){
                return "."+key;
            }
        }
        return null;
    }

    public String getUniqueName(String module){
        Hasher hasher = Hashing.md5().newHasher();
        hasher.putString(UUID.randomUUID().toString(), Charset.forName("UTF8"));

        StringBuilder sb = new StringBuilder(hasher.hash().toString())
                .append(RandomUtils.nextInt())
                .append(RandomStringUtils.randomAlphabetic(6))
                .append(RandomStringUtils.randomAlphanumeric(8))
                .append("-")
                .append(Calendar.getInstance().getTimeInMillis())
                .append("-")
                .append(module);
        return sb.toString().toLowerCase();
    }

    public String getRootDir(){
        return System.getProperty("user.dir")+File.separator+"data"+File.separator;
    }
    public String getFilePath(String app,String module,String uniqueName){
        StringBuilder sb = new StringBuilder();
        sb.append(getRootDir())
                .append(app)
                .append(File.separator)
                .append(module)
                .append(File.separator);

        for(int i = 1;i<=4;i++){
            sb.append(uniqueName.substring(0,i)).append(File.separator);
        }

        File f = new File(sb.toString());
        if(!f.exists()){
            f.mkdirs();
        }
        return sb.append(uniqueName).toString();
    }



}
