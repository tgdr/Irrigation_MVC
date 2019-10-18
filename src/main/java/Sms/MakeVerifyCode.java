package Sms;

import java.util.Random;

public class MakeVerifyCode {
    public static String verifyCode(){
        Random random = new Random();
        char[] set = {91,92,93,94,95,96,58,59,60,61,62,63,64};
        String str = "";
        while (str.length() != 6){
            boolean flag = true;
            int a = (random.nextInt(75) + 48);
            for (int j = 0; j < set.length; j++){
                if (a == set[j]){
                    flag = false;
                }
            }
            if (flag){
                char ch = (char) a;
                //System.out.println(a);
                str += ch;
            }
        }
        return str;
    }

}
