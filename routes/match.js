const express = require("express");
const router = express.Router();
const { auth } = require("../middleware/auth");
const matchMaking = require("../middleware/matchMaking");

const MatchRequest = require("../models/MatchRequest");
const User = require("../models/User");

// POST new match requests
router.post(
  "/",
  auth,
  async (req, res, next) => {
    const user = req.user;
    console.log(req.body, user);
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
router.get(
  "/",
  auth,
  async (req, res, next) => {
    const user = req.user;
    const requests = [];

    try {
      for (requestId of user.matchRequests) {
        requests.push(await MatchRequest.findById(requestId));
      }

      res.status(200).json(requests);
    } catch (error) {
      res.status(500).json({ message: error });
    }
    // next();
  }
  //   matchMaking
);

module.exports = router;
