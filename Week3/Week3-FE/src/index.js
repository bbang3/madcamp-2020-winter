import React from "react";
import ReactDOM from "react-dom";
import "./index.css";
import App from "./App";
import reportWebVitals from "./reportWebVitals";
import { Provider } from "react-redux";
import { applyMiddleware, createStore } from "redux";
import Reducer from "./_reducers"; //index.js에 존재하는 리덕스를 가져온다.
import promiseMiddleware from "redux-promise"; //리덕스 promise를 연결시킴
import ReduxThunk from "redux-thunk"; //리덕스 function을 연결시킴
import { createMuiTheme, MuiThemeProvider } from "@material-ui/core";

const createStoreWithMiddleware = applyMiddleware(
  promiseMiddleware,
  ReduxThunk
)(createStore);

const theme = createMuiTheme({
  typography: {
    fontFamily: "Nanum Gothic",
  },
});

ReactDOM.render(
  <Provider
    store={createStoreWithMiddleware(
      Reducer,
      window._REDUX_DEVTOOLS_EXTENSION_ && window._REDUX_DEVTOOLS_EXTENSION_() //컴퓨터에서 다운받은 것들을 가져온다.
    )}
  >
    <MuiThemeProvider theme={theme}>
      <App />
    </MuiThemeProvider>
  </Provider>,
  // <React.StrictMode>
  //   <App />
  // </React.StrictMode>,
  document.getElementById("root")
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
