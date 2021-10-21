const express = require("express");
const router = express.Router();

const authCtrl = require("../controllers/auth.ctrl");
const { verifyToken } = require("../middlewares/auth");

router.post("/register", authCtrl.register);
router.post("/login", authCtrl.login);
router.post("/logout", authCtrl.logout);
router.post("/update-password", verifyToken, authCtrl.updatePassword);

router.post("/check-token", authCtrl.checkTokenExpired);
router.post("/refresh-token", authCtrl.refreshToken);

router.get("/email-verify/:verify_key", authCtrl.verifyEmail);

module.exports = router;
