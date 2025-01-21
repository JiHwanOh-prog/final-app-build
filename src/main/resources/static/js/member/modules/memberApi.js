/**
 * 아이디 존재 여부를 확인합니다.
 * 
 * @param {string}loginId - 중복검사할 아이디
 * @param {function}callback - 결과 데이터를 처리할 함수
 * 
 * @example
 * checkLoginId('test', data => {
 *     if (data.exists){
 *         처리....
 *         console.log(data.message); // 기본 메세지
 *     }else {
 *         처리....
 *         console.log(data.message); // 기본 메세지
 *     }
 * }
 */
export function checkLoginId(loginId, callback) {
    fetch(`/api/v1/members/loginId/exists?loginId=${loginId}`)
        .then(resp => {
            if (resp.ok) {
                return resp.json();
            }
        })
        .then(data => callback(data));
}