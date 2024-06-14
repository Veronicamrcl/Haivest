from flask import Flask, request, jsonify
import numpy as np
from tensorflow.keras.models import load_model
import tensorflow_hub as hub
from PIL import Image
from google.cloud import storage
import mysql.connector
import os
from datetime import datetime
from jwt.exceptions import ExpiredSignatureError, InvalidTokenError
import jwt
from functools import wraps
import random

import tensorflow as tf
import cv2
from sklearn.cluster import KMeans
from collections import Counter

app = Flask(__name__)

model = load_model('lettuce_model.h5', compile=False, custom_objects={'KerasLayer': hub.KerasLayer})
class_names = ['FN', '-N', '-P', '-K']

def get_image(image_bytes):
    np_arr = np.fromstring(image_bytes, np.uint8)
    image = cv2.imdecode(np_arr, cv2.IMREAD_COLOR)
    image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
    return image

def get_colors(image, number_of_colors):
    modified_image = cv2.resize(image, (224, 224), interpolation=cv2.INTER_AREA)
    modified_image = modified_image.reshape(modified_image.shape[0] * modified_image.shape[1], 3)
    clf = KMeans(n_clusters=number_of_colors, n_init=10)
    labels = clf.fit_predict(modified_image)
    counts = Counter(labels)
    center_colors = clf.cluster_centers_
    ordered_colors = [center_colors[i] / 255 for i in counts.keys()]
    rgb_colors = [ordered_colors[i] * 255 for i in counts.keys()]
    return rgb_colors

@app.route('/')
def home():
    message = "Welcome to Haivest API."
    return jsonify(message)

@app.route('/predict', methods=['POST'])
def predict():
    if 'file' not in request.files:
        return jsonify({'error': 'No file part'}), 400

    file = request.files['file']
    if file.filename == '':
        return jsonify({'error': 'No selected file'}), 400

    img = get_image(file.read())
    features = get_colors(img, 3)
    features = np.expand_dims(features, axis=0)

    predictions = model.predict(features)
    predicted_class = class_names[np.argmax(predictions[0])]
    confidence = round(100 * (np.max(predictions[0])), 2)

    return jsonify({
        'predicted_class': predicted_class,
        'confidence': confidence
    })

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8080)
