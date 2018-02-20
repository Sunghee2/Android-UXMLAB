package e.sky64.retrofit_practice.Service;

/**
 * Created by hh960 on 2018-02-20.
 */
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

import e.sky64.retrofit_practice.Activities.LoginActivity;
import e.sky64.retrofit_practice.R;

//FCM을 통한 푸시 알림 구현. 메세지를 받게 되었을 경우, 이 자바 클래스 실행
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = "FirebaseMsgService";

    private String msg;

    //메세지를 받았을 경우 실행되는 메소드
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //어디에서 받았는지 로그로 확인 가능
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // 메세지에 담긴 데이터 확인 가능
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }


        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
        //메세지의 body를 갖고온다 이것이 우리가 흔히 보게 되는 메세지
        msg = remoteMessage.getNotification().getBody();

        //만약 푸시알림 내용을 클릭하였을 때, 앱의 LoginActivity로 이동
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, LoginActivity.class), 0);

        //푸시 알림 내용 정의
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher) //아이콘 지정
                .setContentTitle("UXMLAB알림")  //제목 지정
                .setContentText(msg)        //내용 지정
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)) //알림 소리 지정
                .setVibrate(new long[]{1, 1000}); //알림 진동 지정

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, mBuilder.build());


        mBuilder.setContentIntent(contentIntent);
    }
}

