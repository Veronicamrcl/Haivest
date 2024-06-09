Buat database: 
CREATE DATABASE haivest; 
CREATE TABLE user (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  tokenJwt VARCHAR(255) NULL,
  refresToken VARCHAR(255) NULL
);
 
Install node_modules: 'npm install express'

Menjalankan server.js: 'node server.js'