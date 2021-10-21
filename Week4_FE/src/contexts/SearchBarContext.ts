import { SearchBarContextType } from './../types/ContextTypes';
import { createContext } from 'react';

export default createContext<SearchBarContextType>({
	searchText: '',
	setSearchText: (searchText: string) => {},
});
