function signUp() {
    const username = $('#signup-username').val();
    const password = $('#signup-password').val();
    const email = $('#signup-email').val();
    const name = $('#signup-name').val();

    if (!username || !password || !email || !name) {
        alert('모든 필드를 입력해주세요.');
        return;
    }

    $.ajax({
        url: '/user-manager/signup',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            accountName: username,
            password,
            email,
            name
        }),
        success: (response) => {
            alert(response.message || '회원가입 성공!');
            window.location.href = '/user-manager/signin';
        },
        error: function (xhr) {
            let errorMessage = '회원가입 실패: ';
            try {
                const response = JSON.parse(xhr.responseText);
                if (response.error) {
                    errorMessage += response.error;
                } else {
                    errorMessage += '알 수 없는 오류가 발생했습니다.';
                }
            } catch (e) {
                errorMessage += '알 수 없는 오류가 발생했습니다.';
            }
            alert(errorMessage);
        }
    });
}

$(function () {
    $('#signup-button').click(() => signUp());
});
