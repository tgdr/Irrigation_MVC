#include <Adafruit_CC3000.h>
#include <SPI.h>
#define RL A1       //RGB灯  红色
#define RG A2       //RGB  绿
#define RB A3       //RGB  蓝
#include "utility/debug.h"
#include "utility/socket.h"
#include <dht.h>
dht DHT;        //DHT22温湿度传感器  
#define DHT22_PIN 8         //温湿度
#define lightsensor A5     //光照度传感器
#define soilsensor A4     //土壤湿度传感器
float datas[2];       //存放两个数据（dht22的湿度温度）
float * GetTemAndHum();   //通过函数获取实时温湿度并传值给datas指针

// These are the interrupt and control pins
#define ADAFRUIT_CC3000_IRQ   7  // MUST be an interrupt pin!
// These can be any two pins
#define ADAFRUIT_CC3000_VBAT  5
#define ADAFRUIT_CC3000_CS    10
// Use hardware SPI for the remaining pins
// On an UNO, SCK = 13, MISO = 12, and MOSI = 11
Adafruit_CC3000 cc3000 = Adafruit_CC3000(ADAFRUIT_CC3000_CS, ADAFRUIT_CC3000_IRQ, ADAFRUIT_CC3000_VBAT,
SPI_CLOCK_DIVIDER); // you can change this clock speed
 
 
#define WLAN_SSID       "ChinaNet2"           //这里填写你的 WIFI  名称
#define WLAN_PASS       "13261221QSXX"            //这里填写你的 WIFI 密码 
// Security can be WLAN_SEC_UNSEC, WLAN_SEC_WEP, WLAN_SEC_WPA or WLAN_SEC_WPA2
#define WLAN_SECURITY   WLAN_SEC_WPA2
 
#define LISTEN_PORT           80   // What TCP port to listen on for connections.
 
Adafruit_CC3000_Server webServer(LISTEN_PORT);
boolean led_state;
//
void setup(void) {
       pinMode(INPUT,lightsensor);
        //简单起见，我们只用板载的 13pin 上的 LED 演示
        pinMode (13, OUTPUT);
           //默认是灭的
        digitalWrite (13, LOW);
        pinMode(A1,OUTPUT);
  pinMode(A2,OUTPUT);
  pinMode(A3,OUTPUT);
           //使用串口输出Debug信息
        Serial.begin(115200);
        Serial.println(F("Hello, CC3000!\n"));
        //while (!Serial);
        //Serial.println ("Input any key to start:");
        //while (!Serial.available ());
           //输出当前可用内存
        Serial.print("Free RAM: ");
        Serial.println(getFreeRam(), DEC);
 
        /* Initialise the module */
        Serial.println(F("\nInitializing..."));
        if (!cc3000.begin()) {
                Serial.println(F("Couldn't begin()! Check your wiring?"));
                while(1);
        }
 
        Serial.print(F("\nAttempting to connect to "));
        Serial.println(WLAN_SSID);
        if (!cc3000.connectToAP(WLAN_SSID, WLAN_PASS, WLAN_SECURITY)) {
                Serial.println(F("Failed!"));
                while(1);
        }
 
        Serial.println(F("Connected!"));
        
 
           //使用 DHCP 分配的IP
        Serial.println(F("Request DHCP"));
        while (!cc3000.checkDHCP()) {
                delay(100); // ToDo: Insert a DHCP timeout!
        } 
          
        //显示当前的IP信息
        /* Display the IP address DNS, Gateway, etc. */
        while (! displayConnectionDetails()) {
                delay(1000);
        }
 
        // Start listening for connections
        webServer.begin();
        Serial.println(F("Listening for connections..."));
        analogWrite(A2,255);
        delay(100);
        analogWrite(A2,0);
}

int getlightvalue(){
  return 10*analogRead(lightsensor);
  
}


float getsoilvalue(){
  return analogRead(soilsensor);
}

//获取温湿度赋值给datas指针
void GetTemAndHum(float *data){
   DHT.read22(DHT22_PIN);  //读取数据
  *data =DHT.temperature;
  *(data+1)=DHT.humidity;
  float * o = data;
 
  return o;
}
 
