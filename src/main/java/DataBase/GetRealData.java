package DataBase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class GetRealData {

    //采集器采集传感器的返回数据实现

    /**
     * getrealdata的流程是 服务器访问内网的控制器，控制器拦截到请求以后会询问采集器要传感器信息，控制器采用应答的模式相应服务器信息
     * 使用的是http协议，arduino采集端返回的是html网页 数据用json的形式返回
     * 所以操作io流拿到arduino返回的网络数据信息，从标准的html网页中截取body中的内容转换为json这样就完成了数据采集和加工
     * @return
     */
    public static String getrealdata(){
        String rs = "";
        try {
            URL url = new URL("http://192.168.43.210/getsensor");
            System.out.println("okok");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");

            BufferedReader buf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result="";
            String temp;
            while((temp = buf.readLine())!=null ){
                result+=temp;
            }
            //    System.out.println(result);
            rs = getBody(result);
            System.out.println();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static String getBody(String val) {
        String start = "<body>";
        String end = "</body>";
        int s = val.indexOf(start) + start.length();
        int e = val.indexOf(end);
        return val.substring(s, e);
    }


}
