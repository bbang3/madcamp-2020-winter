var createError = require("http-errors");
var express = require("express");
var path = require("path");
var cookieParser = require("cookie-parser");
var logger = require("morgan");
const Chat = require("../madcamp_week3_back/models/Chat")

var indexRouter = require("./routes/index");
var usersRouter = require("./routes/users");
var chatRouter = require('./routes/chat')
var requestRouter = require("./routes/match");

const app = express();
const PORT = 8080;
const port = 4000;
const cors = require("cors");

const http = require('http');
const socketIO = require('socket.io');
const server = http.createServer();
const io = socketIO(server);   

const mongoose = require("mongoose");

// view engine setup
app.set("views", path.join(__dirname, "views"));
app.set("view engine", "pug");

app.use(logger("dev"));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(cors({ credentials: true, origin: "http://localhost:3000" }));
app.use(express.static(path.join(__dirname, "public")));

mongoose.connect(
  `mongodb://192.249.18.247:27017/madcamp_week3`,
  {
    useUnifiedTopology: true,
    useNewUrlParser: true,
    useFindAndModify: false,
    useCreateIndex: true,
  },
  () => {
    console.log("connected to DB");
  }
);

app.use("/", indexRouter);
app.use("/users", usersRouter);
app.use("/chat", chatRouter);
app.use("/match", requestRouter);

// catch 404 and forward to error handler
app.use(function (req, res, next) {
  next(createError(404));
});

// error handler
app.use(function (err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get("env") === "development" ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render("error");
});
server.listen(port, () => console.log(`Listening on port ${port}`));

// io.on('connection', async (socket) => {
//     console.log("connected");
//     socket.on("message", ({name, message}) => {//클라이언트에서 보낸 채팅 내용을 받는다.
//         console.log("hello world");
//         console.log("message: " + message);
//         io.emit('updatemessage', { name: name, message: message })// 클라이언트에 다시 채팅 내용을 보낸다.
//         console.log(message);
//     })
//     socket.on('disconnect', () => {
//     })
// })
let roomid = "";

io.on('connection', async (socket) => { //클라이언트의 socket과 연결을 한다./
  console.log("connected");
  socket.on("room", room_id => {//클라이언트에서 보낸 자신의 room_id를 받아온다.
    roomid = room_id;
    console.log("roomid : " + roomid)
    socket.join(room_id);//룸에 입장을 한다.
    console.log(roomid+'방입장');
  })
  console.log("여기에는 들어왔으려나???ㅋㅋㅋㅋㅋㅋㅋㅋ ");
  socket.on("message", ({name, message})=>{//보낸 메시지를 받는다.
    console.log("message: " + message);
    console.log("name: " + name);
    //socket.broadcast.emit("updatemessage",{ name: name, message: message });//채팅방에 메시지를 다시 보낸다.
    io.sockets.in(roomid).emit("updatemessage",{ name: name, message: message });
    // 보낸 메시지를 데이터 베이스에 저장을 해야한다.
    //{
    //   roomId : roomid,
    //   content: [{
    //     message : messages,
    //     sender : name,
    //     timestamp : "0:0:0"
    //   }],
    //   members : "qqqqq"  
    // }
    console.log(roomid);

    //create_group----------------------------
    // const chat = new Chat({
    //   roomId : roomid,
    //   content: [{
    //     message : message,
    //     sender : name,
    //     timestamp : "1:1:1"
    //   }],
    //   members : "AAAAAAAA"  
    // })
    // chat.save();
    //---------------------------create_group

    Chat.updateOne({ //db에 저장을 한다.
      roomId: roomid
    },{
      $push: {
        content: {
          message : message,
          sender : name,
          timestamp : "0:0:0"
        }
      }
    },(err, data) => {
        console.log(data);
        if (err) throw err;
    });

    //______________________________________________
  })
})

app.listen(PORT, function () {
  console.log(`Express server listening on port ${PORT}`);
});

module.exports = app;
