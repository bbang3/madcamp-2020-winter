const mongoose = require('mongoose')
var Schema = mongoose.Schema

var userSchema = new Schema({
    isFacebookUser: { type: Boolean, default: false },
    userId: { type: String, required: true },
    password: String,
    phoneNumber: String,
    name: { type: String, required: true },
    profileImage: { type: String, default: "default_user_profile.png" },
    followingIds: [String],
    posts: [String],
    signUpDate: { type: Date, default: Date.now() }
});

module.exports = mongoose.model("user", userSchema);