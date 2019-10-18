package DataBase;

import java.sql.*;

/**
 * Created by Administrator on 2017/6/27.
 */
public class DbConn {
   static Connection conn;
   static  Statement stmt = null;
  static    ResultSet rs=null;
    public static Connection getConnSql() {
        conn = null;
        try {
           // System.out.print("resultdbdbdbdbdb"+"66666666666");
            Class.forName("com.mysql.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            String dburl = "jdbc:mysql://localhost:3306/irrigation_buu";
            String Connusername = "root";
            String Connuserpwd = "123456";
            conn = DriverManager.getConnection(dburl, Connusername, Connuserpwd);


            System.out.println("连接数据库成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;

    }
    /**
     * 数据库查询方法
     * @param sql
     * @return
     */
    public  ResultSet  doQuery(String sql){
        try{
            //得到连接

           conn =getConnSql();//创建链接
            stmt=conn.createStatement();//创建Statement
            rs=stmt.executeQuery(sql);

        }catch(Exception e){
            e.printStackTrace();
        }
        finally {
            close(rs);
            close(conn);
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rs;
    }

    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(PreparedStatement sm) {
        if (sm != null) {
            try {
                sm.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
