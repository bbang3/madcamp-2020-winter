import { Link, Typography } from "@material-ui/core";
import { Card } from "antd";
import React from "react";
import { NavLink } from "react-router-dom";
import "./Home.css";

const divStyle = {
  display: "flex",
  justifyContent: "center",
  alignItems: "center",
  height: "90vh",
  border: "1px solid blue",
};

const introContent = `"하까"?
체육 시간에 공을 들고 나갈 때,
우리는 친구들에게 "하까?"라고 말합니다.

어느덧 성인이 되어, 바쁜 일상 속 잃어버린 체육 시간.
그 소중한 시간을 'HAKKA'가 되찾아 드리고자 합니다.

`;

const Home = () => {
  return (
    <div>
      <div id="page1">
        <div className="typo-container">
          <div className="typo-hakka">
            <span style={{ color: "deepskyblue" }}>H</span>A
            <span style={{ color: "deepskyblue" }}>K</span>KA{" "}
          </div>
          <h1> 잃어버린 체육시간을, </h1>
          <h1> 다시금 구현하다 </h1>
        </div>
      </div>
    </div>
  );
};

export default Home;
