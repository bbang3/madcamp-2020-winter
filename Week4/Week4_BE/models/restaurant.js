const mongoose = require("mongoose");
const Comment = require("./comment");
const Schema = mongoose.Schema;

const restaurantSchema = new Schema({
  name: { type: String, required: true },
  category: {
    type: mongoose.Schema.Types.ObjectId,
    ref: "Category",
    required: true,
  },
  description: { type: String },
  image: { type: String },
  telephone: { type: String },
  menus: [{ type: mongoose.Schema.Types.ObjectId, ref: "Menu" }],
  comments: [Comment.schema],
  confirmed: { type: Boolean, default: false },
  contractExpiredAt: {
    type: Date,
    // one year after from now
    default: new Date(new Date().setFullYear(new Date().getFullYear() + 1)),
  },

  location: {
    fullAddress: String,
    extraAddress: String,
  },
  openingHours: [
    {
      openTime: String,
      closeTime: String,
    },
  ],
});

module.exports = mongoose.model("Restaurant", restaurantSchema);
