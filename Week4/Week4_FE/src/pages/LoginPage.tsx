import React, { useState, useContext } from 'react';
import { Button, Grid, Paper, TextField } from '@material-ui/core';
import { LoginInput } from '../types/AuthTypes';
import axios from 'axios';
import UserContext from '../contexts/UserContext';
import { Link, useHistory } from 'react-router-dom';
import { SERVER_URL } from '../config';
import useAuthStatus from '../utils/Cookie';
import { createStyles, makeStyles, Theme } from '@material-ui/core/styles';

const useStyles = makeStyles((theme: Theme) =>
	createStyles({
		loginContainer: {
			display: 'flex',
			flexDirection: 'column',
			paddingTop: '0.5rem',
		},
		loginInput: {
			width: '100%',
		},
		root: {
			marginTop: '10%',
		},
		paper: {
			padding: '2rem',
			width: '30%',
		},
	})
);

function LoginPage() {
	const classes = useStyles();
	const history = useHistory();
	const errorMessage = '가입하지 않은 아이디이거나, 잘못된 비밀번호입니다.';
	const { setUserStatus } = useContext(UserContext);
	const [loginInput, setLoginInput] = useState<LoginInput>({
		username: '',
		password: '',
	});
	const [_, setAuthStatus, removeAuthStatus] = useAuthStatus();
	const submitHandler = (event: React.FormEvent) => {
		event.preventDefault();
		axios({
			method: 'post',
			url: `${SERVER_URL}/auth/login`,
			data: {
				username: loginInput.username,
				password: loginInput.password,
			},
			withCredentials: true,
		})
			.then((res) => {
				console.log(res);
				if (res.data.role === 'user' || res.data.role === 'restaurantOwner') {
					setAuthStatus(res.data);
					setUserStatus({ ...res.data });
					history.push('/');
				}
			})
			.catch((error) => {
				console.log(error.response.data.message);
			});
	};
	return (
		<Grid container justify="center" className={classes.root}>
			<Paper className={classes.paper}>
				<div style={{ fontSize: '3rem' }}>로그인</div>
				<div className={classes.loginContainer}>
					<form onSubmit={submitHandler}>
						<div className={classes.loginContainer}>
							<TextField
								className={classes.loginInput}
								label="username"
								value={loginInput.username}
								onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
									setLoginInput({ ...loginInput, username: event.target.value });
								}}
							/>
							<TextField
								className={classes.loginInput}
								label="password"
								type="password"
								value={loginInput.password}
								onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
									setLoginInput({ ...loginInput, password: event.target.value });
								}}
							/>
							<Button type="submit" style={{ fontSize: '1.1rem', fontWeight: 'bold' }}>
								로그인
							</Button>
						</div>
					</form>
					<div className={classes.loginContainer}>
						<div style={{ fontSize: '1.2rem', textAlign: 'center' }}>아직 회원가입하지 않으셨나요?</div>
						<Link to="/signup" style={{ textDecoration: 'none' }}>
							<Button style={{ width: '100%', fontSize: '1rem' }}>회원가입</Button>
						</Link>
					</div>
				</div>
			</Paper>
		</Grid>
	);
}

export default LoginPage;
