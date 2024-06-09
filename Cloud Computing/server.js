const express = require('express');
const bodyParser = require('body-parser');
const app = express();
const port = 3000;

app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

app.use((req, res, next) => {
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept, Authorization');
    next();
  });

const router = require('./routes/routes');
app.use('/', router);

// Proxy ke server Flask
const { createProxyMiddleware } = require('http-proxy-middleware');
app.use('/flask', createProxyMiddleware({ target: 'http://localhost:5000', changeOrigin: true }));

app.listen(port, ()=>{
    console.log(`Server berjalan di http://localhost:${port}`);
});