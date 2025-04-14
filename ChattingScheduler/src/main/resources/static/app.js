const socket = new SockJS('http://bogota.iptime.org:8085/ws-connect', null, {
  transports: ['websocket', 'xhr-streaming', 'xhr-polling']
});

const stompClient = new StompJs.Client({
  webSocketFactory: () => socket,
  reconnectDelay: 5000,
  heartbeatIncoming: 4000,
  heartbeatOutgoing: 4000,
  debug: function(str) {
    console.log('STOMP: ' + str);
  }
});

function connect() {
  console.log('Connecting to WebSocket...');
  stompClient.activate();
}

function disconnect() {
  console.log('Disconnecting from WebSocket...');
  stompClient.deactivate();
  setConnected(false);
}

stompClient.onConnect = (frame) => {
  console.log('Connected: ' + frame);
  setConnected(true);

  // 채팅 기록 가져오기
  $.get('/chat/history/' + $("#folderId").val(), (messages) => {
    console.log('Chat history loaded:', messages);
    messages.forEach((msg) => {
      showChat(msg.username + ": " + msg.content);
    });
  });

  // 메시지 구독
  const subscription = stompClient.subscribe('/subscribe/chat.' + $("#folderId").val(), (message) => {
    console.log('Received message:', message);
    let body = JSON.parse(message.body);
    let username = body.username;
    let content = body.content;
    showChat(username + ": " + content);
  });
};

stompClient.onWebSocketError = (error) => {
  console.error('WebSocket Error:', error);
  setConnected(false);
};

stompClient.onStompError = (frame) => {
  console.error('STOMP Error:', frame.headers['message']);
  console.error('Additional details:', frame.body);
  setConnected(false);
};

function setConnected(connected) {
  console.log('Setting connected state:', connected);
  $("#connect").prop("disabled", connected);
  $("#disconnect").prop("disabled", !connected);
  if (connected) {
    $("#conversation").show();
  } else {
    $("#conversation").hide();
  }
  $("#greetings").html("");
}

function sendChat() {
  if (!stompClient.connected) {
    console.error('Not connected to WebSocket');
    return;
  }
  
  const folderId = $("#folderId").val();
  const senderId = $("#senderId").val();
  const content = $("#content").val();
  
  if (!folderId || !senderId || !content) {
    console.error('Missing required fields');
    return;
  }

  console.log('Sending message:', { folderId, senderId, content });
  stompClient.publish({
    destination: "/publish/chat." + folderId,
    body: JSON.stringify({ 'senderId': senderId, 'content': content })
  });
  document.getElementById('content').value = '';
}

function showChat(message) {
  console.log('Showing message:', message);
  $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

function deleteChat() {
  if (!stompClient.connected) {
    console.error('Not connected to WebSocket');
    return;
  }

  const messageId = prompt("삭제할 메시지 ID를 입력하세요:");
  if (messageId) {
    const folderId = $("#folderId").val();
    const senderId = $("#senderId").val();
    
    console.log('Deleting message:', { messageId, folderId, senderId });
    stompClient.publish({
      destination: "/publish/chat/delete/" + folderId + "/" + messageId,
      body: JSON.stringify({ 'senderId': senderId })
    });
  }
}

$(function () {
  console.log('Initializing WebSocket application...');
  $("form").on('submit', (e) => e.preventDefault());
  $("#connect").click(() => connect());
  $("#disconnect").click(() => disconnect());
  $("#send").click(() => sendChat());
  $("#delete").click(() => deleteChat());
});
