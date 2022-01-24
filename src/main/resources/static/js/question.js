{
  const id = new URL(window.location).pathname.split('/').pop();
  console.log(id);

  document.addEventListener('DOMContentLoaded', async function(event) {
    console.log(`question api url: /api/user/question/${id}`);
    const response = await fetch(`/api/user/question/${id}`);

    if (!response.ok) {
      console.log(404);
      return;
    }

    const json = await response.json();
    console.log(json);
  });
}