// import axios from 'axios';
import { Button, Paper, TextField, Typography } from "@material-ui/core";
import React, { useState } from "react";
import { useCookies } from "react-cookie";
import { useDispatch } from "react-redux"; //dispatch를 이용해서 action을 취할 것임
import { loginUser } from "../_actions/user_action";
export let username = "";
const Login = (props) => {
  const dispatch = useDispatch();
  const [input, setInput] = useState({
    email: "",
    password: "",
  });

  const handleChange = (e) => {
    console.log(e.target.name, e.target.value);
    setInput({ ...input, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault(); // page refresh가 되지 않도록 해주는 것(그래야 밑에 해야할 일들을 할 수가 있음)
    const { email, password } = input;
    //가져온 Email과 Password들을 서버에 보내야함 --> axios라는 것을 이용할 것임
    let body = {
      email: email,
      password: password,
    };
    //리덕스를 사용하지 않았다면 바로 하면 되는 부분임.... 하지만 우리는 리덕스를 사용해야함 ㅜㅜ
    // axios.post('/api/users/login', body)
    // .then(response => {
    //     //처리하고 싶은 것들을 처리하면 되는 것임
    // })

    //action을 취할 것임(바뀌는 부분???)
    dispatch(loginUser(body)) //user_action 내에서 우리는 서버로 보낼 것임??//
      .then((response) => {
        if (response.payload.loginSuccess) {
          const { userId, name, token } = response.payload;
          // loginSuccess는 서버에서 받아온 값... 즉 서버에서 로그인이 성공하면 true를 반환하기 때문임
          console.log("token", token);
          props.login(userId, name, token);
          props.history.push("/");
        } else {
          alert("Error");
        }
      });
  };

  return (
    <div
      style={{
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        width: "100%",
        height: "100vh",
      }}
    >
      <Paper>
        <form onSubmit={handleSubmit} style={{ margin: "30px" }}>
          <Typography gutterBottom> Email </Typography>
          <TextField
            name="email"
            label="email"
            type="email"
            required
            onChange={handleChange}
          />
          <br />
          <Typography gutterBottom> Password </Typography>
          <TextField
            name="password"
            label="password"
            type="password"
            required
            onChange={handleChange}
          />
          <br />
          <br />
          <Button type="submit" variant="contained" color="primary">
            Submit
          </Button>
        </form>
      </Paper>
    </div>
  );
};

export default Login;
