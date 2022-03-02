const mongoose = require("mongoose");

const matchSchema = mongoose.Schema({
  category: { type: String, required: true },

  team1: [{ requestId: String, userId: String, status: Number }],
  team2: [{ requestId: String, userId: String, status: Number }],

  date: { type: Date, required: true },
  location: { lat: Number, lng: Number, address: String },
});

const Match = mongoose.model("Match", matchSchema);

module.exports = Match;
