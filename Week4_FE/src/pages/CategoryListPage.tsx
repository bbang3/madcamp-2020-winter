import { Box, Button, Grid, Paper } from '@material-ui/core';
import { createStyles, fade, makeStyles, Theme } from '@material-ui/core/styles';
import React, { useContext, useEffect, useState } from 'react';
import axios, { AxiosResponse } from 'axios';
import { SERVER_URL } from '../config';
import UserContext from '../contexts/UserContext';
import { useHistory } from 'react-router-dom';
import { CategoryResponseType } from '../types/ResponseTypes';
import RestaurantListPage from './RestaurantListPage';
import SearchBarContext from '../contexts/SearchBarContext';

const defaultProps = {
	style: { width: '6rem', height: '4rem' },
};
const useStyles = makeStyles((theme: Theme) =>
	createStyles({
		root: {
			flex: 1,
			flexDirection: 'row',
			justifyContent: 'space-between',
			marginLeft: '20%',
			marginRight: '20%',
		},
	})
);

function CategoryListPage() {
	const url = `${SERVER_URL}/category`;
	const classes = useStyles();
	const history = useHistory();
	const [categoryId, setCategoryId] = useState<string>('');
	const { userStatus } = useContext(UserContext);
	const { searchText } = useContext(SearchBarContext);
	const [categoryList, setCategoryList] = useState<CategoryResponseType[]>([]);
	useEffect(() => {
		axios({ method: 'get', url }).then((res: AxiosResponse<CategoryResponseType[]>) => {
			const resultArray: CategoryResponseType[] = [];
			res.data.forEach((e: any) => {
				resultArray.push(e);
			});
			setCategoryList(resultArray);
		});
	}, [url]);

	return (
		<div>
			<Paper>
				<div className={classes.root}>
					<Button
						{...defaultProps}
						onClick={() => {
							setCategoryId('');
						}}
					>
						<Box m="auto">{`전체`}</Box>
					</Button>

					{categoryList.map((e, index) => {
						return (
							<Button
								{...defaultProps}
								onClick={() => {
									setCategoryId(e._id);
								}}
							>
								<Box m="auto">{e.name}</Box>
							</Button>
						);
					})}
				</div>
			</Paper>
			<RestaurantListPage categoryId={categoryId} searchText={searchText} />
		</div>
	);
}

export default CategoryListPage;
