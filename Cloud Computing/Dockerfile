# Gunakan node versi LTS sebagai base image
FROM node:lts-alpine as node
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .

FROM python:3.9-alpine as python
WORKDIR /app
COPY requirements.txt ./
RUN pip install -r requirements.txt
COPY . .

CMD ["flask", "run", "--host=0.0.0.0", "--port=5000"]
