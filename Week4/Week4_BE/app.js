var createError = require("http-errors");
var express = require("express");
var path = require("path");
var cookieParser = require("cookie-parser");
var logger = require("morgan");
const mongoose = require("mongoose");
const cors = require("cors");
require("dotenv").config();

const indexRouter = require("./routes/index");
const authRouter = require("./routes/auth");
const restaurantRouter = require("./routes/restaurant");
const categoryRouter = require("./routes/category");
const userRouter = require("./routes/user");
const imageRouter = require("./routes/image");

var app = express();
const PORT = 8080;

mongoose.connect(
  `${process.env.MONGO_HOST}`,
  {
    useUnifiedTopology: true,
    useNewUrlParser: true,
    useFindAndModify: false,
  },
  () => {
    console.log("connected to DB");
  }
);

// view engine setup
app.set("views", path.join(__dirname, "views"));
app.set("view engine", "pug");

app.use(logger("dev"));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, "public")));
app.use(cors({ origin: true, credentials: true }));

app.use("/", indexRouter);
app.use("/auth", authRouter);
app.use("/restaurant", restaurantRouter);
app.use("/category", categoryRouter);
app.use("/image", imageRouter);
app.use("/user", userRouter);

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

app.listen(PORT, () => {
  console.log(`Server is listening on PORT ${PORT}`);
});

module.exports = app;
