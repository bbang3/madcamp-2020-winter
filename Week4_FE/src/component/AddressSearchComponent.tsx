import React, { useState } from 'react';
import { Button, Card, CardContent, CardHeader, Modal, TextField } from '@material-ui/core';
import DaumPostCode from 'react-daum-postcode';
import { Restaurant } from '../types/RestaurantTypes';
import RestaurantInformationInputPage from '../pages/RestaurantInformationInputPage';

interface Props {
	restaurantInformation: Restaurant;
	setRestaurantInformation: (restaurantInformation: Restaurant) => void;
}

function AddressSearch(props: Props) {
	const [open, setOpen] = useState(false);
	const [fullAddress, setFullAddress] = useState('');
	const [zoneCode, setZoneCode] = useState('');
	const [leftAddress, setLeftAddress] = useState('');
	// DaumPostCode style
	const width = 500;
	const height = 772;
	const modalStyle = {
		position: 'absolute',
		top: '10%',
		left: '35%',
		zIndex: '100',
		border: '1px solid #000000',
		overflow: 'hidden',
	};

	const onCompleteHandler = (data: any) => {
		let fullAddress = data.address;
		let extraAddress = '';

		if (data.addressType === 'R') {
			if (data.bname !== '') {
				extraAddress += data.bname;
			}
			if (data.buildingName !== '') {
				extraAddress += extraAddress !== '' ? `, ${data.buildingName}` : data.buildingName;
			}
			fullAddress += extraAddress !== '' ? ` (${extraAddress})` : '';
		}
		setZoneCode(data.zonecode);
		setFullAddress(fullAddress);
		props.setRestaurantInformation({
			...props.restaurantInformation,
			location: {
				...props.restaurantInformation.location,
				fullAddress,
			},
		});
		setOpen(false);
	};

	return (
		<Card>
			<CardContent>
				주소
				<Button
					variant="outlined"
					onClick={() => {
						setOpen(true);
					}}
					style={{ marginLeft: '10px' }}
				>
					주소 찾기
				</Button>
			</CardContent>
			<CardContent>
				<Modal open={open} onClose={() => setOpen(false)}>
					<DaumPostCode onComplete={onCompleteHandler} autoClose width={width} height={height} style={modalStyle} />
				</Modal>
				<div style={{ marginBottom: '10px', fontSize: '1.8em' }}>
					{fullAddress.length > 0 ? fullAddress : props.restaurantInformation.location.fullAddress}
				</div>
				<TextField
					type="text"
					label="상세주소"
					value={props.restaurantInformation.location.extraAddress}
					name="address"
					onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
						props.setRestaurantInformation({
							...props.restaurantInformation,
							location: {
								...props.restaurantInformation.location,
								extraAddress: event.target.value,
							},
						});
					}}
				/>
			</CardContent>
		</Card>
	);
}

export default AddressSearch;
