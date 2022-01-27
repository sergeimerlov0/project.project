async function changePage(page) {
    var cookieValue = document.cookie.replace(/(?:(?:^|.*;\s*)token\s*\=\s*([^;]*).*$)|^.*$/, "$1");
    const url =
        'http://localhost:8091/api/user/new?page=' + page;

    async function getApi(url) {
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + cookieValue,
            },
        });
        var data = JSON.parse(await response.text())
        console.log(data);
        if (response) {
            hideLoader();
        }
        totalPage = data["totalPageCount"];
        show(data);
    }

    getApi(url);


    function hideLoader() {
        document.getElementById('loading').style.display = 'none';
    }

    function show(data) {
        let tab =
            `<tr>
          <th>Id</th>
          <th>Email</th>
          <th>FullName</th>
          <th>City</th>
         </tr>`;

        for (let r of data.items) {
            tab += `<tr>
    <td>${r.id} </td>
    <td>${r.email}</td>
    <td>${r.fullName}</td>
    <td>${r.city}</td>
</tr>`;
        }
        document.getElementById("employees").innerHTML = tab;
    }

}
