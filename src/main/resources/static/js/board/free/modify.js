{
  // DOM 요소
  const $imageInput = document.getElementById('imageInput');
  const $imagePreview = document.getElementById('imagePreview');
  const $previewImg = $imagePreview.querySelector('img');
  const $removeBtn = $imagePreview.querySelector('.remove-image');
  const $uploadBox = document.querySelector('.upload-box');
  const $form = document.querySelector('.write-form');

  // 이미지 선택 시 미리보기
  $imageInput.addEventListener('change', function () {
    const file = this.files[0];

    if (file && file.type.startsWith('image/')) {
      const reader = new FileReader();

      reader.onload = function (e) {
        $previewImg.src = e.target.result;
        $imagePreview.style.display = 'block';
        $uploadBox.style.display = 'none';
      };

      reader.readAsDataURL(file);
    }
  });

  // 이미지 제거
  $removeBtn.addEventListener('click', function () {
    $imageInput.value = '';
    $imagePreview.style.display = 'none';
    $uploadBox.style.display = 'block';
  });

  // 드래그 앤 드롭
  $uploadBox.addEventListener('dragover', function (e) {
    e.preventDefault();
    this.style.borderColor = '#007bff';
    this.style.backgroundColor = '#f8f9fa';
  });

  $uploadBox.addEventListener('dragleave', function (e) {
    e.preventDefault();
    this.style.borderColor = '#ddd';
    this.style.backgroundColor = '#fff';
  });

  $uploadBox.addEventListener('drop', function (e) {
    e.preventDefault();
    this.style.borderColor = '#ddd';
    this.style.backgroundColor = '#fff';

    const file = e.dataTransfer.files[0];
    if (file && file.type.startsWith('image/')) {
      $imageInput.files = e.dataTransfer.files;
      const event = new Event('change');
      $imageInput.dispatchEvent(event);
    }
  });

  // 폼 제출 전 유효성 검사
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

  // 기존 이미지가 있는 경우 미리보기 표시
  // TODO: 서버에서 기존 이미지 URL을 받아와서 처리
  if (false) {
    // 기존 이미지 존재 여부
    $previewImg.src = '기존 이미지 URL';
    $imagePreview.style.display = 'block';
    $uploadBox.style.display = 'none';
  }
}

{ // 서머노트 적용
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
