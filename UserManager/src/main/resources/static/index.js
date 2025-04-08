$(function () {
    $('#signin-button').click(function (e) {
        e.preventDefault();
        window.open('/user-manager/signin', '_blank');
    });

    $('#signup-button').click(function (e) {
        e.preventDefault();
        window.open('/user-manager/signup', '_blank');
    });

    $('#public-button').click(function (e) {
        e.preventDefault();
        window.open('/user-manager/public', '_blank');
    });

    $('#swagger-button').click(function (e) {
        e.preventDefault();
        window.open('/swagger-ui/index.html', '_blank');
    });
});
