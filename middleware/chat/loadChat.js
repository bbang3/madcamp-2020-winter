const Chat = require("../../models/Chat");

const loadChatMiddleware = async (req, res, next) => {
  const roomid = req.body.roomid;
  let chatContent;
  console.log("room_ id :" + roomid);
  //console.log(Chat.find())
  const chat = await Chat.findOne({ roomId: roomid });

  console.log(chat);
  // Chat.findOne({roomId:roomid}), function(err, data){
  //     console.log("data",data);
  //     if(err) throw err;
  //     console.log("err",err);
  //     chatContent = data.content;
  //     //여기에서 룸 아이디에 맞는 데이터를 클라이언트에 보낸다.
  //     console.log("chatContent : " + chatContent);
  // }

  res.json(chat);
};

module.exports = loadChatMiddleware;
