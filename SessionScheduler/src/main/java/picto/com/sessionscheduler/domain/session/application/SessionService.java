package picto.com.sessionscheduler.domain.session.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import picto.com.sessionscheduler.domain.session.dto.PhotoMsg;
import picto.com.sessionscheduler.domain.session.dto.SessionInfo;
import picto.com.sessionscheduler.utils.GeoDistance;
import picto.com.sessionscheduler.domain.session.dao.*;
import picto.com.sessionscheduler.domain.session.dto.GetKakaoLocationInfoResponse;
import picto.com.sessionscheduler.domain.session.dto.Message;
import picto.com.sessionscheduler.domain.session.entity.*;
import picto.com.sessionscheduler.utils.KakaoUtils;

import java.util.*;
import java.util.stream.Collectors;

// 클라이언트는 자신이 보낸 메시지를 필터해야된다. -> 기능적인 이슈
// 단수 복수 꼭 확인할 것

@Service
@RequiredArgsConstructor
public class SessionService {
    // List elements = sessionId = userId
    private final SimpMessageSendingOperations messagingTemplate;
    private final SessionRepository sessionRepository;
    private final TagSelectRepository tagSelectRepository;
    private final PhotoRepository photoRepository;
    private final KakaoUtils kakaoUtils;


    // 접속 중인 사용자의 실시간 위치 정보 업데이트
    // 트랜섹션 어노테이션을 통해서 영속성 계층 생성 could not initialize proxy ~ session 에러 해결
    @Transactional
    public void receivedLocation(Message message) {
        if(message.getMessageType() == Message.MessageType.LOCATION){
            // session DB 업데이트
            Long userId = message.getSenderId();
            // if(message.getSenderId() != userId) throw new IllegalArgumentException("Invalid session id OR user id");
            double lng = message.getLng();
            double lat = message.getLat();

            System.out.println("userId : " + userId);
            Session findSession = sessionRepository.getReferenceById(userId);
            findSession.setCurrentLng(lng);
            findSession.setCurrentLat(lat);

            // 지역명 업데이트
            String updateLocation;
            GetKakaoLocationInfoResponse kakaoResponse = kakaoUtils.convertLocationFromPos(lng, lat);
            if(Objects.requireNonNull(kakaoResponse).getDocuments().isEmpty()) {
                updateLocation = "좌표 식별 불가";
            } else{
                updateLocation = kakaoResponse.getDocuments().get(0).getAddress().getAddress_name();
            }
            findSession.setLocation(updateLocation);

            // 세션 저장
            sessionRepository.save(findSession);
        } else {
            throw new IllegalArgumentException("Invalid message type");
        }
    }

    // 해당 함수가 호출되었다는 것은 [photo-preprocessing -> photo-manager] 에서 정상적으로 디비에 사진을 저장했음을 의미.
    // 해당 사진 중심으로 반경 5km 이내의 접속 중인 사용자들을 탐색.
    @Transactional
    public void sharedPhoto(Message message, UserSessionRegistry registry) {
        Long photoId = message.getPhotoId();
        Photo photo = photoRepository.getReferenceById(photoId);
        double photoLng = message.getLng();
        double photoLat = message.getLat();

        // 해당 사진 중심으로 반경 5km 이내의 접속 중인 사용자들을 탐색.
        if(message.getMessageType() == Message.MessageType.SHARE){
            // 현재 접속 중인 유저들에 대한 Session을 한꺼번에 조회
            Set<Long> sessions = registry
                    .getActiveUsers()
                    .stream()
                    .map(SessionInfo::getUserId)
                    .collect(Collectors.toSet());
            List<Session> dbSessions = sessionRepository.findAllById(sessions);
            for(Session dbSession : dbSessions){
                if(dbSession == null){
                    throw new IllegalArgumentException("Session not found");
                }

                // 접속 중인 사용자 태그 정보를 고려
                List<TagSelect> tags = tagSelectRepository.findByUserId(dbSession.getId());
                List<String> tagNames = tags.stream().map(tagSelect -> tagSelect.getId().getTag()).toList();
                if(!tagNames.contains(photo.getTag())) continue;

                // 자신의 사진이 아닌 경우 주변 사진이 공유되었음을 알린다
                if(!Objects.equals(message.getSenderId(), dbSession.getId())){
                    double searchSessionLng = dbSession.getCurrentLng();
                    double searchSessionLat = dbSession.getCurrentLat();

                    double distance = GeoDistance.calculateDistance(photoLat, photoLng, searchSessionLat, searchSessionLng);
                    // 5km 이내의 사용자들에게 알린다.
                    if(distance <= 5){
                        System.out.println("distance : " + distance + "| user : " + dbSession.getId());
                        messagingTemplate.convertAndSend("/session/" + dbSession.getId(), PhotoMsg.fromEntity(photo));
                    }
                }
            }
        }
        else{
            throw new IllegalArgumentException("Invalid message type");
        }
    }
}
