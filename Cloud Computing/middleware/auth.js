const express = require('express');
const router = express.Router();
const jwt = require('jsonwebtoken');
const bcrypt = require('bcrypt');
const connection = require('../connect');

// Secret key untuk JWT
const JWT_SECRET =  'your_jwt_secret_key';

// Endpoint untuk login
router.post('/login', (req, res) => {
  const { username, password } = req.body;

  console.log('Login request received for username:', username);

  // Query untuk mencari pengguna berdasarkan username
  const sql = 'SELECT * FROM user WHERE username = ?';
  connection.query(sql, [username], (err, result) => {
    if (err) {
      console.error('Database error:', err);
      return res.status(500).json({ error: 'Login failed' });
    }
    if (result.length === 0) {
      console.warn('Username not found:', username);
      return res.status(401).json({ error: 'Username not found' });
    }

    // Membandingkan password yang dimasukkan dengan password yang disimpan dalam database
    bcrypt.compare(password, result[0].password, (err, match) => {
      if (err) {
        console.error('Error comparing passwords:', err);
        return res.status(500).json({ error: 'Login failed' });
      }
      if (!match) {
        console.warn('Incorrect password for username:', username);
        return res.status(401).json({ error: 'Incorrect password' });
      }

      // Membuat token JWT
      const token = jwt.sign({ id: result[0].id }, JWT_SECRET, { expiresIn: '1h' });
      console.log('Login successful for username:', username);
      res.status(200).json({ message: 'Login successful', token });
    });
  });
});

module.exports = router;
