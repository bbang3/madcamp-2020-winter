import { UserStatus } from './AuthTypes';

export interface UserContextType {
	userStatus: UserStatus;
	setUserStatus: (userStatus: UserStatus) => void;
}

export interface SearchBarContextType {
	searchText: string;
	setSearchText: (searchText: string) => void;
}
