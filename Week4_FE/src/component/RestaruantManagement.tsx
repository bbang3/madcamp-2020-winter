import React, { useContext, useEffect, useState } from 'react';
import axios from 'axios';
import { IMAGE_BASE_URL, SERVER_URL } from '../config';
import {
	Button,
	Container,
	FormControl,
	Grid,
	IconButton,
	InputLabel,
	Paper,
	Select,
	Tab,
	Tabs,
	TextField,
} from '@material-ui/core';
import { Restaurant, OpeningHour, Comment } from '../types/RestaurantTypes';
import UserContext from '../contexts/UserContext';
import AddressSearch from './AddressSearchComponent';
import { makeStyles, createStyles } from '@material-ui/core';
import { Link, NavLink, useRouteMatch } from 'react-router-dom';
import restaurantDefaultImage from '../images/restaurantDefaultImage.png';
import RestaurantOwnerComment from './RestaurantOwnerComment';
import { Add, Create, Delete } from '@material-ui/icons';

const useStyles = makeStyles(() =>
	createStyles({
		rootDiv: {
			display: 'flex',
			justifyContent: 'center',
			flexDirection: 'column',
			// maxWidth: '100px',
		},
		item: {
			flex: 1,
		},
	})
);

interface TabPanelProps {
	children?: React.ReactNode;
	dir?: string;
	index: any;
	value: any;
}

