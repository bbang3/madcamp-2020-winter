var express = require("express");
var router = express.Router();
const Restaurant = require("../models/restaurant");
const Category = require("../models/category");

/* GET home page. */
router.get("/", function (req, res, next) {
  res.render("index", { title: "Express" });
});

module.exports = router;
