const mongoose = require("mongoose");

const requestSchema = mongoose.Schema({
  userId: { type: String, required: true },
  category: { type: String, required: true },
  groupSize: { type: Number, required: true },
  skills: { type: Number, required: true },
  intensity: { type: Number, required: true },
  age: { type: Number, required: true },
  date: { type: Date, required: true },
  location: { lat: Number, lng: Number },
  status: { type: Number, default: 0 },
});

const MatchRequest = mongoose.model("MatchRequest", requestSchema);

module.exports = MatchRequest;
