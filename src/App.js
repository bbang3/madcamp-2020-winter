import React from "react";
import "./App.css";
import Navbar from "./components/Navbar/Navbar";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import Home from "./pages";
import MatchMake from "./pages/MatchMake";
import Register from "./pages/Register";
import Login from "./pages/Login";

function App() {
  return (
    <Router>
      <Navbar />
      <Switch>
        <Route path="/" exact component={Home} />
        {/* <Route path="/Home" component={index} /> */}
        <Route path="/match" exact component={MatchMake} />
        <Route path="/login" component={Login} />
        <Route path="/signup" exact component={Register} />
      </Switch>
    </Router>
  );
}

export default App;