function RestaurantManagement() {
	const match = useRouteMatch();
	const classes = useStyles();
	const { userStatus } = useContext(UserContext);
	const initialRestaurnt: Restaurant = {
		name: '',
		category: '',
		description: '',
		image: '',
		telephone: '',
		menus: [],
		openingHours: [...Array<OpeningHour>(7)].map(() => {
			return { openTime: '', closeTime: '' } as OpeningHour;
		}),
		location: { fullAddress: '', extraAddress: '' },
	};
	const [restaurant, setRestaurant] = useState<Restaurant>(initialRestaurnt);
	const [image, setImage] = useState<File | null>(null);
	const [tabnum, setTabnum] = useState(0);

	const handleChange = (event: React.ChangeEvent<{}>, newValue: number) => {
		setTabnum(newValue);
	};

	const dayOfWeek = ['월', '화', '수', '목', '금', '토', '일'];
	const url = `${SERVER_URL}/restaurant/${userStatus.restaurantId}`;

	console.log('restaurantInformation:', restaurant);
	useEffect(() => {
		axios({
			method: 'get',
			url,
		})
			.then((res) => {
				console.log('res', res);
				setRestaurant(res.data);
			})
			.catch((err) => {
				console.log('err:', err);
			});
	}, [url]);

	const handleSubmit = async (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
		const form = new FormData();
		if (!image) return;
		form.append('image', image);
		form.append('restaurant', JSON.stringify(restaurant));

		axios.put(url, form, { headers: { token: userStatus.accessToken } });
	};

	const deleteMenu = async (index: number) => {
		const menuToDelete = restaurant.menus[index];
		console.log(menuToDelete);

		try {
			const response = await axios.delete(url + `/menu/${menuToDelete._id}`, {
				headers: { token: userStatus.accessToken },
			});
			if (response.status === 200) {
				const newMenus = restaurant.menus.filter((_, i) => index !== i);
				setRestaurant({
					...restaurant,
					menus: newMenus,
				});
			}
			console.log(response);
		} catch (error) {
			console.log(error.response.data.message);
		}
	};
	const updateComments = (comments: Comment[]) => {
		setRestaurant({
			...restaurant,
			comments: comments,
		});
	};

	return (
		<div className={classes.rootDiv}>
			<Container>
				<Container style={{ width: '100%', alignItems: 'center' }}>
					<Tabs
						centered
						value={tabnum}
						indicatorColor="primary"
						textColor="primary"
						onChange={handleChange}
						aria-label="simple tabs example"
					>
						<Tab label="가게 정보" fullWidth />
						<Tab label="메뉴" fullWidth />
						<Tab label="댓글 관리" fullWidth />
					</Tabs>
				</Container>
				<Paper style={{ padding: '10px' }}>
					{restaurant.confirmed ? (
						<Button variant="outlined" color="primary">
							승인됨
						</Button>
					) : (
						<Button variant="outlined" color="secondary">
							승인 대기중
						</Button>
					)}
					{tabnum === 0 && (
						<div className={classes.item}>
							<Button component="label" style={{ width: '100%' }}>
								<div>
									{console.log(`${IMAGE_BASE_URL}/restaurants/${restaurant.image}`)}
									<img
										src={
											image
												? URL.createObjectURL(image)
												: restaurant.image
												? `${IMAGE_BASE_URL}/restaurants/${restaurant.image}`
												: restaurantDefaultImage
										}
										alt="가게 대표 이미지"
										style={{ maxWidth: '25vw', maxHeight: '50vh' }}
									/>
								</div>
								<input type="file" onChange={(e) => setImage(e.target.files && e.target.files[0])} hidden />
							</Button>
							<div style={{ width: '100%', textAlign: 'center' }}> 가게 대표 이미지(클릭하여 수정) </div>
							<Grid container spacing={2}>
								<Grid item container spacing={2}>
									<Grid item>
										<TextField value={restaurant.name} label="가게 이름" />
									</Grid>
									<Grid item>
										<TextField value={restaurant.category} label="카테고리" />
									</Grid>
								</Grid>
								<Grid item xs={12}>
									<TextField
										label="가게 설명"
										placeholder="가게 설명 입력해주세요"
										style={{ width: '75%' }}
										value={restaurant.description}
										onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
											setRestaurant({ ...restaurant, description: event.target.value });
										}}
										multiline={true}
									/>
								</Grid>
								<Grid item>
									<TextField
										label="전화번호"
										type="tel"
										placeholder="전화번호를 입력해주세요"
										value={restaurant.telephone}
										onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
											setRestaurant({ ...restaurant, telephone: event.target.value });
										}}
									/>
								</Grid>
								<Grid item>
									<b> 영업시간</b>
									<Grid container spacing={2} justify="center">
										{dayOfWeek.map((e, index) => {
											return (
												<Grid item key={index}>
													<Paper style={{ padding: '10px' }}>
														<Grid container direction="column" spacing={2}>
															<Grid item>{e} </Grid>
															<Grid item>
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
															</Grid>
															<Grid item>
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
															</Grid>
														</Grid>
													</Paper>
												</Grid>
											);
										})}
									</Grid>
								</Grid>
								<Grid item style={{ width: '100%' }}>
									<AddressSearch restaurantInformation={restaurant} setRestaurantInformation={setRestaurant} />
									<Button variant="outlined" onClick={handleSubmit} style={{ float: 'right' }}>
										수정 완료
									</Button>
								</Grid>
							</Grid>
						</div>
					)}

					{tabnum === 1 && (
						<div className={classes.item}>
							<div>
								{restaurant.menus.map((e, index) => {
									return (
										<Paper key={index} style={{ margin: '20px', padding: '10px' }}>
											<Grid container alignItems="center" spacing={2}>
												<Grid item xs={2} style={{ width: '100px', height: '100px' }} justify="center">
													<Container style={{ width: '100%', height: '100%' }}>
														<img
															src={`${IMAGE_BASE_URL}/menus/${e.image}`}
															style={{ maxWidth: '100%', maxHeight: '100%' }}
															alt="메뉴 사진"
														/>
													</Container>
												</Grid>
												<Grid item>
													<TextField value={e.name} label="메뉴명" />
												</Grid>
												<Grid item xs={6}>
													<TextField value={e.description} fullWidth label="메뉴 설명" />
												</Grid>
												<Grid item xs={1}>
													<Link to={`${match.url}/update-menu/${e._id}`} style={{ float: 'right' }}>
														<IconButton>
															<Create />
														</IconButton>
													</Link>
													<IconButton onClick={() => deleteMenu(index)} style={{ float: 'right' }}>
														<Delete />
													</IconButton>
												</Grid>
											</Grid>
										</Paper>
									);
								})}
							</div>
							<div>
								<Link to={`${match.url}/add-menu`}>
									<IconButton>
										<Add />
									</IconButton>
								</Link>
							</div>{' '}
						</div>
					)}

					{tabnum === 2 && (
						<div className={classes.item}>
							{restaurant._id && restaurant.comments && (
								<RestaurantOwnerComment
									restaurantId={restaurant._id}
									restaurantName={restaurant.name}
									comments={restaurant.comments}
									updateComments={updateComments}
								></RestaurantOwnerComment>
							)}
						</div>
					)}
				</Paper>
			</Container>
		</div>
	);
}

export default RestaurantManagement;
