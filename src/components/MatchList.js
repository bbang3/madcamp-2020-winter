import {
  Box,
  Card,
  CardActionArea,
  CardContent,
  Container,
  Link,
  makeStyles,
  Typography,
} from "@material-ui/core";
import { Lens } from "@material-ui/icons";
import axios from "axios";
import React, { useEffect, useState } from "react";
import { NavLink } from "./Navbar/NavbarElements";
import Geocode from "react-geocode";

const useStyles = makeStyles({
  root: {
    maxWidth: "25vw",
  },
  media: {
    height: 140,
  },
});

export const MatchData = ({ request, index, match }) => {
  const { address } = request.location;
  const [matchedAddress, setMatchedAddress] = useState("");

  if (request.status === 1) {
    const { lat, lng } = request.matchedLocation;

    Geocode.fromLatLng(lat, lng).then(
      (res) => {
        const address = res.results[0].formatted_address;
        setMatchedAddress(address);
      },
      (err) => {
        console.log(err);
      }
    );
  }
  return (
    <NavLink
      key={index}
      to={`${match.url}/${request.matchId}`}
      style={{ textDecoration: "none", color: "black" }}
    >
      <Card>
        <CardActionArea key={index}>
          <CardContent>
            {request.status === 0 ? (
              <Box>
                <Lens style={{ fill: "Black" }} />
                <Typography> 매칭 진행 중 </Typography>
              </Box>
            ) : (
              <Box>
                <Lens style={{ fill: "Green" }} />
                <Typography> 매칭 완료 </Typography>
              </Box>
            )}
          </CardContent>
          <CardContent>
            선호하는 시간: {new Date(request.date).toLocaleString()}
          </CardContent>
          <CardContent> 선호하는 장소: {address}</CardContent>
          {request.status === 1 && (
            <>
              <CardContent>
                매칭된 시간: {new Date(request.matchedDate).toLocaleString()}
              </CardContent>
              <CardContent>매칭된 장소: {matchedAddress}</CardContent>
            </>
          )}
        </CardActionArea>
      </Card>
    </NavLink>
  );
};

const MatchList = ({ match }) => {
  const user = {
    token: sessionStorage.getItem("token"),
    userId: sessionStorage.getItem("userId"),
    name: sessionStorage.getItem("name"),
  };
  const [requests, setRequests] = useState([]);
  const classes = useStyles();

  useEffect(() => {
    async function fetchData() {
      const response = await axios.get("/match", {
        headers: { token: user.token },
      });
      setRequests(response.data);
    }
    fetchData();
  }, [user.token]);

  const renderRequests = requests.map((request, index) => {
    console.log(request);
    console.log(typeof request.date);
    return (
      <MatchData match={match} index={index} request={request}></MatchData>
    );
  });

  // return <NavLink to="/"> Hello </NavLink>;
  return <Container>{renderRequests}</Container>;
};

export default MatchList;
