const express = require('express');
const router = express.Router();
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const connection = require('../connect');
const verifyToken = require('../middleware/verifikasi');
const authRouter = require('../middleware/auth');
const { secret } = require('../config/secret'); // Mengimpor secret key dari config
const newsController = require('../controllers/newsController'); // Mengimpor newsController

router.use('/auth', authRouter);

// Secret key untuk JWT
const JWT_SECRET = secret;
const JWT_REFRESH_SECRET = secret; // Anda bisa menggunakan secret yang berbeda untuk refresh token jika diperlukan

// Endpoint awal
router.get("/", (req, res) => {
  res.status(200).json({
    "message": "Welcome to Haivest API."
  });
});

// Endpoint untuk pendaftaran (register)
router.post('/register', (req, res) => {
  const { name, username, password } = req.body;

  if (!name || !username || !password) {
    return res.status(400).json({ error: 'Name, username, and password are required' });
  }

  const checkUserSql = 'SELECT * FROM user WHERE username = ?';
  connection.query(checkUserSql, [username], (err, result) => {
    if (err) {
      return res.status(500).json({ error: 'Database error' });
    }
    if (result.length > 0) {
      return res.status(400).json({ error: 'Username already exists' });
    }

    bcrypt.hash(password, 10, (err, hash) => {
      if (err) {
        return res.status(500).json({ error: 'Hashing password failed' });
      }

      const sql = 'INSERT INTO user (name, username, password) VALUES (?, ?, ?)';
      connection.query(sql, [name, username, hash], (err, result) => {
        if (err) {
          return res.status(500).json({ error: 'Registration failed' });
        }
        res.status(200).json({ message: 'Registration successful' });
      });
    });
  });
});

// Endpoint untuk login
router.post('/login', (req, res) => {
  const { username, password } = req.body;

  const sql = 'SELECT * FROM user WHERE username = ?';
  connection.query(sql, [username], (err, result) => {
    if (err) {
      return res.status(500).json({ error: 'Login failed' });
    }
    if (result.length === 0) {
      return res.status(401).json({ error: 'Username not found' });
    }

    bcrypt.compare(password, result[0].password, (err, match) => {
      if (err) {
        return res.status(500).json({ error: 'Login failed' });
      }
      if (!match) {
        return res.status(401).json({ error: 'Incorrect password' });
      }

      const accessToken = jwt.sign({ id: result[0].id }, JWT_SECRET, { expiresIn: '15m' });
      const refreshToken = jwt.sign({ id: result[0].id }, JWT_REFRESH_SECRET, { expiresIn: '7d' });

      const updateTokenSql = 'UPDATE user SET tokenJwt = ?, refreshToken = ? WHERE id = ?';
      connection.query(updateTokenSql, [accessToken, refreshToken, result[0].id], (err, updateResult) => {
        if (err) {
          return res.status(500).json({ error: 'Failed to store tokens' });
        }
        res.status(200).json({ message: 'Login successful', accessToken, refreshToken });
      });
    });
  });
});

// Endpoint yang membutuhkan verifikasi token
router.get('/protected', verifyToken, (req, res) => {
  res.status(200).json({ message: 'This is a protected route', userId: req.userId });
});

// Endpoint untuk mendapatkan berita tentang tanaman hidroponik
router.get('/news', newsController.getHydroponicNews);

module.exports = router;
