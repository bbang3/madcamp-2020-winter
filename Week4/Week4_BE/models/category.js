const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const categorySchema = new Schema({
  name: { type: String, required: true },
  description: String,
});

module.exports = mongoose.model("Category", categorySchema);
