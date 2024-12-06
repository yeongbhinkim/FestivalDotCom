let stompClient = null;

function connect() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, frame => {
        console.log('Connected: ' + frame);

        // 현재 채팅방 구독
        stompClient.subscribe(`/topic/${currentChatRoomId}`, message => {
            showMessage(JSON.parse(message.body));
        });

        // 채팅 메시지 로드
        loadChatHistory();
    }, error => {
        console.error('WebSocket connection failed:', error);
        reconnect();
    });
}

function sendMessage() {
    const messageInput = document.getElementById("message");
    const messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
        const chatMessage = {
            roomId: currentChatRoomId,
            senderId: sessionId,
            content: messageContent
        };

        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
        scrollToBottom(); // 메시지 전송 후 스크롤
    }
}

function showMessage(message) {
    const chatItems = document.getElementById('chat-items');

    const messageContainer = document.createElement('li');
    messageContainer.classList.add('chat-item');
    messageContainer.classList.add(message.senderId === sessionId ? 'my-message' : 'other-message');

    const iconElement = document.createElement('i');
    iconElement.classList.add('fas', 'fa-user-circle', 'chat-icon');

    const chatInfoElement = document.createElement('div');
    chatInfoElement.classList.add('chat-info');

    const nameElement = document.createElement('div');
    nameElement.classList.add('chat-name');
    nameElement.textContent = message.senderId;

    const messageElement = document.createElement('div');
    messageElement.classList.add('chat-message');
    messageElement.textContent = message.content;

    chatInfoElement.append(nameElement, messageElement);
    messageContainer.append(iconElement, chatInfoElement);
    chatItems.appendChild(messageContainer);

    scrollToBottom(); // 새로운 메시지 추가 후 스크롤
}

function loadChatHistory() {
    fetch(`/chat/view/chatRooms/${currentChatRoomId}/messages`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch chat history');
            }
            return response.json();
        })
        .then(messages => {
            const chatItems = document.getElementById('chat-items');
            chatItems.innerHTML = ''; // 기존 메시지 초기화

            messages.forEach(message => showMessage(message));
            scrollToBottom(); // 메시지 로드 후 스크롤
        })
        .catch(error => {
            console.error('Error loading chat history:', error);
        });
}

function scrollToBottom() {
    const chatBody = document.querySelector('.main');
    chatBody.scrollTop = chatBody.scrollHeight;
}

function reconnect() {
    console.log('Reconnecting WebSocket...');
    setTimeout(connect, 5000); // 5초 후 다시 연결 시도
}

// 페이지 로드 시 WebSocket 연결
window.onload = function () {
    connect();
    scrollToBottom(); // 페이지 로드 후 스크롤
};
