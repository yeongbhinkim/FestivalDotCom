'use strict';

document.addEventListener('DOMContentLoaded', function () {
    // "신청하기" 버튼에 이벤트 리스너 추가
    const regisButton = document.querySelector('.regis-button');
    regisButton.addEventListener('click', function () {
        // name 속성을 사용하여 festivalId 값을 가져오기
        const festivalIdElement = document.getElementsByName('festivalId')[0]; // name 속성이 'festivalId'인 요소 중 첫 번째 선택
        const festivalId = festivalIdElement ? festivalIdElement.value : null;

        if (!festivalId) {
            alert('축제 ID를 찾을 수 없습니다.');
            return;
        }

        // 사용자 정보를 담을 RegisDTO 객체 생성
        const regisDTO = {
            festivalId: festivalId,
            id: null // 사용자 ID는 서버 측에서 추가 설정
        };

        // AJAX 요청으로 서버에 festival 신청 정보 전송
        fetch('/chat/regis/setRegis', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(regisDTO)
        })
            .then(response => {
                if (!response.ok) {
                    if (response.status === 409) {
                        // 중복 신청 상태 (HTTP 409)
                        throw new Error('이미 이 축제에 신청된 상태입니다.');
                    } else if (response.status === 400) {
                        throw new Error('신청 처리 중 문제가 발생했습니다.');
                    } else {
                        throw new Error('오류가 발생했습니다. 다시 시도해 주세요.');
                    }
                }
                return response.text();
            })
            .then(data => {
                alert(data); // 성공 시 서버에서 보내온 메시지를 사용자에게 알림
            })
            .catch(error => {
                console.error('오류:', error);
                alert(error.message);
            });
    });
});
