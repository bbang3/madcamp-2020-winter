const Restaurant = require("../models/restaurant");
const Comment = require("../models/comment");
const { Error } = require("mongoose");

exports.getComments = async (req, res) => {
  const { restr_id } = req.params;

  const restaurant = await Restaurant.findById(restr_id);
  if (!restaurant) {
    return res.status(400).json({
      error: "RestaurantNotFoundError",
      message: "Invalid Restaurant ID",
    });
  }

  console.log(restaurant.comments);
  return res.json(restaurant.comments);
};

exports.addComment = async (req, res) => {
  const { restr_id } = req.params;
  console.log(req.body);

  const restaurant = await Restaurant.findById(restr_id);
  if (!restaurant) {
    return res.status(400).json({
      error: "RestaurantNotFoundError",
      message: "Invalid Restaurant ID",
    });
  }
  const userId = req.user._id;
  const nickname =
    req.user.role === "restaurantOwner" ? restaurant.name : req.user.nickname;
  const { body } = req.body;

  try {
    const comment = await Comment.create({
      userId: userId,
      nickname: nickname,
      body: body,
    });
    restaurant.comments.push(comment);
    console.log(restaurant.comments);
    const savedRestaurant = await restaurant.save();
    return res.status(200).json(savedRestaurant.comments);
  } catch (error) {
    console.log(error);
    return res.status(400).json({
      error: "InvalidCommentFormatError",
      message: "Comment format is invalid",
    });
  }
};

exports.updateComment = async (req, res) => {
  const { restr_id, comment_id } = req.params;
  const restaurant = await Restaurant.findById(restr_id);
  //   console.log(restaurant);

  try {
    if (!restaurant) {
      throw new Error.DocumentNotFoundError();
    }

    const { body } = req.body;
    const comment = restaurant.comments.find(
      (comment) => comment._id.toString() === comment_id
    );
    if (!comment) {
      throw new Error.DocumentNotFoundError();
    }

    if (req.user._id.toString() !== comment.userId) {
      return res.status(403).json({
        error: "UnauthorizedCommentUpdateError",
        message: "You can only update the comment you wrote yourself",
      });
    }
    comment.body = body;
    console.log(comment);

    const savedRestaurant = await restaurant.save();
    return res.status(200).json(savedRestaurant.comments);
  } catch (error) {
    console.log(error);
    return res.status(400).json({
      error: "InvalidUpdateError",
      message: "Update Failed",
    });
  }
};

exports.deleteComment = async (req, res) => {
  const { restr_id, comment_id } = req.params;
  const restaurant = await Restaurant.findById(restr_id);

  if (!restaurant) {
    return res.status(400).json({
      error: "RestaurantNotFoundError",
      message: "Invalid Restaurant ID",
    });
  }
  const toDelete = restaurant.comments.find(
    (comment) => comment._id.toString() === comment_id
  );
  if (toDelete.userId !== req.user._id.toString()) {
    return res.status(403).json({
      error: "UnauthorizedCommentDeleteError",
      message: "You can only delete the comment you wrote yourself",
    });
  }

  const newComments = restaurant.comments.filter(
    (comment) => comment._id.toString() !== comment_id
  );
  console.log(newComments);
  if (restaurant.comments.length === newComments.length) {
    return res.status(400).json({
      error: "InvalidCommentError",
      message: "Invalid Comment ID",
    });
  }

  try {
    restaurant.comments = newComments;
    const savedRestaurant = await restaurant.save();
    return res.status(200).json(savedRestaurant.comments);
  } catch (error) {
    console.log(error);
    return res.status(400).json({
      error: "InvalidDeleteError",
      message: "Delete Failed",
    });
  }
};
