package yun.utils;

import java.sql.*;

/**

 */
public class DataJDBC {

    static String yxt_driver = "com.mysql.jdbc.Driver";
    static String yxt_url = "jdbc:mysql://localhost:3306/bill_print_prod?useUnicode=true&characterEncoding=UTF-8&useSSL=false&useOldAliasMetadataBehavior=true&autoReconnect=true&useServerPrepStmts=true&prepStmtCacheSqlLimit=512";
    static String yxt_user = "root";
    static String yxt_passwd = "root";
    /**
     * 插入数据
     *
     * @return
     * @throws SQLException
     */
    public static int insert(String fplx,String dysj,String dyr,String sfzhm,String zdlsh,String jfr,String jfje) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int num = 0;
        try {
            Class.forName(yxt_driver).newInstance();
            con = DriverManager.getConnection(yxt_url, yxt_user, yxt_passwd);
            System.err.println("连接成功");

            String sql = "INSERT INTO yun_billing" +
                    "(created,fplx,dysj,dyr,sfzhm,zdlsh,jfr,jfje) VALUES (?,?,?,?,?,?,?,?)";
            pstmt = con.prepareStatement(sql);
            java.util.Date date=new java.util.Date();
            java.sql.Date currentDate=new java.sql.Date(date.getTime());
            pstmt.setDate(1, currentDate);
            pstmt.setString(2, null == fplx||"".equals(fplx)?"":fplx);
            pstmt.setString(3, null ==dysj ||"".equals(dysj)?"":dysj);
            pstmt.setString(4, null ==dyr ||"".equals(dyr)?"":dyr);
            pstmt.setString(5, null==sfzhm ||"".equals(sfzhm)?"":sfzhm);
            pstmt.setString(6, null==zdlsh ||"".equals(zdlsh)?"":zdlsh);
            pstmt.setString(7, null==jfr||"".equals(jfr)?"":jfr);
            pstmt.setString(8, null==jfje||"".equals(jfje)?"":jfje);
            num = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return num;
    }


    public static void main(String[] args) throws SQLException {
//        insert();
    }


}
