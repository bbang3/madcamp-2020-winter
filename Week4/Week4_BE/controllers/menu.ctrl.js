const fs = require("fs");
const path = require("path");

const Restaurant = require("../models/restaurant");
const Menu = require("../models/menu");
const Category = require("../models/category");
const { deleteFile } = require("../middlewares/file");

exports.getMenu = async (req, res) => {
  const { menu_id } = req.params;
  console.log("Menu:", menu_id);

  try {
    const menu = await Menu.findById(menu_id).exec();
    if (!menu) throw new Error();

    console.log(menu.name);
    return res.status(200).json(menu);
  } catch (error) {
    console.log(error);
    return res.status(400).json({
      error: "InvalidMenuReadError",
      message: "Invalid request",
    });
  }
};

exports.addMenu = async (req, res) => {
  const { restr_id } = req.params;
  console.log("Restaurant:", restr_id);

  const restaurant = await Restaurant.findById(restr_id).exec();
  if (!restaurant) {
    return res.status(400).json({
      error: "RestaurantNotFoundError",
      message: "Invalid Restaurant ID",
    });
  }

  try {
    const menuInfo = JSON.parse(req.body.menu);
    console.log(menuInfo);
    const menu = new Menu({
      ...menuInfo,
      image: req.file.filename,
    });
    const savedMenu = await menu.save();
    console.log(savedMenu);

    restaurant.menus.push(savedMenu._id);
    const savedRestaurant = await restaurant.save();

    return res.status(200).json(savedRestaurant);
  } catch (error) {
    console.log(error);
    return res.status(400).json({
      error: error.name,
      message: "Invalid Request",
    });
  }
};

exports.updateMenu = async (req, res) => {
  const { restr_id, menu_id } = req.params;
  console.log("Restaurant:", restr_id, ", Menu:", menu_id);
  const user = req.user;

  if (user.restaurant.toString() !== restr_id && user.role !== "admin") {
    return res.status(403).json({
      error: "UnauthorizedUpdateError",
      message: "No permission to update menu",
    });
  }

  try {
    // check if received restaurant id and menu id are valid
    const restaurant = await Restaurant.findById(restr_id)
      .where("menus")
      .in(menu_id);
    if (!restaurant) {
      return res.status(400).json({
        error: "MenuNotFoundError",
        message: "Invalid request",
      });
    }

    const menu = await Menu.findById(menu_id);
    const { image, ...newMenu } = JSON.parse(req.body.menu);
    if (req.file) {
      console.log(req.file);
      newMenu.image = req.file.filename;
      deleteFile("menus", menu.image);
    }

    console.log(newMenu);
    const output = await Menu.findByIdAndUpdate(menu_id, newMenu, {
      new: true,
    }).exec();
    console.log(output);

    return res.json({ message: "Update success" });
  } catch (error) {
    console.log(error);
    return res.status(400).json({
      error: error.name,
      message: "Invalid request",
    });
  }
};

exports.deleteMenu = async (req, res) => {
  const { restr_id, menu_id } = req.params;
  console.log("Restaurant:", restr_id, ", Menu:", menu_id);
  const user = req.user;

  if (user.restaurant.toString() !== restr_id && user.role !== "admin") {
    return res.status(403).json({
      error: "UnauthorizedDeleteError",
      message: "No permission to delete menu",
    });
  }

  try {
    // check if received restaurant id and menu id are valid
    const restaurant = await Restaurant.findById(restr_id)
      .where("menus")
      .in(menu_id);
    if (!restaurant) {
      return res.status(400).json({
        error: "MenuNotFoundError",
        message: "Invalid request",
      });
    }
    const menu = await Menu.findById(menu_id).exec();
    deleteFile("menus", menu.image);
    menu.remove();
    return res.status(200).json({ message: "Delete success" });
  } catch (error) {
    console.log(error);
    return res.status(400).json({
      error: error.name,
      message: "Invalid request",
    });
  }
};
