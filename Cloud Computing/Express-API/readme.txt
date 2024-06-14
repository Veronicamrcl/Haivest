Buat database: 
CREATE DATABASE haivest; 
CREATE TABLE user (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  tokenJwt VARCHAR(255) NULL,
  refresToken VARCHAR(255) NULL
);
 
Install node_modules: 'npm install express'

Menjalankan server.js: 'node server.js'


cek db: mysql -h 34.101.54.172 -u root -p haivest

deploy docker: gcloud builds submit  --tag gcr.io/$GOOGLE_CLOUD_PROJECT/haivest-api-backend

deploy cloud run: gcloud run deploy haivest-api-backend \
 --image gcr.io/$GOOGLE_CLOUD_PROJECT/haivest-api-backend \
 --platform managed \
 --region asia-southeast2 \
 --allow-unauthenticated \
 --max-instances=3 \
 --port=3000

buat db MySQL: gcloud sql databases create haivest --instance=haivest-sql

gcloud sql connect haivest-sql --user=root --quiet --project=haivest1 <<EOF
USE haivest;
  CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    tokenJwt VARCHAR(255) NULL,
    refreshToken VARCHAR(255) NULL
  );
EOF
