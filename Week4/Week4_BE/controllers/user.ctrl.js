const { User, Customer } = require("../models/user");
const bcrypt = require("bcrypt");

exports.getUser = async (req, res) => {
  const { user_id } = req.params;
  console.log("Read user", user_id);

  if (user_id !== req.user._id.toString() && req.user.role !== "admin") {
    return res.status(403).json({
      error: "UnauthorizedReadError",
      message: "You can only read your own information.",
    });
  }

  try {
    const user = await User.findById(
      user_id,
      "-password -__v -emailVerifyKey"
    ).exec();

    return res.status(200).json(user);
  } catch (error) {
    console.log(error);
    return res.status(400).json({
      error: error.name,
      message: "User Read Failed",
    });
  }
};

exports.updateUser = async (req, res) => {
  const { user_id } = req.params;
  console.log("Update user", req.body);

  if (user_id !== req.user._id.toString() && req.user.role !== "admin") {
    return res.status(403).json({
      error: "UnauthorizedUpdateError",
      message: "You can only modify your own information.",
    });
  }

  if (req.body.password !== undefined) {
    const salt = await bcrypt.genSalt(10);
    const hashedPassword = await bcrypt.hash(req.body.password, salt);
    req.body.password = hashedPassword;
  }

  try {
    const user = await Customer.findByIdAndUpdate(
      user_id,
      { ...req.body },
      {
        new: true,
      }
    ).select("-password -emailVerifyKey -__v");
    console.log(user);

    return res.status(200).json(user);
  } catch (error) {
    console.log(error);
    return res.status(400).json({
      error: error.name,
      message: "User Update Failed",
    });
  }
};

exports.deleteUser = async (req, res) => {
  const { user_id } = req.params;
  console.log("Delete user", req.user);

  if (user_id !== req.user._id.toString() && req.user.role !== "admin") {
    return res.status(403).json({
      error: "UnauthorizedDeleteError",
      message: "You can only delete your own information.",
    });
  }

  try {
    const output = await User.findByIdAndRemove(user_id).exec();
    console.log(output);
    return res.status(200).json({ message: "Delete success" });
  } catch (error) {
    console.log(error);
    return res.status(400).json({
      error: error.name,
      message: "User Delete Failed",
    });
  }
};
