require("dotenv").config()
const {
    API_URL
} = process.env

if (!API_URL) {
    throw new Error(".env is incomplete")
  }
  
module.exports = {
    env: {
      API_URL: 'http://10.0.242.142:8083',
    },
  }