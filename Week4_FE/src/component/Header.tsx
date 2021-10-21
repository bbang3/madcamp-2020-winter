import React, { useContext } from 'react';
import { AppBar, Button, IconButton, InputBase, Menu, MenuItem, Toolbar } from '@material-ui/core';
import { createStyles, fade, makeStyles, Theme } from '@material-ui/core/styles';
import { useHistory } from 'react-router-dom';
import { AccountCircle } from '@material-ui/icons';
import SearchIcon from '@material-ui/icons/Search';
import UserContext from '../contexts/UserContext';
import SearchBarContext from '../contexts/SearchBarContext';
import logo from '../images/logo.png';
import useAuthStatus from '../utils/Cookie';

const useStyles = makeStyles((theme: Theme) =>
	createStyles({
		root: {
			flex: 1,
			justifyContent: 'space-between',
		},
		header: {
			// marginLeft: '1%',
			cursor: 'pointer',
			maxHeight: '5rem',
		},
		menu: {
			float: 'right',
			marginRight: '1%',
		},
		search: {
			position: 'relative',
			border: '1px solid #e6e6e6',
			borderRadius: theme.shape.borderRadius,
			// backgroundColor: fade(theme.palette.common.white, 0.15),
			// '&:hover': {
			// 	backgroundColor: fade(theme.palette.common.white, 0.25),
			// },
			width: '50%',
		},
		searchIcon: {
			padding: theme.spacing(0, 2),
			height: '100%',
			position: 'absolute',
			pointerEvents: 'none',
			display: 'flex',
			alignItems: 'center',
			justifyContent: 'center',
		},
		inputRoot: {
			color: 'inherit',
			width: '100%',
		},
		inputInput: {
			padding: theme.spacing(1, 1, 1, 0),
			// vertical padding + font size from searchIcon
			paddingLeft: `calc(1em + ${theme.spacing(4)}px)`,
			transition: theme.transitions.create('width'),
			width: '100%',
			[theme.breakpoints.up('md')]: {
				width: '20ch',
			},
		},
	})
);

function Header() {
	const classes = useStyles();
	const history = useHistory();
	const [_, setAuthStatus, removeAuthStatus] = useAuthStatus();
	const { userStatus, setUserStatus } = useContext(UserContext);
	const { searchText, setSearchText } = useContext(SearchBarContext);
	const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
	const open = Boolean(anchorEl);
	const handleMenu = (event: React.MouseEvent<HTMLElement>) => {
		setAnchorEl(event.currentTarget);
	};
	const handleClose = () => {
		setAnchorEl(null);
	};
	const handleLogout = () => {
		setUserStatus({ accessToken: '', role: '' });
		removeAuthStatus();
		handleClose();
	};
	const handleRestaurantManagementMenu = () => {
		userStatus.restaurantId ? history.push('/RestaurantManagement') : history.push('/RestaurantInformationInput');
		handleClose();
	};
	const handlePasswordChangeMenu = () => {
		history.push('/RestaurantOwnerChangePassword');
	};

	const renderSearchBar = (
		<div className={classes.search}>
			<div className={classes.searchIcon}>
				<SearchIcon />
			</div>
			<InputBase
				placeholder="Search…"
				classes={{
					root: classes.inputRoot,
					input: classes.inputInput,
				}}
				value={searchText}
				onChange={(e) => {
					setSearchText(e.target.value);
				}}
				inputProps={{ 'aria-label': 'search' }}
			/>
		</div>
	);

	const renderMenu =
		userStatus.accessToken.length > 0 ? (
			<div>
				<IconButton
					aria-label="account of current user"
					aria-controls="menu-appbar"
					aria-haspopup="true"
					onClick={handleMenu}
					color="inherit"
				>
					<AccountCircle />
				</IconButton>
				<Menu
					id="menu-appbar"
					anchorEl={anchorEl}
					anchorOrigin={{
						vertical: 'top',
						horizontal: 'right',
					}}
					keepMounted
					transformOrigin={{
						vertical: 'top',
						horizontal: 'right',
					}}
					open={open}
					onClose={handleClose}
				>
					{userStatus.role === 'restaurantOwner' &&
						(userStatus.isInitialPassword ? (
							<MenuItem onClick={handlePasswordChangeMenu}>비밀번호 변경</MenuItem>
						) : (
							<MenuItem onClick={handleRestaurantManagementMenu}>가게 정보 수정</MenuItem>
						))}
					<MenuItem onClick={handleLogout}>로그아웃</MenuItem>
				</Menu>
			</div>
		) : (
			<Button
				color="inherit"
				variant="outlined"
				onClick={() => {
					history.push('/login');
				}}
			>
				Login
			</Button>
		);

	return (
		<AppBar position="static" color="transparent">
			<Toolbar className={classes.root}>
				<img
					// className={classes.header}
					style={{ cursor: 'pointer', height: '5rem' }}
					src={logo}
					alt="logo"
					onClick={() => {
						history.push('/');
					}}
				/>
				{renderSearchBar}
				<div className={classes.menu}>{renderMenu}</div>
			</Toolbar>
		</AppBar>
	);
}

export default Header;
