export function postComment(boardId, commentObj, callback) {
    fetch(`/api/v1/free-boards/${boardId}/comments`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(commentObj)
    })
        .then(resp=> {
            // callback도 같이 조건 검사를 해주는 것이 정석이다.
            if(resp.ok && callback) {
                callback();
            }
        })
}

export function getCommentList(boardId, page, callback) {
    fetch(`/api/v1/free-boards/${boardId}/comments?page=${page}`)
        .then(resp => { // 데이터를 json 형태로 전달
            if(resp.ok) {
                return resp.json();
            }
        })
        .then(data => { // 콜백 함수를 실행
            if(callback) {
                callback(data);
            }
        });
}

// 받아오는게 없으니 바로 콜백처리
export function patchComment(commentId, commentObj, callback){
    fetch(`/api/v1/free-comments/${commentId}`,{
        method: 'PATCH',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(commentObj)
    })
    .then(resp => {
        if(resp.ok && callback) {
            callback();
        }
    });
}

//
export function deleteComment(commentId, callback) {
    fetch(`/api/v1/free-comments/${commentId}`, {
        method: 'DELETE'
    })
        .then(resp => {
            // 만약 응답이 왔고 callback 함수가 있으면 callback을 실행해라!! 라는 의미이다!
            if (resp.ok && callback) {
                callback();
            }
        })

}