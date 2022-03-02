export interface Menu {
	_id?: string,
	name: string;
	description: string;
	sizes: MenuSize[];
	image?: string;
}

export interface MenuSize {
	size: string;
	price: number;
}

export interface OpeningHour {
	openTime: string;
	closeTime: string;
}

export interface Location {
	fullAddress: string;
	extraAddress: string;
}

export interface Comment {
	_id: string;
	nickname: string;
	body: string;
	date: Date;
}

export interface Restaurant {
	_id?: string;
	name: string;
	category: string;
	description: string;
	image?: string;
	telephone: string;
	menus: Menu[];
	openingHours: OpeningHour[];
	location: Location;
	comments?: Comment[];
	contractExpiredAt?: Date;
	confirmed?: boolean;
}

export interface MenuModification {
	newMenus: Menu[];
	removedMenus: Menu[];
	modifiedMenus: Menu[];
}
