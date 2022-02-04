{
  const id = new URL(window.location).pathname.split('/').pop();
  const headers = {'Authorization': `Bearer ${token}`};

  document.addEventListener('DOMContentLoaded', async function() {
    const responseQuestion = await fetch(`/api/user/question/${id}`, {headers});

    if (responseQuestion.status === 400) {
      show404();
    }

    if (!responseQuestion.ok) {
      console.log('Загрузка вопроса завершилась неудачно');
      return;
    }

    const jsonQuestion = await responseQuestion.json();
    const countAnswer = parseInt(jsonQuestion['countAnswer']);

    document.querySelector('.vote-block').innerHTML = voteSection(
        jsonQuestion['countValuable']);
    document.getElementById('title').textContent = jsonQuestion['title'];
    document.getElementById(
        'description').textContent = jsonQuestion['description'];
    document.querySelector('.post-tags').innerHTML = generateTags(
        jsonQuestion['listTagDto']);
    document.querySelector('.post-header__create').textContent = new Date(
        jsonQuestion['persistDateTime']).toLocaleString();
    document.querySelector('.post-header__update').textContent = new Date(
        jsonQuestion['lastUpdateDateTime']).toLocaleString();
    document.querySelector(
        '.post-header__view-count').textContent = jsonQuestion['viewCount'];
    document.querySelector(
        '.vote-count').textContent = jsonQuestion['countValuable'];
    document.querySelector('.post-author').innerHTML = generateAuthor(
        jsonQuestion['authorName'],
        jsonQuestion['authorImage'],
        jsonQuestion['authorReputation'],
        jsonQuestion['persistDateTime'],
    );
    document.getElementById(
        'answers-count').textContent = `Ответов: ${countAnswer}`;
    document.querySelector('.post-comments').innerHTML = generateComments(
        jsonQuestion['comments']);

    if (countAnswer === 0) {
      return;
    }

    const responseAnswer = await fetch(`/api/user/question/${id}/answer`,
        {headers});

    if (!responseAnswer.ok) {
      console.log('Загрузка ответов завершилась неудачно');
      return;
    }

    const jsonAnswer = await responseAnswer.json();

    document.querySelector('.answers-list').innerHTML = jsonAnswer.map(
        answer => generateAnswer(answer)).join('');
  });
}

function generateTags(tags) {
  const aItems = tags.map(
      tag => `<a href="#" class="mr-2" title="${tag['description'] ||
      tag['name']}" rel="tag">#${tag['name']}</a>`).join('');
  return `<div class="tag-block">
    <div class="d-flex ps-relative post-tags">${aItems}</div>
  </div>`;
}

function generateComments(comments) {
  const liItems = comments.map(c => `
    <li class="comment">
      <span class="comment__body">${c['comment']}</span> – 
      <a class="comment__author" href="#" title="${c['fullName']}">${c['fullName']}</a>
      <span class="comment__date">${new Date(c['dateAdded']).toLocaleString()}</span>
    </li>`).join('');

  return `<hr><div class="comment-block">
    <ul class="comments">${liItems}</ul>
    <a href="#" class="small text-secondary">Добавить комментарий</a>
  </div><hr>`;
}

function generateAuthor(name, image, reputation, date) {
  return `<div class="author-block">
    <div class="card text-white bg-secondary border-dark mb-3 author-card">
      <div class="text font-weight-light mx-1">
        <p>задан ${new Date(date).toLocaleString()}</p>
      </div>
      <div class="d-flex">
        <div class="user-avatar ml-1 mb-1">
          <a href="#"><img class="author-card__avatar" src="${image}" alt="" width="32" height="32"></a>
        </div>
        <div class="user-name d-flex ml-1">
          <a class="author-card__name text-info" href="#">${name}</a>
          <span class="author-card__reputation ml-2">${reputation}</span>
        </div>
      </div>
    </div>
  </div>`;
}

function generateAnswer(answer) {
  return `<div class="answer">
  <div class="row">
    <div class="col-md-1 text-center">
      <div class="vote-block">${voteSection(answer['countValuable'])}</div>
    </div>
    <div class="col-md-11">
      <div class="answer__body">
        ${answer['body']}
      </div>
      <div class="more-info d-flex justify-content-between align-items-center">
        <div class="share-edit-follow">
          <!-- share edit follow logic -->
        </div>
        <div class="answer__author">
          ${generateAuthor(answer['nickName'], answer['image'],
      answer['userReputation'], answer['persistDate'])}
        </div>
      </div>
      <div class="answer__comments">
         ${generateComments(answer['comments'] || [])} 
      </div>
    </div>
  </div>`;
}

