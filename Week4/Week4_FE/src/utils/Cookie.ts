import { UserStatus } from './../types/AuthTypes';
import { useCookies } from 'react-cookie';

const TOKEN_NAME = 'authStatus';

// custom hook to handle authToken - we use compositon to decouple the auth system and it's storage
const useAuthStatus = () => {
	// we use react-cookies to access our cookies
	const [cookies, setCookie, removeCookie] = useCookies([TOKEN_NAME]);

	// this function allows to save any string in our cookies, under the key "authToken"
	const setAuthStatus = (authToken: UserStatus) => setCookie(TOKEN_NAME, authToken);

	// this function removes the key "authToken" from our cookies. Useful to logout
	const removeAuthStatus = () => removeCookie(TOKEN_NAME);

	return [cookies[TOKEN_NAME], setAuthStatus, removeAuthStatus];
};

export default useAuthStatus;
