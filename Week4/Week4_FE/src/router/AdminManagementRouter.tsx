import React from 'react';
import { Route, Switch, useRouteMatch } from 'react-router-dom';
import AdminManagementPage from '../pages/AdminManagementPage';
import ConfirmRestaurantPage from '../pages/ConfirmRestaurantPage';

function AdminManagementRouter() {
	const match = useRouteMatch();
	console.log(`${match.path}`);
	return (
		<div>
			<Switch>
				<Route
					exact
					path={`${match.path}`}
					render={() => {
						return <AdminManagementPage />;
					}}
				/>
				<Route
					exact
					path={`${match.path}/confirmRestaurant/:restaurantId`}
					render={() => {
						return <ConfirmRestaurantPage />;
					}}
				/>
				<Route
					path={`${match.path}`}
					render={() => {
						return <div>404</div>;
					}}
				/>
			</Switch>
		</div>
	);
}

export default AdminManagementRouter;
