package picto.com.foldermanager.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import picto.com.foldermanager.domain.user.User;
import picto.com.foldermanager.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class FCMService {

    @Value("${fcm.service-account.file}")
    private Resource serviceAccount;

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(
                            com.google.auth.oauth2.GoogleCredentials.fromStream(serviceAccount.getInputStream()))
                    .build();
            FirebaseApp.initializeApp(options);
        }
    }

    public void sendPushNotification(String targetToken, String title, String body) {
        try {
            Message message = Message.builder()
                    .setToken(targetToken)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent message: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveFcmToken(Long userId, String fcmToken) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        user.setFcmToken(fcmToken);
        userRepository.save(user);
    }
}
