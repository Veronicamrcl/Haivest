from flask import Flask, request, jsonify
import requests
import pickle
import os
import joblib
from sklearn.feature_extraction.text import TfidfVectorizer

app = Flask(__name__)

# Load model clustering
clustering_model = 'kmeans_model.pickle'
loaded_kmeans_model = pickle.load(open(clustering_model, "rb"))

def get_weather(api_key, city):
    base_url = "http://api.openweathermap.org/data/2.5/weather"
    params = {
        'q': city,
        'appid': api_key,
        'units': 'metric'  # Use 'metric' for Celsius, 'imperial' for Fahrenheit
    }
    response = requests.get(base_url, params=params)
    if response.status_code == 200:
        data = response.json()
        weather = {
            'city': data['name'],
            'temperature': data['main']['temp'],
            'description': data['weather'][0]['description'],
            'humidity': data['main']['humidity'],
            'wind_speed': data['wind']['speed']
        }
        return weather
    else:
        print(f"Error: {response.status_code}")
        print(f"Response: {response.json()}")
        return None

def get_plant_info(plant_name):
    data = {
        "kangkung": {
            "Nama tanaman": "Kangkung (Water Spinach)",
            "DFT": {
                "Reservoir Depth": "10-15 cm",
                "Flow Rate": "Ensure a moderate flow to provide adequate oxygenation.",
                "Nutrient Solution": "EC 1.2-1.8, pH 6.0-6.5",
                "Plant Spacing": "15-20 cm apart"
            },
            "NFT": {
                "Channel Slope": "1-2%",
                "Flow Rate": "1-2 liters per minute",
                "Nutrient Solution": "EC 1.2-1.8, pH 6.0-6.5",
                "Plant Spacing": "15-20 cm apart"
            },
            "Tips": [
                "Ensure adequate sunlight (at least 6 hours daily).",
                "Regularly monitor and adjust nutrient solution.",
                "Harvest once the plant reaches 20-30 cm in height."
            ]
        },
        "pakcoy": {
            "Nama tanaman": "Pakcoy (Bok Choy)",
            "DFT": {
                "Reservoir Depth": "15-20 cm",
                "Flow Rate": "Moderate to high to prevent root rot.",
                "Nutrient Solution": "EC 1.5-2.5, pH 6.0-6.5",
                "Plant Spacing": "20-25 cm apart"
            },
            "NFT": {
                "Channel Slope": "1-2%",
                "Flow Rate": "1-2 liters per minute",
                "Nutrient Solution": "EC 1.5-2.5, pH 6.0-6.5",
                "Plant Spacing": "20-25 cm apart"
            },
            "Tips": [
                "Avoid direct exposure to strong winds.",
                "Prevent bolting by keeping temperature consistent.",
                "Harvest when leaves are tender and before the plant starts flowering."
            ]
        },
        "bayam": {
            "Nama tanaman": "Bayam (Spinach)",
            "DFT": {
                "Reservoir Depth": "10-15 cm",
                "Flow Rate": "Ensure steady flow to avoid waterlogging.",
                "Nutrient Solution": "EC 1.8-2.3, pH 6.0-7.0",
                "Plant Spacing": "10-15 cm apart"
            },
            "NFT": {
                "Channel Slope": "1-2%",
                "Flow Rate": "0.5-1 liter per minute",
                "Nutrient Solution": "EC 1.8-2.3, pH 6.0-7.0",
                "Plant Spacing": "10-15 cm apart"
            },
            "Tips": [
                "Ensure the temperature is kept between 15-20°C.",
                "Harvest leaves regularly to encourage new growth.",
                "Maintain good airflow to prevent fungal diseases."
            ]
        },
        "selada": {
            "Nama tanaman": "Selada (Lettuce)",
            "DFT": {
                "Reservoir Depth": "15-20 cm",
                "Flow Rate": "Moderate to ensure sufficient oxygen.",
                "Nutrient Solution": "EC 1.2-1.8, pH 5.5-6.5",
                "Plant Spacing": "20-25 cm apart"
            },
            "NFT": {
                "Channel Slope": "1-2%",
                "Flow Rate": "1-2 liters per minute",
                "Nutrient Solution": "EC 1.2-1.8, pH 5.5-6.5",
                "Plant Spacing": "20-25 cm apart"
            },
            "Tips": [
                "Provide at least 10-12 hours of light daily.",
                "Avoid nutrient deficiencies by monitoring and adjusting regularly.",
                "Harvest when heads are firm and well-formed."
            ]
        },
        "sawi": {
            "Nama tanaman": "Sawi (Mustard Greens)",
            "DFT": {
                "Reservoir Depth": "10-15 cm",
                "Flow Rate": "Moderate to high to prevent water stagnation.",
                "Nutrient Solution": "EC 1.8-2.4, pH 6.0-6.8",
                "Plant Spacing": "15-20 cm apart"
            },
            "NFT": {
                "Channel Slope": "1-2%",
                "Flow Rate": "1-2 liters per minute",
                "Nutrient Solution": "EC 1.8-2.4, pH 6.0-6.8",
                "Plant Spacing": "15-20 cm apart"
            },
            "Tips": [
                "Keep the temperature between 18-24°C.",
                "Harvest before leaves become too mature and tough.",
                "Protect from pests and diseases by using netting or organic sprays."
            ]
        },
        "kailan": {
            "Nama tanaman": "Kailan (Chinese Broccoli)",
            "DFT": {
                "Reservoir Depth": "15-20 cm",
                "Flow Rate": "Moderate to high to ensure good aeration.",
                "Nutrient Solution": "EC 1.5-2.5, pH 6.0-6.5",
                "Plant Spacing": "25-30 cm apart"
            },
            "NFT": {
                "Channel Slope": "1-2%",
                "Flow Rate": "1-2 liters per minute",
                "Nutrient Solution": "EC 1.5-2.5, pH 6.0-6.5",
                "Plant Spacing": "25-30 cm apart"
            },
            "Tips": [
                "Ensure good sunlight exposure, around 6-8 hours daily.",
                "Harvest when stems are still tender and not woody.",
                "Regularly check for aphids and caterpillars."
            ]
        },
        "seledri": {
            "Nama tanaman": "Seledri (Celery)",
            "DFT": {
                "Reservoir Depth": "10-15 cm",
                "Flow Rate": "Moderate to high for adequate oxygenation.",
                "Nutrient Solution": "EC 1.2-1.8, pH 6.0-6.5",
                "Plant Spacing": "15-20 cm apart"
            },
            "NFT": {
                "Channel Slope": "1-2%",
                "Flow Rate": "1-2 liters per minute",
                "Nutrient Solution": "EC 1.2-1.8, pH 6.0-6.5",
                "Plant Spacing": "15-20 cm apart"
            },
            "Tips": [
                "Keep temperature between 15-21°C for optimal growth.",
                "Regularly trim to promote new growth.",
                "Monitor nutrient levels to prevent deficiencies."
            ]
        }
    }
    
    return data.get(plant_name.lower(), "Nama tanaman tidak dikenal. Harap masukkan nama tanaman yang valid.")

