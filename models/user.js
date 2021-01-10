const mongoose = require('mongoose')
var Schema = mongoose.Schema

var userSchema = new Schema({
    isFacebookUser: Boolean,
    username: String,
    password: String,
    phoneNumber: String,
    name: String,
    followingIds: [String],
    posts: [String],
    signUpDate: { type: Date, default: Date.now() }
});

module.exports = mongoose.model("user", userSchema);