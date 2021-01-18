import React, { useState } from "react";
import "./App.css";
import Navbar from "./components/Navbar/Navbar";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import Home from "./pages";
import MatchMake from "./pages/MatchMake";
import Register from "./pages/Register";
import Login from "./pages/Login";
import { useCookies } from "react-cookie";
import axios from "axios";

function App() {
  const [cookies, setCookie, removeCookie] = useCookies(["token"]);
  const [user, setUser] = useState(null);
  const authenticated = user != null;

  const login = (userId, token) => {
    setCookie("token", token);
    setUser({ userId, token });
  };
  const logout = async () => {
    try {
      console.log(cookies.token);
      const response = await axios.get(
        `${process.env.REACT_APP_BASE_URL}/users/logout/${user.userId}`,
        { withCredentials: true }
      );
      removeCookie("token");
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
        <Route path="/match" exact component={MatchMake} />
        <Route
          path="/login"
          render={(props) => <Login {...props} login={login} />}
        />
        <Route path="/signup" exact component={Register} />
      </Switch>
    </Router>
  );
}

export default App;
