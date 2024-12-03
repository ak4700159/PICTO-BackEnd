// WebSocket과 STOMP 클라이언트 라이브러리 설치
// npm install sockjs-client stompjs

// 웹소켓
const SockJS = require('sockjs-client');
const Stomp = require('stompjs');

// 터미널 입출력
const readline = require('readline');
const { stdin: input, stdout: output, exit } = require('process');
const { userInfo } = require('os');
const rl = readline.createInterface({ input, output });

// arguments 받기
const senderId = process.argv[2]

function askQuestion(query) {
    return new Promise((resolve) => {
        rl.question(query, resolve)
    })
}

async function inputMsg(){
    content = await askQuestion(`[UI:${senderId}]`)
    if(content === "q"){
        chatClient.disconnect()
        exit(1)
    }

    lat = content.split('/')[0]
    lng = content.split('/')[1]
    chatClient.sendMessage(lat, lng)
    inputMsg()
}

class ChatClient {
    constructor() {
        this.stompClient = null;
    }

    // 연결 설정
    connect() {
<<<<<<< HEAD
        const socket = new SockJS('http://3.35.153.213:8085/session-scheduler');
=======
        const socket = new SockJS('http://52.79.109.62:8085/session-scheduler');
>>>>>>> origin/main
        this.stompClient = Stomp.over(socket);

        // 디버그 메시지 비활성화
        this.stompClient.debug = null;

        this.stompClient.connect({}, (frame) => {
            console.log('Connected: ' + frame);
            
            // 채팅방 구독
            this.subscribeToChatRoom(); // 테스트용 폴더 ID
            // 세션 연결
            this.stompClient.send(
                "/send/session/enter",
                {},
                JSON.stringify({
                    senderId: senderId,
                    messageType: 'ENTER',
                })
            );
            
            // 연결 성공 후 터미널로 실시간 채팅
            inputMsg()

        }, (error) => {
            console.error('STOMP connection error : ', error);
        });
    }

    // 채팅방 구독
    subscribeToChatRoom() {
        this.stompClient.subscribe(`/session/${senderId}`, (message) => {
            console.log("공유 메시지")
            if(+message.body["senderId"] != senderId)
                console.log('Received message : ', JSON.parse(message.body));
        });
        console.log("세션 구독 성공")
    }

    // 메시지 전송
    sendMessage(lat, lng) {
        const locationMsg = {
            lat : lat,
            lng : lng,
            senderId : senderId,
            messageType : 'LOCATION',
        };

        this.stompClient.send(
            "/send/session/location",
            {},
            JSON.stringify(locationMsg)
        );
        console.log('Sent message:', locationMsg);
    }

    // 연결 종료
    disconnect() {
        if (this.stompClient) {
            this.stompClient.send(
                "/send/session/exit",
                {},
                JSON.stringify({
                    senderId: senderId,
                    messageType: 'EXIT',
                })
            );            this.stompClient.disconnect();
            console.log('Disconnected');
        }
    }
}

// 클라이언트 테스트 실행
const chatClient = new ChatClient();

// 연결 시작
console.log('Starting connection...');
chatClient.connect();

// 프로그램 종료 시 연결 해제
process.on('SIGINT', () => {
    console.log('Closing connection...');
    chatClient.disconnect();
    process.exit();
});