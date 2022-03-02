const mongoose = require('mongoose')
var Schema = mongoose.Schema

var imageSchema = new Schema({
    filename: String,
    mimeType: String,
    path: String,
});

module.exports = mongoose.model("image", imageSchema);