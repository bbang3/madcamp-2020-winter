import { Route, Router, Switch, useHistory, useRouteMatch } from 'react-router-dom';
import AddMenu from '../component/AddMenu';
import UpdateMenu from '../component/UpdateMenu';
import RestaurantManagement from '../component/RestaruantManagement';

function RestaurantManagementPage() {
	const match = useRouteMatch();
	const history = useHistory();
	return (
		<div>
			<Router history={history}>
				<Switch>
					<Route exact path={match.path} component={RestaurantManagement} />
					<Route exact path={`${match.path}/add-menu`} component={AddMenu} />
					<Route exact path={`${match.path}/update-menu/:menu_id`} component={UpdateMenu} />
					<Route path={match.path} render={() => <div>404</div>} />
				</Switch>
			</Router>
		</div>
	);
}

export default RestaurantManagementPage;
