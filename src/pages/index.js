import { Link, Typography } from "@material-ui/core";
import { Card } from "antd";
import React from "react";
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
      <div id="page2">
        <h3>
          {introContent.split("\n").map((line) => {
            return (
              <>
                {line}
                <br />
              </>
            );
          })}
        </h3>
        <h3>당신의 시선에서 바라본 팀 매칭 서비스</h3>
        시간, 장소, 나이, 실력, 플레이스타일 등에 기반하여 같이 운동할 상대와,
        구장을 매칭해주는 개인 맞춤형 스포츠 매칭
      </div>
      <div id="page3" style={divStyle}>
        이용 가능 지역
      </div>
      <div id="page4" style={divStyle}>
        <h1> page 4</h1>
      </div>
    </div>
    // <div
    //   style={{
    //     display: "flex",
    //     justifyContent: "center",
    //     alignItems: "center",
    //     height: "90vh",
    //   }}
    // >

    //   <h1 style={{ fontFamily: "monospace" }}>Home</h1>
    // </div>
  );
};

export default Home;
