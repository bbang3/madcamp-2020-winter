import React, { useContext, useEffect, useRef, useState } from 'react';
import {
	Button,
	Divider,
	IconButton,
	InputBase,
	List,
	ListItem,
	ListItemText,
	Modal,
	Paper,
	TextField,
} from '@material-ui/core';
import { createStyles, fade, makeStyles, Theme } from '@material-ui/core/styles';
import DeleteIcon from '@material-ui/icons/Delete';
import { RestaurantOwnerRegister } from '../types/AuthTypes';
import axios from 'axios';
import UserContext from '../contexts/UserContext';
import { SERVER_URL } from '../config';
import { Restaurant } from '../types/RestaurantTypes';
import moment from 'moment';
import RestaurantInformationModificationModal from '../component/RestaurantInformationModificationModal';
import DeleteRestaurantModal from '../component/DeleteRestaurantModal';
import { useHistory, useRouteMatch } from 'react-router-dom';
import ConfirmRestaurantPage from '../pages/ConfirmRestaurantPage';
import SearchIcon from '@material-ui/icons/Search';

const useStyles = makeStyles((theme: Theme) =>
	createStyles({
		rootDiv: {
			flex: 1,
			flexDirection: 'row',
			justifyContent: 'space-between',
		},
		section: {
			width: '45%',
			padding: '1.5rem',
			margin: '1rem',
		},
		createAccount: {
			display: 'flex',
			flexDirection: 'column',
			padding: '1.5rem',
			// width: '40%',
			// justifyContent: 'space-between',
			// marginLeft: '20%',
			// marginRight: '20%',
		},
		storeListElement: {
			display: 'flex',
			justifyContent: 'space-between',
		},
		storeList: {
			// width: '40%',
			marginLeft: 'auto',
			marginRight: 'auto',
			padding: '1.5rem',
		},
		storeListItem: {
			dispaly: 'flex',
			flexDirection: 'column',
		},
		search: {
			position: 'relative',
			borderRadius: theme.shape.borderRadius,
			backgroundColor: fade(theme.palette.common.white, 0.15),
			'&:hover': {
				backgroundColor: fade(theme.palette.common.white, 0.25),
			},
			marginRight: theme.spacing(2),
			marginLeft: 0,
			width: '100%',
			[theme.breakpoints.up('sm')]: {
				marginLeft: theme.spacing(3),
				width: 'auto',
			},
		},
		searchIcon: {
			padding: theme.spacing(0, 2),
			height: '100%',
			position: 'absolute',
			pointerEvents: 'none',
			display: 'flex',
			alignItems: 'center',
			justifyContent: 'center',
		},
		inputRoot: {
			color: 'inherit',
		},
		inputInput: {
			padding: theme.spacing(1, 1, 1, 0),
			// vertical padding + font size from searchIcon
			paddingLeft: `calc(1em + ${theme.spacing(4)}px)`,
			transition: theme.transitions.create('width'),
			width: '100%',
			[theme.breakpoints.up('md')]: {
				width: '20ch',
			},
		},
	})
);

