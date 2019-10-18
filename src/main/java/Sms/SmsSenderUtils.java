package Sms;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import net.sf.json.JSONException;

import java.io.IOException;

public class SmsSenderUtils {
    // 短信应用SDK AppID
   static int appid = 1400184699; // 1400开头

    // 短信应用SDK AppKey
   static String appkey = "80f04589be21ebf359752d62451270f2";

    // 需要发送短信的手机号码
 //   String[] phoneNumbers = {"21212313123", "12345678902", "12345678903"};

    // 短信模板ID，需要在短信应用中申请
   static int templateId = 276278; // NOTE: 这里的模板ID`7839`只是一个示例，真实的模板ID需要在短信控制台中申请
    //templateId7839对应的内容是"您的验证码是: {1}"
// 签名
    static String smsSign = "李天宇的个人学习笔记"; // NOTE: 这里的签名"腾讯云"只是一个示例，真实的签名需要在短信控制台中申请，另外签名参数使用的是`签名内容`，而不是`签名ID`

public static String  sendsms(String phonenum){

    String vrcode= MakeVerifyCode.verifyCode();
    try {
        String[] params = {phonenum,vrcode};//数组具体的元素个数和模板中变量个数必须一致，例如事例中templateId:5678对应一个变量，参数数组中元素个数也必须是一个
        SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
        SmsSingleSenderResult result = ssender.sendWithParam("86", phonenum,
                templateId, params, smsSign, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
        System.out.println(result);
    } catch (HTTPException e) {
        // HTTP响应码错误

        e.printStackTrace();
    } catch (JSONException e) {

        // json解析错误

        e.printStackTrace();

    } catch (IOException e) {
        // 网络IO错误

        e.printStackTrace();
    }
    return  vrcode;
}




}
