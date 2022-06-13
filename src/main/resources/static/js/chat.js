
const URL = 'http://localhost:8091';
const urlReg = 'http://localhost:8091/registered';
const headers = {'Authorization': `Bearer ${token}`};
let stompClient = null;

const btnSend = document.getElementById('btnSend');
const messageInput = document.querySelector('#message');
const messageArea = document.querySelector('#messageArea');
const connectingElement = document.querySelector('.connecting');
const openButton = document.getElementById('openButton');
const closeButton = document.getElementById('closeButton');


btnSend.addEventListener('click', send);
closeButton.addEventListener('click', closeForm);
openButton.addEventListener('click', openForm);
document.addEventListener('DOMContentLoaded', connect, false)


const colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

//Получение зарегестрированного пользователя
async function getRegisteredUser() {
    const response = await fetch(`${urlReg}`, {headers});
    return await response.json();
}

//Соединение с WebSocket
function connect(e) {
    let socket = new SockJS(URL + '/user/chatApp');
    stompClient = Stomp.over(socket);
    stompClient.connect(headers, onConnected, onError);
    e.preventDefault();
}

//Подписка на сообщения
function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/user/chat/1', onMessageReceived);

    // Tell your username to the server
    stompClient.send("/api/user/chat/1/register",
        {},
        JSON.stringify({message: 'JOIN'})
    )
    connectingElement.textContent = '';
}

function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

function send(event) {
    let messageContent = messageInput.value.trim();
    if (messageContent && stompClient) {
        let chatMessage =
            {messageContent: messageInput.value};
        stompClient.send("/api/user/chat/1/send", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

async function imageExists(imgUrl) {
    if (!imgUrl) {
        return false;
    }
    return new Promise(res => {
        const image = new Image();
        image.onload = () => res(true);
        image.onerror = () => res(false);
        image.src = imgUrl;
    });
}

async function onMessageReceived(payload) {
    let message = JSON.parse(payload.body);
    let messageElement = document.createElement('li');
    if (message.message === "JOIN") {
        messageElement.classList.add('event-message');
        message.message = message.nickName + ' joined!';
    } else {
        messageElement.classList.add('chat-message');

        if (await imageExists(message.image)) {
            let avatarElement = document.createElement('IMG');
            avatarElement.src = message.image;

            messageElement.appendChild(avatarElement);
            let usernameElement = document.createElement('span');
            let usernameText = document.createTextNode(message.nickName);
            usernameElement.appendChild(usernameText);
            messageElement.appendChild(usernameElement);

        } else {

            let avatarElement = document.createElement('i');
            let avatarText = document.createTextNode(message.nickName[0]);
            avatarElement.appendChild(avatarText);
            avatarElement.style['background-color'] = getAvatarColor(message.nickName);

            messageElement.appendChild(avatarElement);
            let usernameElement = document.createElement('span');
            let usernameText = document.createTextNode(message.nickName);
            usernameElement.appendChild(usernameText);
            messageElement.appendChild(usernameElement);
        }

    }
    //
    let textElement = document.createElement('p');
    let messageText = document.createTextNode(message.message);
    textElement.appendChild(messageText);
    messageElement.appendChild(textElement);
    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

function getAvatarColor(messageSender) {
    let hash = 0;
    for (let i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    let index = Math.abs(hash % colors.length);
    return colors[index];
}

function closeForm() {
    document.getElementById("chat-page").style.display = "none";
    document.getElementById("openButton").style.display = "block";
}

function openForm() {
    document.getElementById("chat-page").style.display = "block";
    document.getElementById("openButton").style.display = "none";

}


