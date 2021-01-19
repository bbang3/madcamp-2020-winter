var express = require("express");
const router = express.Router();
//const Chat = require("../models/Chat");
const loadChatMiddleware = require('../middleware/chat/loadChat');

router.post("/loadchat",loadChatMiddleware);



module.exports = router;
