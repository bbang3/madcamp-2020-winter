const Team = require("../models/Team");
const Match = require("../models/Match");

const TEAM_SIZE = 5;

const teamMaking = async (req, res) => {
  const request = res.locals.request;
  console.log("matchMaking", request);

  const matchedTeam = await Team.findOne({
    userIds: { $ne: request.userId },
    category: request.category,
    teamSize: TEAM_SIZE - request.groupSize,
  });
  console.log(matchedRequest);

  if (!matchedTeam) {
    // if no request matched, create new team
    const newTeam = new Team({
      userIds: [request.userId],
      category: request.category,
      teamSize: request.groupSize,
      skills: request.skills,
      intensity: request.intensity,
      age: request.age,
      date: request.date,
      location: request.location,
    });
    return await newTeam.save();
  } else {
    const curTeamSize = matchedTeam.teamSize;
    const newTeamSize = matchedTeam.teamSize + request.groupSize;
    matchedTeam.location.lat =
      (matchedTeam.location.lat * curTeamSize + request.location.lat) /
      newTeamSize;

    matchedTeam.location.lng =
      (matchedTeam.location.lng * curTeamSize + request.location.lng) /
      newTeamSize;

    let currentTime = matchedTeam.date.getTime();
    let newTime =
      (currentTime * curTeamSize + request.date.getTime()) / newTeamSize;

    matchedTeam.date = new Date(newTime);

    matchedTeam.matchedTeam.teamSize += request.groupSize;
    matchedTeam.userIds.push(request.userId);
    return await matchedTeam.save();
  }
};

const matchMaking = async (req, res) => {
  const myTeam = teamMaking(req, res);
  console.log(myTeam);
  if (myTeam.teamSize !== 5) return;

  const oppTeam = await Team.findOne({
    category: myTeam.category,
    teamSize: { $eq: 5 },
  });

  const newMatch = new Match({
    // category: myTeam.category,
    // team1: {
    //     myTeam
    // }
  });

  //   const match;
};

module.exports = matchMaking;
