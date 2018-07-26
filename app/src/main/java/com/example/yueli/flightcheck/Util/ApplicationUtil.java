package com.example.yueli.flightcheck.Util;

import android.app.Application;
import android.util.Log;

import com.example.yueli.flightcheck.Bean.Airport;;
import com.example.yueli.flightcheck.Bean.Airport_;
import com.example.yueli.flightcheck.Bean.Booking;
import com.example.yueli.flightcheck.Bean.BookingItem;
import com.example.yueli.flightcheck.Bean.Flight;
import com.example.yueli.flightcheck.Bean.FlightDetail;
import com.example.yueli.flightcheck.Bean.Flight_;
import com.example.yueli.flightcheck.Bean.MyObjectBox;
import com.example.yueli.flightcheck.Bean.Pickup;
import com.example.yueli.flightcheck.Bean.User;
import com.example.yueli.flightcheck.JsonBean.AirportJsonBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by yueli on 2018/7/7.
 */

public class ApplicationUtil extends Application{
    private List<String> flightMsgList=new ArrayList<>();
    public BoxStore boxStore;
    public Box<Airport>airportBox;
    public Box<User>userBox;
    public Box<Flight>flightBox;
    public Box<FlightDetail>flightDetailBox;
    public Box<Pickup>pickupBox;
    public Box<Booking>bookingBox;
    public String user;
    public long userId;
    private OkHttpClient okHttpClient;
    private Request request;
    private Gson gson;
    //private List<AirportJsonBean.ListItem>airportList;
    public List<BookingItem>bookingList;//登陆时返回订阅信息
    public boolean ifLogin=false;
    public void init(){
        flightMsgList=new ArrayList<>();
      /*  for(int i=0;i<20;i++){
            flightMsgList.add("第"+i+"条数据");
        }*/
       okHttpClient=new OkHttpClient();
        gson=new Gson();
        boxStore=MyObjectBox.builder().androidContext(this).build();
        airportBox=boxStore.boxFor(Airport.class);
        userBox=boxStore.boxFor(User.class);
        flightBox=boxStore.boxFor(Flight.class);
        flightDetailBox=boxStore.boxFor(FlightDetail.class);
        pickupBox=boxStore.boxFor(Pickup.class);
        bookingBox=boxStore.boxFor(Booking.class);
//        boxStore.deleteAllFiles();
      //  cleanLocalData();
        localData();
    }
    private void cleanLocalData(){
        airportBox.removeAll();
        userBox.removeAll();
        flightBox.removeAll();
        flightDetailBox.removeAll();
        bookingBox.removeAll();
        pickupBox.removeAll();
    }
    public String getHttpJsonData(final String url){
        String str=null;
           /*Request request = new Request.Builder()
                        .url(url)
                        .header("X-Bmob-Application-id","4acdbe6ad82a37731e5f257fb3436172")
                        .addHeader("X-Bmob-REST-API-Key","d7a9313d88eeda594970bc9c6315a3c8")
                        .addHeader("Content-Type","application/json")
                        .build();*/
        Request request=new Request.Builder()
                .url(url)
                .build();
        try {
            Response response=okHttpClient.newCall(request).execute();
            if(!response.isSuccessful())
                throw new IOException("Unexpected code"+response);
            str=response.body().string();//just once
            Log.v("arki",str);
           /* Type type=new TypeToken<UsersJsonBean>(){}.getType();
            UsersJsonBean usersJsonBean=gson.fromJson(str,type);
            List<UsersJsonBean.JUser>BookingJsonBean=usersJsonBean.results;
            Log.v("arki",BookingJsonBean.get(0).name);*/

        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
    /*   public List<PickupJsonBean.ListItem> getPickupData(String airport){
         String url="http://192.168.43.245:9000/FlightQuery/brazerAD?place="+airport;
         String result=getHttpJsonData(url);
         Type type=new TypeToken<PickupJsonBean>(){}.getType();
         PickupJsonBean pickupJsonBean=gson.fromJson(result,type);
         if(pickupJsonBean!=null)
            return pickupJsonBean.list;
         return null;
 }
    /* public boolean deleteBooking(String flno){
         String url="http://192.168.43.245:9000/FlightQuery/deleteCareFlightAD?id="+user+"&flight="+flno;
         String result=getHttpJsonData(url);
         Type type=new TypeToken<BookingJsonBean>(){}.getType();
         BookingJsonBean fromJson=gson.fromJson(result,type);
         if(fromJson!=null&&fromJson.result.equals("已成功取消该航班的关注")) {
             bookingList=fromJson.list;
             return true;
         }
         return false;
     }
     public void getAirportJsonData(String io){ //全部机场信息 only once when install
         String url="http://192.168.43.245:9000/FlightQuery/AreaQueryAD?type="+io;
         String result=getHttpJsonData(url);
         Type type=new TypeToken<AirportJsonBean>(){}.getType();
         AirportJsonBean airportJsonBean=gson.fromJson(result,type);
         if(airportJsonBean!=null) {
             airportList = airportJsonBean.list;
             for (AirportJsonBean.ListItem item : airportList) {
                 if(item.state==0)
                     airportBox.put(new Airport(item.airportName, 0));
                 else
                     airportBox.put(new Airport(item.airportName,1));
             }
         }
     }
     public int addBooking(String flno){
         String url="http://192.168.43.245:9000/FlightQuery/addCareFlightAD?id="+user+"&flight="+flno;
         String result=getHttpJsonData(url);
         Type type=new TypeToken<BookingJsonBean>(){}.getType();
         BookingJsonBean fromJson=gson.fromJson(result,type);
         if(fromJson!=null) {
             switch (fromJson.result) {
                 case "关注成功":
                     bookingList=fromJson.list;
                     return 0;
                 case "您已关注过此航班":
                     return 1;
             }
         }
         return -1;
     }
     public boolean Login(String user,String pwd){
         String url="http://192.168.43.245:9000/FlightQuery/loginAD?id="+user+"&passwd="+pwd;
         String result=getHttpJsonData(url);
         Type type=new TypeToken<UserInfoJsonBean>(){}.getType();
         UserInfoJsonBean userInfoJsonBean=gson.fromJson(result,type);
         if(userInfoJsonBean!=null) {
             switch (userInfoJsonBean.status) {
                 case "OK":
                     if (userInfoJsonBean != null)
                         bookingList = userInfoJsonBean.list;
                     return true;
                 case "Failed":
                     return false;
             }
         }
         return false;
     }
     public boolean Register(String user,String pwd,String phone){
         String url="http://192.168.43.245:9000/FlightQuery/registerAD?id="+user+"&passwd="+pwd+"&phone="+phone;
         String result=getHttpJsonData(url);
         Type type=new TypeToken<RegisterJsonBean>(){}.getType();
         RegisterJsonBean registerJsonBean =gson.fromJson(result,type);
         if(registerJsonBean!=null)
             if(registerJsonBean.status.equals("OK")) {
                 ifLogin=true;
                 return true;
             }
         return false;
     }
     public List<AirlineJsonBean.ListItem> getCheckedAirLineData(String from,String to,String time){
         String url="http://192.168.43.245:9000/FlightQuery/AirlineQueryAD?begin="+from+"&end="+to+"&time="+time;
         String result=getHttpJsonData(url);
         Type type=new TypeToken<AirlineJsonBean>(){}.getType();
         AirlineJsonBean airlineJsonBean =gson.fromJson(result,type);
         if(airlineJsonBean!=null)
             return airlineJsonBean.list;
         return null;
     }
     public List<FLNOJsonBean.ListItem> getCheckedFLNOData(String flno,String time){
         String url="http://192.168.43.245:9000/FlightQuery/FlightQueryAD?flightID="+flno+"&time="+time;
         String result=getHttpJsonData(url);
         Type type=new TypeToken<FLNOJsonBean>(){}.getType();
         FLNOJsonBean flnoJsonBean=gson.fromJson(result,type);
         if(flnoJsonBean!=null)
            return flnoJsonBean.list;
         return null;
     }*/
    private void initAirport(){
        String result="{\"list\":[{\"airportName\":\"泉州\",\"state\":0},{\"airportName\":\"武汉\",\"state\":0},{\"airportName\":\"天津\",\"state\":0},{\"airportName\":\"锦州\",\"state\":0},{\"airportName\":\"温州\",\"state\":0},{\"airportName\":\"青岛\",\"state\":0},{\"airportName\":\"长治\",\"state\":0},{\"airportName\":\"厦门\",\"state\":0},{\"airportName\":\"长春\",\"state\":0},{\"airportName\":\"乌兰浩特\",\"state\":0},{\"airportName\":\"乌鲁木齐\",\"state\":0},{\"airportName\":\"伊宁\",\"state\":0},{\"airportName\":\"广州\",\"state\":0},{\"airportName\":\"昆明\",\"state\":0},{\"airportName\":\"重庆\",\"state\":0},{\"airportName\":\"宁波\",\"state\":0},{\"airportName\":\"揭阳潮汕\",\"state\":0},{\"airportName\":\"杭州\",\"state\":0},{\"airportName\":\"珠海\",\"state\":0},{\"airportName\":\"长沙\",\"state\":0},{\"airportName\":\"海口\",\"state\":0},{\"airportName\":\"南宁\",\"state\":0},{\"airportName\":\"福州\",\"state\":0},{\"airportName\":\"舟山\",\"state\":0},{\"airportName\":\"成都\",\"state\":0},{\"airportName\":\"深圳\",\"state\":0},{\"airportName\":\"呼和浩特\",\"state\":0},{\"airportName\":\"郑州\",\"state\":0},{\"airportName\":\"梅州\",\"state\":0},{\"airportName\":\"烟台\",\"state\":0},{\"airportName\":\"赤峰\",\"state\":0},{\"airportName\":\"哈尔滨\",\"state\":0},{\"airportName\":\"贵阳\",\"state\":0},{\"airportName\":\"上海虹桥\",\"state\":0},{\"airportName\":\"丽江\",\"state\":0},{\"airportName\":\"桂林\",\"state\":0},{\"airportName\":\"南昌\",\"state\":0},{\"airportName\":\"西安\",\"state\":0},{\"airportName\":\"兰州\",\"state\":0},{\"airportName\":\"包头\",\"state\":0},{\"airportName\":\"阿拉善左旗\",\"state\":0},{\"airportName\":\"南通\",\"state\":0},{\"airportName\":\"德宏\",\"state\":0},{\"airportName\":\"襄阳\",\"state\":0},{\"airportName\":\"上海浦东\",\"state\":0},{\"airportName\":\"齐齐哈尔\",\"state\":0},{\"airportName\":\"海拉尔\",\"state\":0},{\"airportName\":\"大连\",\"state\":0},{\"airportName\":\"湛江\",\"state\":0},{\"airportName\":\"铜仁\",\"state\":0},{\"airportName\":\"太原\",\"state\":0},{\"airportName\":\"西宁\",\"state\":0},{\"airportName\":\"运城\",\"state\":0},{\"airportName\":\"绵阳\",\"state\":0},{\"airportName\":\"北京首都\",\"state\":0},{\"airportName\":\"宜昌\",\"state\":0},{\"airportName\":\"银川\",\"state\":0},{\"airportName\":\"临沂\",\"state\":0},{\"airportName\":\"和田\",\"state\":0},{\"airportName\":\"鄂尔多斯\",\"state\":0},{\"airportName\":\"通化\",\"state\":0},{\"airportName\":\"恩施\",\"state\":0},{\"airportName\":\"万州\",\"state\":0},{\"airportName\":\"大庆\",\"state\":0},{\"airportName\":\"北海\",\"state\":0},{\"airportName\":\"锡林浩特\",\"state\":0},{\"airportName\":\"榆林\",\"state\":0},{\"airportName\":\"石家庄\",\"state\":0},{\"airportName\":\"南阳\",\"state\":0},{\"airportName\":\"百色\",\"state\":0},{\"airportName\":\"常德\",\"state\":0},{\"airportName\":\"盐城\",\"state\":0},{\"airportName\":\"乌海\",\"state\":0},{\"airportName\":\"黄山\",\"state\":0},{\"airportName\":\"义乌\",\"state\":0},{\"airportName\":\"通辽\",\"state\":0},{\"airportName\":\"西双版纳\",\"state\":0},{\"airportName\":\"阜阳\",\"state\":0},{\"airportName\":\"南京\",\"state\":0},{\"airportName\":\"遵义\",\"state\":0},{\"airportName\":\"二连浩特\",\"state\":0},{\"airportName\":\"淮安\",\"state\":0},{\"airportName\":\"秦皇岛\",\"state\":0},{\"airportName\":\"威海\",\"state\":0},{\"airportName\":\"沈阳\",\"state\":0},{\"airportName\":\"南充\",\"state\":0},{\"airportName\":\"白山\",\"state\":0},{\"airportName\":\"满洲里\",\"state\":0},{\"airportName\":\"朝阳\",\"state\":0},{\"airportName\":\"鸡西\",\"state\":0},{\"airportName\":\"大理\",\"state\":0},{\"airportName\":\"荔波\",\"state\":0},{\"airportName\":\"大同\",\"state\":0},{\"airportName\":\"张家界\",\"state\":0},{\"airportName\":\"济南\",\"state\":0},{\"airportName\":\"潍坊\",\"state\":0},{\"airportName\":\"泉州\",\"state\":1},{\"airportName\":\"武汉\",\"state\":1},{\"airportName\":\"天津\",\"state\":1},{\"airportName\":\"锦州\",\"state\":1},{\"airportName\":\"温州\",\"state\":1},{\"airportName\":\"青岛\",\"state\":1},{\"airportName\":\"长治\",\"state\":1},{\"airportName\":\"厦门\",\"state\":1},{\"airportName\":\"长春\",\"state\":1},{\"airportName\":\"乌兰浩特\",\"state\":1},{\"airportName\":\"乌鲁木齐\",\"state\":1},{\"airportName\":\"伊宁\",\"state\":1},{\"airportName\":\"广州\",\"state\":1},{\"airportName\":\"昆明\",\"state\":1},{\"airportName\":\"重庆\",\"state\":1},{\"airportName\":\"宁波\",\"state\":1},{\"airportName\":\"揭阳潮汕\",\"state\":1},{\"airportName\":\"杭州\",\"state\":1},{\"airportName\":\"珠海\",\"state\":1},{\"airportName\":\"长沙\",\"state\":1},{\"airportName\":\"海口\",\"state\":1},{\"airportName\":\"南宁\",\"state\":1},{\"airportName\":\"福州\",\"state\":1},{\"airportName\":\"舟山\",\"state\":1},{\"airportName\":\"成都\",\"state\":1},{\"airportName\":\"深圳\",\"state\":1},{\"airportName\":\"呼和浩特\",\"state\":1},{\"airportName\":\"郑州\",\"state\":1},{\"airportName\":\"梅州\",\"state\":1},{\"airportName\":\"烟台\",\"state\":1},{\"airportName\":\"赤峰\",\"state\":1},{\"airportName\":\"哈尔滨\",\"state\":1},{\"airportName\":\"贵阳\",\"state\":1},{\"airportName\":\"上海虹桥\",\"state\":1},{\"airportName\":\"丽江\",\"state\":1},{\"airportName\":\"桂林\",\"state\":1},{\"airportName\":\"南昌\",\"state\":1},{\"airportName\":\"西安\",\"state\":1},{\"airportName\":\"兰州\",\"state\":1},{\"airportName\":\"包头\",\"state\":1},{\"airportName\":\"阿拉善左旗\",\"state\":1},{\"airportName\":\"南通\",\"state\":1},{\"airportName\":\"德宏\",\"state\":1},{\"airportName\":\"襄阳\",\"state\":1},{\"airportName\":\"上海浦东\",\"state\":1},{\"airportName\":\"齐齐哈尔\",\"state\":1},{\"airportName\":\"海拉尔\",\"state\":1},{\"airportName\":\"大连\",\"state\":1},{\"airportName\":\"湛江\",\"state\":1},{\"airportName\":\"铜仁\",\"state\":1},{\"airportName\":\"太原\",\"state\":1},{\"airportName\":\"西宁\",\"state\":1},{\"airportName\":\"运城\",\"state\":1},{\"airportName\":\"绵阳\",\"state\":1},{\"airportName\":\"北京首都\",\"state\":1},{\"airportName\":\"宜昌\",\"state\":1},{\"airportName\":\"银川\",\"state\":1},{\"airportName\":\"临沂\",\"state\":1},{\"airportName\":\"和田\",\"state\":1},{\"airportName\":\"鄂尔多斯\",\"state\":1},{\"airportName\":\"通化\",\"state\":1},{\"airportName\":\"恩施\",\"state\":1},{\"airportName\":\"万州\",\"state\":1},{\"airportName\":\"大庆\",\"state\":1},{\"airportName\":\"北海\",\"state\":1},{\"airportName\":\"锡林浩特\",\"state\":1},{\"airportName\":\"榆林\",\"state\":1},{\"airportName\":\"石家庄\",\"state\":1},{\"airportName\":\"南阳\",\"state\":1},{\"airportName\":\"百色\",\"state\":1},{\"airportName\":\"常德\",\"state\":1},{\"airportName\":\"盐城\",\"state\":1},{\"airportName\":\"乌海\",\"state\":1},{\"airportName\":\"黄山\",\"state\":1},{\"airportName\":\"义乌\",\"state\":1},{\"airportName\":\"通辽\",\"state\":1},{\"airportName\":\"西双版纳\",\"state\":1},{\"airportName\":\"阜阳\",\"state\":1},{\"airportName\":\"南京\",\"state\":1},{\"airportName\":\"遵义\",\"state\":1},{\"airportName\":\"二连浩特\",\"state\":1},{\"airportName\":\"淮安\",\"state\":1},{\"airportName\":\"秦皇岛\",\"state\":1},{\"airportName\":\"威海\",\"state\":1},{\"airportName\":\"沈阳\",\"state\":1},{\"airportName\":\"南充\",\"state\":1},{\"airportName\":\"白山\",\"state\":1},{\"airportName\":\"满洲里\",\"state\":1},{\"airportName\":\"朝阳\",\"state\":1},{\"airportName\":\"鸡西\",\"state\":1},{\"airportName\":\"大理\",\"state\":1},{\"airportName\":\"荔波\",\"state\":1},{\"airportName\":\"大同\",\"state\":1},{\"airportName\":\"张家界\",\"state\":1},{\"airportName\":\"济南\",\"state\":1},{\"airportName\":\"潍坊\",\"state\":1},{\"airportName\":\"澳门\",\"state\":1},{\"airportName\":\"芽庄\",\"state\":1},{\"airportName\":\"名古屋\",\"state\":1},{\"airportName\":\"纽约\",\"state\":1},{\"airportName\":\"首尔仁川\",\"state\":1},{\"airportName\":\"关岛\",\"state\":1},{\"airportName\":\"东京成田\",\"state\":1},{\"airportName\":\"香港\",\"state\":1},{\"airportName\":\"莫斯科\",\"state\":1},{\"airportName\":\"大阪\",\"state\":1},{\"airportName\":\"那霸\",\"state\":1},{\"airportName\":\"桃园\",\"state\":1},{\"airportName\":\"新加坡\",\"state\":1},{\"airportName\":\"雅加达\",\"state\":1},{\"airportName\":\"阿姆斯特丹\",\"state\":1},{\"airportName\":\"普吉岛\",\"state\":1},{\"airportName\":\"甲米\",\"state\":1},{\"airportName\":\"曼谷\",\"state\":1},{\"airportName\":\"芝加哥\",\"state\":1},{\"airportName\":\"安克雷奇\",\"state\":1},{\"airportName\":\"高雄\",\"state\":1},{\"airportName\":\"青森\",\"state\":1},{\"airportName\":\"奥克兰\",\"state\":1},{\"airportName\":\"河内\",\"state\":1},{\"airportName\":\"曼谷\",\"state\":1},{\"airportName\":\"扎幌\",\"state\":1},{\"airportName\":\"东京羽田\",\"state\":1},{\"airportName\":\"台北松山\",\"state\":1},{\"airportName\":\"卡拉奇\",\"state\":1},{\"airportName\":\"萨拉戈萨\",\"state\":1},{\"airportName\":\"珀斯\",\"state\":1},{\"airportName\":\"哈巴\",\"state\":1},{\"airportName\":\"阿斯塔纳\",\"state\":1},{\"airportName\":\"新西伯利亚\",\"state\":1},{\"airportName\":\"温哥华\",\"state\":1},{\"airportName\":\"费尔班克斯\",\"state\":1},{\"airportName\":\"帕劳\",\"state\":1},{\"airportName\":\"克诺\",\"state\":1},{\"airportName\":\"法兰克福\",\"state\":1}]}";
        Type type=new TypeToken<AirportJsonBean>(){}.getType();
        AirportJsonBean airportJsonBean=gson.fromJson(result,type);
        for(AirportJsonBean.ListItem airport:airportJsonBean.list) {
            Airport airport1=new Airport(airport.airportName,airport.state);
            airportBox.put(airport1);
        }
    }
    public Airport getAirport(String name){
        Airport airport=airportBox.query().equal(Airport_.name,name).build().findFirst();
        return airport;
    }
    //"ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    //"0123456789"
    private String randomProduce(String str,int len){
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char ch = str.charAt(new Random().nextInt(str.length()));
            sb.append(ch);
        }
        return sb.toString();
    }
    private void initFlight(){
        String s[]={"JET","B737","E190","A320","A319","CRJ","CRJ900","A330","A380","A321","B738"};
        Flight f=new Flight("GS857101","E190","B3155");
        flightBox.put(f);
        for(int i=0;i<100;i++){
            String flno=randomProduce("ABCDEFGHIJKLMNOPQRSTUVWXYZ",2)+randomProduce("0123456789",6);
            String planeNo=randomProduce("ABCDEFGHIJKLMNOPQRSTUVWXYZ",1)+randomProduce("0123456789",4);
            Flight flight=new Flight(flno,s[i%s.length],planeNo);
            flightBox.put(flight);
        }
    }
    String time[]={"09:30","08:10","11:30","21:45","16:25"};
    public void localData(){
        User user=new User("arki","123");
        long id=userBox.put(user);
       // Log.v("arki","id:"+id); 1
        initAirport();
        initFlight();
        Airport tj=getAirport("天津");//3 k0
        Airport shhq=getAirport("上海虹桥");//34 k0
        for(int i=1;i<25;i++){
            if(i%3==0)
                flightDetailBox.put(new FlightDetail((long)i,randomProduce("ABCDEFGHIJKLMNOPQRSTUVWXYZ",1)
                        +i%10,"2018-07-20 "+time[i%time.length],"2018-07-21 "+time[(i+3)%time.length],"延误"
                        ,tj.id,shhq.id));
            else
                flightDetailBox.put(new FlightDetail((long)i,randomProduce("ABCDEFGHIJKLMNOPQRSTUVWXYZ",1)
                    +i%10,"2018-07-20 "+time[i%time.length],"2018-07-21 "+time[(i+3)%time.length],"正点"
            ,tj.id,shhq.id));
        }
       // Log.v("arki","air max:"+airportBox.query().build().max(Airport_.id)); 231
       // Log.v("arki","f max:"+flightBox.query().build().max(Flight_.id));  101
       for(int i=1;i<25;i++){
            if(i%2==0)
                pickupBox.put(new Pickup(tj.id,(long)i));
            else
                pickupBox.put(new Pickup(shhq.id,(long)i));
       }
   }
   public Flight getFlight(String flno){
      return flightBox.query().equal(Flight_.flno,flno).build().findFirst();
   }
    public List<String> getFlightMsgList(){return this.flightMsgList;}
    public void setFlightMsgList(List<String>list){this.flightMsgList=list;}
}
