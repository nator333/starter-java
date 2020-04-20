// Track the currently selected demo
let currentDemo = 'initiateAuth';

// Change the currently selected demo
function changeTab(newTab) {
    if (newTab === 'initiateAuth') {
        currentDemo = 'initiateAuth';
        $('#initiateAuth').addClass('current');
        $('#refreshToken').removeClass('current');
        $('.refreshToken').hide();
        $('.username').show();
        $('form button').html('Execute auth login');
    } else {
        currentDemo = 'refreshToken';
        $('#refreshToken').addClass('current');
        $('#initiateAuth').removeClass('current');
        $('.username').hide();
        $('.refreshToken').show();
        $('form button').html('Execute refresh token');
    }
}

// Set up handlers for tabs
$('#initiateAuth').on('click', function(e) {
    e.preventDefault();
    changeTab('initiateAuth');
});
$('#refreshToken').on('click', function(e) {
    e.preventDefault();
    changeTab('refreshToken');
});

// Set up handler for "flash" message
$('#flash a').on('click', function(e) {
    e.preventDefault();
    $('#flash').hide();
});