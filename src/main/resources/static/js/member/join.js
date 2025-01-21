import * as smsApi from './modules/smsApi.js';
import * as memberAPI from './modules/memberApi.js';

{
  // DOM 객체
  const $form = document.querySelector('.join-form');
  const $loginId = document.getElementById('loginId');
  const $password = document.getElementById('password');
  const $passwordCheck = document.getElementById('passwordCheck');
  const $phone = document.getElementById('phone');
  const $sendAuthBtn = document.getElementById('sendAuthBtn');
  const $authNumberGroup = document.getElementById('authNumberGroup');
  const $authNumber = document.getElementById('authNumber');
  const $verifyAuthBtn = document.getElementById('verifyAuthBtn');
  const $authTimer = document.getElementById('authTimer');

  // 유효성 검사 패턴
  const idPattern = /^[a-zA-Z0-9]{6,12}$/; // 영문, 숫자 조합 6~12자
  const pwPattern = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,16}$/; // 영문, 숫자, 특수문자 조합 8~16자
  const phonePattern = /^01[016789]\d{7,8}$/; // 휴대폰 번호 형식 (01X-XXXX-XXXX)

  // 인증 관련 변수
  let isPhoneVerified = false; // 휴대폰 인증 완료 여부
  let authTimeoutId = null; // 인증 타이머의 ID (타이머 정리용)
  let isAuthenticating = false; // 인증 진행 중 여부 (인증번호 전송 후 ~ 완료/만료 전)

  // 입력 필드 유효성 검사
  {
    // 아이디 유효성 검사
    $loginId.addEventListener('input', function () {
      const isValid = idPattern.test(this.value);
      toggleValidationUI(this, isValid);
    });

    // 아이디 중복검사 (change 이벤트)
    $loginId.addEventListener('change', function () {
      // 유효성에 적합하지 않으면 중복검사 진행 안함!
      if(!idPattern.test(this.value)){return;}

      // 중복검사 결과를 보여줄 요소
      const $inputNotice = this.closest('.input-group').querySelector('.input-notice');

      memberAPI.checkLoginId(this.value, function (data){
        if (data.exists) {
          toggleValidationUI($loginId, false);
          $inputNotice.style.color = 'red';
        }else {
          toggleValidationUI($loginId, true);
          $inputNotice.style.color = 'green';
        }

        $inputNotice.textContent = data.message;
      });
    });

    // 비밀번호 유효성 검사
    $password.addEventListener('input', function () {
      const isValid = pwPattern.test(this.value);
      toggleValidationUI(this, isValid);
    });

    // 비밀번호 확인 검사
    $passwordCheck.addEventListener('input', function () {
      const isValid = this.value === $password.value;
      toggleValidationUI(this, isValid);
    });

    // 핸드폰 번호 유효성 검사
    $phone.addEventListener('input', function () {
      const isValid = phonePattern.test(this.value);
      toggleValidationUI(this, isValid);
      $sendAuthBtn.disabled = !isValid;
    });

    // 인증번호 입력 제한 (숫자만)
    $authNumber.addEventListener('input', function () {
      this.value = this.value.replace(/[^0-9]/g, '').slice(0, 6);
    });
  }

  // 휴대폰 인증 관련
  {
    // 인증번호 전송
    $sendAuthBtn.addEventListener('click', function () {
      if (!phonePattern.test($phone.value)) {
        alert('올바른 휴대폰 번호를 입력해주세요.');
        $phone.focus();
        return;
      }

      // 인증 진행 중 상태로 변경
      isAuthenticating = true;
      resetAuthState();

      smsApi.sendVerificationCode($phone.value, function(data) {

        console.log(data);

        if (data.success) {
          $authNumberGroup.style.display = 'block';
          $authTimer.style.display = 'block';
          startAuthTimer();
          alert('인증번호가 전송되었습니다.');
        } else {
          isAuthenticating = false;
          alert('인증번호 전송에 실패했습니다. 다시 시도해주세요.');
        }
      });
    });

    // 인증번호 확인
    $verifyAuthBtn.addEventListener('click', function () {
      if (!isAuthenticating) {
        alert('인증이 만료되었습니다. 다시 시도해주세요.');
        resetAuthState();
        return;
      }

      const authNumber = $authNumber.value;
      if (authNumber.length !== 6) {
        alert('인증번호 6자리를 입력해주세요.');
        return;
      }

      smsApi.verifyCode(authNumber, function(data) {
        if (data.success) {
          completeAuth();
          alert('인증이 완료되었습니다.');
        } else {
          alert('인증번호가 일치하지 않습니다.');
          $authNumber.value = '';
          $authNumber.focus();
        }
      });
    });

    // 인증 타이머 (3분)
    function startAuthTimer() {
      let timeLeft = 180;
      clearTimeout(authTimeoutId);

      function updateTimer() {
        const minutes = Math.floor(timeLeft / 60);
        const seconds = timeLeft % 60;
        $authTimer.textContent = `${minutes}:${seconds.toString().padStart(2, '0')}`;

        if (timeLeft === 0) {
          expireAuth();
          return;
        }

        timeLeft--;
        authTimeoutId = setTimeout(updateTimer, 1000);
      }

      updateTimer();
    }

    // 인증 완료 처리
    function completeAuth() {
      isPhoneVerified = true;
      isAuthenticating = false;
      clearTimeout(authTimeoutId);

      $authTimer.style.display = 'none';
      $authNumberGroup.style.display = 'none';
      $phone.readOnly = true;
      $sendAuthBtn.disabled = true;
    }

    // 인증 만료 처리
    function expireAuth() {
      isAuthenticating = false;
      clearTimeout(authTimeoutId);

      $authTimer.textContent = '인증시간이 만료되었습니다.';
      $authNumber.value = '';
      $sendAuthBtn.disabled = false;
    }

    // 인증 상태 초기화
    function resetAuthState() {
      clearTimeout(authTimeoutId);
      $authNumber.value = '';
      $authTimer.textContent = '';
    }
  }

  // UI 관련 유틸리티 함수
  function toggleValidationUI(element, isValid) {
    if (!isValid) {
      element.classList.add('invalid');
    } else {
      element.classList.remove('invalid');
    }
  }

  // 폼 제출 처리
  {
    $form.addEventListener('submit', function (e) {
      // 아이디 검사
      if (!idPattern.test($loginId.value)) {
        e.preventDefault();
        alert('아이디는 영문, 숫자 조합 6~12자로 입력해주세요.');
        $loginId.focus();
        return;
      }

      // 비밀번호 검사
      if (!pwPattern.test($password.value)) {
        e.preventDefault();
        alert('비밀번호는 영문, 숫자, 특수문자 조합 8~16자로 입력해주세요.');
        $password.focus();
        return;
      }

      // 비밀번호 확인 검사
      if ($password.value !== $passwordCheck.value) {
        e.preventDefault();
        alert('비밀번호가 일치하지 않습니다.');
        $passwordCheck.focus();
        return;
      }

      // 휴대폰 인증 검사
      if (!isPhoneVerified) {
        e.preventDefault();
        alert('휴대폰 인증이 필요합니다.');
        return;
      }
    });
  }
}

