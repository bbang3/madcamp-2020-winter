import axios from "axios";
import { LOGIN_USER, REGISTER_USER } from "./types";

axios.defaults.baseURL = process.env.REACT_APP_BASE_URL;
console.log(process.env.REACT_APP_BASE_URL);

export function loginUser(dataToSubmit) {
  //email과 password는 dataToSubmit 안에 파라미터를 통해서 들어옴
  const request = axios
    .post(`/users/login`, dataToSubmit)
    .then((response) => response.data); //서버에서 받은 data를 request에 저장을 한다.
  //reducer에 넘겨주기(reducer에서는,,,,previous state과 위의 action(즉. axios를 통해서 통신을 한 부분을 의미)가지고 nextstate를 return을 해야함)
  return {
    type: LOGIN_USER,
    payload: request,
    //reducer에 넘겨주는 부분(타입과 request를 넘겨주어야 함)
    //일단은 reducer로 자동으로 간다고 생각을 하면, .. reducer에서 받아오는 파라미터 action은 저 리턴 값을 가지고 있는 것임
  };
}

export function registerUser(dataToSubmit) {
  //email과 password는 dataToSubmit 안에 파라미터를 통해서 들어옴

  const request = axios
    .post(`/users/register`, dataToSubmit)
    .then((response) => response.data); //서버에서 받은 data를 request에 저장을 한다.
  //reducer에 넘겨주기(reducer에서는,,,,previous state과 위의 action(즉. axios를 통해서 통신을 한 부분을 의미)가지고 nextstate를 return을 해야함)
  return {
    type: REGISTER_USER,
    payload: request,
    //reducer에 넘겨주는 부분(타입과 request를 넘겨주어야 함)
    //일단은 reducer로 자동으로 간다고 생각을 하면, .. reducer에서 받아오는 파라미터 action은 저 리턴 값을 가지고 있는 것임
  };
}
