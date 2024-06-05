var mysql = require('mysql2');
require('dotenv').config();

const connection = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: '', 
  database: 'haivest' 
});

connection.connect((err) => {
  if (err) {
    console.error('Koneksi ke MySQL gagal: ', err);
    return;
  }
  console.log('MYSQL berhasil terkoneksi');
});

module.exports = connection;