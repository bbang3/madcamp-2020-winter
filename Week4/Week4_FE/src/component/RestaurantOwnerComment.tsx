import {
	Button,
	Container,
	Dialog,
	DialogActions,
	DialogContent,
	DialogContentText,
	DialogTitle,
	Grid,
	IconButton,
	List,
	ListItem,
	ListItemText,
	Paper,
	TextField,
} from '@material-ui/core';
import { Create, Send } from '@material-ui/icons';
import DeleteIcon from '@material-ui/icons/Delete';
import UpdateIcon from '@material-ui/icons/Update';
import axios from 'axios';
import moment from 'moment';
import React, { useContext, useState } from 'react';
import { RouteComponentProps } from 'react-router-dom';
import { SERVER_URL } from '../config';
import UserContext from '../contexts/UserContext';
import { Comment } from '../types/RestaurantTypes';

interface Props {
	restaurantId: string;
	restaurantName: string;
	comments: Comment[];
	updateComments: (comments: Comment[]) => void;
}

function RestaurantOwnerComment({ restaurantId, restaurantName, comments, updateComments }: Props) {
	const { userStatus } = useContext(UserContext);

	const [newComment, setNewComment] = useState('');
	const [updateComment, setUpdateComment] = useState('');
	const [openDialogIndex, setOpenDialogIndex] = useState(-1);
	const [updateCommentIndex, setUpdateCommentIndex] = useState(-1);

	const commentBaseUrl: string = `${SERVER_URL}/restaurant/${restaurantId}/comment`;

	const addComment = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
		const postCommentUrl = commentBaseUrl;
		axios({
			method: 'post',
			url: postCommentUrl,
			data: { body: newComment },
			headers: { token: userStatus.accessToken },
		}).then((res) => {
			setNewComment('');
			axios({
				method: 'get',
				url: commentBaseUrl,
			}).then((res) => updateComments(res.data));
		});
	};

	const modifyComment = (commentId: string) => {
		const updateCommentUrl = `${commentBaseUrl}/${commentId}`;
		axios({
			method: 'put',
			url: updateCommentUrl,
			data: { body: updateComment },
			headers: { token: userStatus.accessToken },
		}).then(() => {
			setUpdateCommentIndex(-1);
			setUpdateComment('');
			axios({
				method: 'get',
				url: commentBaseUrl,
			}).then((res) => updateComments(res.data));
		});
	};

	const deleteComment = () => {
		if (!comments) return;

		const deleteCommentUrl = `${commentBaseUrl}/${comments[openDialogIndex]._id}`;
		axios({
			method: 'delete',
			url: deleteCommentUrl,
			headers: {
				token: userStatus.accessToken,
			},
		})
			.then((res) => {
				axios({
					method: 'get',
					url: commentBaseUrl,
				}).then((res) => updateComments(res.data));
				setOpenDialogIndex(-1);
			})
			.catch((err) => {
				console.error(err);
			});
	};

	return (
		<div>
			<Paper>
				<p>댓글</p>
				<List>
					{comments?.map((comment, index) => {
						return (
							<div>
								<ListItem>
									<ListItemText primary={comment.nickname} />
									<ListItemText primary={comment.body} />
									<ListItemText primary={moment(comment.date).format('YYYY-MM-DD-hh-mm-ss')} />
									{comment.nickname === restaurantName && (
										<div>
											<IconButton
												aria-label="update"
												onClick={() => {
													setUpdateComment(comment.body);
													setUpdateCommentIndex(index);
												}}
											>
												<Create fontSize="small" />
											</IconButton>
											<IconButton
												aria-label="delete"
												onClick={() => {
													setOpenDialogIndex(index);
												}}
											>
												<DeleteIcon fontSize="small" />
											</IconButton>
										</div>
									)}
								</ListItem>
								{updateCommentIndex === index && (
									<div>
										<p>댓글 수정하기</p>
										<TextField
											value={updateComment}
											onChange={(e) => {
												setUpdateComment(e.target.value);
											}}
										/>
										<Button onClick={() => modifyComment(comment._id)}>수정</Button>
										<Button
											onClick={() => {
												setUpdateCommentIndex(-1);
												setUpdateComment('');
											}}
										>
											취소
										</Button>
									</div>
								)}
							</div>
						);
					})}
				</List>
				<Container style={{ width: '100%', border: '1px solid black', alignItems: 'center', borderRadius: '5px' }}>
					{userStatus.role && (
						<Grid container alignItems="center" spacing={2}>
							<Grid item>댓글 입력:</Grid>
							<Grid item>
								<TextField
									style={{ width: '100%' }}
									value={newComment}
									onChange={(e) => {
										setNewComment(e.target.value);
									}}
								/>
							</Grid>
							<Grid item>
								<IconButton onClick={addComment}>
									<Send color="primary" />
								</IconButton>
							</Grid>
						</Grid>
					)}
				</Container>
			</Paper>
			<Dialog
				open={openDialogIndex > -1}
				keepMounted
				onClose={() => {
					setOpenDialogIndex(-1);
				}}
				aria-labelledby="alert-dialog-slide-title"
				aria-describedby="alert-dialog-slide-description"
			>
				<DialogTitle id="alert-dialog-slide-title">{'댓글 삭제'}</DialogTitle>
				<DialogContent>
					<DialogContentText id="alert-dialog-slide-description">댓글을 삭제하시겠습니까?</DialogContentText>
				</DialogContent>
				<DialogActions>
					<Button onClick={deleteComment} color="primary">
						삭제
					</Button>
					<Button
						onClick={() => {
							setOpenDialogIndex(-1);
						}}
						color="primary"
					>
						취소
					</Button>
				</DialogActions>
			</Dialog>
		</div>
	);
}

export default RestaurantOwnerComment;
