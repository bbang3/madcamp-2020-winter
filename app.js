let createError = require('http-errors');
let express = require('express');
let path = require('path');
let cookieParser = require('cookie-parser');
let logger = require('morgan');
const mongoose = require('mongoose')


let indexRouter = require('./routes/index');
let phoneRouter = require('./routes/phones');
let imageRouter = require('./routes/images');
let userRouter = require('./routes/users');

let app = express();
const PORT = 8080;

// const DBConfig = require('./.config_db.json')

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'pug');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

// connect to DB
// console.log(`mongodb://${DBConfig.username}:${DBConfig.passwd}@localhost:27017/${DBConfig.db}`);
mongoose.connect(
  `mongodb://192.249.18.244:27017/madcamp`, {
  useUnifiedTopology: true,
  useNewUrlParser: true,
  useFindAndModify: false
}, () => { console.log('connected to DB') }
);


app.use('/', indexRouter);
app.use('/api/phone', phoneRouter);
app.use('/api/image', imageRouter);
app.use('/api/user', userRouter);

// catch 404 and forward to error handler
app.use(function (req, res, next) {
  next(createError(404));
});

// error handler
app.use(function (err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

app.listen(PORT, () => {
  console.log(`Server is listening on PORT ${PORT}`)
});

module.exports = app;
