import React, { useContext, useEffect, useState } from 'react';
import { RouteComponentProps } from 'react-router-dom';
import axios from 'axios';
import { SERVER_URL } from '../config';
import { Restaurant } from '../types/RestaurantTypes';
import {
	makeStyles,
	createStyles,
	Theme,
	List,
	ListItemText,
	Divider,
	Paper,
	ListItem,
	TextField,
	Button,
	Dialog,
	DialogTitle,
	DialogContent,
	DialogContentText,
	DialogActions,
	IconButton,
	Container,
	ListItemIcon,
	Collapse,
	Typography,
	Grid,
} from '@material-ui/core';
import DeleteIcon from '@material-ui/icons/Delete';
import UpdateIcon from '@material-ui/icons/Update';
import MapContainer from '../component/MapContainer';
import UserContext from '../contexts/UserContext';
import moment from 'moment';
import menuDefaultImage from '../images/menuDefaultImage.png';
import { ExpandLess, ExpandMore } from '@material-ui/icons';
import { useRouteMatch } from 'react-router-dom';
import restaurantDefaultImage from '../images/restaurantDefaultImage.png';

const useStyles = makeStyles((theme: Theme) =>
	createStyles({
		rootDiv: {
			display: 'flex',
			flexDirection: 'column',
			position: 'relative',
			justifyContent: 'center',
		},
		menu: {
			display: 'flex',
			flexDirection: 'column',
			alignItems: 'center',
		},
		image: {
			height: 'auto',
			width: '100%',
			objectFit: 'cover',
		},
		contianter: {
			width: '15rem',
			height: '18rem',
		},
		topImageContainer: {
			width: '100%',
			height: '10%',
			position: 'relative',
		},
		topImage: {
			position: 'relative',
			objectFit: 'cover',
			width: 'inherit',
			height: '1%',
			// backgroundSize: 'auto',
			// width: '100%',
		},
		paper: {
			position: 'relative',
			width: '70%',
			marginTop: '-10%',
			paddingTop: '3rem',
			paddingBottom: '3rem',
			paddingLeft: '5rem',
			paddingRight: '5rem',
		},
		restaurantName: {
			fontSize: '9rem',
			fontWeight: 'bold',
		},
		otherRestaurantInformation: {
			fontSize: '2.5rem',
		},
		otherRestaurantInformationTag: {
			fontSize: '1.5rem',
		},
		otherRestaurantInformationContainer: {
			marginTop: '1rem',
			marginBottom: '1rem',
		},
		openingHourCollapse: {
			paddingLeft: '0.3rem',
		},
		menus: {
			display: 'flex',
			flexDirection: 'row',
		},
		menuItem: {
			display: 'flex',
			margin: '1.5rem',
			width: '18rem',
			height: '24rem',
		},
	})
);

interface MatchParams {
	restaurantId: string;
}

// interface MatchProps extends RouteComponentProps<MatchParams> {
// 	restaurantId: string;
// }
interface Props {
	restaurantId: string;
}

