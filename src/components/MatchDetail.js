import axios from "axios";
import React, { useEffect, useState } from "react";

const MatchDetail = ({ user, match }) => {
  const [matchInfo, setMatchInfo] = useState(null);

  useEffect(() => {
    async function fetchData() {
      const response = await axios.get(`/match/${match.params.match_id}`, {
        withCredentials: true,
        headers: { token: user.token },
      });
      setMatchInfo(response.data);
    }
    fetchData();
  }, [user.token, match.params.match_id]);

  return <div>{matchInfo._id}</div>;
};

export default MatchDetail;
