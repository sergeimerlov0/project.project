window.token = getCookie('token');

(async () => {
  if (!token) {
    location.replace('/login');
    return;
  }

  const response = await fetch('/api/check/user-status', {
    headers: {
      'Authorization': `Bearer ${token}`,
    },
  });

  if (!response.ok) {
    location.replace('/login');
    return;
  }
})();

function getCookie(name) {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop().split(';').shift();
}