const express = require('express');
const router = express.Router();
const jwt = require('jsonwebtoken');
const bcrypt = require('bcrypt');
const connection = require('../connect');

// Secret keys for JWT
const JWT_SECRET = 'your_jwt_secret_key';
const JWT_REFRESH_SECRET = 'your_jwt_refresh_secret_key';

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

// Endpoint untuk refresh token
router.post('/token', (req, res) => {
  const { refreshToken } = req.body;

  if (!refreshToken) {
    return res.status(403).json({ error: 'No token provided' });
  }

  const sql = 'SELECT * FROM user WHERE refreshToken = ?';
  connection.query(sql, [refreshToken], (err, result) => {
    if (err) {
      return res.status(500).json({ error: 'Failed to verify token' });
    }
    if (result.length === 0) {
      return res.status(403).json({ error: 'Invalid refresh token' });
    }

    jwt.verify(refreshToken, JWT_REFRESH_SECRET, (err, user) => {
      if (err) {
        return res.status(403).json({ error: 'Invalid refresh token' });
      }

      const accessToken = jwt.sign({ id: user.id }, JWT_SECRET, { expiresIn: '15m' });
      const updateAccessTokenSql = 'UPDATE user SET tokenJwt = ? WHERE id = ?';
      connection.query(updateAccessTokenSql, [accessToken, user.id], (err, updateResult) => {
        if (err) {
          return res.status(500).json({ error: 'Failed to update access token' });
        }
        res.status(200).json({ accessToken });
      });
    });
  });
});

// Endpoint untuk logout
router.post('/logout', (req, res) => {
  const { refreshToken } = req.body;

  if (!refreshToken) {
    return res.status(403).json({ error: 'No token provided' });
  }

  const sql = 'UPDATE user SET tokenJwt = NULL, refreshToken = NULL WHERE refreshToken = ?';
  connection.query(sql, [refreshToken], (err, result) => {
    if (err) {
      return res.status(500).json({ error: 'Logout failed' });
    }
    res.status(200).json({ message: 'Logout successful' });
  });
});

// Endpoint untuk pendaftaran (register)
router.post('/register', (req, res) => {
  const { username, password } = req.body;

  // Cek apakah username atau password kosong
  if (!username || !password) {
    return res.status(400).json({ error: 'Username and password are required' });
  }

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

module.exports = router;
