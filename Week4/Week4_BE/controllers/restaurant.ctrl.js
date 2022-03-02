const fs = require("fs");
const path = require("path");

const Restaurant = require("../models/restaurant");
const Menu = require("../models/menu");
const Category = require("../models/category");
const { deleteFiles, deleteFile } = require("../middlewares/file");

/* CREATE new restaurant */
exports.createRestaurant = async (req, res) => {
  console.log(req.user);
  const user = req.user;

  if (user.role !== "admin" && user.role !== "restaurantOwner") {
    return res.status(403).json({
      message: "Admin or Restaurant owner can create new restaurant info",
    });
  }

  const newRestaurant = JSON.parse(req.body.restaurant);
  console.log("CREATE restaurant:\n", newRestaurant);
  const { category } = newRestaurant;
  try {
    const categoryObject = await Category.findOne({ name: category });
    if (!categoryObject) {
      res.status(400);
      return res.json({ message: "Invalid category" });
    }

    if (req.file) newRestaurant.image = req.file.filename;

    const menus = newRestaurant.menus;
    let menuIds = [];
    if (menus) {
      const promises = menus.map((element) => {
        const menu = new Menu(element);
        return menu.save();
      });

      const savedMenus = await Promise.all(promises);
      menuIds = savedMenus.map((element) => element._id);
    }

    const restaurant = new Restaurant({
      ...newRestaurant,
      category: categoryObject._id,
      menus: menuIds,
    });

    console.log("Restaurant", restaurant);
    const output = await restaurant.save();
    user.restaurant = output._id;
    console.log(user);
    user.save();

    res.status(200).json(output);
  } catch (error) {
    console.log(error);
    res.status(400).json({ message: "Restaurant was ill-formed" });
  }
};

// RETRIEVE single Restaurant info
exports.getRestaurant = async (req, res) => {
  const { restr_id } = req.params;
  console.log(restr_id);
  try {
    const restaurant = await Restaurant.findById(restr_id, "-__v")
      .populate("menus")
      .populate("category")
      .lean();
    console.log(restaurant.name);
    return res.status(200).json({
      ...restaurant,
      category: restaurant.category.name,
    });
  } catch (error) {
    return res.status(400).json({ message: "Invalid restaurant id" });
  }
};

// RETRIEVE all restaurants info - ONLY allowed for admin
exports.getRestaurants = async (req, res) => {
  // console.log("cookie", req.headers);
  const user = req.user;
  const searchOption = {};
  if (req.query.category) {
    console.log("Category:", req.query.category);
    const categoryId = req.query.category;
    try {
      const category = await Category.findById(categoryId);
      console.log(category.name);
      searchOption.category = category._id;
    } catch (error) {
      console.log(error);
      return res.status(400).json("Category was ill-formed");
    }
  }

  const restaurants = await Restaurant.find(searchOption, "-__v")
    .populate("category")
    .lean();

  // const responseData = Object.assign(restaurants);
  restaurants.forEach((restaurant) => {
    restaurant.category = restaurant.category.name;
  });

  console.log("Restaurants: ", restaurants.length);
  return res.status(200).json(restaurants);
};

// UPDATE restaurant info
exports.updateRestaurant = async (req, res) => {
  const { restr_id } = req.params;
  try {
    console.log("Update id:", restr_id);
    console.log("Files:", req.file);

    const restaurant = await Restaurant.findById(restr_id).exec();
    if (!restaurant) throw new Error("Invalid restaurant ID");

    const updateInfo = JSON.parse(req.body.restaurant);
    console.log("UPDATE restaurant:\n", updateInfo);
    if (!updateInfo) throw new Error("Update not found");

    const { category, image, ...updateData } = updateInfo;
    if (req.file) {
      updateData.image = req.file.filename;
      deleteFile("restaurants", restaurant.image);
    }

    restaurant.category = await Category.findOne({ name: updateInfo.category });
    await restaurant.save();

    const updatedRestaurant = await Restaurant.findByIdAndUpdate(
      restr_id,
      updateData,
      { new: true }
    );

    console.log("After update:", updatedRestaurant);
    return res.status(200).json(updatedRestaurant);
  } catch (error) {
    console.log(error);
    deleteFile(req.file.originalname);
    return res.status(400).json({
      message: error.message,
    });
  }
};

// DELETE restaurant info
exports.deleteRestaurant = async (req, res) => {
  const { restr_id } = req.params;
  console.log("Delete id:", restr_id);
  try {
    const restaurant = await Restaurant.findById(restr_id);
    if (!restaurant) {
      return res.status(404).json({ message: "Restaurant not found" });
    }

    const menus = restaurant.menus;

    const output = await Menu.deleteMany().where("_id").in(menus).exec();
    console.log(output);
    await Restaurant.findByIdAndDelete(restr_id).exec();

    res.status(200).json({ message: "Delete sucess" });
  } catch (error) {
    console.log(error);
    res.status(400).json({ message: "Deleted failed" });
  }
};
