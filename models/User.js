const mongoose = require("mongoose");
const bcrypt = require("bcrypt");
const saltRounds = 10;
const jwt = require("jsonwebtoken");

const userSchema = mongoose.Schema({
  name: {
    type: String,
    required: true,
    maxlength: 50,
  },
  email: {
    type: String,
    required: true,
    trim: true, //space를 없애주는 역할
    unique: 1,
  },
  password: {
    type: String,
    required: true,
    minlength: 5,
  },
  match: [String],

  token: {
    type: String,
  },
  tokenExp: {
    type: Number,
  },
});

//save를 하기 전에 무언가를 하겠다라는 의미
userSchema.pre("save", function (next) {
  var user = this;

  if (user.isModified("password")) {
    //비밀번호를 암호화 시킨다. Salt를 이용해서 비밀번호를 암호화 해야함
    //그전에 salt를 먼저 생성(saltRounds => salt가 몇글자인지?... )
    bcrypt.genSalt(saltRounds, function (err, salt) {
      if (err) return next(err);

      bcrypt.hash(user.password, salt, function (err, hash) {
        // Store hash in your password DB.
        if (err) return next(err);
        user.password = hash;
        next();
      });
    });
  } else {
    next();
  }
});

userSchema.methods.comparePassword = function (plainPassword, cb) {
  bcrypt.compare(plainPassword, this.password, function (err, isMatch) {
    if (err) return cb(err);
    return cb(null, isMatch);
  });
};

userSchema.methods.generateToken = function (cb) {
  var user = this;
  //jsonwebtoken을 이용해서 토큰을 생성하기
  var token = jwt.sign({ id: user._id.toHexString() }, "secretToken");

  user.token = token;

  user.save(function (err, user) {
    if (err) return cb(err);
    cb(null, user);
  });
};

userSchema.statics.findByToken = function (token, cb) {
  var user = this;
  console.log("ptoken: " + token);
  //토큰을 decode한다.
  jwt.verify(token, "secretToken", function (err, decoded) {
    if (err) {
      return res.status(401).json({ message: err, success: false });
    }

    //유저 아이디를 이용해서 유저를 찾은 다음에
    //클라이언트에서 가져온 토큰과 데이터 베이스에 보관된 토큰이 일치하는지 확인
    console.log("_id: " + decoded.id);
    user.findOne({ _id: decoded.id, token: token }, function (err, user) {
      if (err) return cb(err);
      return cb(null, user);
    });
  });
};

const User = mongoose.model("User", userSchema);

module.exports = User;
