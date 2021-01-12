var express = require('express');
var router = express.Router();

var path = require('path');
const fs = require('fs');
const multer = require('multer');

const Image = require('../models/image.js');

// handling file upload
var storage = multer.diskStorage({
  destination: (req, file, cb) => { cb(null, path.join(__dirname, '../images')); },
  filename: (req, file, cb) => { cb(null, `${file.fieldname}-${Date.now()}${path.extname(file.originalname)}`); } // format: {img(fieldname)}-{date}_{filname}
});

// not used
const fileFilter = (req, file, cb) => {
  if (file.mimetype === "image/jpg" ||
    file.mimetype === "image/jpeg" ||
    file.mimetype === "image/png") {
    cb(null, true);
  } else {
    req.fileValidationError = 'Image uploaded is not of type jpg/jpeg or png';
    cb(null, false);
  }
}
let upload = multer({ storage: storage, fileFilter: fileFilter });

// RETRIEVE all images
router.get('/', async (req, res) => {
  try {
    const images = await Image.find({}, { "path": false }); // hide path in response
    console.log("Retrieve all");
    res.status(200).json(images);
  } catch (error) {
    res.status(500).json({
      message: error
    });
  }
});

// RETRIEVE single image
router.get('/:filename', async (req, res) => {
  try {
    const filePath = path.join(__dirname, '../images', req.params.filename);
    console.log(filePath);

    res.set('Content-Type', 'images/*');
    res.status(200).sendFile(filePath);
  } catch (error) {
    res.status(500).json({ message: error });
  }
});

// UPDATE single image
router.post('/', upload.single('image'), async (req, res) => {
  // console.log(req.file);
  // console.log(req);
  if (req.fileValidationError) { return res.status(500).send(req.fileValidationError); }

  console.log(req.file);
  console.log(req.body);
  res.status(200).send('Image upload success');
});

// CREATE single image
router.put('/', upload.single('image'), async (req, res) => {
  // console.log(req.file);
  // console.log(req);
  if (req.fileValidationError) { return res.status(500).send(req.fileValidationError); }

  console.log(req.file);
  console.log(req.body);
  res.status(200).send('Image upload success');
});

// DELETE single image
router.delete('/:filename', async (req, res) => {
  try {
    const filePath = path.join(__dirname, '../images', req.params.filename);
    console.log(filePath);

    fs.unlink(filePath);
    res.status(200).send('Image delete success');
  } catch (error) {
    res.status(500).json({ message: error });
  }
});

module.exports = router;
