const express = require("express");
const router = express.Router();
const { auth } = require("../middleware/auth");
const matchMaking = require("../middleware/matchMaking");

const MatchRequest = require("../models/MatchRequest");
const Match = require("../models/Match");
const User = require("../models/User");

// POST new match requests
router.post(
  "/",
  auth,
  async (req, res, next) => {
    const user = req.user;

    try {
      const newRequest = new MatchRequest(req.body);
      const output = await newRequest.save();
      console.log(output);
      res.status(200).json({ success: true });

      res.locals.request = output;
      user.matchRequests.push(output._id);
      user.save();
    } catch (error) {
      res.status(500).json({ message: error, success: false });
    }
    next();
  },
  matchMaking
);

// GET user's match requests
router.get("/", auth, async (req, res, next) => {
  const user = req.user;

  try {
    const requests = [];
    for (requestId of user.matchRequests) {
      let request = await MatchRequest.findById(requestId);
      if (request.matchId) {
        const match = await Match.findById(request.matchId);
        request = {
          ...request._doc,
          matchedDate: match.date,
          matchedLocation: match.location,
        };
      }
      console.log(request);
      requests.push(request);
    }
    console.log("getRequests", requests);
    res.status(200).json(requests);
  } catch (error) {
    res.status(500).json({ message: error });
  }
});

router.get("/:match_id", auth, async (req, res) => {
  console.log(req.params.match_id);
  const match = await Match.findById(req.params.match_id);

  res.status(200).json(match);
});

module.exports = router;
