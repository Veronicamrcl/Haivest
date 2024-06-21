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

def nutrition_info(predicted_class):
    data = {
        'FN': {
            "message": "Congratulations, your plant is healthy!",
            "tips": [
                "Proper Watering: Ensure the plants receive adequate water without overwatering.",
                "Regular Fertilization: Use balanced fertilizers suitable for the type of plants.",
                "Choosing Rich Growing Media: Select nutrient-rich growing media.",
                "Soil pH Check: Ensure the soil pH is appropriate for optimal nutrient absorption.",
                "Crop Rotation: Practice crop rotation to prevent depletion of specific nutrients in the soil.",
                "Pruning: Prune dead or unhealthy parts of the plants to prevent the spread of diseases."
            ]
        },
        '-N': {
            "message": "Your plant is suffering from nitrogen deficiency.",
            "cause_disease": "Nitrogen deficiency can stunt plant growth as nitrogen is a key component of chlorophyll, proteins, and nucleic acids. Plants lacking nitrogen will experience suboptimal photosynthesis, leading to reduced yields and resistance to diseases.",
            "symptoms": [
                "Yellow Leaves: Leaves start yellowing, especially the older ones.",
                "Stunted Growth: Plant growth becomes slow and stunted.",
                "Small Leaves: Leaves tend to be smaller than usual.",
                "Reduced Productivity: Decrease in the number and quality of flowers and fruits."
            ],
            "tips": [
                "Use Nitrogen Fertilizers: Apply nitrogen-containing fertilizers like urea or ammonium nitrate.",
                "Plant Legumes: Grow legumes that can fix nitrogen from the air, such as beans and peas.",
                "Composting: Use compost rich in nitrogen.",
                "Crop Rotation: Rotate crops with nitrogen-fixing plants to enrich the soil with nitrogen."
            ]
        },
        '-P': {
            "message": "Your plant is suffering from phosphorus deficiency.",
            "cause_disease": "Phosphorus deficiency can disrupt the formation of ATP (adenosine triphosphate), crucial for cellular energy. Plants lacking phosphorus will show stunted root, flower, and fruit development, and reduced resistance to environmental stress and diseases.",
            "symptoms": [
                "Purple or Red Leaves: Leaves may turn purple or red, especially the older ones.",
                "Stunted Growth: Overall plant growth is stunted with underdeveloped roots.",
                "Reduced Flower and Fruit Production: Plants may produce fewer flowers and fruits.",
                "Leaf Death: Older leaves may die off faster than normal."
            ],
            "tips": [
                "Use Phosphorus Fertilizers: Apply phosphorus-containing fertilizers like superphosphate or rock phosphate.",
                "Grow Plants with Strong Root Systems: Plant species with strong root systems that can access deeper soil phosphorus.",
                "Composting: Use compost rich in phosphorus.",
                "Lime the Soil: Ensure the soil pH is not too low (acidic), as phosphorus is more available in neutral to slightly alkaline soils."
            ]
        },
        '-K': {
            "message": "Your plant is suffering from potassium deficiency.",
            "cause_disease": "Potassium deficiency can impair a plant's ability to regulate water and nutrient transport, enzyme activation, and photosynthesis. Plants lacking potassium are more susceptible to drought, frost, and disease.",
            "symptoms": [
                "Yellow or Brown Leaf Edges: Leaf edges turn yellow or brown and may curl.",
                "Weak Stems: Stems become weak and may break easily.",
                "Reduced Fruit Quality: Fruits may be smaller, have poor color, and lack flavor.",
                "Slow Growth: Overall plant growth is slower and less vigorous."
            ],
            "tips": [
                "Use Potassium Fertilizers: Apply potassium-containing fertilizers such as potassium sulfate or potassium chloride.",
                "Composting: Use compost that is rich in potassium.",
                "Wood Ashes: Apply wood ashes (in moderation) to soil, as they are a good source of potassium.",
                "Regular Soil Testing: Regularly test soil potassium levels and amend as necessary."
            ]
        }
    }
    return data.get(predicted_class, "Penyakit tidak dikenal. Harap foto yang jelas.")

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
    info = nutrition_info(predicted_class)
    return jsonify(info)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8080)
