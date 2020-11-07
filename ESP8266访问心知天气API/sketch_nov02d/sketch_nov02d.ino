#include<ESP8266WiFi.h>
#include<ArduinoJson.h>

#define DebugBegin(baud_rate) Serial.begin(baud_rate)
#define DebugPrintln(message) Serial.println(message)
#define DebugPrint(message) Serial.print(message)

const char* ssid ="";//输入热点名称
const char* password ="";//输入热点密码
const char* host ="api.seniverse.com";
const char* APIKEY ="";//输入自己申请的知心天气私钥
const char* city ="hangzhou";
const char* language ="zh-Hans";

const unsigned long BAUD_RATE=115200;
const unsigned long HTTP_TIMEOUT=5000;
const size_t MAX_CONTENT_SIZE=1000;

struct WeatherData{
  char city[16];
  char weather[32];
  char temp[16];
  char udate[32];
};

WiFiClient client;
char response[MAX_CONTENT_SIZE];
char endOfHeaders[]="\r\n\r\n";

void setup() {
  // put your setup code here, to run once:
  delay(1000);
  WiFi.mode(WIFI_STA);
  DebugBegin(BAUD_RATE);
  DebugPrint("Connecting to");
  DebugPrintln(ssid);
  WiFi.begin(ssid,password);
  WiFi.setAutoConnect(true);
  while(WiFi.status()!=WL_CONNECTED){
    delay(500);
    DebugPrint(".");
  }
  DebugPrintln("");
  DebugPrintln("WiFi connected");
  delay(500);
  DebugPrintln("IP address:");
  DebugPrintln(WiFi.localIP());
  //client.setTimeout(HTTP_TIMEOUT);
}

void loop() {
  // put your main code here, to run repeatedly:
  while(!client.connected()){
    if(!client.connect(host,80)){//尝试进行连接
      DebugPrintln("connection....");
      delay(500);
    }
  }
  //连接成功
  if(sendRequest(host,city,APIKEY)&&skipResponseHeaders()){
    clrEsp8266ResponseBuffer();
    readReponseContent(response,sizeof(response));
    WeatherData weatherData;
    if(parseUserData(response,&weatherData)){
      printUserData(&weatherData);
    }
  }
  delay(5000);
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
  DebugPrintln("creat a request:");
  DebugPrintln(String("GET ")+GetUrl+"HTTP/1.1\r\n"+"Host:"+host+"\r\n"+"Connection:close\r\n\r\n");
  delay(1000);
  return true;
}

bool skipResponseHeaders(){
  bool ok=client.find(endOfHeaders);
  if(!ok){
    DebugPrintln("No response of invalid response!");
  }
  return ok;
}

void readReponseContent(char* content,size_t maxSize){
  size_t length=client.readBytes(content,maxSize);
  delay(100);
  DebugPrintln("Get the data from Internet");
  content[length]=0;
  DebugPrintln(content);
  DebugPrintln("Read data Over!");
  client.flush();
}

bool parseUserData(char* content,struct WeatherData* weatherData){
  DynamicJsonBuffer jsonBuffer;
  JsonObject&root=jsonBuffer.parseObject(content);
  if(!root.success()){
    DebugPrintln("JSON parsing failed!");
    return false;
  }

  strcpy(weatherData->city,root["results"][0]["location"]["name"]);
  strcpy(weatherData->weather,root["results"][0]["now"]["text"]);
  strcpy(weatherData->temp,root["results"][0]["now"]["temperature"]);
  strcpy(weatherData->udate,root["results"][0]["last_update"]);

  return true;
}


void printUserData(const struct WeatherData* weatherData){
  DebugPrintln("Print parsed data:");
  DebugPrint("City:");
  DebugPrint(weatherData->city);
  DebugPrint("  Weather:");
  DebugPrint(weatherData->weather);
  DebugPrint("  Temp:");
  DebugPrint(weatherData->temp);
  DebugPrint("℃");
  DebugPrint("  UpdateTime:");
  DebugPrintln(weatherData->udate);
}

void stopConnect(){
  DebugPrintln("Disconnect");
  client.stop();
}

void clrEsp8266ResponseBuffer(void){
  memset(response,0,MAX_CONTENT_SIZE);
}
