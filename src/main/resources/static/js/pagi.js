var currentPageNumber = 1;
var totalPage = -1;


function prevPage() {
    if (currentPageNumber > 1) {
        currentPageNumber--;
        changePage(currentPageNumber);
    }
}

function nextPage() {
    if (numPages() != -1 && currentPageNumber < numPages()) {
        currentPageNumber++;
        changePage(currentPageNumber);
    }

}


async function changePage(page) {
    var btn_next = document.getElementById("btn_next");
    var btn_prev = document.getElementById("btn_prev");
    // var btn_1 = document.getElementById("btn_1");
    // var btn_2 = document.getElementById("btn_2");
    // var btn_3 = document.getElementById("btn_3");
    // var listing_table = document.getElementById("listingTable");
    // var page_span = document.getElementById("page");

    // Проверить страницу
    if (page < 1) page = 1;
    if (numPages() != -1 && page > numPages()) page = numPages();

    const url =
        'http://localhost:8080/api/user/new?page=' + page + '&items=10';


// Определение асинхронной функции
    async function getapi(url) {

        // Сохранение ответа
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwYXNzd29yZCIsImp0aSI6InZhc3lhQG1haWwucnUifQ.hO0REtuCGoVbnnlXSCkIAgTXSO9GGEMWqEdkrNMnkFk',
            },
        });

        //
        // Хранение данных в формате JSON
        var data = JSON.parse(await response.text())
        console.log(data);
        if (response) {
            hideloader();
        }
        totalPage = data["totalPageCount"];
        show(data);


        // page_span.innerHTML = page + "/" + numPages();

        if (page == 1) {
            btn_prev.style.visibility = "hidden";
        } else {
            btn_prev.style.visibility = "visible";
        }

        if (page == numPages()) {
            btn_next.style.visibility = "hidden";
        } else {
            btn_next.style.visibility = "visible";
        }
    }

// Вызов этой асинхронной функции
    getapi(url);

// Функция скрытия загрузчика
    function hideloader() {
        document.getElementById('loading').style.display = 'none';
    }

// Функция для определения innerHTML для таблицы HTML
    function show(data) {
        let tab =
            `<tr>
          <th>Id</th>
          <th>Email</th>
          <th>FullName</th>
          <th>City</th>
         </tr>`;

        // Цикл для доступа ко всем строкам
        for (let r of data.items) {
            tab += `<tr> 
    <td>${r.id} </td>
    <td>${r.email}</td>
    <td>${r.fullName}</td>
    <td>${r.city}</td>              
</tr>`;
        }
        // Установка innerHTML в качестве переменной вкладки
        document.getElementById("employees").innerHTML = tab;
    }
}

function numPages() {
    return totalPage
}

window.onload = function () {
    changePage(1);
};