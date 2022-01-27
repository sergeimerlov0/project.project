var currentPageNumber = 1;
var totalPage = -1;

//метод предыдущей страницы
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

     // Проверить страницу
    if (page < 1) page = 1;
    if (numPages() != -1 && page > numPages()) page = numPages();

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

function numPages() {
    return totalPage
}

window.onload = function () {
    changePage(1);
};
