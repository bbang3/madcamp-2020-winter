import React from "react";
import { useCookies } from "react-cookie";
import {
  Nav,
  NavLink,
  Bars,
  NavMenu,
  NavBtn,
  NavBtnLink,
} from "./NavbarElements";

const Navbar = ({ login, logout, authenticated }) => {
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
          <NavLink exact to="/mypage" activeStyle>
            My Page
          </NavLink>
          {/* Second Nav */}
          {/* <NavBtnLink to="/sign-in">Sign In</NavBtnLink> */}
        </NavMenu>
        {authenticated ? (
          <NavBtn onClick={() => logout()}>
            <NavBtnLink to="/">Log out</NavBtnLink>
          </NavBtn>
        ) : (
          <NavBtn>
            <NavBtnLink to="/login">Log in</NavBtnLink>
          </NavBtn>
        )}

        <NavBtn>
          <NavBtnLink to="/signup">Sign Up</NavBtnLink>
        </NavBtn>
      </Nav>
    </>
  );
};

export default Navbar;
