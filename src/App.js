import React, { useEffect, useState } from "react";
import "./App.css";
import Navbar from "./components/Navbar/Navbar";
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Redirect,
} from "react-router-dom";
import Home from "./pages";
import MatchMake from "./pages/MatchMake";
import Register from "./pages/Register";
import Login from "./pages/Login";
import axios from "axios";
import MyPage from "./pages/MyPage";
import Chat from "./pages/Chat";
import Chat2 from "./pages/Chat2";

function App() {
  const [user, setUser] = useState(null);
  const authenticated = user != null;
  console.log(user);

  useEffect(() => {
    const userId = window.sessionStorage.getItem("userId");
    const name = window.sessionStorage.getItem("name");
    const token = window.sessionStorage.getItem("token");
    if (token !== null) setUser({ userId, name, token });
  }, []);

  const login = (userId, name, token) => {
    window.sessionStorage.setItem("userId", userId);
    window.sessionStorage.setItem("name", name);
    window.sessionStorage.setItem("token", token);
    setUser({ userId, name, token });
  };
  const logout = async () => {
    try {
      const token = window.sessionStorage.getItem("token");
      const response = await axios.post(
        `${process.env.REACT_APP_BASE_URL}/users/logout`,
        {},
        { withCredentials: true, headers: { token: token } }
      );
      console.log(response.data);
      window.sessionStorage.removeItem("userId");
      window.sessionStorage.removeItem("name");
      window.sessionStorage.removeItem("token");
      setUser(null);
    } catch (error) {
      console.log("Logout error");
    }
  };

  return (
    <Router>
      <Navbar authenticated={authenticated} login={login} logout={logout} />
      <Switch>
        <Route path="/" exact component={Home} />
        {/* <Route path="/Home" component={index} /> */}
        <Route
          path="/match"
          exact
          render={(props) => <MatchMake {...props} user={user} />}
        />
        <Route
          path="/mypage"
          render={(props) => <MyPage {...props} user={user} />}
        />
        <Route
          path="/login"
          render={(props) => <Login {...props} login={login} />}
        />
        <Route path="/signup" exact component={Register} />
        <Route path="/chat" exact component={Chat} />
        <Route path="/chat2" exact component={Chat2} />
      </Switch>
    </Router>
  );
}

export default App;
