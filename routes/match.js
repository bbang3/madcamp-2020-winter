const express = require("express");
const router = express.Router();

const MatchRequest = require("../models/MatchRequest");
const User = require("../models/User");

// POST new match requests
router.post("/", async (req, res) => {
  console.log(req.body);
  try {
    const newRequest = new MatchRequest(req.body);
    const output = await newRequest.save();
    console.log(output);

    const user = await User.findById(output.userId);
    user.match.push(output._id);
    user.save();

    res.status(200).json({ success: true });
  } catch (error) {
    res.status(500).json({ message: error, success: false });
  }
});

module.exports = router;
