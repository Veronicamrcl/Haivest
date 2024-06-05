Buat database: 
CREATE DATABASE haivest; 
CREATE TABLE user (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL
);
 
Install node_modules: 'npm install express'

Menjalankan server.js: 'node server.js'