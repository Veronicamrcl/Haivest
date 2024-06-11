const axios = require('axios');
require('dotenv').config();

const apiKey = process.env.NEWS_API_KEY;

async function getHydroponicNews(req, res) {
  try {
    //const query = encodeURIComponent('tanaman hidroponik'); // Mencari berita mengenai tanaman hidroponik
    const url = `https://newsapi.org/v2/everything?q=hydroponics&apiKey=${apiKey}`;
    const response = await axios.get(url);
    const data = response.data;
    res.json(data);
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: 'An error occurred' });
  }
}

module.exports = {
  getHydroponicNews,
};
