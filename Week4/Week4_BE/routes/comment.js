const express = require("express");
const router = express.Router({ mergeParams: true });
const Comment = require("../models/comment");

const { verifyToken } = require("../middlewares/auth");
const commentCtrl = require("../controllers/comment.ctrl");

router.get("/", commentCtrl.getComments);
router.post("/", verifyToken, commentCtrl.addComment);
router.put("/:comment_id", verifyToken, commentCtrl.updateComment);
router.delete("/:comment_id", verifyToken, commentCtrl.deleteComment);

module.exports = router;
