const express = require("express");
const router = express.Router({ mergeParams: true });
const multer = require("multer");
const path = require("path");

const { verifyToken } = require("../middlewares/auth");
const menuCtrl = require("../controllers/menu.ctrl");

// handling file upload
const storage = multer.diskStorage({
  destination: (req, file, cb) => {
    cb(null, path.join(__dirname, "../images/menus"));
  },
  filename: (req, file, cb) => {
    cb(null, `menu-${Date.now()}${path.extname(file.originalname)}`);
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
    console.log("error!");
    req.fileValidationError = "Image uploaded is not of type jpg/jpeg or png";
    cb(null, false);
  }
};
let upload = multer({ storage: storage, fileFilter: fileFilter });

router.get("/:menu_id", menuCtrl.getMenu);
router.post("/", verifyToken, upload.single("image"), menuCtrl.addMenu);
router.put(
  "/:menu_id",
  verifyToken,
  upload.single("image"),
  menuCtrl.updateMenu
);
router.delete("/:menu_id", verifyToken, menuCtrl.deleteMenu);

module.exports = router;
