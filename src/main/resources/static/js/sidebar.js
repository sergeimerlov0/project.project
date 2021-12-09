const $sidebar = document.querySelector('#sidebar')
sidebar()
var s = document.getElementById("sidebar");
s.className = "container-fluid fixed-top";
function sidebar() {
  let html = `
      <div class="row">
        <div class="col-lg-2 bg-light border border-info rounded mt-5">
          <div class="container">
            <div class="row">
              <div class="col-lg-8">
                <nav class="nav flex-column">
                  <a href="#" class="nav-link active font-weight-bold pl-0 text-dark">Home</a>
                  <a class="nav-link text-uppercase font-weight-100 mt-1 pl-0 text-dark">Public</a>
                  <a href="#" class="nav-link font-weight-100 text-dark">Questions</a>
                  <a href="#" class="nav-link font-weight-100 text-dark">Tags</a>
                  <a href="#" class="nav-link font-weight-100 text-dark">Users</a>
                  <a class="nav-link text-uppercase font-weight-100 mt-1 pl-0 text-dark">Public</a>
                  <a href="#" class="nav-link font-weight-100 text-dark">Users</a>
                  <a class="nav-link text-uppercase font-weight-100 mt-1 pl-0 text-dark">Public</a>
                  <a href="#" class="nav-link font-weight-100 text-dark">Users</a>
                  <a href="#" class="nav-link font-weight-100 text-dark">Users</a>
                  <a class="nav-link text-uppercase font-weight-100 mt-1 pl-0 text-dark">Public</a>
                </nav>
              </div>
            </div>
            <div class="row">
              <div class="col-lg-8 offset-lg-2 text-center pb-5">
                <p class="font-weight-bold">Lorem ipsum</p>
                <p class="font-weight-normal">Lorem ipsum dolor sit amet, consectetur adipisicing elit.</p>
                <img src="" alt="" class="rounded mx-auto d-block" width="100" height="100">
                <button class="btn btn-primary mt-3">Create a free team</button>
                <button class="btn btn-link">What is teams?</button>
              </div>
            </div>
          </div>
        </div>
      </div>
  `
  $sidebar.insertAdjacentHTML('beforeend', html)
}