$(document).ready(function () {
    restartTable();
});


function restartTable() {
    let tableBody = $("#allUsersTableBody")

    tableBody.children().remove();

    fetch("admin/rest/allusers")
        .then((response) => {
            response.json().then(data => data.forEach(function (item, i, data) {
                let table = createTable(item);
                tableBody.append(table);
            }));
        }).catch(error => {
        console.log(error);
    });
}

function createTable(user) {
    let userRoles = "";
    for (let i = 0; i < user.roles.length; i++) {
        userRoles += (" " + user.roles[i].role).replace('ROLE_','');
        if (i < user.roles.length - 1 ){
            userRoles += ' ,';
        }
    }
    return `<tr>
            <td>${user.id}</td>
            <td>${user.name}</td>
            <td>${user.lastName}</td>
            <td>${user.age}</td>
            <td>${user.email}</td>
            <td>${user.username}</td>
            <td>${userRoles}</td>
            <td>
                <a  href="/admin/rest/${user.id}" class="btn btn-primary editBtn" >Edit</a>
            </td>
            <td>
                <a  href="/admin/rest/delete/${user.id}" class="btn btn-danger deleteBtn">Delete</a>
            </td>
        </tr>`;
}








