const express = require('express');
const router = express.Router();
const bcrypt = require('bcrypt');
const connection = require('../connect'); 
const verifyToken = require('../middleware/verifikasi');
const authRouter = require('../middleware/auth');

router.use('/auth', authRouter);

// Endpoint awal
router.get("/", (req, res) => {
  res.status(200).json({
    "message": "Welcome to Haivest API."
  });
});

// Endpoint untuk pendaftaran (register)
router.post('/register', (req, res) => {
  const { username, password } = req.body;

  bcrypt.hash(password, 10, (err, hash) => {
    if (err) {
      return res.status(500).json({ error: 'Hashing password failed' });
    }

    const sql = 'INSERT INTO user (username, password) VALUES (?, ?)';
    connection.query(sql, [username, hash], (err, result) => {
      if (err) {
        return res.status(500).json({ error: 'Registration failed' });
      }
      res.status(200).json({ message: 'Registration successful' });
    });
  });
});

// Endpoint yang membutuhkan verifikasi token
router.get('/protected', verifyToken, (req, res) => {
  res.status(200).json({ message: 'This is a protected route', userId: req.userId });
});

module.exports = router;
