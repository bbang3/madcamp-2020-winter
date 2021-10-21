import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { createStyles, fade, makeStyles, Theme } from '@material-ui/core/styles';
import { SERVER_URL } from '../config';
import { RouteComponentProps, useHistory } from 'react-router-dom';
import { RestaurantResponseType } from '../types/ResponseTypes';
import { Button } from '@material-ui/core';
import restaurantDefaultImage from '../images/restaurantDefaultImage.png';
import { Restaurant } from '../types/RestaurantTypes';
import { time } from 'console';

interface MatchParams {
	categoryId: string;
}

interface Props {
	categoryId: string;
	searchText: string;
}

const useStyles = makeStyles((theme: Theme) =>
	createStyles({
		root: {
			display: 'flex',
			flexWrap: 'wrap',
			marginLeft: '10%',
			marginRight: '10%',
			flex: 1,
			flexDirection: 'row',
		},
		restaurant: {
			display: 'flex',
			flexDirection: 'column',
			width: '15rem',
			height: '18rem',
			position: 'relative',
			// alitnItems: 'start',
			// width: '100%',
			// height: '100%',
			// alignItems: 'center',
		},
		image: {
			position: 'relative',
			objectFit: 'cover',
			width: 'inherit',
			height: '60%',
		},
		contianter: {
			margin: '1.5rem',
			width: '15rem',
			height: '18rem',
		},
		name: {
			paddingTop: '0.4rem',
			fontSize: '1.3rem',
			fontWeight: 'bold',
		},
		description: {
			height: '4rem',
			paddingLeft: '0.4rem',
			paddingRight: '0.4rem',
		},
		time: {
			paddingBottom: '0.4rem',
		},
	})
);

function RestaurantListPage(props: Props) {
	const classes = useStyles();
	const curr = new Date();
	const today = curr.getDay();
	const url = `${SERVER_URL}/restaurant?category=${encodeURI(props.categoryId)}`;
	const allRestaurantUrl = `${SERVER_URL}/restaurant`;
	const [restaurantList, setRestaurantList] = useState<Restaurant[]>([]);
	const history = useHistory();
	useEffect(() => {
		props.categoryId.length === 0
			? axios({
					method: 'get',
					url: allRestaurantUrl,
			  }).then((res) => {
					const resultRestaurantList: Restaurant[] = [];
					res.data.forEach((e: any) => {
						resultRestaurantList.push(e);
					});
					setRestaurantList(resultRestaurantList);
			  })
			: axios({
					method: 'get',
					url,
			  }).then((res) => {
					const resultRestaurantList: Restaurant[] = [];
					res.data.forEach((e: any) => {
						resultRestaurantList.push(e);
					});
					setRestaurantList(resultRestaurantList);
			  });
	}, [url]);
	return (
		<div className={classes.root}>
			{restaurantList
				.filter((e) => {
					return e.confirmed && e.name.includes(props.searchText);
				})
				.map((e, index) => {
					const openTime = e.openingHours[today].openTime.split(':');
					const closeTime = e.openingHours[today].openTime.split(':');
					return (
						<div key={index}>
							<Button
								className={classes.contianter}
								variant="outlined"
								onClick={() => {
									history.push(`/restaurant/${e._id}`);
								}}
							>
								<div className={classes.restaurant}>
									{e.image ? (
										<img
											className={classes.image}
											src={`${SERVER_URL}/image/restaurants/${e.image}`}
											alt="Restaurant"
										/>
									) : (
										<img className={classes.image} src={restaurantDefaultImage} alt="Restaurant" />
									)}
									<div className={classes.name}>{e.name}</div>
									<div className={classes.description}>{e.description}</div>
									{e.openingHours[today] && (
										<div className={classes.time}>
											<div>{`영업시간: ${openTime[0]}:${openTime[1]} - ${closeTime[0]}:${closeTime[1]}`}</div>
										</div>
									)}
								</div>
							</Button>
						</div>
					);
				})}
		</div>
	);
}

export default RestaurantListPage;
