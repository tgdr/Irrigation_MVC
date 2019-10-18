package controller;

import DataBase.DbUtils;
import DataBase.GetRealData;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import request.HttpThread;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import static DataBase.GetRealData.getBody;

@Controller
public class IrrController {
    public static String urlhead="http://192.168.43.210/";
    private void readwait()
    {
        System.out.println("*********进入计时方法**************");
        int delay_once = 0;
        do
        {
            try
            {
                Thread.sleep(50L);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            delay_once++;
            if (HttpThread.succflag == 1) {
                return;
            }
        } while (

                delay_once < 80);
    }



    @RequestMapping(method = {RequestMethod.POST}, value = {"/getyz.do"})
    @ResponseBody
    public ModelAndView getyz(@RequestBody String jsonstr){
        ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
        mav.addObject("result","ok");
        mav.addObject("data",DbUtils.getyz());
        return mav;

    }


    @RequestMapping(method = {RequestMethod.POST}, value = {"/setyz.do"})
    @ResponseBody
    public ModelAndView setyz(@RequestBody String jsonstr){
        ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
        if (jsonstr == null || jsonstr.equals("")) {
            mav.addObject("result", "failed");
            return mav;
        }
        else{
            JSONObject object = JSONObject.fromObject(jsonstr);

            if (object.has("tem")&&object.has("hum")&&object.has("light")&&object.has("soil")   && object.has("tem1")&&object.has("hum1")&&object.has("light1")&&object.has("soil1")) {
               boolean success= DbUtils.setyz(object.getDouble("tem"),object.getDouble("hum"),object.getDouble("light"),object.getDouble("soil"),
                       object.getDouble("tem1"),object.getDouble("hum1"),object.getDouble("light1"),object.getDouble("soil1"));
                if(success){
                    mav.addObject("result","ok");
                }
                else{
                    mav.addObject("result","failed");
                }
            }
            else{
                mav.addObject("result","failed");
            }
        }




        return mav;

    }

    @RequestMapping(method = {RequestMethod.POST}, value = {"/setpump.do"})
    @ResponseBody
    public ModelAndView setpump(@RequestBody String jsonstr){
        ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
        if (jsonstr == null || jsonstr.equals("")) {
            mav.addObject("result", "failed");
            return mav;
        }
        else{
            JSONObject jsonObject = JSONObject.fromObject(jsonstr);


            if (jsonObject.getString("what").equals("open")){
                Timer timer=new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        URL url = null;
                        String rs=null;
                        try {
                            URL url1 = new URL("http://192.168.43.210/open");
                            System.out.println("okok");
                            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
                            connection.setDoInput(true);
                            connection.setDoOutput(true);
                            connection.setRequestMethod("GET");
                            System.out.println("开启水泵");
                            BufferedReader buf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            String result="";
                            String temp;
                            while((temp = buf.readLine())!=null ){
                                result+=temp;
                            }
                            System.out.println(getBody(result));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },200);

            }else if (jsonObject.getString("what").equals("close")){
                Timer timer=new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        URL url = null;
                        String rs=null;
                        try {
                            url = new URL("http://192.168.43.210:80/close");
                            System.out.println("okok");
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setDoInput(true);
                            connection.setDoOutput(true);
                            connection.setRequestMethod("GET");
                            System.out.println("关闭水泵");
                            BufferedReader buf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            String result="";
                            String temp;
                            while((temp = buf.readLine())!=null ){
                                result+=temp;
                            }
                            System.out.println(getBody(result));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },200);
            }

        }


        return mav;

    }




    @RequestMapping(method = {RequestMethod.POST}, value = {"/getknowledge.do"})
    @ResponseBody
    public ModelAndView getknowledge(@RequestBody String jsonstr){
        Map map = DbUtils.getknowledge();
        ArrayList idlist = (ArrayList) map.get("id");
        ArrayList contentlist= (ArrayList) map.get("content");
        JSONArray array = new JSONArray();
        for(int i =0;i<idlist.size();i++){
            JSONObject object = new JSONObject();
            object.put("msgid",idlist.get(i));
            object.put("msgcontent",contentlist.get(i));
            array.add(object);
        }
        ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
        mav.addObject("msgresp",array);
        return mav;

    }





    @RequestMapping({"/GetRealTimeSensorValues.do"})
    public ModelAndView GetRealTimeSensorValue(@RequestBody String strjson)
    {
        ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
        GetRealData gd = new GetRealData();
        strjson = gd.getrealdata();
        System.out.println(strjson);
        //readwait();
            try
            {
                JSONObject jsonObject = JSONObject.fromObject(strjson);
                System.out.println("##返回数值-webContent##" + jsonObject.toString());
                mav.addObject("result","ok");
                mav.addObject("Irrtemperature", Float.parseFloat(jsonObject.optString("temp")));
                mav.addObject("Irrhumidity", Float.parseFloat(jsonObject.optString("humi")));
                mav.addObject("Irrlight", Float.parseFloat(jsonObject.optString("light")));
                mav.addObject("Irrsoilhum", Float.parseFloat(jsonObject.optString("soil")));

                System.out.println("##返回数值-处理后webContent##" + mav.toString());
            }
            catch (Exception e)
            {
                System.err.println("###HttpThread.e ###：" + e.toString());
                mav.addObject("result", "failed");
                mav.addObject("reason","json format is not true");
            }


        return mav;
    }

    @RequestMapping({"/RandomSensor.do"})
    public ModelAndView GetCarAccountBalance(@RequestBody String strjson)
    {
        ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
        mav.addObject("result","ok");
        mav.addObject("Irrtemperature", new Random().nextFloat()*30+30);
        mav.addObject("Irrhumidity", new Random().nextFloat()*98);
        mav.addObject("Irrlight", new Random().nextFloat()*3000+350);
        mav.addObject("Irrsoilhum", new Random().nextFloat()*92);

        return mav;
    }
}
