const express = require("express");
const router = express.Router();
const path = require("path");

router.get("/:dirname/:filename", async (req, res) => {
  try {
    console.log("Get image:", req.params);
    const { dirname, filename } = req.params;
    const filePath = path.join(__dirname, `../images/${dirname}`, filename);
    console.log(filePath);

    res.set("Content-Type", "image/jpg");
    res.status(200).sendFile(filePath);
  } catch (error) {
    console.log(error);
    res.status(400).json({ message: "Invalid image request" });
  }
});

module.exports = router;
