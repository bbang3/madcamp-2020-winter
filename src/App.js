import React from "react";
import "./App.css";
import Navbar from "./components/Navbar/Navbar";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import Home from "./pages";
import MatchMake from "./pages/MatchMake";
import Register from "./pages/Register";
import Login from "./pages/Login";
import Chat from "./pages/Chat";
import Chat2 from "./pages/Chat2";
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
        <Route path="/chat" exact component={Chat} />
        <Route path="/chat2" exact component={Chat2} />
      </Switch>
    </Router>
  );
}

export default App;
