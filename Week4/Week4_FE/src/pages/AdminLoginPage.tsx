import React, { useState, useContext } from 'react';
import { Button, TextField } from '@material-ui/core';
import axios, { AxiosResponse } from 'axios';
import { LoginInput } from '../types/AuthTypes';
import { LoginResponseType } from '../types/ResponseTypes';
import UserContext from '../contexts/UserContext';
import { SERVER_URL } from '../config';
import useAuthStatus from '../utils/Cookie';

function AdminLoginpage() {
	const [loginInput, setLoginInput] = useState<LoginInput>({ username: '', password: '' });
	const { setUserStatus } = useContext(UserContext);
	const [_, setAuthStatus, removeAuthStatus] = useAuthStatus();

	const submitHandler = (event: React.FormEvent) => {
		event?.preventDefault();
		axios({
			method: 'post',
			url: `${SERVER_URL}/auth/login`,
			data: {
				username: loginInput.username,
				password: loginInput.password,
			},
		}).then((res) => {
			if (res.data.role === 'admin') {
				setAuthStatus(res.data);
				setUserStatus({ ...res.data });
			}
		});
	};

	return (
		<form onSubmit={submitHandler}>
			<div>
				<TextField
					label="username"
					value={loginInput.username}
					onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
						setLoginInput({ ...loginInput, username: event.target.value });
					}}
				/>
			</div>
			<div>
				<TextField
					type="password"
					label="password"
					value={loginInput.password}
					onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
						setLoginInput({ ...loginInput, password: event.target.value });
					}}
				/>
			</div>
			<Button type="submit">Login</Button>
		</form>
	);
}

export default AdminLoginpage;
