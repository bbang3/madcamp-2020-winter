const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const User = mongoose.model(
  "User",
  new Schema({
    username: { type: String, required: true },
    password: { type: String, required: true },
    role: { type: String, required: true },

    // admin, customer, restaurantOwner
  })
);

const RestaurantOwner = User.discriminator(
  "RestaurantOwner",
  new Schema({
    isInitialPassword: { type: Boolean },
    restaurant: { type: Schema.Types.ObjectId, ref: "Restaurant" },
  })
);

const Customer = User.discriminator(
  "Customer",
  new Schema({
    nickname: { type: String },
    confirmed: { type: Boolean },
    emailVerifyKey: { type: String },
  })
);

exports.User = User;
exports.RestaurantOwner = RestaurantOwner;
exports.Customer = Customer;
