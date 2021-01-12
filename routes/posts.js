const express = require('express');
const router = express.Router();

const path = require('path');
const fs = require('fs/promises');
const multer = require('multer');

const Post = require('../models/post.js');
const User = require('../models/user.js');

// handling file upload
const storage = multer.diskStorage({
    destination: (req, file, cb) => { cb(null, path.join(__dirname, '../images')); },
    filename: (req, file, cb) => { cb(null, `${file.fieldname}-${Date.now()}${path.extname(file.originalname)}`); } // format: {fieldname}-{date} (with ext)
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
let upload = multer({ storage: storage, fileFilter: fileFilter });

// CREATE new posts
router.post('/', upload.single('image'), async (req, res) => {
    try {
        // console.log(req);
        console.log(req.file);
        console.log(req.body.userId);
        console.log(req.body.description);
        let { userId, description } = req.body;
        const author = await User.findOne({ userId: userId });

        if (!author) {
            return res.status(404).send("User not found");
        }

        let imageFileName = req.file.filename;
        let newPost = Post({
            authorId: author._id,
            author: author.name,
            description: description,
            images: [imageFileName]
        });

        const output = await newPost.save();
        console.log(output._id);

        author.posts.push(output._id);
        author.save();

        res.status(200).json(output);
    } catch (error) {
        console.log(error);
        res.status(500).json({ message: error });
    }
});

// RETRIEVE single post
router.get('/:postId', async (req, res) => {
    try {
        console.log(req.params.postId);
        const post = await Post.findById(req.params.postId);
        if (!post) {
            return res.status(404).send('Post not found');
        }
        res.status(200).json(post);
    } catch (error) {
        res.status(500).json({ message: error });
    }
});

router.delete('/:postId', async (req, res) => {
    try {
        console.log(req.params.postId);

        const delPost = await Post.findById(req.params.postId);
        if (!delPost) {
            return res.status(404).json({ message: "Post not found" });
        }
        console.log(delPost);
        const authorId = delPost.authorId;
        const author = await User.findById(authorId);

        if (!author) {
            return res.status(404).json({ message: "Author not found" });
        }
        console.log("!");

        console.log(delPost.images);
        for (image of delPost.images) {
            const filePath = path.join(__dirname, `../images/${image}`);
            console.log(filePath);
            await fs.unlink(filePath);
        }

        await Post.findByIdAndRemove(delPost._id);
        author.posts.remove(delPost._id);
        author.save();


        res.status(200).send('Delete success');
    } catch (error) {
        res.status(500).json({ message: error });
    }
});

module.exports = router;