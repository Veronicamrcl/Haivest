const jwt = require('jsonwebtoken');
require('dotenv').config(); // Mengimport dotenv untuk mengakses variabel lingkungan
const { secret } = require('../config/secret'); // Mengimpor secret dari config

const JWT_SECRET = secret; // Menggunakan secret key dari config

function verifyToken(req, res, next) {
  const authHeader = req.headers['authorization'];

  if (!authHeader) {
    return res.status(403).json({ error: 'No token provided' });
  }

  const token = authHeader.split(' ')[1]; // Mengambil token setelah kata 'Bearer'

  if (!token) {
    return res.status(403).json({ error: 'No token provided' });
  }

  jwt.verify(token, JWT_SECRET, (err, decoded) => {
    if (err) {
      return res.status(500).json({ error: 'Failed to authenticate token' });
    }

    req.userId = decoded.id;
    next();
  });
}

module.exports = verifyToken;
