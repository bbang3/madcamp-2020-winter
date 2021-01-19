import React, { useState, useEffect } from 'react'
import io from 'socket.io-client'
import TextField from '@material-ui/core/TextField'
import '../App.css'
import axios from "axios";

const socket = io.connect('http://192.249.18.247:4000/');
//,{ transport : ['websocket'] }
function Chat2() {
  const [state, setState] = useState({ message: '', name: '' });
  const [chat, setChat] = useState([]);
  const [count, setCount] = useState(0);
  const groupid = "4"; 
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

  }, [count])
    
  useEffect(() => {


    //setCount(count + 1);
    socket.on('updatemessage', async ({ name: name, message: message }) => {//서버에 보내진 채팅을 가져온다.
        //setCount(count + 1);
        console.log("socket message arrived");
        // console.log("name: " + name);
        // console.log('message: ' + message);
        console.log("setting chat", chat.length);
        await setChat([...chat, { name, message}])//새로운 채팅이 들어와서 저장이 된다.
        //console.log("recieved chat issssssssssssssssssssssss");
        console.log(chat);
        await setCount(count + 1);

    })  
      //setChat([...chat, { namec, messagec }])//새로운 채팅이 들어와서 저장이 된다.
  },[count])

  const onLoadChat = e => {
    const loadedChat = [];
    for(var i = 0 ; i < e.content.length; i++){
      let name = e.content[i].sender;
      // console.log("name: ", name);
      // console.log("meg: ", meg);
      let meg = e.content[i].message;
      //const subchat = {name, meg};
      //chat[i].name = name;
      //chat[i].message = meg;
      //setChat(chat => [...chat,{name: name, message: meg}])
      // prevChat.push({name: name, message: meg});
      // prevChat.push({name: "!!", message: "@@@@"});
      loadedChat.push({name: name, message: meg});
    }
    setChat(loadedChat);
    //console.log("chatcontent: ",e.content[0].message)
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
    console.log(chat.length);
    return chat.map(({ name, message }, index) => (//저장된 채팅을 보여준다.
      <div key={index}>
        <h3>
          {name}: <span>{message}</span>
        </h3>
      </div>
    ))
  }

  return (
    <div className="card">
      <form onSubmit={onMessageSubmit}>
        <h1>Messanger</h1>
        <div className="name-field">
          <TextField
            name="name"
            onChange={e => onTextChange(e)}
            value={state.name}
            label="Name"
          />
        </div>
        <div>
          count: {count}
        </div>
        <div>
          <TextField
            name="message"
            onChange={e => onTextChange(e)}
            value={state.message}
            id="outlined-multiline-static"
            variant="outlined"
            label="Message"
          />
        </div>
        <button onClick={() => setCount(count + 1)}>Send Message</button>
      </form>
      <div className="render-chat">
        <h1>Chat Log</h1>
        {renderChat()}
      </div>
    </div>
  )
}

export default Chat2