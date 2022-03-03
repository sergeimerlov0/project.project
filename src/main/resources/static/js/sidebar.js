const $sidebar = document.querySelector('#sidebar')
sidebar()
const s = document.getElementById("sidebar");
s.className = "container-fluid";
function sidebar() {
  let html = `
      <div class="row">
        <div class="col-lg-2 bg-light mt-5 fixed-top" style="overflow-y: auto; height: 100%">
          <div class="container">
            <div class="row">
              <div class="col-lg-8">
                <nav class="nav flex-column">
                  <a href="#" class="nav-link active font-weight-bold pl-0 text-dark">Home</a>
                  <a class="nav-link text-uppercase font-weight-100 mt-1 pl-0 text-dark">Public</a>
                  <a href="/questions" class="nav-link font-weight-100 text-dark">Questions</a>
                  <a href="/tags" class="nav-link font-weight-100 text-dark">Tags</a>
                  <a href="/users" class="nav-link font-weight-100 text-dark">Users</a>
                  <a href="#" class="nav-link font-weight-100 text-dark">Unanswered</a>
                             
                </nav>                
              </div>
            </div>
            <hr/>
            
            </div>
          </div>
        </div>
      </div>
  `
  $sidebar.insertAdjacentHTML('beforeend', html)
}