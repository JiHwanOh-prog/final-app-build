{
  // 폼 제출 전 유효성 검사
  const $form = document.querySelector('.write-form');
  $form.addEventListener('submit', function (e) {
    const $title = document.querySelector('input[name="title"]');
    const $content = document.querySelector('textarea[name="content"]');

    if ($title.value.trim().length === 0) {
      e.preventDefault();
      alert('제목을 입력해주세요.');
      $title.focus();
      return;
    }

    if ($content.value.trim().length === 0) {
      e.preventDefault();
      alert('내용을 입력해주세요.');
      $content.focus();
      return;
    }
  });
}

{
  // DOM 요소 선택
  const $imageInput = document.getElementById('imageInput'); // 파일 입력 필드
  const $imagePreview = document.getElementById('imagePreview'); // 이미지 미리보기 컨테이너
  const $previewImg = $imagePreview.querySelector('img'); // 미리보기 이미지
  const $removeBtn = $imagePreview.querySelector('.remove-image'); // 이미지 제거 버튼
  const $uploadBox = document.querySelector('.upload-box'); // 이미지 업로드 영역

  // 이미지 선택 시 미리보기 처리
  $imageInput.addEventListener('change', function () {
    const file = this.files[0]; // 선택된 파일 가져오기

    // 파일이 존재하고 이미지 파일인 경우에만 처리
    if (file && file.type.startsWith('image/')) {
      const reader = new FileReader(); // 파일 리더 객체 생성

      // 파일 읽기가 완료되면 실행될 콜백
      reader.onload = function (e) {
        $previewImg.src = e.target.result; // 미리보기 이미지 소스 설정
        $imagePreview.style.display = 'block'; // 미리보기 영역 표시
        $uploadBox.style.display = 'none'; // 업로드 박스 숨김
      };

      // 파일을 Data URL로 읽기 시작
      reader.readAsDataURL(file);
    }
  });

  // 이미지 제거 버튼 클릭 처리
  $removeBtn.addEventListener('click', function () {
    $imageInput.value = ''; // 파일 입력 초기화
    $imagePreview.style.display = 'none'; // 미리보기 숨김
    $uploadBox.style.display = 'block'; // 업로드 박스 다시 표시
  });

  // 드래그 앤 드롭 기능 구현
  // 드래그 오버 시 시각적 피드백
  $uploadBox.addEventListener('dragover', function (e) {
    e.preventDefault(); // 기본 드래그 동작 방지
    this.style.borderColor = '#007bff'; // 테두리 색상 변경
    this.style.backgroundColor = '#f8f9fa'; // 배경색 변경
  });

  // 드래그 벗어날 때 스타일 초기화
  $uploadBox.addEventListener('dragleave', function (e) {
    e.preventDefault();
    this.style.borderColor = '#ddd'; // 테두리 색상 원복
    this.style.backgroundColor = '#fff'; // 배경색 원복
  });

  // 파일 드롭 시 처리
  $uploadBox.addEventListener('drop', function (e) {
    e.preventDefault();
    this.style.borderColor = '#ddd'; // 스타일 초기화
    this.style.backgroundColor = '#fff';

    const file = e.dataTransfer.files[0]; // 드롭된 파일 가져오기

    // 이미지 파일인 경우에만 처리
    if (file && file.type.startsWith('image/')) {
      $imageInput.files = e.dataTransfer.files; // 파일 입력에 드롭된 파일 설정

      // change 이벤트를 수동으로 발생시켜 미리보기 처리
      const event = new Event('change');
      $imageInput.dispatchEvent(event);
    }
  });
}

{
  // 서머노트 적용
  $('.textarea-field').summernote({
    placeholder: '글 내용을 입력해주세요.',
    tabsize: 2,
    height: 500,
    toolbar: [
      ['style', ['style']],
      ['font', ['bold', 'underline', 'clear']],
      ['color', ['color']],
      ['para', ['ul', 'ol', 'paragraph']],
      ['table', ['table']],
      ['insert', ['link', 'picture']],
      ['view', ['fullscreen', 'codeview', 'help']]
    ]
  });
}
