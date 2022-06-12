'use strict';
const URL ='http://localhost:8091';
const urlReg = 'http://localhost:8091/registered';
console.log(token);
const headers = {'Authorization': `Bearer ${token}`};
var stompClient = null;
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
const btnSend = document.getElementById('btnSend');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');
const openButton = document.getElementById('openButton');
const closeButton = document.getElementById('closeButton');

btnSend.addEventListener('click', send);

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

console.log("Пошла загрузка скрипта");
console.log('Массив цветов: ' + colors.length);

//Получение зарегестрированного пользователя
async function getRegisteredUser() {
    const response = await fetch(`${urlReg}`, {headers});
    return await response.json();
}
async function showUser() {
    const registeredUser = await getRegisteredUser();
    return registeredUser;
}

function connect(event) {
    console.log("Подключаемся к соккету логгирование");
    console.log('Headers ' + headers );
        var socket = new SockJS(URL + '/user/chatApp');
        stompClient = Stomp.over(socket);
        stompClient.connect(headers, onConnected, onError);
    event.preventDefault();
}


async function onConnected() {
    const registeredUser = await getRegisteredUser();
    // Subscribe to the Public Topic
    stompClient.subscribe('/user/chat/1', onMessageReceived);

    // Tell your username to the server
    stompClient.send("/api/user/chat/1/register",
        {},
        JSON.stringify({message: 'JOIN' })
    )
    console.log('Подключено к серверу Websocket')
    connectingElement.textContent = '';
}


function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function send(event) {
    console.log(' В методе отправки сообщения: '+ messageInput);
    var messageContent = messageInput.value.trim();

    if(messageContent && stompClient) {
        var chatMessage =
            {messageContent: messageInput.value};
        stompClient.send("/api/user/chat/1/send", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}


function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    var messageElement = document.createElement('li');
    console.log('Сообщение  бэка ' + message.message + ' от пользователя ' + message.nickName);

    if(message.message === "JOIN") {
        messageElement.classList.add('event-message');
        message.message = message.nickName + ' joined!';
    }
    else {
        messageElement.classList.add('chat-message');

        // var img = new Image();
        // img.src = message.image;
        // img.onload = function (){
        //     var avatarElement = document.createElement('IMG');
        //     avatarElement.src = message.image;
        // }
        // img.onerror = function (){
            var avatarElement = document.createElement('i');
            var avatarText = document.createTextNode(message.nickName[0]);
            avatarElement.appendChild(avatarText);
            avatarElement.style['background-color'] = getAvatarColor(message.nickName);

        // }







        messageElement.appendChild(avatarElement);
        //
        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.nickName);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    //
    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.message);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}


function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}


connect();






















// usernameForm.addEventListener('submit', connect, true)
// messageForm.addEventListener('submit', send, true);
// messageForm.addEventListener('submit', send, true)



// function openForm() {
//     document.getElementById("chat-page").style.display = "block";
//     document.getElementById("openButton").style.visibility = "hidden";
//
// }
//
// function closeForm() {
//     document.getElementById("chat-page").style.display = "none";
//     document.getElementById("openButton").style.visibility = "visible";
// }