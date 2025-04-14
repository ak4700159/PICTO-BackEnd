const stompClient = new StompJs.Client({
  brokerURL: 'http://bogota.iptime.org:8085/ws-connect'
});

stompClient.onConnect = (frame) => {
  setConnected(true);
  console.log('Connected: ' + frame);

  $.get('/chat/history/' + $("#folderId").val(), (messages) => {
    messages.forEach((msg) => {
      showChat(msg.username + ": " + msg.content);
    });
  });

  stompClient.subscribe('/subscribe/chat.' + $("#folderId").val(), (message) => {
    let body = JSON.parse(message.body);
    let username = body.username;
    let content = body.content;
    showChat(username + ": " + content);
  });
};

function sendChat() {
  stompClient.publish({
    destination: "/publish/chat." + $("#folderId").val(),
    body: JSON.stringify({ 'senderId': $("#senderId").val(), 'content': $("#content").val() })
  });
  document.getElementById('content').value = '';
}

stompClient.onWebSocketError = (error) => {
  console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
  console.error('Broker reported error: ' + frame.headers['message']);
  console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
  $("#connect").prop("disabled", connected);
  $("#disconnect").prop("disabled", !connected);
  if (connected) {
    $("#conversation").show();
  }
  else {
    $("#conversation").hide();
  }
  $("#greetings").html("");
}

function connect() {
  stompClient.activate();
}

function disconnect() {
  stompClient.deactivate();
  setConnected(false);
  console.log("Disconnected");
}

function showChat(message) {
  $("#greetings").append("<tr><td>" + message + "</td></tr>");
  console.log(message);
}

function deleteChat() {
  const messageId = prompt("삭제할 메시지 ID를 입력하세요:");
  if (messageId) {
    stompClient.publish({
      destination: "/publish/chat/delete/" + $("#folderId").val() + "/" + messageId,
      body: JSON.stringify({ 'senderId': $("#senderId").val() })
    });
  }
}

$(function () {
  $("form").on('submit', (e) => e.preventDefault());
  $("#connect").click(() => connect());
  $("#disconnect").click(() => disconnect());
  $("#send").click(() => sendChat());
  $("#delete").click(() => deleteChat());
});
