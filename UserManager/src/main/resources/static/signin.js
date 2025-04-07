function signIn() {
  const username = $('#signin-username').val();
  const email = $('#signin-email').val();
  const password = $('#signin-password').val();

  // 기존 토큰 삭제
  localStorage.removeItem('accessToken');
  localStorage.removeItem('refreshToken');

  $.ajax({
    url: '/user-manager/signin',
    type: 'POST',
    contentType: 'application/json',
    data: JSON.stringify({
      username: username,
      email: email,
      password: password
    }),
    success: function (response) {
      if (response.data.accessToken && response.data.refreshToken) {
        // 토큰 저장
        localStorage.setItem('accessToken', response.data.accessToken);
        localStorage.setItem('refreshToken', response.data.refreshToken);
        alert('로그인 성공!');
        window.location.href = '/user-manager/public';
      } else {
        alert('로그인 실패: 토큰을 받지 못했습니다.');
      }
    },
    error: function (xhr) {
      let errorMessage = '로그인 실패: ';
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
  // 버튼 클릭 이벤트도 유지
  $('#signin-button').click(function (e) {
    e.preventDefault(); // 기본 버튼 동작 방지
    signIn();
  });
});
