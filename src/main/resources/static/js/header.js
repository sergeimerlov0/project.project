const model = [
    {
        type: 'logo',
        value: 'Kata',
        imageStyles: {
            width: '120px',
            height: '40px'
        }
    },
    {
        type: 'topnav', value: [
            'About',
            'Products',
            'For Teams'
        ]
    },
    {type: 'search'},
    {
        type: 'auth', value: [
            'Logout'
        ]
    }
]

const $header = document.querySelector('#header')

function header() {
    let html = `
    <div class="container">
      <div class="row">
        <div style="display: flex;
                    align-items: center;
                    width: 100%;
                    height: 50px;" 
                    class="header__inner" id="header__inner">
        
        </div>
      </div>
    </div>
  `
    $header.insertAdjacentHTML('beforeend', html)
}

header()
const $header__inner = document.querySelector('#header__inner')
model.forEach(block => {
    let html = ''

    if (block.type === 'topnav') {
        html = topnav(block)
    } else if (block.type === 'auth') {
        html = auth(block)
    } else if (block.type === 'logo') {
        html = logo(block)
    } else if (block.type === 'search') {
        html = search(block)
    }

    $header__inner.insertAdjacentHTML('beforeend', html)
})

const h = document.getElementById("header");
h.style.borderBottom = "1px solid #f5f5f5";
h.className = "container-fluid fixed-top bg-light";

function logo(block) {
    return `
      <div class="col-lg-2">
        <a style="float: left;
                  width: 120px;"
                  href="https://kata.academy/" class="logo" id="logo">
          <img src="https://kata.academy/images/icons/logo2.svg" height="40px" alt=""/>
        </a>
      </div>
    `
}

function topnav(block) {
    let html = ''
    block.value.forEach(item => {
        html += `
      <a href="#" class="topnav__link text-dark">${item}</a>
    `
    })

    return `
    <div class="col-lg-3">
      <nav style="display: flex;
                  align-items: center;
                  justify-content: space-between;"
                  class="topnav" id="topnav">
        ${html}
      </nav>
    </div>
  `
}

function search(block) {
    return `
  <style>.search button:before {
      content: "🔍";
      font-size: 16px;
      color: #F9F0DA;
    }</style>
  <div class="col-lg-5">
    <div class="search" id="search">
      <form>
        <input style="width: 100%;
                      height: 42px;
                      padding-left: 10px;
                      border: 2px solid #7BA7AB;
                      border-radius: 5px;
                      outline: none;
                      background: #F9F0DA;
                      color: #9E9C9C;"
                      type="text" placeholder="Искать здесь...">
        <button style="position: absolute; 
                      top: 0;
                      right: 0;
                      width: 42px;
                      height: 42px;
                      border: none;
                      background: #7BA7AB;
                      border-radius: 0 5px 5px 0;
                      cursor: pointer;"
                      type="submit"></button>
      </form>
    </div>
  </div>
 `
}

function auth(block) {
    let html = ''
    block.value.forEach(item => {
        html += `
      <button class="btn btn-small btn-primary">${item}</button>
    `
    })

    return `
    <div class="col-lg-2">
      
      <div>
    <a href="/login" type="button" class="btn btn-small btn-primary" id="logout"'>Выйти</a>
    </a>
</div>
    
    </div>
  `
}