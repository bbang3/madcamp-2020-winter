var express = require('express');
var router = express.Router();

var path = require('path');
const fs = require('fs')
const multer = require('multer')

const Image = require('../models/image.js')

// handling file upload
var storage = multer.diskStorage({
    destination: (req, file, cb) => { cb(null, 'images'); },
    filename: (req, file, cb) => { cb(null, `${file.fieldname}-${Date.now()}${path.extname(file.originalname)}`); } // format: {img(fieldname)}-{date}_{filname}
});

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
let upload = multer({ storage: storage, fileFilter: fileFilter});

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
router.get('/:image_id', async (req, res) => {
    try {
        const image = await Image.findById(req.params.image_id);
        const filePath = image.path;
        console.log(filePath);

        res.set('Content-Type', image.mimeType);
        res.status(200).sendFile(filePath);
    } catch (error) {
        res.status(500).json({message: error});
    }
});

// CREATE single image
router.post('/', upload.single('img'), async (req, res) => {
    console.log(req.file);
    if(req.fileValidationError) { return res.send(req.fileValidationError); }

    try {
        const image = new Image({
            filename: req.file.originalname,
            mimeType: req.file.mimetype,
            path: path.join(__dirname, `../images/${req.file.filename}`)
        });

        const output = await image.save();
        console.log('Create Success');
        res.status(201).json({
            _id: image._id,
            filename: image.filename
        }
        );
      }
      catch (err){
        res.status(500).json({
          result: 0,
          message: err
        });
      }
});

// DELETE single image
router.delete('/:image_id', async (req, res) => {
    console.log(req.params.image_id)

  try {
    const delResult = await Image.deleteOne({_id: req.params.image_id});
    if(delResult.deletedCount == 0) {
      res.status(404).json({message: "Image not found"})
    }
    else {
      res.status(200).json({message: "Delete success"})
    }
  } catch (error) { 
    console.log(error);
    res.status(500).json({message: error});
  }
});

module.exports = router;
