package e.sky64.retrofit_practice.Service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by hh960 on 2018-02-20.
 */
//FCM을 통한 푸시 알림 구현
public class FirebaseInstanceIDService  extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        // 업데이트된 인스턴스ID 토큰을 가져온다. 이 토큰을 내 서버로 보내면 더 많은 기능 구현 가능
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);
    }

    //토큰을 내 서버로 보내는 메소드 (추후 구현)
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }
}

