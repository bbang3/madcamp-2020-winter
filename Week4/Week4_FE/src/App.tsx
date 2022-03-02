import React, { useEffect, useState } from 'react';
import logo from './logo.svg';
import './App.css';
import { BrowserRouter as Router, useHistory, Route, Redirect, Switch, RouteProps } from 'react-router-dom';
import CategoryListPage from './pages/CategoryListPage';
import RestaurantInformationInputPage from './pages/RestaurantInformationInputPage';
import RestaurantManagementPage from './pages/RestaruantManagementPage';
import RestaurantListPage from './pages/RestaurantListPage';
import RestaurantPage from './pages/RestaurantPage';
import AdminManagementRouter from './router/AdminManagementRouter';
import AdminManagementPage from './pages/AdminManagementPage';
import AdminLoginPage from './pages/AdminLoginPage';
import LoginPage from './pages/LoginPage';
import SignUpPage from './pages/SignUpPage';
import Header from './component/Header';
import UserContext from './contexts/UserContext';
import SearchBarContext from './contexts/SearchBarContext';
import { UserStatus } from './types/AuthTypes';
import RestaurantOwnerChangePasswordPage from './pages/RestaurantOwnerChangePasswordPage';
import useAuthStatus from './utils/Cookie';

function App() {
	const [userStatus, setUserStatus] = useState<UserStatus>({ accessToken: '', role: '' });
	const [searchText, setSearchText] = useState('');
	const [_, setAuthStatus, removeAuthStatus] = useAuthStatus();
	useEffect(() => {
		_ ? setUserStatus(_) : setUserStatus({ accessToken: '', role: '' });
	}, [userStatus.accessToken]);
	return (
		<UserContext.Provider value={{ userStatus, setUserStatus }}>
			<SearchBarContext.Provider value={{ searchText, setSearchText }}>
				<Router>
					<Header />
					<Switch>
						<Route exact path="/" component={CategoryListPage} />
						<Route
							exact
							path="/login"
							render={(props: RouteProps) =>
								userStatus.accessToken.length === 0 ? (
									<LoginPage />
								) : (
									<Redirect to={{ pathname: '/', state: { from: props.location } }} />
								)
							}
						/>
						<Route
							exact
							path="/AdminLogin"
							render={(props) =>
								userStatus.role === 'admin' ? (
									<Redirect to={{ pathname: '/AdminManagement', state: { from: props.location } }} />
								) : userStatus.accessToken.length === 0 ? (
									<AdminLoginPage />
								) : (
									<Redirect to={{ pathname: '/', state: { from: props.location } }} />
								)
							}
						/>
						<AdminRoute exact={false} path="/AdminManagement" userStatus={userStatus}>
							<AdminManagementRouter />
						</AdminRoute>
						<RestaurantOwnerRoute exact path="/RestaurantOwnerChangePassword" userStatus={userStatus}>
							<Route
								exact
								path="/RestaurantOwnerChangePassword"
								render={(props) =>
									userStatus.isInitialPassword ? (
										<RestaurantOwnerChangePasswordPage />
									) : !userStatus.restaurantId ? (
										<Redirect to={{ pathname: '/RestaurantInformationInput', state: { from: props.location } }} />
									) : (
										<Redirect to={{ pathname: '/RestaurantManagement', state: { from: props.location } }} />
									)
								}
							/>
						</RestaurantOwnerRoute>
						<RestaurantOwnerRoute exact path="/RestaurantInformationInput" userStatus={userStatus}>
							<Route
								exact
								path="/RestaurantInformationInput"
								render={(props) =>
									userStatus.isInitialPassword ? (
										<Redirect to={{ pathname: '/RestaurantOwnerChangePassword', state: { from: props.location } }} />
									) : !userStatus.restaurantId ? (
										<RestaurantInformationInputPage />
									) : (
										<Redirect to={{ pathname: '/RestaurantManagement', state: { from: props.location } }} />
									)
								}
							/>
						</RestaurantOwnerRoute>
						<RestaurantOwnerRoute exact={false} path="/RestaurantManagement" userStatus={userStatus}>
							<Route
								path="/RestaurantManagement"
								render={(props) =>
									userStatus.isInitialPassword ? (
										<Redirect to={{ pathname: '/RestaurantOwnerChangePassword', state: { from: props.location } }} />
									) : !userStatus.restaurantId ? (
										<Redirect to={{ pathname: '/RestaurantInformationInput', state: { from: props.location } }} />
									) : (
										<RestaurantManagementPage />
									)
								}
							/>
						</RestaurantOwnerRoute>
						<Route exact path="/RestaurantList/:categoryId" component={RestaurantListPage} />
						<Route exact path="/Restaurant/:restaurantId" component={RestaurantPage} />
						<Route exact path="/SignUp" component={SignUpPage} />
						<Route path="/" render={() => <div>404</div>} />
					</Switch>
				</Router>
			</SearchBarContext.Provider>
		</UserContext.Provider>
	);
}

function AdminRoute(props: { path: string; exact: boolean; userStatus: UserStatus; children: React.ReactChild }) {
	return (
		<Route
			exact={props.exact}
			path={props.path}
			render={() => (props.userStatus.role === 'admin' ? props.children : <Redirect to={{ pathname: '/' }} />)}
		/>
	);
}

function RestaurantOwnerRoute(props: {
	path: string;
	exact: boolean;
	userStatus: UserStatus;
	children: React.ReactChild;
}) {
	return (
		<Route
			exact={props.exact}
			path={props.path}
			render={() =>
				props.userStatus.role === 'restaurantOwner' ? props.children : <Redirect to={{ pathname: '/' }} />
			}
		/>
	);
}

export default App;
