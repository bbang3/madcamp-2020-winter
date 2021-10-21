import { Button, createStyles, makeStyles, Paper, Theme } from '@material-ui/core';
import axios from 'axios';
import React from 'react';
import { SERVER_URL } from '../config';
import { Restaurant } from '../types/RestaurantTypes';

const useStyles = makeStyles((theme: Theme) =>
	createStyles({
		rootDiv: {
			width: 400,
			backgroundColor: theme.palette.background.paper,
			border: '2px solid #000',
			boxShadow: theme.shadows[5],
			padding: theme.spacing(2, 4, 3),
		},
	})
);
interface Props {
	index: number;
	restaurantList: Restaurant[];
	setRestaurnatList: (restaurnantList: Restaurant[]) => void;
	setRemoveItemIndex: (open: number) => void;
}

function DeleteRestaurantModal(props: Props) {
	const classes = useStyles();
	const url = `${SERVER_URL}/restaurant/${props.restaurantList[props.index]._id}`;
	const deleteButtonHandler = () => {
		axios({
			method: 'delete',
			url,
		})
			.then((res) => {
				const modifiedRestaurantList = [...props.restaurantList];
				modifiedRestaurantList.splice(props.index, 1);
				props.setRestaurnatList(modifiedRestaurantList);
				closeModal();
			})
			.catch((err) => {
				console.log(err);
			});
	};

	const closeModal = () => {
		props.setRemoveItemIndex(-1);
	};

	const cancleButtonHandler = () => {
		closeModal();
	};

	return (
		<Paper className={classes.rootDiv}>
			<p>{`식당 ${props.restaurantList[props.index].name}을 목록에서 삭제하시겠습니까?`}</p>
			<Button onClick={deleteButtonHandler}>삭제</Button>
			<Button onClick={cancleButtonHandler}>취소</Button>
		</Paper>
	);
}

export default DeleteRestaurantModal;
