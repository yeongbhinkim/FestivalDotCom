let stompClient = null;

function connectChatList() {
    const socket = new SockJS('/ws'); // WebSocket 연결 엔드포인트
    stompClient = Stomp.over(socket);

    stompClient.connect({}, frame => {
        // console.log('Connected: ' + frame);

        // 채팅방 목록 변경 사항을 구독
        stompClient.subscribe('/topic/chatList', message => {
            if (message.body === 'updated') {
                updateChatRoomList(); // 목록 업데이트
            }
        });

        // 초기 목록 로드
        updateChatRoomList();
    }, error => {
        console.error('WebSocket connection error:', error);
        reconnectChatList(); // 연결 실패 시 재시도
    });
}

function updateChatRoomList() {
    fetch('/chat/view/chatList')
        .then(response => {
            if (!response.ok) throw new Error('Failed to fetch chat rooms');
            return response.json();
        })
        .then(chatData => {
            const chatRoomList = document.getElementById('chat-room-list');
            chatRoomList.innerHTML = ''; // 기존 목록 초기화

            if (chatData.roomList.length === 0) {
                const emptyMessage = document.createElement('li');
                emptyMessage.classList.add('chat-room-item', 'empty');
                emptyMessage.textContent = '현재 채팅방이 없습니다. 새로운 방을 만들어보세요!';
                chatRoomList.appendChild(emptyMessage);
                return;
            }

            chatData.roomList.forEach((room, index) => {
                const listItem = document.createElement('li');
                listItem.classList.add('chat-room-item');

                const roomInfo = document.createElement('div');
                roomInfo.classList.add('chat-room-info');

                const roomName = document.createElement('span');
                roomName.classList.add('room-name');
                roomName.textContent = room.roomName;

                const lastMessage = document.createElement('span');
                lastMessage.classList.add('last-message');

                const lastMessageContent = chatData.messageList
                    .filter(message => message.roomId === room.roomId.toString())
                    .at(-1)?.lastMessage;

                lastMessage.textContent = (lastMessageContent && lastMessageContent !== 'undefined')
                    ? ' ' + lastMessageContent
                    : ' 메시지가 없습니다.';

                const joinButton = document.createElement('a');
                joinButton.classList.add('join-button');
                joinButton.href = `/chat/view/chatRoom/${room.roomId}/${room.roomName}`;
                joinButton.textContent = '입장';

                roomInfo.append(roomName, lastMessage);
                listItem.append(roomInfo, joinButton);
                chatRoomList.appendChild(listItem);

            });
        })
        .catch(error => {
            console.error('Error updating chat room list:', error);
        });
}

function reconnectChatList() {
    console.warn('Attempting to reconnect WebSocket...');
    setTimeout(connectChatList, 5000); // 5초 후 재연결
}

// 페이지 로드 시 WebSocket 연결
window.onload = function () {
    connectChatList();
};