//
void loop(void) {
        // Try to get a client which is connected.
        Adafruit_CC3000_ClientRef client = webServer.available();
        if (client) {
                   //处理输入的 GET 信息，对于 GET 方法来说，Url中既有传递的信息
                processInput (client);
                      //对发送 HTTP 请求的浏览器发送HTTP代码
                sendWebPage (client);
        }
        client.close();
}
 
//分析收到的 GET 方法的参数
void processInput (Adafruit_CC3000_ClientRef client) {
        char databuffer[45];
      //安全起见，保证截断
       databuffer[44]='\0';
        while (client.available ()) {
                client.read (databuffer, 40);
               
          //client.println();
     
        //下面这个代码是查找PC端发送的数据中的换行，以此作为字符串的结尾
                char* sub = strchr (databuffer, '\r');
                if (sub > 0)
                        *sub = '\0';
                Serial.println (databuffer);
                     
                //下面是解析 GET 方法提供的参数
       //如果是 open 命令，那么点亮 LED
                if (strstr (databuffer, "open") != 0) {
                        Serial.println (F("clicked open"));
                        digitalWrite (13, HIGH);
                        led_state = true;
                        break;
                }
           //如果是 close 命令，那么熄灭 LED
                else if (strstr (databuffer, "close") != 0) {
                        Serial.println (F("clicked close"));
                        digitalWrite (13, LOW);
                        led_state = false;
                        break;
                }
                //获取传感器值并利用GET请求返回客户端一个HTML网页   拿到网页后解析body里面的值 转换为json字符串
               else if (strstr (databuffer, "getsensor") != 0) {                
                // webServer.write("HTTP/1.1 200 OK");
        //  webServer.write("");
        //RGB灯 作为提示灯
analogWrite(A3,255);
                GetTemAndHum(datas);
                delay (20);
      //拼接4个传感器的值为json格式
               String res1= "{light:";
                res1+= getlightvalue();
                String res2= ",temp:";
                res1.concat(res2);
                res1+= *datas;
                res2 = ",humi:";
                res1.concat(res2);
                res1+= *(datas+1);

res2 = ",soil:";
res1.concat(res2);
res1+=getsoilvalue();
                
                res2="}";
                res1.concat(res2);
               char b[55];
               
                 strcpy(b,res1.c_str());
                Serial.println(b);
                 //手动设置Http请求头  
                webServer.write("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n");
                //预留出空间 strcat 需要把第二个字符串的东西复制给第一个 确保空间充足防止溢出假死
                char  out[120]= "<html><head><title>data</title></head><body>";
                strcat(out,b);  
                char tail[15]="</body></html>";      //html文件尾部
                strcat(out,tail);  
                Serial.println(out); 
                webServer.write(out);


                client.close();
                      analogWrite(A3,0);
                }
                break;
        }
}
 
void sendWebPage (Adafruit_CC3000_ClientRef client) {
        //为了节省空间，这里只发送简单的提示字符
       //webServer.write ("Waiting for command");

        delay (20);
      //  client.close();
}
 
//输出当前WIFI 设备通过 DHCP 取得的基本信息
bool displayConnectionDetails(void) {
        uint32_t ipAddress, netmask, gateway, dhcpserv, dnsserv;
 
        if(!cc3000.getIPAddress(&ipAddress, &netmask, &gateway, &dhcpserv, &dnsserv)) {
                Serial.println(F("Unable to retrieve the IP Address!\r\n"));
                return false;
        }
        else {
                Serial.print(F("\nIP Addr: "));
                cc3000.printIPdotsRev(ipAddress);
                Serial.print(F("\nNetmask: "));
                cc3000.printIPdotsRev(netmask);
                Serial.print(F("\nGateway: "));
                cc3000.printIPdotsRev(gateway);
                Serial.print(F("\nDHCPsrv: "));
                cc3000.printIPdotsRev(dhcpserv);
                Serial.print(F("\nDNSserv: "));
                cc3000.printIPdotsRev(dnsserv);
                Serial.println();
                return true;
        }
}
