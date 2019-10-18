package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class GetSensorValueServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
            request.setCharacterEncoding("utf-8");
            response.setCharacterEncoding("utf-8");

        try {
            URL url = new URL("http://192.168.43.210:80/getsensor");
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


            System.out.println(getBody(result));
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
    }

    public String getBody(String val) {
                  String start = "<body>";
                   String end = "</body>";
                   int s = val.indexOf(start) + start.length();
                   int e = val.indexOf(end);
                 return val.substring(s, e);
    }
}
