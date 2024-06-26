// import {getUserRoleFromCookie, removeAccessTokenFromCookie} from "./cookieManagement.js";

document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('logoutButton').addEventListener('click', function (event) {
        event.preventDefault(); // 기본 동작(링크 이동) 방지
        // console.log("test");
        // const token = getUserRoleFromCookie();
        //     console.log('token ID:', token);
        // if (token) {
        // const decoded = jwt_decode(token);
        //     console.log('decoded ID:', decoded);
        //     console.log('User ID:', decoded.userId);
        //     console.log('role ID:', decoded.role);
        // }
        oauthlogOut();
        // logout();

    });
});

async function logout() {
    try {
        const response = await fetch('/api/v1/tokens', {
            method: 'DELETE',
            credentials: 'include' // 쿠키를 포함시킨 요청
        });
        // removeAccessTokenFromCookie();
        if (response.status === 204) {
            alert('로그아웃 되었습니다.');
            // 로그아웃 후 처리, 예를 들어 로그인 페이지로 리다이렉트
            window.location.replace('/oauthlogin');

        } else {
            alert('로그아웃 실패. 서버 상태를 확인해주세요.');
        }
    } catch (error) {
        alert('로그아웃 중 에러가 발생했습니다: ' + error);
    }
}


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
                logout();
                // 현재 페이지에서 로그아웃 URL로 이동
                window.location.href = logoutUrl;
            } else if (logoutUrl.includes('naver')) {
                // 팝업 창을 열어 로그아웃 URL로 이동
                const popup = window.open(logoutUrl, 'popup', 'width=10,height=10');

                //  팝업 창을 닫음
                setTimeout(() => {
                    popup.close();
                    logout();
                }, 300);
            } else if (logoutUrl.includes('google')) {
                // 팝업 창을 열어 로그아웃 URL로 이동
                const popup = window.open(logoutUrl, 'popup', 'width=10,height=10');

                //  팝업 창을 닫음
                setTimeout(() => {
                    popup.close();
                    logout();
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