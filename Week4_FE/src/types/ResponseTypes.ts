import { OpeningHour } from './RestaurantTypes';
export interface LoginResponseType {
	role: string;
	token: string;
}

export interface CategoryResponseType {
	_id: string;
	name: string;
}

export interface RestaurantResponseType {
	_id: string;
	name: string;
	description: string;
	openingHours: OpeningHour[];
}