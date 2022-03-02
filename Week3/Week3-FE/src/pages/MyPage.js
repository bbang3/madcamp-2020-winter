import { Route, Router } from "react-router-dom";
import MatchDetail from "../components/MatchDetail";
import MatchList from "../components/MatchList";
const MyPage = ({ user, match }) => {
  return (
    <>
      {console.log(match)}
      <Route
        exact
        path={match.path}
        render={(props) => <MatchList {...props} user={user} />}
      />
      <Route
        path={`${match.path}/:match_id`}
        render={(props) => <MatchDetail {...props} user={user} />}
      />
    </>
  );
};

export default MyPage;
