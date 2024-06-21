# Haivest! - Cloud Computing Documentation ☁

## Diagram Architecture 🏗
Our application architecture is shown in the picture below. We use Cloud Run, Cloud Storage, and Cloud SQL services to support our application to run with Cloud Build. So, the frontend will consume the API from two servers; Backend and Machine Learning models 1 and 2. For the Backend, it will directly connect to Cloud SQL as a database. For the Machine Learning models, it will directly connect to Cloud Storage to store the analyzed images from the user and to Cloud SQL as a database. We put these services in asia-southeast2 region.

![rejuvify-architecture drawio (1)](https://github.com/Veronicamrcl/Haivest/blob/552c2c6ca7838297dbde361c1a0e01ad638af35b/Cloud%20Computing/Haivest%20Cloud%20Architecture.drawio.png)

## Tech Used 🔧
Our Cloud Computing project is created with:
* Cloud SQL
* Cloud Run
* Cloud Storage
* Express.js
* Flask