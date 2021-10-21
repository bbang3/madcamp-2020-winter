import { UserContextType } from './../types/ContextTypes';
import { createContext } from 'react';
import { UserStatus } from '../types/AuthTypes';

export default createContext<UserContextType>({
	userStatus: { accessToken: '', role: '' },
	setUserStatus: (userStatus: UserStatus) => {},
});
