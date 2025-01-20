var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');
var roomIdInput = document.querySelector('#roomId');

var stompClient = null;
var username = null;
var roomId = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {
    // 기본 이벤트 방지
    event.preventDefault();

    // 사용자 이름과 방 ID 가져오기
    username = document.querySelector('#name').value.trim();
    roomId = roomIdInput.value.trim();

    // 입력값 검증
    if (!username || !roomId) {
        alert('사용자 이름과 방 ID를 모두 입력해주세요.');
        return;
    }

    // 사용자 페이지 숨기고 채팅 페이지 표시
    usernamePage.classList.add('hidden');
    chatPage.classList.remove('hidden');

    // 웹소켓 연결
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onError);
}

function onConnected() {
    // 채팅방 구독
    stompClient.subscribe(`/sub/chat/room/${roomId}`, onMessageReceived);

    // 입장 메시지 전송
    stompClient.send("/pub/chat/enter",
        {},
        JSON.stringify({
            sender: username,
            content: 'JOIN',
            roomId: roomId
        })
    );

    connectingElement.classList.add('hidden');

    // 채팅 히스토리 로드
    loadChatHistory();
}

function onError(error) {
    connectingElement.textContent = '서버 연결에 실패했습니다. 페이지를 새로고침해주세요.';
    connectingElement.style.color = 'red';
}

function sendMessage(event) {
    event.preventDefault();

    var messageContent = messageInput.value.trim();
    if (messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageContent,
            roomId: roomId
        };

        stompClient.send("/pub/chat/message", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    var messageElement = document.createElement('li');

    if (message.content === 'JOIN') {
        messageElement.classList.add('event-message');
        messageElement.textContent = message.sender + '번 회원과 상담을 진행합니다';
    } else if (message.content === 'LEAVE') {
        messageElement.classList.add('event-message');
        messageElement.textContent = message.sender + '번 회원이 상담을 종료했습니다';
    } else {
        messageElement.classList.add('message');
        // 메시지 송신자에 따라 클래스 추가
        if (message.sender === username) {
            messageElement.classList.add('sent');
        } else {
            messageElement.classList.add('received');
        }

        var senderElement = document.createElement('div');
        senderElement.classList.add('message-sender');
        senderElement.textContent = message.sender;
        // messageElement.appendChild(senderElement);

        var textElement = document.createElement('div');
        textElement.classList.add('message-content');
        textElement.textContent = message.content;
        messageElement.appendChild(textElement);
    }

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

function loadChatHistory() {
    fetch(`/api/chat/history/${roomId}`)
        .then(response => response.json())
        .then(messages => {
            messages.forEach(message => {
                var messageElement = document.createElement('li');
                messageElement.classList.add('message');

                if (message.sender === username) {
                    messageElement.classList.add('sent');
                } else {
                    messageElement.classList.add('received');
                }

                var senderElement = document.createElement('div');
                senderElement.classList.add('message-sender');
                senderElement.textContent = message.sender;
                messageElement.appendChild(senderElement);

                var textElement = document.createElement('div');
                textElement.classList.add('message-content');
                textElement.textContent = message.content;
                messageElement.appendChild(textElement);

                messageArea.appendChild(messageElement);
            });
            messageArea.scrollTop = messageArea.scrollHeight;
        })
        .catch(error => {
            console.error('채팅 히스토리 로드 실패:', error);
        });
}

// 이벤트 리스너 등록
usernameForm.addEventListener('submit', connect, true);
messageForm.addEventListener('submit', sendMessage, true);