#include<ESP8266WiFi.h>
#include<ArduinoJson.h>

const char* ssid ="";//输入热点名称
const char* password ="";//输入热点密码
const char* host ="api.seniverse.com";
const char* APIKEY ="";//输入自己申请的知心天气私钥
const char* city ="hangzhou";//可根据需要改为其余城市的拼音
const char* language ="zh-Hans";

const unsigned long BAUD_RATE=115200;
const unsigned long HTTP_TIMEOUT=5000;
const size_t MAX_CONTENT_SIZE=1000;

struct WeatherData{//存储天气数据的结构体，可根据需要自行添加成员变量
  char city[16];
  char weather[32];
  char temp[16];
  char udate[32];
};

WiFiClient client;//创建了一个网络对象
char response[MAX_CONTENT_SIZE];
char endOfHeaders[]="\r\n\r\n";

void setup() {
  Serial.begin(BAUD_RATE);
  wifiConnect();//连接WiFi
  client.setTimeout(HTTP_TIMEOUT);
}

void loop() {
  while(!client.connected()){
    if(!client.connect(host,80)){//尝试建立连接
      Serial.println("connection....");
      delay(500);
    }
  }
  //连接成功，发送GET请求
  if(sendRequest(host,city,APIKEY)&&skipResponseHeaders()){//发送http请求 并且跳过响应头
    clrEsp8266ResponseBuffer();//清除缓存
    readReponseContent(response,sizeof(response));//从HTTP服务器响应中读取正文
    WeatherData weatherData;
    if(parseUserData(response,&weatherData)){//判断Json数据包是否分析成功
      printUserData(&weatherData);//输出读取到的天气信息
    }
  }
  stopConnect();
  delay(5000);
}

void wifiConnect(){
  WiFi.mode(WIFI_STA);//设置esp8266工作模式
  Serial.print("Connecting to");
  Serial.println(ssid);
  WiFi.begin(ssid,password);//连接WiFi
  WiFi.setAutoConnect(true);
  while(WiFi.status()!=WL_CONNECTED){//该函数返回WiFi的连接状态
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.println("WiFi connected");
  delay(500);
  Serial.println("IP address:");
  Serial.println(WiFi.localIP());
}

bool sendRequest(const char* host,const char* cityid,const char* apiKey){
  String GetUrl="/v3/weather/now.json?key=";
  GetUrl+=APIKEY;
  GetUrl+="&location=";
  GetUrl+=city;
  GetUrl+="&language=";
  GetUrl+=language;
  GetUrl+="&unit=c ";
  client.print(String("GET ")+GetUrl+"HTTP/1.1\r\n"+"Host:"+host+"\r\n"+"Connection:close\r\n\r\n");
  Serial.println("creat a request:");
  Serial.println(String("GET ")+GetUrl+"HTTP/1.1\r\n"+"Host:"+host+"\r\n"+"Connection:close\r\n\r\n");
  delay(1000);
  return true;
}

bool skipResponseHeaders(){
  bool ok=client.find(endOfHeaders);
  if(!ok){
    Serial.println("No response of invalid response!");
  }
  return ok;
}

void readReponseContent(char* content,size_t maxSize){
  size_t length=client.readBytes(content,maxSize);
  delay(100);
  Serial.println("Get the data from Internet");
  content[length]=0;
  Serial.println(content);//输出读取到的数据
  Serial.println("Read data Over!");
  client.flush();//刷新客户端
}

bool parseUserData(char* content,struct WeatherData* weatherData){
  DynamicJsonBuffer jsonBuffer;//创建一个动态缓冲区实例
  JsonObject&root=jsonBuffer.parseObject(content);//根据需要解析的数据来计算缓冲区的大小
  if(!root.success()){
    Serial.println("JSON parsing failed!");
    return false;
  }
  //复制数据包中所需的字符串
  strcpy(weatherData->city,root["results"][0]["location"]["name"]);
  strcpy(weatherData->weather,root["results"][0]["now"]["text"]);
  strcpy(weatherData->temp,root["results"][0]["now"]["temperature"]);
  strcpy(weatherData->udate,root["results"][0]["last_update"]);

  return true;
}


void printUserData(const struct WeatherData* weatherData){
  Serial.println("Print parsed data:");
  Serial.print("City:");
  Serial.print(weatherData->city);
  Serial.print("  Weather:");
  Serial.print(weatherData->weather);
  Serial.print("  Temp:");
  Serial.print(weatherData->temp);
  Serial.print("℃");
  Serial.print("  UpdateTime:");
  Serial.println(weatherData->udate);
}

void stopConnect(){
  Serial.println("Disconnect");
  client.stop();//停止客户端访问
}

void clrEsp8266ResponseBuffer(void){
  memset(response,0,MAX_CONTENT_SIZE);
}
