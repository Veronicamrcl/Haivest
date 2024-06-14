// profile.js
const express = require('express');
const router = express.Router();
const connection = require('../connect');
const verifyToken = require('../middleware/verifikasi');

// 1. GET Profile
router.get('/', verifyToken, (req, res) => {
    const userId = req.userId; 

    const sql = 'SELECT id, name, username FROM user WHERE id = ?';
    connection.query(sql, [userId], (err, result) => {
        if (err) {
            return res.status(500).json({ error: 'Gagal mengambil profil' });
        }
        if (result.length === 0) {
            return res.status(404).json({ error: 'Pengguna tidak ditemukan' });
        }

        const profile = result[0];
        res.status(200).json({ profile });
    });
});

// 2. PUT Profile
router.put('/', verifyToken, (req, res) => {
    const userId = req.userId; 
    const { name, username } = req.body;

    if (!name || !username) {
        return res.status(400).json({ error: 'Nama dan username harus diisi' });
    }

    const sql = 'UPDATE user SET name = ?, username = ? WHERE id = ?';
    connection.query(sql, [name, username, userId], (err, result) => {
        if (err) {
            return res.status(500).json({ error: 'Gagal memperbarui profil' });
        }
        res.status(200).json({ message: 'Profil berhasil diperbarui' });
    });
});

// 3. DELETE Account
router.delete('/', verifyToken, (req, res) => {
    const userId = req.userId;

    const sql = 'DELETE FROM user WHERE id = ?';
    connection.query(sql, [userId], (err, result) => {
        if (err) {
            return res.status(500).json({ error: 'Gagal menghapus akun' });
        }
        res.status(200).json({ message: 'Akun berhasil dihapus' });
    });
});

module.exports = router;
