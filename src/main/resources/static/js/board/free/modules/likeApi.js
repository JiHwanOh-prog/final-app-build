export function checkLike(boardId, callback) {
    fetch(`/api/v1/free-boards/${boardId}/likes/check`)
        .then(res => {
            if (res.ok) {
                return res.json();
            }
        })
        .then(data => callback(data));
}

export function toggleLike(boardId, callback) {
    fetch(`/api/v1/free-boards/${boardId}/likes`, {
        method:'PUT'
    })
    .then(resp => {
        if (resp.ok) {
            return resp.json();
        }
    })
    .then(data => callback(data));
}
