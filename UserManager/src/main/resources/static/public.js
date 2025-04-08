// 페이지 로드 시 토큰 확인
document.addEventListener('DOMContentLoaded', function () {
    const accessToken = localStorage.getItem('accessToken');
    if (!accessToken) {
        alert('로그인이 필요합니다.');
        return;
    }
});

function refreshToken() {
    const refreshToken = localStorage.getItem('refreshToken');
    if (!refreshToken) {
        alert('refreshToken이 없습니다.');
        return;
    }

    $.ajax({
        url: '/user-manager/refresh-token',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            refreshToken: refreshToken
        }),
        success: function (response) {
            if (response.data.accessToken && response.data.refreshToken) {
                localStorage.setItem('accessToken', response.data.accessToken);
                localStorage.setItem('refreshToken', response.data.refreshToken);
                alert('토큰이 성공적으로 갱신되었습니다.');
            } else {
                alert('토큰 갱신 실패: 응답에 토큰이 없습니다.');
            }
        },
        error: function (xhr) {
            let errorMessage = '토큰 갱신 실패: ';
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
            window.location.href = '/user-manager/signin';
        }
    });
}

function fetchPublicData() {
    const accessToken = localStorage.getItem('accessToken');
    if (!accessToken) {
        alert('accessToken이 없습니다.');
        return;
    }

    $.ajax({
        url: '/test',
        type: 'POST',
        headers: {
            'Authorization': 'Bearer ' + accessToken,
            'Content-Type': 'application/json'
        },
        success: function (response) {
            $('#message-container').text(response);
        },
        error: function (xhr) {
            if (xhr.status === 403) {
                alert('인증이 만료되었습니다. 다시 로그인해주세요.');
            } else {
                alert('오류가 발생했습니다: ' + xhr.responseText);
            }
        }
    });
}

$(function () {
    // 버튼 클릭 시 데이터 새로고침
    $('#fetch-button').click(() => fetchPublicData());

    // 토큰 갱신 버튼 클릭 이벤트
    $('#refresh-token-button').click(() => refreshToken());
}); 