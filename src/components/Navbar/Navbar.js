import {
  AppBar,
  Button,
  IconButton,
  Link,
  Toolbar,
  Typography,
} from "@material-ui/core";
import { Menu } from "@material-ui/icons";
import React from "react";
import {
  Nav,
  NavLink,
  Bars,
  NavMenu,
  NavBtn,
  NavBtnLink,
} from "./NavbarElements";

const Navbar = () => {
  return (
    <>
      <Nav>
        <NavLink exact to="/" activeStyle>
          <h1> "logo" </h1>
        </NavLink>

        <NavMenu>
          <NavLink exact to="/" activeStyle>
            Home
          </NavLink>
          <NavLink exact to="/match" activeStyle>
            Match
          </NavLink>
          <NavLink exact to="/contact-us" activeStyle>
            Contact Us
          </NavLink>
          {/* Second Nav */}
          {/* <NavBtnLink to="/sign-in">Sign In</NavBtnLink> */}
        </NavMenu>
        <NavBtn>
          <NavBtnLink to="/signin">Sign In</NavBtnLink>
        </NavBtn>
      </Nav>
    </>
  );
};

export default Navbar;
