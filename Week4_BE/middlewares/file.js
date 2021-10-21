const fs = require("fs");
const path = require("path");

exports.deleteFiles = async (files) => {
  const promises = files.map((file) => {
    const filePath = path.join(
      __dirname,
      `../images/menus/${file.originalname}`
    );
    return fs.unlink(filePath, function () {});
  });

  const output = await Promise.all(promises);
  console.log("Delete files output:", output);
};

exports.deleteFile = async (dirname, filename) => {
  const filePath = path.join(__dirname, "../images", dirname, filename);
  fs.unlink(filePath, (error) => {
    console.log(error);
  });
};
