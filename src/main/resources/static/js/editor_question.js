let toolbarOptions = [
    ['bold', 'italic', 'underline', 'strike'],        // toggled buttons
    ['blockquote', 'code-block'],

    [{ 'header': 1 }, { 'header': 2 }],               // custom button values
    [{ 'list': 'ordered'}, { 'list': 'bullet' }],
    [{ 'script': 'sub'}, { 'script': 'super' }],      // superscript/subscript
    [{ 'indent': '-1'}, { 'indent': '+1' }],          // outdent/indent
    [{ 'direction': 'rtl' }],                         // text direction

    [{ 'size': ['small', false, 'large', 'huge'] }],  // custom dropdown
    [{ 'header': [1, 2, 3, 4, 5, 6, false] }],

    [{ 'color': [] }, { 'background': [] }],          // dropdown with defaults from theme
    [{ 'font': [] }],
    [{ 'align': [] }],

    ['clean']                                         // remove formatting button
];

let quill = new Quill('#document-full', {
    modules: {
        toolbar: toolbarOptions
    },
    theme: 'snow'
});

let partTag = "";
let cardWrapper = null;
let newString = "";
let inputString = "";
let format = "`~!@~$%^&*()\[\]{}|.<>\/\"\'?";
var flagSuccess = 0;

const titleInput = document.querySelector('#inputQuestion');
const tagInput = document.querySelector('#inputTags');

document.getElementById('inputTags').addEventListener('keyup', function (event) {
    inputString = event.target.value;
    partTag = inputString.toLowerCase();

    if (inputString.includes(",") || inputString.includes(" ") || inputString.includes(";")) {
        let y = lastSeparatorPosition(inputString);
        partTag = inputString.substring(y + 1).toLowerCase();
    }
    for (let t = 0; t < format.length; t++) {
        //некоторые знаки (!@$%^&*()<>?~|) двоятся (пользователь печатает один знак, а сообщений выходит два)

        if (inputString.endsWith(format.charAt(t))) {
            let el = document.getElementById('forTagAlert');
            removeAllChild(el);
            document.getElementById('forTagAlert').appendChild(warnMsg("Разделителем между метками может быть запятая, пробел или точка с запятой!", "alert-warning")
                .cloneNode(true));
            el.children.length > 1 ? el.removeChild(el.lastChild) : {};

        }
    }
    getTagList(partTag);
})

const pushTag = (btn) => {
    let btnId = "" + btn.id;
    let headerId = "tagName_" + btnId.substring(btnId.length - 1, btnId.length);
    let insertTagValue = document.getElementById(headerId).textContent;

    if (tagInput.value.lastIndexOf(",") > -1 || tagInput.value.lastIndexOf(" ") > -1
        || tagInput.value.lastIndexOf(";") > -1) {
        let u = lastSeparatorPosition(tagInput.value);
        newString = tagInput.value.substring(0, u + 1) + insertTagValue + ";";
    } else {
        newString = insertTagValue + ";";
    }
    tagInput.value = newString;
}

function lastSeparatorPosition(string) {
    let a = string.lastIndexOf(",");
    let b = string.lastIndexOf(";");
    let c = string.lastIndexOf(" ");
    return Math.max(a, b, c);
}

function getTagList(partTag) {
    for(let t = 0; t < format.length; t++) {
        if (partTag.includes(format.charAt(t)) || partTag === "") {
            return;
        }
    }

    return  fetch('http://localhost:8091/api/user/tag/latter?string=' + partTag, {
        headers: {
            'Authorization': 'Bearer ' + window.token,
        }
    }).then(checkError)
        .then(renderTags)
        .catch(error => console.log(error));
}

function checkError(response) {
    if(response.status >=200 && response.status <=299) {
        return response.json();
    } else {
        console.log("Меток, начинающихся с данного символа " + partTag + " нет.");
        throw Error(response.statusText);
    }
}

