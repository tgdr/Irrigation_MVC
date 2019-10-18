package DataBase;

import net.sf.json.JSONObject;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/27.
 */
public class DbUtils {


    //获取传感器阈值信息（4种传感器  温度 湿度 光照度 土壤湿度 0标识某传感器的下限阈值；1标识某传感器的上限阈值）
    public static String getyz() {
        ResultSet rs = null;
        Connection conn = DbConn.getConnSql();
        PreparedStatement ps = null;
        JSONObject resjson = new JSONObject();
        float yz_tem_0, yz_hum_0, yz_tr_0, yz_light_0;
        float yz_tem_1, yz_hum_1, yz_tr_1, yz_light_1;
        int enable;
        if (conn != null) {
            try {
                String connstr = "select enable,yz_temperature_0,yz_temperature_1,yz_humidity_0,yz_humidity_1,yz_light_0,yz_light_1,yz_tr_0,yz_tr_1 from irr_yz;";
                ps = conn.prepareStatement(connstr);
                rs = ps.executeQuery();
                if (rs.next()) {
                    enable = rs.getInt("enable");
                    yz_tem_0 = rs.getFloat("yz_temperature_0");
                    yz_hum_0 = rs.getFloat("yz_humidity_0");
                    yz_light_0 = rs.getFloat("yz_light_0");
                    yz_tr_0 = rs.getFloat("yz_tr_0");


                    yz_tem_1 = rs.getFloat("yz_temperature_1");
                    yz_hum_1 = rs.getFloat("yz_humidity_1");
                    yz_light_1 = rs.getFloat("yz_light_1");
                    yz_tr_1 = rs.getFloat("yz_tr_1");
                    resjson.put("enable", enable);
                    System.out.println("yzlight0"+yz_light_0);
                    resjson.put("yz_temperature_0", yz_tem_0);
                    resjson.put("yz_humidity_0", yz_hum_0);
                    resjson.put("yz_light_0", yz_light_0);
                    resjson.put("yz_tr_0", yz_tr_0);

                    resjson.put("yz_temperature_1", yz_tem_1);
                    resjson.put("yz_humidity_1", yz_hum_1);
                    resjson.put("yz_light_1", yz_light_1);
                    resjson.put("yz_tr_1", yz_tr_1);
                }

                //System.out.println(status + "ddddddddd");
            } catch (Exception e) {
                // TODO: handle exception

                e.printStackTrace();
            } finally {
                try {
                    conn.close();
                    ps.close();

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
        return resjson.toString();
    }




    //数据库获取灌溉知识实现
    public static Map getknowledge() {
        ResultSet rs = null;
        Connection conn = DbConn.getConnSql();
        PreparedStatement ps = null;
        JSONObject resjson = new JSONObject();
       String knowledge;
        Map<String,ArrayList> map = new HashMap();
        ArrayList msgid = new ArrayList();
        ArrayList msg_content = new ArrayList();


        if (conn != null) {
            try {
                String connstr = "select msg_id,msg_content  from irr_msg;";
                ps = conn.prepareStatement(connstr);
                rs = ps.executeQuery();
                while (rs.next()) {
                    msgid.add(rs.getInt("msg_id"));
                    msg_content.add(rs.getString("msg_content"));
                }
                map.put("id",msgid);
                map.put("content",msg_content);

                //System.out.println(status + "ddddddddd");
            } catch (Exception e) {
                // TODO: handle exception

                e.printStackTrace();
            } finally {
                try {
                    conn.close();
                    ps.close();

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
        return map;
    }


    //数据库登录功能处理
    public static int Login(String username, String password) {

        ResultSet rs = null;
        String resultpassword = "";
        int resultauth = -1;
        Connection conn = DbConn.getConnSql();

        PreparedStatement ps = null;

        if (conn != null) {
            try {

                String connstr = "select Password,auth  from user_reg where Username = '" + username + "';";
                ps = conn.prepareStatement(connstr);


                rs = ps.executeQuery();

                if (rs.next()) {
                    resultpassword = rs.getString("Password");
                    if (resultpassword.equals(password)) {
                        resultauth = rs.getInt("auth");
                    } else
                        resultauth = -1;
                } else {
                    resultauth = -1;
                }

                //System.out.println(status + "ddddddddd");
            } catch (Exception e) {
                // TODO: handle exception
                resultauth = -1;
                e.printStackTrace();
            } finally {
                try {
                    conn.close();
                    ps.close();

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
        return resultauth;
    }

    //用户可以根据情况手动设置阈值调用这个方法实现阈值的设定
    public static boolean setyz(double tem, double hum, double light, double soilhum,   double tem1, double hum1, double light1, double soilhum1) {
        boolean result = false;
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DbConn.getConnSql();
            stmt = conn.createStatement();
            String execsql="UPDATE irr_yz SET yz_temperature_0= " + tem + " ,yz_humidity_0=" + hum + ",yz_light_0="+light+",yz_tr_0=" +soilhum +  ",yz_temperature_1=" + tem1+ ",yz_humidity_1=" + hum1 + ",yz_light_1="+light1+",yz_tr_1=" +soilhum1 + " where temp = 127;";
            System.out.println(execsql);
            stmt.executeUpdate(execsql);
            result = true;
    //    System.out.print(result+"result"+"     "+execsql);
    } catch (SQLException e) {
        e.printStackTrace();
        result = false;
    } finally {
        try {
            conn.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        return result;
    }

    //注册功能的逻辑实现 phonenum位手机号 int auth为用户权限等级  系统中有管理员 一般用户 游客 超级管理员等类型的用户
    public static boolean registeruser(String phonenum,String username,String password,int auth){
        boolean status = false;
        Connection conn = null;
        Statement stmt = null;
        int result=-1;
   String execsql = "insert into user_reg(Phonenum,Username,Password,auth,RegTime) values ('"+phonenum+"','"+username+"','"+password+"',"+auth+",'"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) +"')";
        try {
            conn = DbConn.getConnSql();

            stmt = conn.createStatement();
           result  =stmt.executeUpdate(execsql);
           if(result>0){
               status= true;
           }
           else
               status = false;

        } catch (SQLException e) {
            e.printStackTrace();
           // System.out.print("result"+result+"66666666666");

            status=false;
        }
        catch (Exception e1){
            status=false;
        }
        finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                status = false;
            }
        }
        return status;
    }








}
