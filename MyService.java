package com.example.yournote;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.os.Handler;


import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


//포그라운드 서비스가 실행되는 클래스
public class MyService extends Service implements Runnable {






    private static final int REBOOT_DELAY_TIMER = 10 * 1000;

    // GPS를 받는 주기 시간
    private static final int LOCATION_UPDATE_DELAY = 20 * 1000; // 5 * 60 * 1000

    private Handler mHandler;
    private boolean mIsRunning;
    private int mStartId = 0;








    static Intent serviceIntent;

    private OnScrean_BroadcastReceiver oReceiver = null;



    //버전이 높아지면서 Manifest로 직접 액션필터를 추가할 수 없기에
    //포그라운드 서비스가 실핼 될 때마나 필터를 추가하도록 호출하는 역할을 함
    private void registerScreenReceiver(){
        OnScrean_BroadcastReceiver screenOnReceiver = new OnScrean_BroadcastReceiver();
        IntentFilter filter = new IntentFilter("android.intent.action.SCREEN_OFF");
        registerReceiver(screenOnReceiver, filter);
    }


    //실제로 메인액티비티에서 호출하게 되는 메소드
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent,flags,startId);





        mStartId = startId;

        // 5분후에 시작
        mHandler = new Handler();
        mHandler.postDelayed(MyService.this, LOCATION_UPDATE_DELAY);
        mIsRunning = true;


                startForegroundService();
                registerScreenReceiver();






        return START_STICKY;
    }





    //서비스가 종료될때 실행됨
    public void startForegroundService(){


        //포그라운드 실행시 노티피케이션 정의는 필수다.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"default");
        builder.setSmallIcon(R.drawable.ic_stat_name);
        builder.setContentTitle("켜는 것은 메모장");
        builder.setContentText("여기 눌러서 공부좀 하세요..");

        builder.setDefaults(Notification.DEFAULT_LIGHTS);

        Intent notificationIntent = new Intent(this,MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);

        builder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("default","노트채널", NotificationManager.IMPORTANCE_NONE));
        }




        startForeground(1,builder.build());


    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }





    public void onCreate() {

        // 등록된 알람은 제거
        Log.d("PersistentService", "onCreate()");
        unregisterRestartAlarm();

        super.onCreate();

        mIsRunning = false;

    }



    public void onDestroy() {
        registerRestartAlarm();

        super.onDestroy();

        mIsRunning = false;



    }



    private void registerRestartAlarm() {

        Log.d("PersistentService", "registerRestartAlarm()");

        Intent intent = new Intent(this, RestartService.class);
        intent.setAction(RestartService.ACTION_RESTART_PERSISTENTSERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);

        long firstTime = SystemClock.elapsedRealtime();
        firstTime += REBOOT_DELAY_TIMER; // 10초 후에 알람이벤트 발생

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime,REBOOT_DELAY_TIMER, sender);
    }


    /**
     * 기존 등록되어있는 알람을 해제한다.
     */
    private void unregisterRestartAlarm() {

        Log.d("PersistentService", "unregisterRestartAlarm()");
        Intent intent = new Intent(this, RestartService.class);
        intent.setAction(RestartService.ACTION_RESTART_PERSISTENTSERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(sender);
    }


    @Override
    public void run() {

    }
}


