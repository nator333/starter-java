// Track the currently selected demo
let currentDemo = 'initiateAuth';

// Change the currently selected demo
function changeTab(newTab) {
    if (newTab === 'signUp') {
        currentDemo = 'signUp';
        $('#signUp').addClass('current');
        $('#initiateAuth').removeClass('current');
        $('#refreshToken').removeClass('current');
        $('#createPost').removeClass('current');
        $('.refreshToken').hide();
        $('.username').show();
        $('form button').html('Execute sign up');
    } else if (newTab === 'initiateAuth') {
        currentDemo = 'initiateAuth';
        $('#initiateAuth').addClass('current');
        $('#signUp').removeClass('current');
        $('#refreshToken').removeClass('current');
        $('#createPost').removeClass('current');
        $('.refreshToken').hide();
        $('.username').show();
        $('form button').html('Execute auth login');
    } else if (newTab === 'refreshToken') {
        currentDemo = 'refreshToken';
        $('#refreshToken').addClass('current');
        $('#signUp').removeClass('current');
        $('#initiateAuth').removeClass('current');
        $('#createPost').removeClass('current');
        $('.username').hide();
        $('.refreshToken').show();
        $('form button').html('Execute refresh token');
    } else if (newTab === 'createPost') {
        currentDemo = 'createPost';
        $('#createPost').addClass('current');
        $('#refreshToken').removeClass('current');
        $('#signUp').removeClass('current');
        $('#initiateAuth').removeClass('current');
        $('.username').show();
        $('.refreshToken').hide();
        $('form button').html('Create post');
    }
}

// Set up handlers for tabs
$('#signUp').on('click', function (e) {
    e.preventDefault();
    changeTab('signUp');
});
$('#initiateAuth').on('click', function (e) {
    e.preventDefault();
    changeTab('initiateAuth');
});
$('#refreshToken').on('click', function (e) {
    e.preventDefault();
    changeTab('refreshToken');
});
$('#createPost').on('click', function (e) {
    e.preventDefault();
    changeTab('createPost');
});

// Set up handler for "flash" message
$('#flash a').on('click', function (e) {
    e.preventDefault();
    $('#flash').hide();
});