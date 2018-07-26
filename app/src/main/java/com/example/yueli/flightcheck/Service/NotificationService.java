package com.example.yueli.flightcheck.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.yueli.flightcheck.Bean.Flight;
import com.example.yueli.flightcheck.MainActivity;
import com.example.yueli.flightcheck.R;
import com.example.yueli.flightcheck.Util.ApplicationUtil;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yueli on 2018/7/15.
 */

public class NotificationService extends Service {
    private Context context;
    public  NotificationService(Context context){this.context=context;}
    public NotificationService(){this.context=this;}
    List<Flight> flightList;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    // 获取消息线程
    private MessageThread messageThread = null;

    // 点击查看
    private Intent messageIntent = null;
    private PendingIntent messagePendingIntent = null;

    // 通知栏消息
    private int messageNotificationID = 1000;
    private Notification messageNotification = null;
    private NotificationManager messageNotificatioManager = null;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 初始化
        ApplicationUtil applicationUtil=(ApplicationUtil)getApplication();
        flightList=applicationUtil.flightBox.getAll();
        messageNotification = new Notification();
        messageNotification.icon = R.drawable.ic_launcher;
        messageNotification.tickerText = "新消息";
        messageNotification.defaults = Notification.DEFAULT_SOUND;
        messageNotificatioManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        messageIntent = new Intent(this, MainActivity.class);

        messagePendingIntent = PendingIntent.getActivity(this, 0,
                messageIntent, 0);

        // 开启线程
        messageThread = new MessageThread();
        messageThread.isRunning = true;
        messageThread.start();

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 从服务器端获取消息
     *
     */
    class MessageThread extends Thread {
        // 设置是否循环推送
        public boolean isRunning = true;

        public void run() {
            String NOTIFICATION_CHANNEL="arki";
             while (isRunning) {
                 try {
                     // 间隔时间
                     Thread.sleep(30000);
                     // 获取服务器消息
                     String serverMessage = getServerMessage();
                     if (serverMessage != null && !"".equals(serverMessage)) {
                         if (Build.VERSION.SDK_INT >= 26) {

                             NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL, "航班测试", NotificationManager.IMPORTANCE_LOW);
                             channel.enableLights(false);
                             channel.enableVibration(false);
                             channel.setImportance(NotificationManager.IMPORTANCE_LOW);  //设置为low, 通知栏不会有声音
                             messageNotificatioManager.createNotificationChannel(channel);
                         }
                         // 更新通知栏
                         Notification.Builder builder1 = new Notification.Builder(context);
                         builder1.setSmallIcon(R.drawable.ic_action_plane); //设置图标
                         builder1.setTicker("显示第二个通知");
                         builder1.setContentTitle("航班通知"); //设置标题
                         String msg=flightList.get((new Random().nextInt(flightList.size()))).flno+"即将进港，请工作人员做好准备！";
                         builder1.setContentText(msg); //消息内容
                         Intent intent=new Intent("notify");
                         intent.putExtra("msg",msg);
                         sendBroadcast(intent);
                         builder1.setWhen(System.currentTimeMillis()); //发送时间
                         builder1.setDefaults(Notification.DEFAULT_ALL); //设置默认的提示音，振动方式，灯光
                         builder1.setAutoCancel(true);//打开程序后图标消失
                         builder1.setContentIntent(messagePendingIntent);
                         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                             builder1.setChannelId(NOTIFICATION_CHANNEL);
                         }
                         messageNotification = builder1.build();
                         messageNotificatioManager.notify(messageNotificationID, // 通过通知管理器发送通知
                                 messageNotification);
                         // 每次通知完，通知ID递增一下，避免消息覆盖掉
                         messageNotificationID++;
                     }
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
             }
        }
    }

    @Override
    public void onDestroy() {
        // System.exit(0);
        messageThread.isRunning = false;
        super.onDestroy();
    }

    /**
     * 模拟发送消息
     *
     * @return 返回服务器要推送的消息，否则如果为空的话，不推送
     */
    public String getServerMessage() {
        return "NEWS!";
    }
}