function voteSection(reputation) {
  return `<p>
            <a href="#"
               title="Ответ отражает стремление помочь; он понятен и несёт пользу.">
                <svg class="ui-lib-mitten-icon _theme_white mitten-like-button_theme_undefined"
                     width="32" height="32" viewBox="0 0 32 32"
                     xmlns="http://www.w3.org/2000/svg">
                    <path transform="translate(6, 3)"
                          d="M10 0.5C14.509 0.5 14.6493 3.8605 14.0025 7C13.8604 7.69002 13.6802 8.36936 13.5
\t\t\t\t\t\t\t\t\t\t\t      9H21C21 9.6985 20.9758 10.3647 20.9305 11C20.2622 20.3689 15 23 15 23H5L4 9.99999C7.5
\t\t\t\t\t\t\t\t\t\t\t      8 9.5 3 10 0.5ZM10.8485 11H18.925C18.6206 14.939 17.4419 17.4475 16.3661
\t\t\t\t\t\t\t\t\t\t\t      18.9716C15.7342 19.8667 15.1166 20.4525 14.6828 20.8019C14.5868 20.8792 14.4998 20.945
\t\t\t\t\t\t\t\t\t\t\t      14.4236 21H6.85206L6.08364 11.0105C7.7312 9.75251 8.97805 8.03148 9.87616 6.40365C10.5657
\t\t\t\t\t\t\t\t\t\t\t      5.15381 11.097 3.87294 11.4688 2.73296C11.6065 2.79228 11.703 2.85406 11.7697
\t\t\t\t\t\t\t\t\t\t\t      2.90665C11.956 3.05352 12.1278 3.29856 12.2209 3.80168C12.4364 4.96525 12.0893 6.65732
\t\t\t\t\t\t\t\t\t\t\t      11.577 8.45056L10.8485 11Z"
                          fill-rule="evenodd" clip-rule="evenodd"></path>
                    <path transform="translate(6, 3)" d="M3 23L2 10H0V23H3Z"
                          fill-rule="evenodd"
                          clip-rule="evenodd"></path>
                </svg>
            </a>
        </p>
        <p><span class="vote-count">${reputation}</span></p>
        <p>
            <a href="#"
               title="Ответ не отражает стремление помочь; он непонятен и не несёт пользу.">
                <svg class="ui-lib-mitten-icon _theme_white mitten-dislike-button_theme_undefined"
                     width="32" height="32" viewBox="0 0 32 32"
                     xmlns="http://www.w3.org/2000/svg">
                    <path transform="translate(26, 29) rotate(180)"
                          d="M10 0.5C14.509 0.5 14.6493 3.8605 14.0025 7C13.8604 7.69002 13.6802 8.36936 13.5 9H21C21
\t\t\t\t\t\t\t\t\t\t      9.6985 20.9758 10.3647 20.9305 11C20.2622 20.3689 15 23 15 23H5L4 9.99999C7.5 8 9.5 3 10
\t\t\t\t\t\t\t\t\t\t      0.5ZM10.8485 11H18.925C18.6206 14.939 17.4419 17.4475 16.3661 18.9716C15.7342 19.8667 15.1166
\t\t\t\t\t\t\t\t\t\t      20.4525 14.6828 20.8019C14.5868 20.8792 14.4998 20.945 14.4236 21H6.85206L6.08364
\t\t\t\t\t\t\t\t\t\t      11.0105C7.7312 9.75251 8.97805 8.03148 9.87616 6.40365C10.5657 5.15381 11.097 3.87294 11.4688
\t\t\t\t\t\t\t\t\t\t      2.73296C11.6065 2.79228 11.703 2.85406 11.7697 2.90665C11.956 3.05352 12.1278 3.29856 12.2209
\t\t\t\t\t\t\t\t\t\t      3.80168C12.4364 4.96525 12.0893 6.65732 11.577 8.45056L10.8485 11Z"
                          fill-rule="evenodd" clip-rule="evenodd"></path>
                    <path transform="translate(26, 29) rotate(180)" d="M3 23L2 10H0V23H3Z"
                          fill-rule="evenodd"
                          clip-rule="evenodd"></path>
                </svg>
            </a>
        </p>`;
}

function show404() {
  document.querySelector('html').className = 'h-100';
  document.querySelector('body').className = 'h-100';
  document.querySelector('footer').outerHTML = '';

  const info = document.createElement('div');
  info.style.height = '100%';
  info.innerHTML = `<div class="container" style="height: 90%;">
    <div class="row h-100">
        <div class="col-sm-12 align-self-center">
            <div class="mx-auto text-center">
              <h4>404: Вопрос не найден</h4>
              <p>Посмотрите <a href="/questions">недавние вопросы</a></p>
          </div>
        </div>
    </div>
  </div>`;

  const div = document.querySelector('body > div');
  div.replaceWith(info);
}
