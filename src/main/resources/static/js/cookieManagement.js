// cookieManagement.js
import Cookies from 'https://cdn.skypack.dev/js-cookie';

export const getAccessTokenFromCookie = () => Cookies.get('accessToken');
export const getUserRoleFromCookie = () => Cookies.get('userRole');

export const removeAccessTokenFromCookie = () => Cookies.remove('accessToken');
