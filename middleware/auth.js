const User = require("../models/User");

let auth = (req, res, next) => {
  //인증 처리를 하는 곳

  // Get token from client header
  // console.log(req);
  let token = req.headers["token"];

  console.log("auth token: " + token);
  //토큰을 복호화 한 후 유저를 찾는다.
  User.findByToken(token, (err, user) => {
    console.log(token);
    if (err) throw err;
    if (!user) {
      return res.json({ isAuth: false, error: true, success: false });
    }
    console.log("user: ", user);
    req.token = token;
    req.user = user;
    next();
  });

  //유저가 있으면 인증

  //유저가 없으면 인증 불가
};

module.exports = { auth };
