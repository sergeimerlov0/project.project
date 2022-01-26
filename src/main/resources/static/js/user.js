async function changePage(page) {
    var cookieValue = document.cookie.replace(/(?:(?:^|.*;\s*)token\s*\=\s*([^;]*).*$)|^.*$/, "$1");
    const url =
        'http://localhost:8080/api/user/new?page=' + page;

// Определение асинхронной функции
    async function getapi(url) {

        // Сохранение ответа
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + cookieValue,
            },
        });
        // Хранение данных в формате JSON
        var data = JSON.parse(await response.text())
        console.log(data);
        if (response) {
            hideloader();
        }
        totalPage = data["totalPageCount"];
        show(data);
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
