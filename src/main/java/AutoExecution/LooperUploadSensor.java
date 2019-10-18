package AutoExecution;

import DataBase.DbConn;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class LooperUploadSensor extends Thread {
    @Override
    public void run() {
        super.run();
        while (true)
                updatatodb(new Random().nextFloat(),new Random().nextFloat(),new Random().nextFloat(),new Random().nextFloat());



    }
    public static void updatatodb(float temperature,float humidity,float light,float tr){

        Connection conn = null;
        Statement stmt = null;
        String execsql = "insert into irr_sensor(irr_temperature,irr_humidity,irr_soilhum,irr_light,irr_collectiontime) values ("+temperature+","+humidity+","+light+","+tr+",'"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) +"')";
        try {
            conn = DbConn.getConnSql();
            stmt = conn.createStatement();
           stmt.executeUpdate(execsql);

Thread.sleep(10000);
        } catch (SQLException e) {
            e.printStackTrace();
            // System.out.print("result"+result+"66666666666");

        }
        catch (Exception e1){

        }
        finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();

            }
        }
    }
}