function AdminManagementPage() {
	const [restaurantOwnerRegisterInformation, setRestaurantOnwerRegisterInformation] = useState<RestaurantOwnerRegister>(
		{ registerNumber: '', password: '' }
	);
	const [password, setPassword] = useState('');
	const classes = useStyles();
	const [open, setOpen] = useState<boolean[]>([]);
	const [removeItemIndex, setRemoveItemIndex] = useState(-1);
	const [notConfirmedItemIndex, setNotconfirmedItemIndex] = useState(-1);

	const [searchText, setSearchText] = useState('');
	const match = useRouteMatch();
	const { userStatus } = useContext(UserContext);
	const history = useHistory();
	const [confirmedRestaurantList, setConfirmedRestaurantList] = useState<Restaurant[]>([]);
	const [notConfirmedRestaurantList, setNotConfirmedRestaurantList] = useState<Restaurant[]>([]);
	const [restaurantList, setRestaurantList] = useState<Restaurant[]>([]);

	const url = `${SERVER_URL}/restaurant`;
	useEffect(() => {
		axios({
			method: 'get',
			url,
		}).then((res) => {
			console.log(res.data[0]);
			setOpen([...open, false]);
			setConfirmedRestaurantList(
				res.data.filter((e: any) => {
					return e.confirmed;
				})
			);
			setNotConfirmedRestaurantList(
				res.data.filter((e: any) => {
					return !e.confirmed;
				})
			);
			setRestaurantList(res.data);
		});
	}, [url]);

	const createRestaurantOwnerAccount = (event: React.FormEvent) => {
		event?.preventDefault();
		const createdPassword = Math.random().toString(36).slice(-8);
		setPassword(createdPassword);
		axios({
			method: 'post',
			url: `${SERVER_URL}/auth/register`,
			data: {
				username: restaurantOwnerRegisterInformation.registerNumber,
				password: createdPassword,
				role: 'restaurantOwner',
			},
		}).then((res) => console.log(createdPassword));
	};

	const filteredRestaurantList = (type: string, days: number) => {
		return (
			<div>
				<Paper className={classes.storeList}>
					<p>{days > 0 ? `계약 ${days}일 전` : '계약 만료'}</p>
					<List>
						{confirmedRestaurantList.length > 0 &&
							confirmedRestaurantList
								.filter((e) => {
									return type === '+'
										? moment(e.contractExpiredAt).isBefore(moment(new Date()).add(days, 'days')) &&
												moment(e.contractExpiredAt).isAfter(moment(new Date()))
										: moment(e.contractExpiredAt).isBefore(moment(new Date()));
								})
								.map((e, index) => {
									const storeName = `가게 이름: ${e.name}`;
									const storeCategory = `카테고리: ${e.category}`;
									const storeContractExpirteDate = `계약만료일: ${moment(e.contractExpiredAt).format('YYYY/MM/DD')}`;
									return (
										<div key={index} className={classes.storeListElement}>
											<div>
												<ListItem className={classes.storeListItem}>
													<ListItemText primary={storeName} />
													<ListItemText primary={storeCategory} />
													{e.contractExpiredAt && <ListItemText primary={storeContractExpirteDate} />}
												</ListItem>
											</div>
											{days === 0 && (
												<IconButton
													aria-label="delete"
													onClick={() => {
														setRemoveItemIndex(index);
													}}
												>
													<DeleteIcon />
												</IconButton>
											)}
											<Modal
												open={removeItemIndex === index}
												onClose={() => {
													setRemoveItemIndex(-1);
												}}
											>
												<DeleteRestaurantModal
													index={index}
													restaurantList={restaurantList}
													setRestaurnatList={setRestaurantList}
													setRemoveItemIndex={setRemoveItemIndex}
												/>
											</Modal>
											<Divider />
										</div>
									);
								})}
					</List>
				</Paper>
			</div>
		);
	};
	const allRestaurantList = () => {
		return (
			<Paper className={classes.storeList}>
				<p>승인 식당 목록</p>
				<div className={classes.search}>
					<div className={classes.searchIcon}>
						<SearchIcon />
					</div>
					<InputBase
						placeholder="Search…"
						value={searchText}
						classes={{
							root: classes.inputRoot,
							input: classes.inputInput,
						}}
						inputProps={{ 'aria-label': 'search' }}
						onChange={(e) => {
							setSearchText(e.target.value);
						}}
					/>
				</div>
				<List>
					{confirmedRestaurantList.length > 0 &&
						confirmedRestaurantList
							.filter((e) => {
								return e.name.includes(searchText);
							})
							.map((e, index) => {
								const storeName = `가게 이름: ${e.name}`;
								const storeCategory = `카테고리: ${e.category}`;
								const storeContractExpirteDate = `계약만료일: ${moment(e.contractExpiredAt).format('YYYY/MM/DD')}`;
								return (
									<div>
										<div key={index} className={classes.storeListElement}>
											<div>
												<ListItem className={classes.storeListItem}>
													<ListItemText primary={storeName} />
													<ListItemText primary={storeCategory} />
													{e.contractExpiredAt && <ListItemText primary={storeContractExpirteDate} />}
													<Modal
														open={open[index]}
														onClose={() => {
															const newOpenState = [...open];
															newOpenState[index] = false;
															setOpen(newOpenState);
														}}
													>
														<RestaurantInformationModificationModal
															index={index}
															restaurantList={restaurantList}
															setRestaurnatList={setRestaurantList}
															open={open}
															setOpen={setOpen}
														/>
													</Modal>
												</ListItem>
											</div>
											<Button
												variant="contained"
												color="primary"
												style={{ maxHeight: '2rem', minHeight: '2rem' }}
												onClick={() => {
													const newOpenState = [...open];
													newOpenState[index] = true;
													setOpen(newOpenState);
												}}
											>
												수정하기
											</Button>
										</div>
										<Divider />
									</div>
								);
							})}
				</List>
			</Paper>
		);
	};

	const showNotConfirmedRestaurantList = () => {
		return (
			<Paper className={classes.storeList}>
				<p>승인 전 식당 목록</p>
				<List>
					{notConfirmedRestaurantList.length > 0 &&
						notConfirmedRestaurantList.map((e, index) => {
							const storeName = `가게 이름: ${e.name}`;
							const storeCategory = `카테고리: ${e.category}`;
							const storeContractExpirteDate = `계약만료일: ${moment(e.contractExpiredAt).format('YYYY/MM/DD')}`;
							return (
								<div>
									<div key={index} className={classes.storeListElement}>
										<div>
											<ListItem className={classes.storeListItem}>
												<ListItemText primary={storeName} />
												<ListItemText primary={storeCategory} />
												{e.contractExpiredAt && <ListItemText primary={storeContractExpirteDate} />}
												<Modal
													open={open[index]}
													onClose={() => {
														const newOpenState = [...open];
														newOpenState[index] = false;
														setOpen(newOpenState);
													}}
												>
													<RestaurantInformationModificationModal
														index={index}
														restaurantList={restaurantList}
														setRestaurnatList={setRestaurantList}
														open={open}
														setOpen={setOpen}
													/>
												</Modal>
											</ListItem>
										</div>
										<Button
											variant="contained"
											color="primary"
											style={{ maxHeight: '2rem', minHeight: '2rem' }}
											onClick={() => {
												setNotconfirmedItemIndex(index);
												history.push(`${match.path}/confirmRestaurant/${e._id}`);
											}}
										>
											승인하기
										</Button>
									</div>
									<Divider />
								</div>
							);
						})}
				</List>
			</Paper>
		);
	};
	return (
		<div className={classes.rootDiv}>
			<div className={classes.section}>
				<form onSubmit={createRestaurantOwnerAccount}>
					<div>
						<Paper className={classes.createAccount}>
							<TextField
								label="사업자등록번호"
								placeholder="사업자등록번호를 입력하시오"
								value={restaurantOwnerRegisterInformation.registerNumber}
								onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
									setRestaurantOnwerRegisterInformation({
										...restaurantOwnerRegisterInformation,
										registerNumber: event.target.value,
									});
								}}
							/>
							<TextField disabled label="비밀번호" placeholder="생성된 비밀번호" value={password} />
							<Button type="submit">Create New Restaurant Owner's Account</Button>
						</Paper>
					</div>
				</form>
			</div>
			<div className={classes.section}>{filteredRestaurantList('+', 15)}</div>
			<div className={classes.section}>{filteredRestaurantList('-', 0)}</div>
			<div className={classes.section}>{allRestaurantList()}</div>
			<div className={classes.section}>{showNotConfirmedRestaurantList()}</div>

			{/* <Route exact path={`${match.path}/confirmPage`}>
				<ConfirmRestaurantPage
					index={notConfirmedItemIndex}
					confirmedRestaurantList={confirmedRestaurantList}
					setConfirmedRestaurantList={setConfirmedRestaurantList}
					notConfirmedRestaurantList={notConfirmedRestaurantList}
					setNotConfirmedRestaurantList={setNotConfirmedRestaurantList}
				/>
			</Route> */}
		</div>
	);
}

export default AdminManagementPage;
