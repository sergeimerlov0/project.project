fetch('./api/check/status')
    .then(
        function (response) {
            if (response.status === 403) {
                console.log('You must be authorized!');
                location.replace('/login');
            }
        })
    .catch(function (err) {
        console.log('Fetch Error :-S', err);
    });