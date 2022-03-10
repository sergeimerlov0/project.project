{
  const form = document.querySelector('.form-signin');
  const info = document.querySelector('.info');
  const hasSameHostReferrer = () => (document.referrer &&
      new URL(document.referrer).hostname ===
      location.hostname);

  function setCookie(name, value, options = {}) {
    debugger
    options = {
      path: '/',
      ...options
    };
    if (options.expires instanceof Date) {
      options.expires = options.expires.toUTCString();
    }
    let updatedCookie = encodeURIComponent(name) + "=" + encodeURIComponent(value);

    for (let optionKey in options) {
      updatedCookie += "; " + optionKey;
      let optionValue = options[optionKey];
      if (optionValue !== true) {
        updatedCookie += "=" + optionValue;
      }
    }
    document.cookie = updatedCookie;
  }

  form.addEventListener('submit', async function(event) {
    event.preventDefault();

    const response = await fetch('/api/auth/token', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        email: this.elements.email.value,
        password: this.elements.password.value,
      }),
    });

    if (!response.ok) {
      info.textContent = 'Неверный логин или пароль';
      return;
    }

    const token = await response.text();
    setCookie("token",token)

    location.replace(hasSameHostReferrer() ? document.referrer : '/main');
  });
}
