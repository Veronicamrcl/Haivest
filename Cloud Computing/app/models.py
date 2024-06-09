import mysql.connector
from flask import current_app

def get_db_connection():
    conn = mysql.connector.connect(
        host='localhost',
        user='root',
        password='',
        database='haivest'
    )
    return conn

def get_user_by_username(username):
    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute('SELECT * FROM user WHERE username = %s', (username,))
    user = cursor.fetchone()
    cursor.close()
    conn.close()
    return user

def update_tokens(user_id, access_token, refresh_token):
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute('UPDATE user SET tokenJwt = %s, refreshToken = %s WHERE id = %s', (access_token, refresh_token, user_id))
    conn.commit()
    cursor.close()
    conn.close()
