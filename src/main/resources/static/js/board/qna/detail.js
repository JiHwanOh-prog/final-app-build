{
  // DOM 요소
  const $answerForm = document.querySelector('.answer-form');
  const $answerList = document.querySelector('.answer-list');
  const $deleteBtn = document.querySelector('.delete-btn');

  /**
   * 답변 작성 폼 제출 처리
   */
  $answerForm?.addEventListener('submit', function (e) {
    e.preventDefault();

    const $textarea = this.querySelector('.answer-textarea');
    const content = $textarea.value.trim();

    // 내용 검증
    if (content.length === 0) {
      alert('답변 내용을 입력해주세요.');
      $textarea.focus();
      return;
    }

    // TODO: 서버에 답변 등록 요청
    // const formData = new FormData(this);
    // submitAnswer(formData);

    // 임시: 답변 추가 UI 표시
    addAnswerToList({
      writer: 'currentUser',
      date: new Date().toLocaleString(),
      content: content,
    });

    // 폼 초기화
    this.reset();
  });

  /**
   * 답변 목록에 새 답변 추가
   * @param {Object} answer - 답변 데이터 객체
   */
  function addAnswerToList(answer) {
    const answerItem = document.createElement('li');
    answerItem.className = 'answer-item';
    answerItem.innerHTML = `
            <article class="answer-article">
                <div class="answer-header">
                    <div class="answer-info">
                        <span class="writer">${answer.writer}</span>
                        <span class="date">${answer.date}</span>
                    </div>
                    <div class="answer-actions">
                        <button type="button" class="delete-btn">삭제</button>
                    </div>
                </div>
                <div class="answer-content">
                    ${answer.content.replace(/\n/g, '<br>')}
                </div>
            </article>
        `;

    // 답변 삭제 버튼 이벤트 추가
    const deleteBtn = answerItem.querySelector('.delete-btn');
    deleteBtn?.addEventListener('click', () => {
      if (confirm('답변을 삭제하시겠습니까?')) {
        // TODO: 서버에 삭제 요청
        answerItem.remove();
        updateAnswerCount();
      }
    });

    // 목록 최상단에 추가
    $answerList.insertBefore(answerItem, $answerList.firstChild);
    updateAnswerCount();
  }

  /**
   * 답변 개수 업데이트
   */
  function updateAnswerCount() {
    const count = $answerList.children.length;
    const $title = document.querySelector('.answer-title');
    $title.textContent = `답변 ${count}개`;
  }

  /**
   * 게시글 삭제
   */
  $deleteBtn?.addEventListener('click', function () {
    if (confirm('게시글을 삭제하시겠습니까?')) {
      // TODO: 서버에 삭제 요청
      location.href = '/board/qna/list';
    }
  });

  // 초기 답변 개수 설정
  updateAnswerCount();
}
