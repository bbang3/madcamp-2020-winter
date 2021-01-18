const express = require("express");
const router = express.Router();
const User = require("../models/User");
const { auth } = require("../middleware/auth");

/* GET users listing. */
router.get("/", function (req, res, next) {
  res.send("User router");
});

router.post("/register", async (req, res) => {
  //회원 가입 할때 필요한 정보들을 client에서 가져오면
  //그것들을 데이터 베이스에 넣어준다.
  try {
    const user = new User(req.body);
    const output = await user.save();

    console.log("Register success");
    res.status(200).json({ success: true });
  } catch (error) {
    res.status(500).json({ success: false, ...error });
  }
});

router.post("/login", async (req, res) => {
  //요청된 이메일을 데이터베이스에서 있는지 찾는다.
  try {
    const user = await User.findOne({ email: req.body.email });
    if (!user) {
      return res.status(404).json({
        loginSuccess: false,
        message: "User not found",
      });
    }

    // 요청된 이메일이 데이터베이스에 있다면 비밀번호가 맞는 비밀번호인지 확인.
    user.comparePassword(req.body.password, (err, isMatch) => {
      if (!isMatch)
        return res.status(500).json({
          loginSuccess: false,
          message: "Wrong password",
        });
      //비밀번호까지 맞다면 토큰을 생성하기.
      user.generateToken((err, user) => {
        if (err) return res.status(500).send(err);

        // 토큰을 저장한다. 어디에? 쿠키,로컬 스토리지
        return res
          .cookie("x_auth", user.token)
          .status(200)
          .json({ loginSuccess: true, userId: user._id }); //리턴 값이 lofinSuccess true와 userid 임
      });
    });
  } catch (error) {
    res.status(500).json({
      loginSuccess: false,
      message: error,
    });
  }
});

router.get("/auth", auth, (req, res) => {
  //여기까지 미들웨어를 통과해왔다는 얘기는 Authecation 이 true 라는 말
  res.status(200).json({
    _id: req.user._id,
    isAdmin: req.user.role === 0 ? false : true,
    isAuth: true,
    email: req.user.email,
    name: req.user.name,
    lastname: req.user.lastname,
    role: req.user.role,
    image: req.user.image,
  });
});

// router.get("/logout", auth, (req, res) => {
//   console.log("여기에 들어왔나?? ㅜㅜ");
//   User.findOneAndUpdate({ _id: req.user._id }, { token: "" }, (err, user) => {
//     console.log("여기에 들어왔나?? ㅜㅜ 2");
//     if (err) return res.json({ success: falsedfgdfg, err });
//     return res.status(200).send({
//       success: true,
//     });
//   });
// });

module.exports = router;