function renderTags(tagsJson) {
    removeCardWrapper();
    if (tagsJson.length === 0) {
        removeCardWrapper();
        return;
    } else if (tagsJson.length > 5) {
        tagsJson.length = 5;
    }
    cardWrapper = document.createElement('div');
    cardWrapper.className = "card-group";

    for (let i = 0, y = 1; i < tagsJson.length, y < tagsJson.length + 1; i++, y++) {
        cardWrapper.innerHTML += `<div class="card">
            <div class="card-body">
            <p id="tagName_${y}"  class="card-title"><strong>${tagsJson[i].name}</strong></p>
            <p class ="text-muted" hidden><small> id = ${tagsJson[i].id}</small></p>
            <p class="text-break"><small> это описание: ${tagsJson[i].description}</small></p>
            <button id="tagBtn_${y}" type="button" onclick="pushTag(this)" class="btn btn-outline-primary btn-sm">
            <small>Вставить</small></button>
            </div></div>`;
        document.getElementById('tagCard').appendChild(cardWrapper);
    }
}

document.getElementById('checkQuestion').addEventListener('click', (e) => {
    e.preventDefault();
    let arrTag = tagInput.value.split(/[; ,]/).filter((el => {
        return el !== null && typeof el !== 'undefined' && el !== "";
    }));
    fieldCheck(titleInput.value, arrTag, tagInput.value);
    if(flagSuccess === 1) {
        removeCardWrapper();
        document.getElementById('forSuccessAlert').appendChild(warnMsg("Все ок, можно отправлять вопрос!", "alert-success"));
    }
});

function fieldCheck(title, tags, tagInputValue ) {
    if (title.length === 0) {
        flagSuccess = 0;
        document.getElementById('forTitleAlert').appendChild(warnMsg("Не заполнено поле!", "alert-warning").cloneNode(true));
    }
    if (quill.root.innerHTML.length < 30) {
        flagSuccess = 0;
        document.getElementById('forContentAlert').appendChild(warnMsg("Укажите не менее одной строки вопроса!", "alert-warning").cloneNode(true));
    }
    if ((tags.length > 5) || (tags.length === 0)) {
        flagSuccess = 0;
        document.getElementById('forTagAlert').appendChild(warnMsg("Укажите не более 5ти меток!", "alert-warning").cloneNode(true))
    }
    if ((title.length !== 0) && (tags.length <= 5) && (tagInputValue.length !== 0)
        && (quill.root.innerHTML.length > 30 )){
        flagSuccess = 1;
    }
}
document.getElementById('sendQuestion').addEventListener('click', (e) => {
    e.preventDefault();
    let arrTag = tagInput.value.split(/[; ,]/).filter((el => {
        return el !== null && typeof el !== 'undefined' && el !== "";
    }));
    fieldCheck(titleInput.value, arrTag, tagInput.value);
    if (flagSuccess !== 0) {
        fetchPost(titleInput.value, quill.root.innerHTML, arrTag);
    }
});
function fetchPost(title, description, tags) {
    fetch('http://localhost:8091/api/user/question', {
        method: 'POST',
        mode: 'cors',
        cache: 'no-cache',
        credentials: 'same-origin',
        headers: {
            'Authorization': 'Bearer ' + window.token,
            'Content-Type': 'application/json;charset=utf-8',
            'Accept': 'application/json'
        },
        body: JSON.stringify({
            title: title,
            description: description,
            tags: tags
        })
    }).then((response) => {
        if (response.ok) {
            document.getElementById('forSuccessAlert').appendChild(warnMsg("Ваш ответ отправлен!", "alert-success"));
        }
    }).catch((error) => {
        console.log('Что-то пошло не так', error);
    });
}

function warnMsg (message, type) {
    let temp = document.createElement('div');
    temp.innerHTML =
        `<div class="alert ${type} alert-dismissible fade show" role="alert">
         ${message} 
         <button type="button" class="close" data-dismiss="alert" aria-label="Close">
         <span aria-hidden="true">&times;</span>
         </button></div>`;
    return temp;
}

function removeCardWrapper() {
    if (cardWrapper !== null) {
        cardWrapper.remove();
    }
}
function removeAllChild(el) {
    while (el.firstChild) {
        el.removeChild(el.lastChild);
    }
}
