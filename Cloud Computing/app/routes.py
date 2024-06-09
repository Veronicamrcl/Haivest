from flask import Blueprint, request, jsonify
from app.cloud_storage import upload_blob

main_bp = Blueprint('main', __name__)

@main_bp.route('/upload', methods=['POST'])
def upload_to_storage():
    if 'file' not in request.files:
        return jsonify(error='No file part'), 400

    file = request.files['file']
    if file.filename == '':
        return jsonify(error='No selected file'), 400

    upload_blob('your_bucket_name', file, file.filename)
    return jsonify(message='File uploaded successfully'), 200
