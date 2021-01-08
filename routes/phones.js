var express = require('express');
var router = express.Router();
const Phone = require('../models/phone.js')

/* GET all phones. */
router.get('/', async (req, res, next) => {
  try {
    const phones = await Phone.find()
    console.log("Retrieve all");
    res.status(200).json(phones);
  } catch (error) {
    res.status(500).json({
      message: error
    })
  }
});

// CREATE new phones
router.post("/", async (req, res, next) => {
  var { name, phoneNumber, email } = req.body;
  var phone = new Phone({
    name: name,
    phoneNumber: phoneNumber,
    email: email
  });
  // phone.name = name;
  // phone.phoneNumber = phoneNumber;
  // phone.email = email

  try {
    const newPhone = await phone.save();
    console.log('Create Success');
    res.status(200).json({
      message: "Create success",
      data: newPhone
    });
  }
  catch (err){
    res.status(500).json({
      result: 0,
      message: err
    });
  }
});

// RETRIEVE specific phone
router.get('/:phone_id', async (req, res) => {
  console.log(req.params.phone_id);

  try {
    const phone = await Phone.findById(req.params.phone_id)
    res.status(200).json({
      message: "Read single item success",
      data: phone
    })
  } catch (error) {
    res.status(500).json({
      message: error
    })
  }
})

module.exports = router;
