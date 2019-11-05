package yun.utils;


import com.google.common.base.Strings;
import com.google.common.net.InternetDomainName;
import com.google.inject.Singleton;
import ninja.Context;
import ninja.Cookie;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;

@Singleton
public class CookieUtils {

    static final Logger logger = LoggerFactory.getLogger(CookieUtils.class);


    Integer maxAge;

    public CookieUtils(){}

    public CookieUtils(int cookieMaxAge){
        maxAge = cookieMaxAge;
    }

    protected int getMaxAge(){
        if(null==maxAge||0==maxAge){
            return Integer.valueOf(3600).intValue();
        }else{
            return maxAge;
        }
    }

    public void setPlainCookie(Context context, String key, String content){
        String hostName = context.getHostname();
        hostName = hostName.substring(0, hostName.indexOf(":"));

        String domain = hostName;
        try {
            Cookie cookie  = Cookie.builder(key, URLEncoder.encode(content,"utf-8"))
//                    .setDomain(domain)
                    .setMaxAge(getMaxAge())
                    .setPath("/")
                    .build();
            context.getSession().put(key,content);
            context.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }


    public void setCookie(Context context, String key, String content) throws UnsupportedEncodingException {
        key = encryptKey(key);
        content = this.encryptValue(content);
        this.setPlainCookie(context,key,content);
    }

    public String getPlainCookie(Context context, String key){
        if(context.hasCookie(key)){
            Cookie cookie = context.getCookie(key);
            try {
                return URLDecoder.decode(cookie.getValue(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else{
            return null;
        }
        return key;
    }

    public String getCookie(Context context, String key){
        key = encryptKey(key);
        String value = getPlainCookie(context,key);
        if(!Strings.isNullOrEmpty(value)){
            return decryptValue(value);
        }else{
            return null;
        }
    }

    public void clearCookie(Context context){
        for(Cookie c : context.getCookies()){
            context.unsetCookie(c);
            String hostName = context.getHostname();
            hostName = hostName.substring(0, hostName.indexOf(":"));

            String domain = hostName;

            Cookie cookie = Cookie.builder(c.getName(), "")
//                    .setDomain(domain)
                    .setMaxAge(0)
                    .setPath("/")
                    .build();
            context.addCookie(cookie);
        }
        context.getSession().clear();
        context.cleanup();
    }

    public void removeCookie(Context context, String key){
        if(context.hasCookie(key)){
            Cookie cookie = context.getCookie(key);
            context.unsetCookie(cookie);
            context.addCookie(Cookie.builder(cookie).setMaxAge(0).build());
        }
        context.getSession().remove(key);
    }

    public String encryptValue(String string){
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(valueSalt);
        return encryptor.encrypt(string);
    }

    public String decryptValue(String string){
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(valueSalt);
        return encryptor.decrypt(string);
    }

    public String encryptKey(String string){
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }

        byte[] byteArray = new byte[0];
        try {
            byteArray = string.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
            return null;
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    private static final String valueSalt = "adsfklasdasdfadsfasdfasfewvadsfksdfasfdg";
}