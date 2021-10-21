import { Box, Button, createStyles, Grid, makeStyles, Paper, TextField } from '@material-ui/core';
import axios from 'axios';
import { useContext, useState } from 'react';
import { useHistory } from 'react-router-dom';
import { SERVER_URL } from '../config';
import UserContext from '../contexts/UserContext';
import { Menu } from '../types/RestaurantTypes';
import menuDefaultImage from '../images/menuDefaultImage.png';

const useStyles = makeStyles(() =>
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

function AddMenu() {
	const classes = useStyles();
	const history = useHistory();
	const { userStatus } = useContext(UserContext);
	const [menu, setMenu] = useState<Menu>({
		name: '',
		description: '',
		sizes: [
			{
				size: '',
				price: 0,
			},
		],
	});
	const [image, setImage] = useState<File | null>(null);

	const handleSubmit = async () => {
		if (!image) return;
		const form = new FormData();
		form.append('image', image);
		form.append('menu', JSON.stringify(menu));

		try {
			const response = await axios.post(`${SERVER_URL}/restaurant/${userStatus.restaurantId}/menu`, form, {
				headers: { 'Content-Type': 'multipart/form-data', token: userStatus.accessToken },
			});
			console.log(response);
			if (response.status === 200) {
				history.push('/RestaurantManagement');
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
		<div className={classes.root}>
			<Paper className={classes.container}>
				<form>
					<Grid container spacing={2} direction="column">
						<Grid item>
							<Button component="label">
								<div>
									<img
										src={image ? URL.createObjectURL(image) : menuDefaultImage}
										alt="메뉴 이미지"
										style={{ maxWidth: '25vw', maxHeight: '50vh' }}
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
							<div> 메뉴 이미지 </div>
						</Grid>
						<Grid item>
							<TextField name="name" value={menu.name} label="메뉴명" onChange={handleChange} />
						</Grid>
						<Grid item>
							<TextField
								name="description"
								fullWidth
								value={menu.description}
								label="메뉴설명"
								onChange={handleChange}
							/>
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
					</Grid>
					<Button onClick={addSize}> 사이즈 추가하기 </Button>
				</form>
				<Box>
					<Button onClick={handleSubmit}> 확인 </Button>
					<Button onClick={(e) => history.goBack()}> 취소 </Button>
				</Box>
			</Paper>
		</div>
	);
}

export default AddMenu;