function RestaurantPage(props: Props) {
	const classes = useStyles();
	const match = useRouteMatch<MatchParams>();
	const { userStatus } = useContext(UserContext);
	const getRestaurantUrl = props.restaurantId
		? `${SERVER_URL}/restaurant/${props.restaurantId}`
		: `${SERVER_URL}/restaurant/${match.params.restaurantId}`;
	const [comment, setComment] = useState('');
	const [updateCommentBody, setUpdateCommentBody] = useState('');
	const [openDialogIndex, setOpenDialogIndex] = useState(-1);
	const [updateCommentIndex, setUpdateCommentIndex] = useState(-1);
	const [openOpeningHours, setOpenOpeningHours] = useState(false);
	const [restaurantInformation, setRestaurantInformation] = useState<Restaurant>({
		name: '',
		category: '',
		menus: [],
		telephone: '',
		description: '',
		openingHours: [
			{
				openTime: '',
				closeTime: '',
			},
			{
				openTime: '',
				closeTime: '',
			},
			{
				openTime: '',
				closeTime: '',
			},
			{
				openTime: '',
				closeTime: '',
			},
			{
				openTime: '',
				closeTime: '',
			},
			{
				openTime: '',
				closeTime: '',
			},
			{
				openTime: '',
				closeTime: '',
			},
		],
		location: {
			fullAddress: '',
			extraAddress: '',
		},
	});
	useEffect(() => {
		axios({
			method: 'get',
			url: getRestaurantUrl,
		}).then((res) => {
			setRestaurantInformation(res.data);
			console.log(res);
		});
	}, [getRestaurantUrl]);
	const dayOfWeek = ['일', '월', '화', '수', '목', '금', '토', '일'];
	const today = new Date().getDay();
	return (
		<div className={classes.rootDiv}>
			{/* <div className={classes.topImageContainer}> */}
			{restaurantInformation.image ? (
				<img
					className={classes.topImage}
					src={`${SERVER_URL}/image/restaurants/${restaurantInformation.image}`}
					alt="Restaurant"
					style={{ height: '50vh' }}
				/>
			) : (
				<img className={classes.topImage} src={restaurantDefaultImage} alt="Restaurant" style={{ height: '0%' }} />
			)}
			{/* </div> */}
			<Grid container style={{ height: '100%', border: '1px solid black' }} justify="center">
				<Paper className={classes.paper} variant="outlined">
					<div className={classes.restaurantName}> {restaurantInformation.name}</div>
					<div className={classes.otherRestaurantInformationContainer}>
						<div className={classes.otherRestaurantInformation}> {restaurantInformation.description}</div>
					</div>
					<Divider />
					<div className={classes.otherRestaurantInformationContainer}>
						<div className={classes.otherRestaurantInformationTag}> 전화번호</div>
						<div className={classes.otherRestaurantInformation}> {restaurantInformation.telephone}</div>
					</div>
					<Divider />
					<ListItem
						button
						onClick={() => {
							setOpenOpeningHours(!openOpeningHours);
						}}
					>
						<ListItemText
							primary={
								<Typography style={{ fontSize: '2.5rem' }}>{`${dayOfWeek[today]}요일 여는 시간: ${
									restaurantInformation.openingHours[today].openTime
								} 닫는 시간: ${restaurantInformation.openingHours[new Date().getDay()].closeTime}`}</Typography>
							}
						/>
						{openOpeningHours ? <ExpandLess /> : <ExpandMore />}
					</ListItem>
					<Collapse in={openOpeningHours} timeout="auto" unmountOnExit>
						<List component="div" disablePadding>
							{restaurantInformation.openingHours.map((e, index) => {
								return index === new Date().getDay() ? (
									<ListItemText
										className={classes.openingHourCollapse}
										key={index}
										primary={
											<Typography
												style={{ fontWeight: 'bold', fontSize: '2rem' }}
											>{`    ${dayOfWeek[index]}요일 여는 시간: ${e.openTime} 닫는 시간: ${e.closeTime}`}</Typography>
										}
									/>
								) : (
									<ListItemText
										className={classes.openingHourCollapse}
										key={index}
										primary={
											<Typography
												style={{ fontSize: '2rem' }}
											>{`    ${dayOfWeek[index]}요일 여는 시간: ${e.openTime} 닫는 시간: ${e.closeTime}`}</Typography>
										}
									/>
								);
							})}
						</List>
					</Collapse>
					<Divider />
					<div className={classes.otherRestaurantInformationContainer}>
						<div className={classes.otherRestaurantInformationTag}> 주소</div>
						<div className={classes.otherRestaurantInformation}>
							{`${restaurantInformation.location.fullAddress} ${restaurantInformation.location.extraAddress}`}
						</div>
					</div>
					<MapContainer fullAddress={restaurantInformation.location.fullAddress} name={restaurantInformation.name} />
					<Divider />
					<div className={classes.menus}>
						{restaurantInformation.menus.map((e, index) => {
							return (
								<Paper className={classes.menuItem}>
									<div key={index} className={classes.menu}>
										{e.image ? (
											<img className={classes.image} src={`${SERVER_URL}/image/menus/${e.image}`} alt="Restaurant" />
										) : (
											<img className={classes.image} src={menuDefaultImage} alt="Restaurant" />
										)}
										<div style={{ fontSize: '2rem' }}>{e.name}</div>
										<div style={{ fontSize: '1.3rem' }}>{e.description}</div>
										{e.sizes.map((el, indexl) => {
											return (
												<div key={indexl}>
													<div>
														{el.size} {el.price}
													</div>
												</div>
											);
										})}
									</div>
								</Paper>
							);
						})}
					</div>
					{!props.restaurantId && (
						<div>
							<div style={{ fontSize: '2rem' }}>댓글</div>
							<Paper>
								<List>
									{restaurantInformation.comments?.map((e, index) => {
										return (
											<div>
												<div>
													<ListItem>
														<ListItemText primary={e.body} />
														<div>
															<ListItemText primary={e.nickname} />
															<ListItemText primary={moment(e.date).format('YYYY-MM-DD-hh-mm-ss')} />
														</div>
														{e.nickname === userStatus.nickname && (
															<div>
																<IconButton
																	aria-label="update"
																	onClick={() => {
																		setUpdateCommentBody(e.body);
																		setUpdateCommentIndex(index);
																	}}
																>
																	<UpdateIcon fontSize="small" />
																</IconButton>
																<IconButton
																	aria-label="delete"
																	onClick={() => {
																		setOpenDialogIndex(index);
																	}}
																>
																	<DeleteIcon fontSize="small" />
																</IconButton>
															</div>
														)}
													</ListItem>
													{updateCommentIndex === index && (
														<div>
															<p>댓글 수정하기</p>
															<TextField
																value={updateCommentBody}
																onChange={(e) => {
																	setUpdateCommentBody(e.target.value);
																}}
															/>
															<Button
																onClick={() => {
																	if (restaurantInformation.comments) {
																		const deleteCommentUrl = `${SERVER_URL}/restaurant/${restaurantInformation._id}/comment/${e._id}`;
																		axios({
																			method: 'put',
																			url: deleteCommentUrl,
																			data: {
																				body: updateCommentBody,
																			},
																			headers: {
																				token: userStatus.accessToken,
																			},
																		}).then(() => {
																			setUpdateCommentIndex(-1);
																			setUpdateCommentBody('');
																			axios({
																				method: 'get',
																				url: getRestaurantUrl,
																			}).then((res) => setRestaurantInformation(res.data));
																		});
																	}
																}}
															>
																수정
															</Button>
															<Button
																onClick={() => {
																	setUpdateCommentIndex(-1);
																	setUpdateCommentBody('');
																}}
															>
																취소
															</Button>
														</div>
													)}
												</div>
												<Divider />
											</div>
										);
									})}
								</List>
								{userStatus.nickname && (
									<div>
										<TextField
											value={comment}
											onChange={(e) => {
												setComment(e.target.value);
											}}
										/>
										<Button
											onClick={() => {
												const postCommentUrl = `${SERVER_URL}/restaurant/${restaurantInformation._id}/comment`;
												axios({
													method: 'post',
													url: postCommentUrl,
													data: {
														body: comment,
													},
													headers: {
														token: userStatus.accessToken,
													},
													withCredentials: true,
												}).then((res) => {
													setComment('');
													axios({
														method: 'get',
														url: getRestaurantUrl,
													}).then((res) => setRestaurantInformation(res.data));
												});
											}}
										>
											댓글 작성하기
										</Button>
									</div>
								)}
							</Paper>
						</div>
					)}
				</Paper>
			</Grid>
			<Dialog
				open={openDialogIndex > -1}
				keepMounted
				onClose={() => {
					setOpenDialogIndex(-1);
				}}
				aria-labelledby="alert-dialog-slide-title"
				aria-describedby="alert-dialog-slide-description"
			>
				<DialogTitle id="alert-dialog-slide-title">{'댓글 삭제'}</DialogTitle>
				<DialogContent>
					<DialogContentText id="alert-dialog-slide-description">댓글을 삭제하시겠습니까?</DialogContentText>
				</DialogContent>
				<DialogActions>
					<Button
						onClick={() => {
							if (restaurantInformation.comments) {
								const deleteCommentUrl = `${SERVER_URL}/restaurant/${restaurantInformation._id}/comment/${restaurantInformation?.comments[openDialogIndex]._id}`;
								axios({
									method: 'delete',
									url: deleteCommentUrl,
									headers: {
										token: userStatus.accessToken,
									},
								})
									.then((res) => {
										axios({
											method: 'get',
											url: getRestaurantUrl,
										}).then((res) => setRestaurantInformation(res.data));
										setOpenDialogIndex(-1);
									})
									.catch((err) => {
										console.error(err);
									});
							}
						}}
						color="primary"
					>
						댓글 삭제
					</Button>
					<Button
						onClick={() => {
							setOpenDialogIndex(-1);
						}}
						color="primary"
					>
						취소
					</Button>
				</DialogActions>
			</Dialog>
		</div>
	);
}

export default RestaurantPage;
