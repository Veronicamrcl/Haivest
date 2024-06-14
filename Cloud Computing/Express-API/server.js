require('dotenv').config(); // Mengimport dotenv untuk mengakses variabel lingkungan
const express = require('express');
const bodyParser = require('body-parser');
const app = express();
const config = require('./config/secret'); // Mengimport konfigurasi secret
const newsController = require('./controllers/newsController'); // Mengimport newsController

app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

app.use((req, res, next) => {
  res.header('Access-Control-Allow-Origin', '*');
  next();
});

const router = require('./routes/routes');
app.use('/', router);

app.listen(3000, () => {
  console.log('Server berjalan');
});
