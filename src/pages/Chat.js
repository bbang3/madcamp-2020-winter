import React, { useState, useEffect } from 'react'
import io from 'socket.io-client'
import TextField from '@material-ui/core/TextField'
import '../App.css'
import axios from "axios";
import ScrollToBottom from 'react-scroll-to-bottom';
import {
  username
}from "../pages/Login";
import sendimg from "../image/send3.png"
import Avatar from '@material-ui/core/Avatar';
import CardHeader from '@material-ui/core/CardHeader';


const socket = io.connect('http://192.249.18.247:4000/');
//,{ transport : ['websocket'] }
function Chat() {
  const [state, setState] = useState({ message: '', name: '' });
  const [chat, setChat] = useState([]);
  const [count, setCount] = useState(0);
  const groupid = "3"; 
  var prevChat = [];
  // 매칭된 방을 클릭하면 받아오는 group_id

  useEffect(() => {
    // const request = axios
    // .post('http://192.249.18.247:4000/chat/getchat',{roomid: groupid})
    // .then((response) => response.data);
    axios
    .post('http://192.249.18.247:8080/chat/loadchat',{roomid: groupid})
    .then(async(response) =>  await onLoadChat(response.data));
    // console.log("Data: " + response.data.chatContent);
    // console.log("!!");
    
    

  },[])

  useEffect(() => {

    socket.emit("room", groupid);
    //socket emit을 하면 room으로 나의 groupid를 보낸다...
    //console.log("아 힘들어 죽어 ㅜㅜㅜ 엄마 ㅜㅜㅜㅜ 살려줘 ㅜㅜㅜ ")

  }, [count])
    
  useEffect(() => {
    console.log("before socket", chat.length);

    socket.on('updatemessage', async ({ name: name, message: message }) => {//서버에 보내진 채팅을 가져온다.
      
        console.log("socket message arrived");
  
        console.log("setting chat", chat.length);
        await setChat([...chat,{name: username, message: message}])//새로운 채팅이 들어와서 저장이 된다.
        //console.log("recieved chat issssssssssssssssssssssss");
        console.log("after setting chat", chat.length);
        await setCount(count + 1);

    })  
      //setChat([...chat, { namec, messagec }])//새로운 채팅이 들어와서 저장이 된다.
  },[count])

  const onLoadChat = e => {
    const loadedChat = [];
    for(var i = 0 ; i < e.content.length; i++){
      let name = e.content[i].sender;

      let meg = e.content[i].message;

      loadedChat.push({name: name, message: meg});
    }
    setChat(loadedChat);
  }

  const onTextChange = e => {
    setState({ ...state, [e.target.name]: e.target.value })
  }

  const onMessageSubmit = async e => {
    e.preventDefault();
    const { name, message } = state;
    console.log("client message :" +  message);
    socket.emit("message",  { name, message });//내가 쓴 메시지를 서버에 보낸다.
    console.log("emit socket");
    await setState({ message: '', name });
  }

  const renderChat = () => {
    console.log("renderchat", chat.length);
    return chat.map(({ name, message }, index) => (//저장된 채팅을 보여준다.
      <div key={index}>
        {/* <h3>
          {name}: <span>{message}</span>
        </h3> */}
        <CardHeader
        avatar={
          <Avatar aria-label="recipe" >
            <h6>{name[1]}{name[2]}</h6>
          </Avatar>
        }
        title={message}
        />
      </div>
    ))
  }
  // //prevChat.push({name: "!!", message: "@@@@"})
  // console.log('prevChat: ',{prevChat});
  return (
    // <div className="card">
    //   <form onSubmit={onMessageSubmit}>
    //     {/* <div className="name-field">
    //       <TextField
    //         name="name"
    //         onChange={e => onTextChange(e)}
    //         value={state.name}
    //         label="Name"
    //       />
    //     </div> */}

    //     <div>
    //       <TextField
    //         name="message"
    //         onChange={e => onTextChange(e)}
    //         value={state.message}
    //         id="outlined-multiline-static"
    //         variant="outlined"
    //         label="Message"
    //       />
    //       <button onClick={() => setCount(count + 1)}>Send</button>
    //     </div>
    //     {/* <button onClick={() => setCount(count + 1)}>Send Message</button> */}
    //   </form>
    //   <ScrollToBottom className="render-chat">
    //     <h1>ROOM</h1>
    //     <div>
    //     {renderChat()}
    //     </div> 
    //   </ScrollToBottom>
    // </div>

    <div className = "card2">
      <div className = "chatform">
      <h1>ROOM</h1>
      </div>
      <ScrollToBottom className="render-chat">
        {/* <h1>ROOM</h1> */}
        <div>
          {renderChat()}
        </div> 
      </ScrollToBottom>
      <form onSubmit={onMessageSubmit} style={{ height : "80px" }}> 
        {/* <div className="name-field">
          <TextField
            name="name"
            onChange={e => onTextChange(e)}
            value={state.name}
            label="Name"
          />
        </div> */}

        <div>
          <TextField
          item xs={10}
            name="message"
            onChange={e => onTextChange(e)}
            value={state.message}
            //id="outlined-multiline"
            id= "full-width-text-field"
            variant="outlined"
            label="Message"
            style = {{width: 310}}
            //size = "small"
            //fullWidth = true
          />
          {/* <Button onClick={() => setCount(count + 1)}>
            <Email color="primary"
            width='65px'
            style = {{width: 70}}/>
          </Button> */}
          <button >
            <img
            src={ sendimg }
            width='43px'
            //padding = "10px"
            //height='50px'
            //alt='testA' 
            onClick={() => setCount(count + 1)}/>
          </button>
        </div>
        {/* <button onClick={() => setCount(count + 1)}>Send Message</button> */}
      </form>
    </div>
    
  )
}

export default Chat