import {PaginationServiceDto} from "./paginationService";

export class EntityPagination extends PaginationServiceDto{

    constructor() {
        super('/api/user');
    }

    getUserByReg = async (page,items) => {
        const response = await fetch('/api/user/new?page' + page + '&items=' + items);
        return response.json();
    }
}

$(document).ready(function () {
//     let table = document.createElement('tableUsers');
//     let thead = document.createElement('Users');
//     let tbody = document.createElement('tbody');
//
//     table.appendChild(thead);
//     table.appendChild(tbody);
//
// // Adding the entire table to the body tag
//     document.getElementById('body').appendChild(table);
// });

    let tableBody = $("#users")

    tableBody.children().remove();
    fetch("/api/user/new?page=1&").then((response) => {
        response.json().then(data => data.forEach(function (item){
            let table = createTable(item);
            tableBody.append(table);
        }));
    }).catch(error => {
        console.log(error);
    })
});

function createTable(userDto){
    return '<tr> <td>${userDto.id}</td>'
}