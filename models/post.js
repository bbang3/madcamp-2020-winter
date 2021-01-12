const mongoose = require('mongoose')
var Schema = mongoose.Schema

var postSchema = new Schema({
    authorId: { type: String, required: true }, // User's ObjectId
    author: { type: String, required: true },
    authorProfile: { type: String, default: "default_user_profile.png" }, // Filename of Profile image
    description: String,
    images: { type: [String], required: true }, // Filenames of images
    date: { type: Date, default: Date.now }
});

module.exports = mongoose.model("post", postSchema);