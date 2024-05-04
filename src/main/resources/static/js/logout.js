import {getUserRoleFromCookie, removeAccessTokenFromCookie} from "./cookieManagement.js";

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

        logout();

    });
});

async function logout() {
    try {
        const response = await fetch('/api/v1/tokens', {
            method: 'DELETE',
            credentials: 'include' // 쿠키를 포함시킨 요청
        });
        removeAccessTokenFromCookie();
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
