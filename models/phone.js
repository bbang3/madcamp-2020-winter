const mongoose = require('mongoose')
var Schema = mongoose.Schema

var phoneSchema = new Schema({
    name: String,
    phoneNumber: String,
    email: String
});

module.exports = mongoose.model("phone", phoneSchema);