// 주소 찾기 API 처리

{
  // DOM 객체
  const $findZipcodeBtn = document.getElementById('find-zipcode-btn');
  const $zipcode = document.getElementById('zipcode');
  const $address = document.getElementById('address');
  const $addressDetail = document.getElementById('addressDetail');


  // 우편번호 검색 버튼 클릭 이벤트
  $findZipcodeBtn.addEventListener('click', function () {
    // 코드가 길 뿐 사실 옵션을 주는 것으로 생각하면 됩니다.
    new daum.Postcode({
      oncomplete: function(data) {
        // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

        // 각 주소의 노출 규칙에 따라 주소를 조합한다.
        // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
        let addr = ''; // 주소 변수
        let extraAddr = ''; // 참고항목 변수

        //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
        if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
          addr = data.roadAddress;
        } else { // 사용자가 지번 주소를 선택했을 경우(J)
          addr = data.jibunAddress;
        }

        // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
        if(data.userSelectedType === 'R'){
          // 법정동명이 있을 경우 추가한다. (법정리는 제외)
          // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
          if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
            extraAddr += data.bname;
          }
          // 건물명이 있고, 공동주택일 경우 추가한다.
          if(data.buildingName !== '' && data.apartment === 'Y'){
            extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
          }
          // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
          if(extraAddr !== ''){
            extraAddr = ' (' + extraAddr + ')';
          }
        }

        // 우편번호와 주소 정보를 해당 필드에 넣는다.
        $zipcode.value = data.zonecode;
        $address.value = addr + (extraAddr ? ` ${extraAddr}` : '');
        // 커서를 상세주소 필드로 이동한다.
        $addressDetail.focus();
      }
    }).open();
  });

  // 우편번호 입력 필드 클릭시 검색 실행
  $zipcode.addEventListener('click', function () {
    $findZipcodeBtn.click();
  });

  // 주소 입력 필드 클릭시 검색 실행
  $address.addEventListener('click', function () {
    $findZipcodeBtn.click();
  })
}