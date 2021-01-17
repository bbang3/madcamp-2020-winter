import React from "react";
import "./App.css";
import Navbar from "./components/Navbar/";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import Home from "./pages";
import Match from "./pages/Match";
import MatchForm from "./components/MatchForm";
import MatchMake from "./pages/MatchMake";
// import Services from "./pages/services";
// import About from "./pages/about";
// import Contact from "./pages/contact";
// import SignUp from "./pages/signup";

function App() {
  return (
    <Router>
      <Navbar />
      <Switch>
        <Route path="/" exact component={Home} />
        {/* <Route path="/Home" component={index} /> */}
        <Route path="/match" component={MatchMake} />
        {/* <Route path="/contact-us" component={Contact} /> */}
        {/* <Route path="/sign-up" component={SignUp} /> */}
      </Switch>
    </Router>
  );
}

export default App;
