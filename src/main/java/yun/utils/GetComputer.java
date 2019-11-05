package yun.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class GetComputer {

    //通过截取cmd流方式得到计算机的配置信息(不好)
    public static List<String> getIpAddress() {
        Process p = null;
        List<String> address = new ArrayList<String>();
        try {
            p = new ProcessBuilder("ipconfig", "/all").start();
        } catch (Exception e) {
            return address;
        }
        StringBuffer sb = new StringBuffer();
        //读取进程输出值
        InputStream inputStream = p.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String s = "";
        try {
            while ((s = br.readLine()) != null) {
                sb.append(s + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(sb);
        return address;
    }

    public static String getIpconfig() {
        String ipconfig="";
        Map<String, String> map = System.getenv();
//        System.out.println(map.get("USERNAME"));//获取用户名
//        System.out.println(map.get("COMPUTERNAME"));//获取计算机名
//        System.out.println(map.get("USERDOMAIN"));//获取计算机域名
        ipconfig+=map.get("USERNAME")+"<br/>"+map.get("COMPUTERNAME")+"<br/>"+map.get("USERDOMAIN")+"<br/>";
        return ipconfig;
    }

    //得到计算机的ip地址和mac地址
    public static String getConfig() {
        String config = "";
        try {
            InetAddress address = InetAddress.getLocalHost();
            NetworkInterface ni = NetworkInterface.getByInetAddress(address);
            //ni.getInetAddresses().nextElement().getAddress();
            byte[] mac = ni.getHardwareAddress();
            String sIP = address.getHostAddress();
            String sMAC = "";
            Formatter formatter = new Formatter();
            for (int i = 0; i < mac.length; i++) {
                sMAC = formatter.format(Locale.getDefault(), "%02X%s", mac[i],
                        (i < mac.length - 1) ? "-" : "").toString();

            }
            System.out.println("IP：" + sIP);
            System.out.println("MAC：" + sMAC);
            config+=""+"IP：" + sIP+"<br/>"+"MAC：" + sMAC;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config;
    }

    //得到计算机的ip,名称,操作系统名称,操作系统版本
    public static String Config() {
        String config="";
        try {
            InetAddress addr = InetAddress.getLocalHost();
            String ip = addr.getHostAddress().toString(); //获取本机ip
            String hostName = addr.getHostName().toString(); //获取本机计算机名称
//            System.out.println("本机IP：" + ip + "\n本机名称:" + hostName);
            Properties props = System.getProperties();
//            System.out.println("操作系统的名称：" + props.getProperty("os.name"));
//            System.out.println("操作系统的版本：" + props.getProperty("os.version"));
            config+="本机IP：" + ip + "\n本机名称:" + hostName+"<br/>"+
                    "操作系统的名称：" + props.getProperty("os.name")+"<br/>"+
                    "操作系统的版本：" + props.getProperty("os.version");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config;
    }

    //java环境
    public static String all() {
        Properties props = System.getProperties();
        StringBuffer sb = new StringBuffer();

        sb.append("Java的运行环境版本：" + props.getProperty("java.version")+"<br/>");
        sb.append("Java的运行环境供应商：" + props.getProperty("java.vendor")+"<br/>");
        sb.append("Java供应商的URL：" + props.getProperty("java.vendor.url")+"<br/>");
        sb.append("Java的安装路径：" + props.getProperty("java.home")+"<br/>");
        sb.append("Java的虚拟机规范版本：" + props.getProperty("java.vm.specification.version")+"<br/>");
        sb.append("Java的虚拟机规范供应商：" + props.getProperty("java.vm.specification.vendor")+"<br/>");
        sb.append("Java的虚拟机规范名称：" + props.getProperty("java.vm.specification.name")+"<br/>");
        sb.append("Java的虚拟机实现版本：" + props.getProperty("java.vm.version")+"<br/>");
        sb.append("Java的虚拟机实现供应商：" + props.getProperty("java.vm.vendor")+"<br/>");
        sb.append("Java的虚拟机实现名称：" + props.getProperty("java.vm.name")+"<br/>");
        sb.append("Java运行时环境规范版本：" + props.getProperty("java.specification.version")+"<br/>");
        sb.append("Java运行时环境规范供应商：" + props.getProperty("java.specification.vender")+"<br/>");
        sb.append("Java运行时环境规范名称：" + props.getProperty("java.specification.name")+"<br/>");
        sb.append("Java的类格式版本号：" + props.getProperty("java.class.version")+"<br/>");
        sb.append("Java的类路径：" + props.getProperty("java.class.path")+"<br/>");
        sb.append("加载库时搜索的路径列表：" + props.getProperty("java.library.path")+"<br/>");
        sb.append("默认的临时文件路径：" + props.getProperty("java.io.tmpdir")+"<br/>");
        sb.append("一个或多个扩展目录的路径：" + props.getProperty("java.ext.dirs")+"<br/>");
        sb.append("操作系统的名称：" + props.getProperty("os.name")+"<br/>");
        sb.append("操作系统的构架：" + props.getProperty("os.arch")+"<br/>");
        sb.append("操作系统的版本：" + props.getProperty("os.version")+"<br/>");
        sb.append("文件分隔符：" + props.getProperty("file.separator")+"<br/>");//在 unix 系统中是＂／＂ sb.append("路径分隔符："+props.getProperty("path.separator"));//在 unix 系统中是＂:＂ System.out.println("行分隔符："+props.getProperty("line.separator"));//在 unix 系统中是＂/n＂ System.out.println("用户的账户名称："+props.getProperty("user.name"));
        sb.append("用户的主目录：" + props.getProperty("user.home")+"<br/>");
        sb.append("用户的当前工作目录：" + props.getProperty("user.dir")+"<br/>");
        return sb.toString();
    }

    public static void main(String[] args) {
//        getIpconfig();
//        getIpAddress();
//        System.out.println(Config());
//     System.out.println(all());
    }


}
