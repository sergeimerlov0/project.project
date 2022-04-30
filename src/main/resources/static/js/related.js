
window.token = getCookie('token');

const tagList = document.getElementById('relatedTags');
let output = '';

let url = '/api/user/tag/related';

fetch(url,{
    headers: {
        'Authorization': `Bearer ${token}`,
    },
}).then(res => res.json())
    .then(data => {
         data.forEach(tag => {
             console.log(tag)
            output += `
           
            
            <div>
            <a class="card-link btn-light" href="#"> ${tag.title}</a> × ${tag.countQuestion} 
            
            </div>
            <br>
           
            `
        })
        tagList.innerHTML = output;
    })


function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}








