import React, { useContext, useEffect, useState } from 'react';
import {
	Button,
	createStyles,
	FormControl,
	InputLabel,
	makeStyles,
	MenuItem,
	Select,
	TextField,
	Theme,
} from '@material-ui/core';
import { Restaurant } from '../types/RestaurantTypes';
import { SERVER_URL } from '../config';
import axios, { AxiosResponse } from 'axios';
import { CategoryResponseType } from '../types/ResponseTypes';
import moment from 'moment';
import UserContext from '../contexts/UserContext';

const useStyles = makeStyles((theme: Theme) =>
	createStyles({
		rootDiv: {
			width: 400,
			backgroundColor: theme.palette.background.paper,
			border: '2px solid #000',
			boxShadow: theme.shadows[5],
			padding: theme.spacing(2, 4, 3),
		},
		formControl: {
			minWidth: 120,
		},
		textField: {
			width: 200,
		},
	})
);

interface Props {
	index: number;
	restaurantList: Restaurant[];
	setRestaurnatList: (restaurnantList: Restaurant[]) => void;
	open: boolean[];
	setOpen: (open: boolean[]) => void;
}

function RestaurantInformationModificationModal(props: Props) {
	const { userStatus } = useContext(UserContext);
	const classes = useStyles();
	const categoryUrl = `${SERVER_URL}/category`;
	const updateRestaurantUrl = `${SERVER_URL}/restaurant/${props.restaurantList[props.index]._id}`;
	const [categoryList, setCategoryList] = useState<CategoryResponseType[]>([]);
	const [modifiedRestaurant, setModifiedRestaurant] = useState(props.restaurantList[props.index]);

	useEffect(() => {
		axios({ method: 'get', url: categoryUrl }).then((res: AxiosResponse<CategoryResponseType[]>) => {
			const resultArray: CategoryResponseType[] = [];
			res.data.forEach((e: any) => {
				resultArray.push(e);
			});
			setCategoryList(resultArray);
		});
	}, [categoryUrl]);

	const modifyButtonHandler = () => {
		const formData = new FormData();
		formData.append('restaurant', JSON.stringify(modifiedRestaurant));
		// console.log(JSON.stringify());
		console.log(updateRestaurantUrl);
		axios
			.put(updateRestaurantUrl, formData, {
				headers: {
					'Content-Type': 'multipart/form-data',
					token: userStatus.accessToken,
				},
			})
			.then((res) => {
				const modifiedRestaurantList = [...props.restaurantList];
				modifiedRestaurantList[props.index] = modifiedRestaurant;
				props.setRestaurnatList(modifiedRestaurantList);
				closeModal();
			})
			.catch((err) => {
				console.log(err);
			});
		console.log(modifiedRestaurant);
	};

	const closeModal = () => {
		const newOpenState = [...props.open];
		newOpenState[props.index] = false;
		props.setOpen(newOpenState);
	};

	const cancleButtonHandler = () => {
		closeModal();
	};

	return (
		<div className={classes.rootDiv}>
			<div>
				<TextField
					required
					label="가게 이름"
					value={modifiedRestaurant.name}
					onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
						setModifiedRestaurant({ ...modifiedRestaurant, name: event.target.value });
					}}
					placeholder="가게 이름을 입력해주세요"
				/>
			</div>
			<div>
				<FormControl className={classes.formControl}>
					<InputLabel required id="category">
						카테고리
					</InputLabel>
					<Select
						labelId="category"
						value={modifiedRestaurant.category}
						onChange={(event: React.ChangeEvent<{ value: unknown }>) => {
							setModifiedRestaurant({ ...modifiedRestaurant, category: event.target.value as string });
						}}
					>
						{categoryList.map((e, index) => {
							return (
								<MenuItem value={e.name} key={index}>
									{e.name}
								</MenuItem>
							);
						})}
					</Select>
				</FormControl>
			</div>
			<div>
				<TextField
					id="date"
					label="계약일자"
					type="date"
					defaultValue={
						modifiedRestaurant.contractExpiredAt
							? moment(modifiedRestaurant.contractExpiredAt).format('YYYY-MM-DD')
							: ''
					}
					className={classes.textField}
					InputLabelProps={{
						shrink: true,
					}}
					onChange={(e) => {
						setModifiedRestaurant({
							...modifiedRestaurant,
							contractExpiredAt: new Date(e.target.value),
						});
					}}
				/>
			</div>
			<Button onClick={modifyButtonHandler}>수정</Button>
			<Button onClick={cancleButtonHandler}>취소</Button>
		</div>
	);
}

export default RestaurantInformationModificationModal;
