const express = require("express");
const router = express.Router();
const { User } = require("../models/user");

const { verifyToken } = require("../middlewares/auth");
const userCtrl = require("../controllers/user.ctrl");

router.get("/:user_id", verifyToken, userCtrl.getUser);
router.put("/:user_id", verifyToken, userCtrl.updateUser);
router.delete("/:user_id", verifyToken, userCtrl.deleteUser);

module.exports = router;
