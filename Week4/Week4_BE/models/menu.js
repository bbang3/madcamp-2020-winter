const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const Restaurant = require("./restaurant");

const sizeSchema = new Schema({
  size: String,
  price: { type: Number, required: true },
});

const menuSchema = new Schema({
  name: { type: String, required: true },
  description: { type: String },
  sizes: [
    {
      type: sizeSchema,
      required: true,
    },
  ],
  image: { type: String },
});

menuSchema.pre("remove", async function (next) {
  const output = await this.model("Restaurant")
    .updateMany({ menus: this._id }, { $pull: { menus: this._id } })
    .exec();
  console.log(output);
  next();
});

module.exports = mongoose.model("Menu", menuSchema);
