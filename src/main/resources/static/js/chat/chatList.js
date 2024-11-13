var stompClient = null;

function connectChatRooms() {
    var socket = new SockJS('/ws'); // WebSocket 연결 엔드포인트
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);

        // 채팅방 목록 변경 사항을 구독
        stompClient.subscribe('/topic/chatRooms', function(message) {
            if (message.body === 'updated') {
                location.reload(); // 변경 사항이 있을 때 목록을 새로고침
            }
        });
    });
}

// 페이지 로드 시 WebSocket 연결
window.onload = function() {
    connectChatRooms();
};
