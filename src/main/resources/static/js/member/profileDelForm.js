'use strict';

async function oauthlogOut() {
    try {
        // /oauthlogOut 엔드포인트를 호출하여 로그아웃 URL을 가져옴
        const response = await fetch('/oauthlogOut', {
            method: 'GET'
        });

        if (response.ok) {
            const logoutUrl = await response.text();

            // URL에 따라 분기 처리
            if (logoutUrl.includes('kakao')) {
                // 현재 페이지에서 로그아웃 URL로 이동
                window.location.href = logoutUrl;
                // 탈퇴 시작
                outBtnClick();
            } else if (logoutUrl.includes('naver')) {
                // 팝업 창을 열어 로그아웃 URL로 이동
                const popup = window.open(logoutUrl, 'popup', 'width=10,height=10');

                // 팝업 창을 닫음
                setTimeout(() => {
                    popup.close();
                    // 탈퇴 시작
                    outBtnClick();
                }, 300);
            } else if (logoutUrl.includes('google')) {
                // 팝업 창을 열어 로그아웃 URL로 이동
                const popup = window.open(logoutUrl, 'popup', 'width=10,height=10');

                // 팝업 창을 닫음
                setTimeout(() => {
                    popup.close();
                    // 탈퇴 시작
                    outBtnClick();
                }, 300);
            } else {
                alert('알 수 없는 로그아웃 URL입니다.');
            }

        } else {
            alert('로그아웃 URL을 가져오는 데 실패했습니다. 서버 상태를 확인해주세요.');
        }
    } catch (error) {
        alert('로그아웃 중 에러가 발생했습니다: ' + error);
    }
}

// outBtn 클릭 이벤트에 oauthlogOut 호출 추가
outBtn.addEventListener('click', async function (e) {
    e.preventDefault(); // 기본이벤트 막기
    const checkbox = document.getElementById('agree');
    if (!checkbox.checked) {
        alert('약관에 동의하셔야 합니다.');
    } else {
        if (window.confirm('정말 탈퇴하시겠습니까?')) {
            await oauthlogOut();
        }
    }
});

// 탈퇴 함수
function outBtnClick() {
    document.getElementById('frm').submit();
}