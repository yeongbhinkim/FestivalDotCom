var stompClient = null;

function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);

        // 현재 채팅방의 주제 구독
        stompClient.subscribe('/topic/' + currentChatRoomId, function(message) {
            showMessage(JSON.parse(message.body));
        });

        // 기존 채팅 메시지 로드
        loadChatHistory();
    });
}

function sendMessage() {
    var messageContent = document.getElementById("message").value;
    if (messageContent && stompClient) {
        var chatMessage = {
            roomId: currentChatRoomId,
            senderId: sessionId,
            content: messageContent
        };
        stompClient.send("/chat/view/chat.sendMessage", {}, JSON.stringify(chatMessage));
        document.getElementById("message").value = '';
    }
}

function showMessage(message) {
    var chatItems = document.getElementById('chat-items');

    var messageContainer = document.createElement('li');
    messageContainer.classList.add('chat-item');

    if (message.senderId === sessionId) {
        messageContainer.classList.add('my-message');  // 본인이 보낸 메시지일 경우 스타일 추가
    }
    var iconElement = document.createElement('i');
    iconElement.classList.add('fas', 'fa-user-circle', 'chat-icon');

    var chatInfoElement = document.createElement('div');
    chatInfoElement.classList.add('chat-info');

    var nameElement = document.createElement('div');
    nameElement.classList.add('chat-name');
    nameElement.textContent = message.sender;

    var messageElement = document.createElement('div');
    messageElement.classList.add('chat-message');
    messageElement.textContent = message.content;

    chatInfoElement.appendChild(nameElement);
    chatInfoElement.appendChild(messageElement);
    messageContainer.appendChild(iconElement);
    messageContainer.appendChild(chatInfoElement);
    chatItems.appendChild(messageContainer);
}

function loadChatHistory() {
    fetch('/chat/view/chatRooms/' + currentChatRoomId + '/messages')
        .then(response => response.json())
        .then(messages => {
            messages.forEach(message => {
                showMessage(message);
            });
        });
}

window.onload = function() {
    connect();
};
