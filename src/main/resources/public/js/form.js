let refreshToken = ""

// Show an information box at the top of the page
function showFlash(message) {
    $('#flash span').html(message);
    $('#flash').show();
}

// Intercept our form button click
$('form button').on('click', function (e) {
    e.preventDefault();

    $('#authResult').hide();
    $('#accessToken').val("");
    $('#refreshedToken').val("");
    refreshToken = "";

    // Based on the selected demo, fire off an ajax request
    // We expect just a string of text back from the server (keeping it simple)
    const url = "/" + currentDemo;
    $.ajax(url, {
        method: 'POST',
        dataType: 'text',
        data: {
            userPoolId: $('#userPoolId').val(),
            clientId: $('#clientId').val(),
            username: $('#username').val(),
            phone: $('#phone').val(),
            postRefreshToken: $('#postRefreshToken').val(),
            originalPostId: $('#originalPostId').val(),
            accessToken: $('#accessTokenSent').val(),
            amount: $('#amount').val(),
            ticketId: $('#ticketId').val()
        },
        success: function (data) {
            console.log(data);
            const authResult = JSON.parse(data);
            if (authResult.isSignUp !== undefined) {
                showFlash(authResult.userSub + " Is Signed Up");
            } else {
                $('#authResult').show();
                $('#accessToken').val(authResult.accessToken);
                $('#accessTokenSent').val(authResult.accessToken);
                $('#refreshedToken').val(authResult.refreshToken);
                $('#postRefreshToken').val(authResult.refreshToken);
                refreshToken = authResult.refreshToken;
                showFlash("Success");
            }
        },
        error: function (jqxhr) {
            alert('There was an error sending a request to the server :(');
        }
    })
});