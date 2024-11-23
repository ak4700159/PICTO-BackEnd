package picto.com.sessionscheduler.config.websocket;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

// 스프링에서 제공하는 내장 메시지 브로커를 사용한다.

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //클라이언트에서 보낸 메세지를 받을 prefix (클라이언트 -> 서버)
        //클라이언트가 서버로 메시지를 보낼 때 사용할 주소의 접두사
        //@MessageMapping("/chat.message")   '/send' 제외하고 매핑
        registry.setApplicationDestinationPrefixes("/send");
        //해당 주소를 구독하고 있는 클라이언트들에게 메세지 전달 prefix (서버 -> 클라이언트)
        registry.enableSimpleBroker("/session");
    }

    // 주소 : ws://localhost:8083/session-scheduler 로 소켓 연결을 한다.
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/session-scheduler")   //SockJS 연결 주소
                .withSockJS(); //버전 낮은 브라우저에서도 적용 가능
    }
}