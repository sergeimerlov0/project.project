{
  const form = document.querySelector('.form-signin');
  const info = document.querySelector('.info');
  const hasSameHostReferrer = () => (document.referrer &&
      new URL(document.referrer).hostname ===
      location.hostname);

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
    document.cookie = `token=${token}; expires=;`;

    location.replace(hasSameHostReferrer() ? document.referrer : '/main');
  });
}
