import {
	Button,
	TextField,
	FormControl,
	Select,
	MenuItem,
	InputLabel,
	makeStyles,
	createStyles,
} from '@material-ui/core';
import React, { useContext, useState } from 'react';
import ConvertXlsxToJson from '../utils/ConvertXlsxToJson';
import axios from 'axios';
import { OpeningHour, Restaurant } from '../types/RestaurantTypes';
import { SERVER_URL } from '../config';
import AddressSearch from '../component/AddressSearchComponent';
import UserContext from '../contexts/UserContext';
import restaurantDefaultImage from '../images/restaurantDefaultImage.png';
import { useHistory } from 'react-router-dom';
import useAuthStatus from '../utils/Cookie';

const useStyles = makeStyles(() =>
	createStyles({
		rootDiv: {
			display: 'flex',
		},
		formControl: {
			minWidth: 120,
		},
	})
);

const greetingMessage = (collegeName: string) =>
	`저희 ${collegeName}과 계약하신 것을 환영합니다, 아래 내용을 기입하신 후 승인 신청 버튼을 눌러주세요.\n검토 후 승인 완료 시 사이트에 가게 내용이 표시됩니다.`;

function RestaurantInformationInputPage() {
	const history = useHistory();
	const classes = useStyles();
	const [_, setAuthStatus, __] = useAuthStatus();
	const { userStatus, setUserStatus } = useContext(UserContext);
	const [selectedFile, setSelectedFile] = useState<File | null>(null);
	const [restaurant, setRestaurant] = useState<Restaurant>({
		name: '',
		category: '',
		menus: [],
		telephone: '',
		description: '',
		openingHours: [...Array<OpeningHour>(7)].map(() => {
			return { openTime: '', closeTime: '' } as OpeningHour;
		}),
		location: {
			fullAddress: '',
			extraAddress: '',
		},
	});
	const [image, setImage] = useState<File | null>(null);

	const categoryList = [
		'중국집',
		'한식',
		'찜/탕',
		'분식',
		'일식/돈까스',
		'족발/보쌈',
		'치킨',
		'피자/양식',
		'카페/디저트',
		'야식',
		'프랜차이즈',
	];

	const dayOfWeek = ['월', '화', '수', '목', '금', '토', '일'];

	const showPreviewButtonHandler = () => {
		if (selectedFile) {
			try {
				ConvertXlsxToJson(selectedFile).then((value) => {
					setRestaurant({ ...restaurant, menus: Object.values(value) });
				});
			} catch (err) {
				console.log('ghghgh');
				console.error(err);
			}
		}
	};

	const submitButtonHandler = async () => {
		if (!image) return;
		const form = new FormData();
		form.append('image', image);
		form.append('restaurant', JSON.stringify(restaurant));

		try {
			const response = await axios.post(`${SERVER_URL}/restaurant/`, form, {
				headers: { token: userStatus.accessToken },
			});

			if (response.status === 200) {
				const newUserStatus = { ...userStatus, restaurantId: response.data._id };
				setUserStatus(newUserStatus);
				setAuthStatus(newUserStatus);
				history.push('/RestaurantManagement');
			}
		} catch (error) {
			console.log(error);
		}
	};

	const showMenus = () => {
		return restaurant?.menus.map((e, index) => {
			return (
				<li key={index}>
					<div>{`메뉴: ${e.name}`}</div>
					<div>{`설명: ${e.description}`}</div>
					{e.sizes.map((s, index) => {
						return (
							<div key={index}>{s.size.length > 0 ? `사이즈: ${s.size} 가격: ${s.price}` : `가격: ${s.price}`}</div>
						);
					})}
				</li>
			);
		});
	};

	return (
		<div className={classes.rootDiv}>
			<div>
				<div>{greetingMessage('고려대')}</div>
				<Button component="label">
					<div>
						<img
							src={image ? URL.createObjectURL(image) : restaurantDefaultImage}
							alt="가게 대표 이미지"
							style={{ maxWidth: '25vw', maxHeight: '50vh' }}
						/>
					</div>
					<input type="file" onChange={(e) => setImage(e.target.files && e.target.files[0])} hidden />
				</Button>

				<div>
					<TextField
						required
						label="가게 이름"
						value={restaurant.name}
						onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
							setRestaurant({ ...restaurant, name: event.target.value });
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
							value={restaurant.category}
							onChange={(event: React.ChangeEvent<{ value: unknown }>) => {
								setRestaurant({ ...restaurant, category: event.target.value as string });
							}}
						>
							{categoryList.map((e, index) => {
								return (
									<MenuItem value={e} key={index}>
										{e}
									</MenuItem>
								);
							})}
						</Select>
					</FormControl>
				</div>
				<div>
					<TextField
						label="가게 설명"
						placeholder="가게 설명 입력해주세요"
						value={restaurant.description}
						onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
							setRestaurant({ ...restaurant, description: event.target.value });
						}}
						multiline={true}
					/>
				</div>
				<div>
					<TextField
						label="전화번호"
						type="tel"
						placeholder="전화번호를 입력해주세요"
						value={restaurant.telephone}
						onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
							setRestaurant({ ...restaurant, telephone: event.target.value });
						}}
					/>
				</div>
				<div>
					{dayOfWeek.map((e, index) => {
						return (
							<div key={index}>
								{e}
								<TextField
									label="여는 시간"
									type="time"
									value={restaurant.openingHours[index].openTime}
									InputLabelProps={{
										shrink: true,
									}}
									inputProps={{
										step: 300, // 5 min
									}}
									onChange={(e) => {
										const newOpeningHours = restaurant.openingHours;
										newOpeningHours[index].openTime = e.target.value;
										setRestaurant({
											...restaurant,
											openingHours: newOpeningHours,
										});
									}}
								/>
								<TextField
									label="닫는 시간"
									type="time"
									value={restaurant.openingHours[index].closeTime}
									InputLabelProps={{
										shrink: true,
									}}
									inputProps={{
										step: 300, // 5 min
									}}
									onChange={(e) => {
										const newOpeningHours = restaurant.openingHours;
										newOpeningHours[index].closeTime = e.target.value;
										setRestaurant({
											...restaurant,
											openingHours: newOpeningHours,
										});
									}}
								/>
							</div>
						);
					})}
				</div>
				<AddressSearch restaurantInformation={restaurant} setRestaurantInformation={setRestaurant} />
				<div>
					<input type="file" onChange={(e) => setSelectedFile(e.target.files && e.target.files[0])} accept=".xlsx" />
				</div>
				<div>
					<Button onClick={showPreviewButtonHandler}>Convert Excel to Json</Button>
				</div>
				<Button onClick={submitButtonHandler}>승인신청</Button>
			</div>
			<ul>{showMenus()}</ul>
		</div>
	);
}

export default RestaurantInformationInputPage;
