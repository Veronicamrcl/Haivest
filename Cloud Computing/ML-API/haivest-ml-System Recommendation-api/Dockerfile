# Gunakan Python versi 3.8 sebagai base image
FROM python:3.10

# Set working directory di dalam container
WORKDIR /app

# Copy file requirements.txt ke dalam container
COPY requirements.txt requirements.txt

# Install dependensi Python
RUN pip install -r requirements.txt

# Copy seluruh kode proyek ke dalam container
COPY . .

# Expose port yang digunakan oleh aplikasi Flask
EXPOSE 8080

# Command untuk menjalankan aplikasi Flask
CMD ["python", "main.py"]