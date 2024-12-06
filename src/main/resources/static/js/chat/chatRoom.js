var stompClient = null;

function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);

        // 현재 채팅방 구독
        stompClient.subscribe('/topic/' + currentChatRoomId, function(message) {
            showMessage(JSON.parse(message.body));
        });

        // 채팅 메시지 로드
        loadChatHistory();
    });
}

function sendMessage() {
    var messageContent = document.getElementById("message").value.trim();
    if (messageContent && stompClient) {
        var chatMessage = {
            roomId: currentChatRoomId,
            senderId: sessionId,
            content: messageContent
        };

        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        document.getElementById("message").value = '';
        scrollToBottom(); // 입력 후 자동 스크롤
    }
}

function showMessage(message) {
    var chatItems = document.getElementById('chat-items');

    var messageContainer = document.createElement('li');
    messageContainer.classList.add('chat-item');

    // 메시지 작성자 구분
    if (message.senderId === sessionId) {
        messageContainer.classList.add('my-message');
    } else {
        messageContainer.classList.add('other-message');
    }

    var iconElement = document.createElement('i');
    iconElement.classList.add('fas', 'fa-user-circle', 'chat-icon');

    var chatInfoElement = document.createElement('div');
    chatInfoElement.classList.add('chat-info');

    var nameElement = document.createElement('div');
    nameElement.classList.add('chat-name');
    nameElement.textContent = message.senderId;

    var messageElement = document.createElement('div');
    messageElement.classList.add('chat-message');
    messageElement.textContent = message.content;

    chatInfoElement.appendChild(nameElement);
    chatInfoElement.appendChild(messageElement);
    messageContainer.appendChild(iconElement);
    messageContainer.appendChild(chatInfoElement);
    chatItems.appendChild(messageContainer);

    scrollToBottom(); // 새로운 메시지 추가 후 스크롤
}

function loadChatHistory() {
    fetch('/chat/view/chatRooms/' + currentChatRoomId + '/messages')
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch chat history');
            }
            return response.json();
        })
        .then(messages => {
            messages.forEach(message => {
                showMessage(message);
            });
            scrollToBottom(); // 메시지 로드 후 스크롤
        })
        .catch(error => {
            console.error('Error loading chat history:', error);
        });
}

function scrollToBottom() {
    var chatBody = document.querySelector('.main');
    chatBody.scrollTop = chatBody.scrollHeight;
}

window.onload = function() {
    connect();
    scrollToBottom(); // 페이지 로드 시 스크롤
};
