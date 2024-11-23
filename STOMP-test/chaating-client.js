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

const senderId = process.argv[2]
const folderId = process.argv[3]

function askQuestion(query) {
    return new Promise((resolve) => {
        rl.question(query, resolve)
    })
}

async function inputMsg(){
    content = await askQuestion(`[SI$:${senderId}|FI:${folderId}] `)
    if(content === "q"){
        chatClient.disconnect()
        exit(1)
    }
    chatClient.sendMessage(content)
    inputMsg()
}

class ChatClient {
    constructor() {
        this.stompClient = null;
    }

    // 연결 설정
    connect() {
        const socket = new SockJS('http://localhost:8083/chatting-scheduler');
        this.stompClient = Stomp.over(socket);

        // 디버그 메시지 비활성화
        this.stompClient.debug = null;

        this.stompClient.connect({}, (frame) => {
            console.log('Connected: ' + frame);
            
            // 채팅방 구독
            this.subscribeToChatRoom(); // 테스트용 폴더 ID
            this.stompClient.send(
                "/send/chat/enter",
                {},
                JSON.stringify({
                    folderId: folderId,
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
        this.stompClient.subscribe(`/folder/${folderId}`, (message) => {
            content = JSON.parse(message.body)
            if(content["senderId"] != senderId)
                console.log('Received message : ', content);
        });
    }

    // 메시지 전송
    sendMessage(content) {
        const chatMessage = {
            folderId: folderId,
            content: content,
            senderId: senderId,
            messageType: 'TALK',
        };

        this.stompClient.send(
            "/send/chat/message",
            {},
            JSON.stringify(chatMessage)
        );
        console.log('Sent message:', chatMessage);
    }

    // 연결 종료
    disconnect() {
        if (this.stompClient) {
            this.stompClient.send(
                "/send/chat/exit",
                {},
                JSON.stringify({
                    folderId: folderId,
                    content: "",
                    senderId: senderId,
                    messageType: 'EXIT',
                    sendDatetime: +(new Date().toUTCString())
                })
            );            
            this.stompClient.disconnect();
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