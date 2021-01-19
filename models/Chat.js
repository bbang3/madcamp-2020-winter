const mongoose = require('mongoose');

const messageSchema = new mongoose.Schema({
    message: { type: String },
    sender: { type: String },
    //timestamp: { type: Date }
    timestamp: { type: String }
})

const chatSchema = new mongoose.Schema({
    roomId: { type: String },
    content: [messageSchema],
    //members: { type: [String], required: true }
    members: { type: String}
})

//module.exports = mongoose.model('Chat', chatSchema);

const Chat = mongoose.model('Chat', chatSchema);

module.exports = Chat;
