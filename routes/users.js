const express = require('express');
const router = express.Router();
const User = require('../models/user.js');

// GET ALL
router.get('/', async (req, res) => {
    try {
        const output = await User.find();
        res.status(200).json(output);
    } catch (error) {
        res.status(500).json({ message: error });
    }

});

// GET Specific user
router.get('/:user_id', async (req, res) => {
    try {
        const output = await User.findById(req.params.user_id);
        res.status(200).json(output);
    } catch (error) {
        res.status(500).json({ message: error });
    }
});

// GET following friends info
router.get('/:user_id/following', async (req, res) => {
    try {
        console.log(req.params.user_id);
        const currentUser = await User.findById(req.params.user_id);
        let followingUserArray = [];
        console.log(currentUser.followingIds);

        for (followingId of currentUser.followingIds) {
            let followingUser = await User.findById(followingId, '-userId -password -signupDate -followingIds');
            followingUserArray.push(followingUser);
        }
        res.status(200).json(
            followingUserArray
        );
    } catch (error) {
        res.status(500).json({ message: error });
    }
});

// user ADD
router.post('/', async (req, res) => {
    console.log(req.body);
    let newUser = new User(req.body);
    try {
        const output = await newUser.save();
        res.status(200).json(output);
    } catch (error) {
        res.status(500).json({ message: error });
    }
})

// login request
router.post('/login', async (req, res) => {
    try {
        console.log(req.body.userId, req.body.password);
        const currentUser = await User.findOne({userId: req.body.userId, password: req.body.password});
        console.log(currentUser);
        if(currentUser === null) res.status(404).send("User not found");
        else res.status(200).json(currentUser);
    } catch (error) {
        res.status(500).json({ message: error });
    }
})

module.exports = router;