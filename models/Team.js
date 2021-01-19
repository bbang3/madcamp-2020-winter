const mongoose = require("mongoose");

const TeamSchema = mongoose.Schema({
  requestIds: [{ type: String, required: true }],
  category: { type: String, required: true },
  teamSize: { type: Number, required: true },
  skills: { type: Number, required: true },
  intensity: { type: Number, required: true },
  age: { type: Number, required: true },
  date: { type: Date, required: true },
  location: { lat: Number, lng: Number },
  //   status: { type: Number, default: 0 },
  //   matchId: { type: String, default: null },
});

const Team = mongoose.model("Team", TeamSchema);

module.exports = Team;
