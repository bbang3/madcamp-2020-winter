const express = require("express");
const router = express.Router();
const multer = require("multer");
const path = require("path");

const restrCtrl = require("../controllers/restaurant.ctrl");
const menuRouter = require("./menu");
const commentRouter = require("./comment");
const { verifyToken } = require("../middlewares/auth");

// handling file upload
const storage = multer.diskStorage({
  destination: (req, file, cb) => {
    cb(null, path.join(__dirname, "../images/restaurants"));
  },
  filename: (req, file, cb) => {
    cb(null, `restaurant-${Date.now()}${path.extname(file.originalname)}`);
  },
});

const fileFilter = (req, file, cb) => {
  if (
    file.mimetype === "image/jpg" ||
    file.mimetype === "image/jpeg" ||
    file.mimetype === "image/png"
  ) {
    cb(null, true);
  } else {
    req.fileValidationError = "Image uploaded is not of type jpg/jpeg or png";
    console.log("111");
    cb(null, false);
  }
};
let upload = multer({ storage: storage, fileFilter: fileFilter });

router.get("/", restrCtrl.getRestaurants);
router.get("/:restr_id", restrCtrl.getRestaurant);
router.use("/:restr_id/menu", menuRouter);
router.use("/:restr_id/comment", commentRouter);

router.post(
  "/",
  verifyToken,
  upload.single("image"),
  restrCtrl.createRestaurant
);
router.put(
  "/:restr_id",
  verifyToken,
  upload.single("image"),
  restrCtrl.updateRestaurant
);
router.delete("/:restr_id", restrCtrl.deleteRestaurant);

module.exports = router;
