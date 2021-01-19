const mongoose = require("mongoose");

const requestSchema = mongoose.Schema({
  userId: { type: String, required: true },
  category: { type: String, required: true },
  groupSize: { type: Number, required: true },
  skills: { type: Number, required: true },
  intensity: { type: Number, required: true },
  age: { type: Number, required: true },
  date: { type: Date, required: true },
  location: { lat: Number, lng: Number, address: String },
  status: { type: Number, default: 0 },
  matchId: { type: String, default: null },
});

const MatchRequest = mongoose.model("MatchRequest", requestSchema);

module.exports = MatchRequest;
