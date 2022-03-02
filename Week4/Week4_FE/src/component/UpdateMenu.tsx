import { Box, Button, Container, createStyles, Grid, makeStyles, Paper, TextField, Theme } from '@material-ui/core';
import axios from 'axios';
import React, { useContext, useEffect, useState } from 'react';
import { useHistory, useRouteMatch } from 'react-router-dom';
import { IMAGE_BASE_URL, SERVER_URL } from '../config';
import UserContext from '../contexts/UserContext';
import { Menu } from '../types/RestaurantTypes';
import menuDefaultImage from '../images/menuDefaultImage.png';

interface UpdateMenuParam {
	menu_id: string;
}

const useStyles = makeStyles((theme: Theme) =>
	createStyles({
		root: {
			display: 'flex',
			alignItems: 'center',
			flexWrap: 'wrap',
			flex: 1,
			flexDirection: 'column',
		},
		container: {
			padding: '10px',
			height: '100%',
			display: 'flex',
			flexDirection: 'column',
			alignItems: 'center',
		},
		image: {
			height: '50vh',
			width: 'auto',
		},
	})
);

function UpdateMenu() {
	const classes = useStyles();
	const match = useRouteMatch<UpdateMenuParam>();
	const history = useHistory();
	const { userStatus } = useContext(UserContext);
	const requestUrl = `${SERVER_URL}/restaurant/${userStatus.restaurantId}/menu/${match.params.menu_id}`;

	const [menu, setMenu] = useState<Menu>({
		_id: '',
		name: '',
		description: '',
		sizes: [],
		image: '',
	});
	const [image, setImage] = useState<File | null>(null);

	useEffect(() => {
		const getMenu = async () => {
			try {
				const response = await axios.get(requestUrl);

				if (response.status === 200) {
					console.log(response.data);
					setMenu(response.data);
				}
			} catch (error) {
				console.log(error);
				alert('메뉴 정보를 가져오는 중에 문제가 발생했습니다.');
				history.goBack();
			}
		};
		getMenu();
	}, [history, requestUrl]);

	const handleSubmit = async (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
		const form = new FormData();
		if (image) form.append('image', image);

		form.append('menu', JSON.stringify(menu));

		try {
			const response = await axios.put(requestUrl, form, {
				headers: { 'Content-Type': 'multipart/form-data', token: userStatus.accessToken },
			});
			console.log(response);
			if (response.status === 200) {
				history.goBack();
			} else {
			}
		} catch (error) {
			console.log(error);
		}
	};

	const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		setMenu({
			...menu,
			[e.target.name]: e.target.value,
		});
		console.log(e);
	};
	const handleSizeChange = (e: React.ChangeEvent<HTMLInputElement>, index: number) => {
		const tempSizes = [...menu.sizes];
		tempSizes[index] = {
			...tempSizes[index],
			[e.target.name]: e.target.value,
		};

		setMenu({
			...menu,
			sizes: tempSizes,
		});
	};

	const addSize = () => {
		const newSizes = menu.sizes;
		newSizes.push({
			size: '',
			price: 0,
		});
		setMenu({ ...menu, sizes: newSizes });
	};

	const removeSize = (index: number) => {
		const newSizes = menu.sizes.filter((element, i) => i !== index);
		setMenu({
			...menu,
			sizes: newSizes,
		});
	};

	return (
		<Container className={classes.root}>
			<Paper className={classes.container}>
				<Grid container direction="column" spacing={2}>
					<Grid item>
						<Button component="label">
							<div>
								{console.log(`${IMAGE_BASE_URL}/menus/${menu.image}`)}
								<img
									className={classes.image}
									src={image ? URL.createObjectURL(image) : `${IMAGE_BASE_URL}/menus/${menu.image}`}
									alt="메뉴 이미지"
								/>
							</div>
							<input
								type="file"
								onChange={(e) => {
									console.log(e.target.files);
									setImage(e.target.files && e.target.files[0]);
								}}
								hidden
							/>
						</Button>
						<div style={{ textAlign: 'center' }}> 메뉴 이미지 </div>
					</Grid>
					<Grid item>
						<TextField name="name" value={menu.name} label="메뉴명" onChange={handleChange} />
						<TextField name="description" value={menu.description} label="메뉴설명" onChange={handleChange} />
					</Grid>
					<Grid item>
						{menu.sizes.map((element, index) => {
							console.log(element);
							return (
								<div key={index}>
									<TextField
										name="size"
										value={element.size}
										label={`사이즈명 ${index + 1}`}
										onChange={(e: React.ChangeEvent<HTMLInputElement>) => handleSizeChange(e, index)}
									/>
									<TextField
										name="price"
										value={element.price}
										type="number"
										label={`가격 ${index + 1}`}
										onChange={(e: React.ChangeEvent<HTMLInputElement>) => handleSizeChange(e, index)}
									/>
									<Button onClick={() => removeSize(index)}>삭제</Button>
								</div>
							);
						})}
					</Grid>
					<Button onClick={addSize}> 사이즈 추가하기 </Button>
					<Box style={{ float: 'right' }}>
						<Button variant="outlined" onClick={handleSubmit}>
							확인
						</Button>
						<Button variant="outlined" onClick={(e) => history.goBack()}>
							취소
						</Button>
					</Box>
				</Grid>
			</Paper>
		</Container>
	);
}

export default UpdateMenu;
