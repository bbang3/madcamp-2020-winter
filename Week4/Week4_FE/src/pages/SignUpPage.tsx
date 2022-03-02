import React, { useState } from 'react';
import { Button, Grid, Paper, TextField } from '@material-ui/core';
import { SignUpStatus } from '../types/AuthTypes';
import axios from 'axios';
import { SERVER_URL } from '../config';
import { createStyles, makeStyles, Theme } from '@material-ui/core/styles';

const useStyles = makeStyles((theme: Theme) =>
	createStyles({
		signUpContainer: {
			display: 'flex',
			flexDirection: 'column',
			paddingTop: '0.5rem',
		},
		signUpInput: {
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

function SignUpPage() {
	const classes = useStyles();
	const [signUpStatus, setSignUpStatus] = useState<SignUpStatus>({
		username: '',
		password: '',
		nickname: '',
	});

	const submitHandler = () => {
		axios({
			method: 'post',
			url: `${SERVER_URL}/auth/register`,
			data: {
				...signUpStatus,
				role: 'user',
			},
		});
	};
	return (
		<Grid container justify="center" className={classes.root}>
			<Paper className={classes.paper}>
				<div style={{ fontSize: '3rem' }}>회원가입</div>
				<form onSubmit={submitHandler}>
					<TextField
						className={classes.signUpInput}
						label="아이디"
						placeholder="아이디를 입력하시오"
						value={signUpStatus.username}
						onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
							setSignUpStatus({ ...signUpStatus, username: event.target.value });
						}}
					/>
					<TextField
						className={classes.signUpInput}
						label="비밀번호"
						placeholder="비밀번호를 입력하시오"
						value={signUpStatus.password}
						onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
							setSignUpStatus({ ...signUpStatus, username: event.target.value });
						}}
					/>
					<TextField
						className={classes.signUpInput}
						label="별명"
						placeholder="별명을 입력하시오"
						value={signUpStatus.nickname}
						onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
							setSignUpStatus({ ...signUpStatus, username: event.target.value });
						}}
					/>
					<Button type="submit" style={{ width: '100%', fontSize: '1.1rem' }}>
						회원가입
					</Button>
				</form>
			</Paper>
		</Grid>
	);
}

export default SignUpPage;
