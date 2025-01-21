{
  // DOM 객체
  const $form = document.querySelector('.login-form');
  const $loginId = document.getElementById('loginId');
  const $password = document.getElementById('password');
  const $remember = document.getElementById('remember');

  // 저장된 아이디가 있으면 불러오기 (쿠키 대신 JS의 localStorage 사용)
  const savedLoginId = localStorage.getItem('savedLoginId');
  if (savedLoginId) {
    $loginId.value = savedLoginId;
    $remember.checked = true;
  }

  // 폼 제출
  $form.addEventListener('submit', function (e) {
    // 아이디 저장 처리
    if ($remember.checked) {
      localStorage.setItem('savedLoginId', $loginId.value);
    } else {
      localStorage.removeItem('savedLoginId');
    }

    // 입력값 검증
    if (!$loginId.value.trim()) {
      e.preventDefault();
      alert('아이디를 입력해주세요.');
      $loginId.focus();
      return;
    }

    if (!$password.value) {
      e.preventDefault();
      alert('비밀번호를 입력해주세요.');
      $password.focus();
      return;
    }
  });
}
