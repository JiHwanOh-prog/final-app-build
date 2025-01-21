// 자바스크립트 모듈화
// export 키워드를 사용하여 특정 함수나 클래스를 내보낸다.

/**
 * SMS 인증 관련 API 모듈
 * @module smsApi
 */

/**
 * SMS 인증 번호를 발송합니다.
 * @param {string} phoneNumber - 인증번호를 받을 전화번호
 * @param {function} callback - 응답을 처리할 콜백 함수
 * @return {void}
 * 
 * @example
 * sendVerificationCode('01012345678', data => {
 *     if (data.success) {
 *         console.log(data.message); // 기본 메세지 사용 가능
 *     }
 * });
 */
export function sendVerificationCode(phoneNumber, callback) {
    fetch('/sms/verify', {
        method: 'POST',
        headers:{
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({phoneNumber: phoneNumber})
    })
        .then(resp => {
            if (resp.ok){
                return resp.json();
            }
        })
        .then(data => callback(data));
}

/**
 * 입력받은 인증번호의 유효성을 검증합니다.
 * 
 * @param {string} code - 사용자가 입력한 인증번호
 * @param {function} callback - 응답을 처리할 콜백함수
 * @return {void}
 * 
 * @example
 * verifyCode('123456', data => {
 *    if (data.success) {
 *        console.log(data.message); // 기본 메세지 사용 가능
 *    } 
 * });
 */
export function verifyCode(code, callback) {
    fetch('/sms/verify/check', {
        method: 'POST',
        headers:{
            'Content-Type': 'application/json'
        },
        body : JSON.stringify({smsCode: code})
    })
        .then(resp => {
            if (resp.ok){
                return resp.json();
            }
        })
        .then(data => callback(data));
}