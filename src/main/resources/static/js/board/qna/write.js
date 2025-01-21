{
  // DOM 요소 선택
  const $imageInput = document.getElementById('imageInput');
  const $imageList = document.getElementById('imageList');
  const $form = document.querySelector('.write-form');

  // 파일 업로드 제한
  const MAX_FILES = 5; // 최대 업로드 가능한 이미지 수

  // 선택된 파일들을 관리하기 위한 DataTransfer 객체 생성
  let selectedFiles = new DataTransfer();

  /**
   * 이미지 입력 변경 이벤트 핸들러
   * input[type="file"]에서 파일이 선택되면 실행
   */
  $imageInput.addEventListener('change', function () {
    const files = Array.from(this.files);

    // 파일 수 제한 체크
    if (selectedFiles.files.length + files.length > MAX_FILES) {
      alert(`이미지는 최대 ${MAX_FILES}장까지 업로드 가능합니다.`);
      this.value = ''; // 선택한 파일 초기화
      return;
    }

    // 선택된 각 파일에 대해 이미지 타입 체크 후 미리보기 추가
    files.forEach((file) => {
      if (file.type.startsWith('image/')) {
        addImagePreview(file);
      }
    });
  });

  /**
   * 이미지 미리보기 추가 함수
   * @param {File} file - 업로드된 이미지 파일
   */
  function addImagePreview(file) {
    const reader = new FileReader();

    // 파일 읽기가 완료되면 실행될 콜백
    reader.onload = function (e) {
      // 이미지 아이템 요소 생성
      const imageItem = document.createElement('div');
      imageItem.className = 'image-item';
      imageItem.innerHTML = `
        <img src="${e.target.result}" alt="미리보기 이미지">
        <button type="button" class="remove-image">✕</button>
      `;

      // 삭제 버튼에 이벤트 리스너 추가
      const $removeBtn = imageItem.querySelector('.remove-image');

      $removeBtn.addEventListener('click', () => {
        // FileList에서 해당 파일 제거
        const files = Array.from(selectedFiles.files).filter((f) => f !== file);

        // 새로운 FileList 생성
        selectedFiles = new DataTransfer();
        files.forEach((f) => selectedFiles.items.add(f));

        // input[type="file"]의 files 속성 업데이트
        $imageInput.files = selectedFiles.files;

        // 미리보기 이미지 제거
        imageItem.remove();
      });

      // 이미지 목록에 새 이미지 추가
      $imageList.appendChild(imageItem);

      // FileList에 파일 추가
      selectedFiles.items.add(file);
      $imageInput.files = selectedFiles.files;
    };

    // 파일을 Data URL로 읽기
    reader.readAsDataURL(file);
  }

  /**
   * 폼 제출 전 유효성 검사
   */
  $form.addEventListener('submit', function (e) {
    const $title = document.querySelector('input[name="title"]');
    const $content = document.querySelector('textarea[name="content"]');

    // 제목 필수 입력 체크
    if ($title.value.trim().length === 0) {
      e.preventDefault();
      alert('제목을 입력해주세요.');
      $title.focus();
      return;
    }

    // 내용 필수 입력 체크
    if ($content.value.trim().length === 0) {
      e.preventDefault();
      alert('내용을 입력해주세요.');
      $content.focus();
      return;
    }
  });
}
