Jalanin di local: pip install --no-cache-dir -r requirements.txt
python app.py


Jalanin di cloud:
gcloud builds submit \
 --tag gcr.io/$GOOGLE_CLOUD_PROJECT/haivest-ml-api


gcloud run deploy haivest-ml-api \
 --image gcr.io/$GOOGLE_CLOUD_PROJECT/haivest-ml-api \
 --platform managed \
 --region asia-southeast2 \
 --allow-unauthenticated \
 --max-instances=3 \
 --port=9000 \
 --memory=8Gi