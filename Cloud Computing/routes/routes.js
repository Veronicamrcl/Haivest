// routes.js
const express = require('express');
const router = express.Router();
const bcrypt = require('bcrypt');
const connection = require('../connect'); 
const verifyToken = require('../middleware/verifikasi');
const authRouter = require('../middleware/auth');
router.use('/auth', authRouter);

//Endpoint awal
router.get("/", (req, res) => {
  res.status(200).json({
      "message": "Welcome to Haivest API."
  })
});

// Endpoint untuk pendaftaran (register)
router.post('/register', (req, res) => {
  const { username, password } = req.body;

  // Hash password sebelum menyimpan ke database
  bcrypt.hash(password, 10, (err, hash) => {
    if (err) {
      return res.status(500).json({ error: 'Hashing password failed' });
    }

    // Query untuk menambahkan pengguna baru ke database
    const sql = 'INSERT INTO user (username, password) VALUES (?, ?)';
    connection.query(sql, [username, hash], (err, result) => {
      if (err) {
        return res.status(500).json({ error: 'Registration failed' });
      }
      res.status(200).json({ message: 'Registration successful' });
    });
  });
});

// Endpoint untuk masuk (login)
router.post('/login', (req, res) => {
  const { username, password } = req.body;

  // Query untuk mencari pengguna berdasarkan username
  const sql = 'SELECT * FROM user WHERE username = ?';
  connection.query(sql, [username], (err, result) => {
    if (err) {
      return res.status(500).json({ error: 'Login failed' });
    }
    if (result.length === 0) {
      return res.status(401).json({ error: 'Username not found' });
    }

    // Membandingkan password yang dimasukkan dengan password yang disimpan dalam database
    bcrypt.compare(password, result[0].password, (err, match) => {
      if (err) {
        return res.status(500).json({ error: 'Login failed' });
      }
      if (!match) {
        return res.status(401).json({ error: 'Incorrect password' });
      }
      res.status(200).json({ message: 'Login successful' });
    });
  });
});


// Endpoint yang membutuhkan verifikasi token
router.get('/protected', verifyToken, (req, res) => {
  res.status(200).json({ message: 'This is a protected route', userId: req.userId });
});

module.exports = router;
