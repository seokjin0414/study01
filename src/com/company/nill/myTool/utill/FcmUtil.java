package com.company.nill.myTool.utill;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class FcmUtil {
    private Logger L = LoggerFactory.getLogger(this.getClass());


    public void send_FCM(String tokenId, String title, String content , String linkUrl, String imgUrl) {
        try {

            ClassPathResource refreshToken = new ClassPathResource("kkk-dc228-firebase-adminsdk-fzhh3-0847452c6f.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(refreshToken.getInputStream()))
                    .setDatabaseUrl("https://kkk-dc228.firebaseio.com")
                    .build();



            //Firebase 처음 호출시에만 initializing 처리
            if(FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }


            String registrationToken = tokenId;

            // message 작성
            AndroidNotification noti = AndroidNotification
                    .builder()
                    .setTitle(title)
                    .setBody(content)
                    .setColor("#f45342")
                    .build();

            AndroidConfig config = AndroidConfig
                    .builder()
                    .setTtl(3500 * 1000)
                    .setPriority(AndroidConfig.Priority.NORMAL)
                    //.setNotification(noti)
                    .build();

            Message msg = Message
                    .builder()
                    .setAndroidConfig(config)
                    .setToken(registrationToken)
                    .putData("title", title)
                    .putData("body", content)
                    .putData("url", linkUrl)
                    .putData("img", imgUrl)
                    .build();

           String response = FirebaseMessaging.getInstance().send(msg);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
