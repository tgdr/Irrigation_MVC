package controller;

import DataBase.DbUtils;
import Sms.SmsSenderUtils;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.annotation.PostConstruct;

/**
 * Created by Administrator on 2019/2/2.
 */

@Controller

public class UserController {

    @PostConstruct
    public void startsensorThread(){
       // LooperUploadSensor th = new LooperUploadSensor();
       // th.start();
    }


    @RequestMapping(method = {RequestMethod.GET}, value = {"/"})
    public String getserverinfo(Model model){
        System.out.println("into controller!");

        return "/WEB-INF/views/Index.html";
    }





    @RequestMapping(method = {RequestMethod.POST}, value = {"/registeruser.do"})
    @ResponseBody
    public ModelAndView registeruser(@RequestBody String jsonstr){
        ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
        JSONObject object = JSONObject.fromObject(jsonstr);

        if(object.has("Username") && object.has("Password") && object.has("auth")&&object.has("Phonenum")){
         boolean status =   DbUtils.registeruser(object.getString("Phonenum"),object.getString("Username"),object.getString("Password"),object.getInt("auth"));

         if(status == true){
             //String getvrcode= SmsSenderUtils.sendsms(object.getString("Phonenum"));
             mav.addObject("result","ok");
         }
         else
             mav.addObject("result","failed");

        }
        else
            mav.addObject("result","failed");

        return  mav;
    }

    @RequestMapping(method = {RequestMethod.POST}, value = {"/getvrcode.do"})
    @ResponseBody
    public ModelAndView getvrcode(@RequestBody String jsonstr){
        ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
        JSONObject object = JSONObject.fromObject(jsonstr);
        if(object.has("Phonenum")){

                String getvrcode= SmsSenderUtils.sendsms(object.getString("Phonenum"));
                mav.addObject("result","ok");
                mav.addObject("vrcode",getvrcode);

        }
        else
            mav.addObject("result","failed");

        return  mav;
    }

    @RequestMapping(method = {RequestMethod.POST}, value = {"/login.do"})
    @ResponseBody
    public ModelAndView login(@RequestBody String jsonstr){
        ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
        JSONObject object = JSONObject.fromObject(jsonstr);
        if(object.has("Username") && object.has("Password")){
            int status =   DbUtils.Login(object.getString("Username"),object.getString("Password"));
            if(status >-1){
                mav.addObject("result","ok");
                mav.addObject("auth",status);
            }

            else
                mav.addObject("result","failed");

        }
        else
            mav.addObject("result","failed");

        return  mav;
    }




}
