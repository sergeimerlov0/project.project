
const listArray = []
for (let i=0; i<40; i++) {
    listArray.push(`<li class="list-group-item">${i}</li>`)
}
const numberOfItems = listArray.length
const numberPerPage = 5
const currentPage = 1
const numberOfPages = Math.ceil (numberOfItems/numberPerPage)

function buildPage( currPage ) {
    const trimStart = (currPage-1)*numberPerPage
    const trimEnd = trimStart + numberPerPage
    console.log(listArray.slice(trimStart, trimEnd))
}

for ( let i=-1; i<2; i++) {
    $('.paginator').append(`<button class="btn btn-primary" 
    value="${currPageNum+i}">${currPageNum +i}</button>`)
}
$(document).ready( function (){
    buildPage(1)
    buildPagination(currentPage)
    $('.paginator').on('click', 'button', function () {
        var clickedPage = parseInt($(this).val())
        buildPage(clickedPage)
        buildPagination(clickedPage, numberOfPages)
    });
});