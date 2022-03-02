const jwt = require("jsonwebtoken");
const { User } = require("../models/user");
const nodemailer = require("nodemailer");
require("dotenv").config();

exports.verifyToken = async (req, res, next) => {
  const token = req.header("token");
  if (!token) {
    console.log("Token not found");
    return res.status(401).send("Access Denied");
  }
  try {
    const verified = jwt.verify(token, process.env.TOKEN_SECRET);
    console.log("verifiedASDF: ", verified);
    const user = await User.findById(verified._id);
    if (!user) return res.status(401).json({ message: "Invalid user" });
    console.log(user);

    req.user = user;
    next();
  } catch (error) {
    console.log(error);
    if (error.name === "TokenExpiredError") {
      return res.status(401).json({ message: "Token Expired" });
    }
    return res.status(401).json({ message: "Invalid token" });
  }
};

exports.createAccessToken = (user) => {
  const token = jwt.sign({ _id: user._id }, process.env.TOKEN_SECRET, {
    expiresIn: "1h",
  });
  return token;
};

exports.createRefreshToken = (user) => {
  const token = jwt.sign({ _id: user._id }, process.env.TOKEN_SECRET, {
    expiresIn: "7d",
  });
  return token;
};

exports.sendVerifyEmail = async (user) => {
  try {
    const smtpTransport = nodemailer.createTransport({
      service: "Gmail",
      auth: {
        user: process.env.ADMIN_EMAIL_ID,
        pass: process.env.ADMIN_EMAIL_PASSWORD,
      },
      tls: {
        rejectUnauthorized: false,
      },
    });

    const verifyUrl = `${process.env.BASE_URL}/auth/email-verify/${user.emailVerifyKey}`;
    const mailOptions = {
      from: process.env.ADMIN_EMAIL_ID,
      to: `${user.username}@gmail.com`,
      subject: "인증 메일입니다.",
      html:
        `Hello, ${user.username} <br /> Click to verify your account <br />` +
        `<a href=${verifyUrl}> 이메일 인증하기 </a>`,
    };

    const result = await smtpTransport.sendMail(mailOptions);
    console.log(result);
  } catch (error) {
    console.log(error);
  }
};
