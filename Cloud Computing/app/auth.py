from flask import Blueprint, request, jsonify
import jwt
import bcrypt
import datetime
from app.models import get_user_by_username, update_tokens

auth_bp = Blueprint('auth', __name__)

JWT_SECRET = 'your_jwt_secret_key'
JWT_REFRESH_SECRET = 'your_jwt_refresh_secret_key'

@auth_bp.route('/login', methods=['POST'])
def login():
    data = request.get_json()
    username = data.get('username')
    password = data.get('password')
    
    user = get_user_by_username(username)
    if not user or not bcrypt.checkpw(password.encode('utf-8'), user['password'].encode('utf-8')):
        return jsonify(error='Invalid credentials'), 401
    
    access_token = jwt.encode({'id': user['id'], 'exp': datetime.datetime.utcnow() + datetime.timedelta(minutes=15)}, JWT_SECRET)
    refresh_token = jwt.encode({'id': user['id'], 'exp': datetime.datetime.utcnow() + datetime.timedelta(days=7)}, JWT_REFRESH_SECRET)
    
    update_tokens(user['id'], access_token, refresh_token)
    
    return jsonify(access_token=access_token, refresh_token=refresh_token), 200

def init_auth(app):
    app.register_blueprint(auth_bp, url_prefix='/auth')