@app.route('/predict', methods=['POST'])
def weather_predict():
    api_key = "6e7baf62330ffc3502b0a245bbc99b92"  # Replace with your actual OpenWeatherMap API key
    city = request.json['city']  # Assuming the city is sent in the request body as JSON
    weather = get_weather(api_key, city)
    if weather:
        temperature_input = weather['temperature']
        humidity_input = weather['humidity']
        input_wilayah = [[temperature_input, humidity_input]]
        prediction = predict(input_wilayah)
        prediction = plant_info(prediction[0])
        return prediction
    else:
        return jsonify({"error": "City not found or error retrieving data."}), 400

def predict(data):
    # Eksekusi model
    predict_cluster = loaded_kmeans_model.predict(data)

    # Mengambil label cluster
    predict_cluster_int = predict_cluster[0]

    # Menambahkan label cluster kedalam var input untuk proses
    data[0].append(predict_cluster_int)

    # Eksekusi model klasifikasi
    # Load model klasifikasi
    classification_model = f'model_cluster_{predict_cluster_int}.pickle'
    loaded_model = pickle.load(open(classification_model, "rb"))
    predict = loaded_model.predict(data)
    return predict

def plant_info(data):
    if data:
        info = get_plant_info(data)
        return jsonify(info)
    else:
        return jsonify({"error": "No plant name provided."})


if __name__ == "__main__":
    app.run(host='0.0.0.0', port=8080)