import { Box } from "@material-ui/core";
import { GoogleApiWrapper, Map, Marker } from "google-maps-react";
import React from "react";

const Confirm = ({ google, values }) => {
  console.log(values);

  return (
    <div>
      주어진 정보가 맞는지 다시 한번 확인해주세요.
      <Box>팀원 수: {values.groupSize}</Box>
      <Box>실력: {values.skills}</Box>
      <Box>플레이스타일: {values.intensity}</Box>
      <Box>평균 나이: {values.age}</Box>
      <Box>경기 시각: {values.date.toLocaleString("ko-KR")}</Box>
      <Box>경기 장소: {values.location.address}</Box>
      <Box>
        <Map
          google={google}
          zoom={15}
          style={{ width: "70%", height: "70%" }}
          initialCenter={values.location}
          disableDefaultUI={true}
        >
          <Marker position={values.location} />
        </Map>
      </Box>
      <Box>* 경기 시각과 장소는 매칭 결과에 따라 소폭 변동될 수 있습니다.</Box>
    </div>
  );
};

export default GoogleApiWrapper({
  apiKey: `${process.env.REACT_APP_GOOGLE_MAP_API}`,
})(Confirm);
