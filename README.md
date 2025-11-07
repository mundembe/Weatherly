# 🌤️ Weatherly  
**A Modern Weather & Lifestyle App built with Jetpack Compose + Firebase**

## 📱 **Overview**

**Weatherly** is a weather and lifestyle app that provides **real-time weather forecasts**, **clothing suggestions**, and **multi-language support**.  
It’s designed with **Material 3**, powered by **Firebase**, and optimized for **South African users** 🇿🇦.

![WhatsApp Image 2025-10-14 at 21 10 59_23737831](https://github.com/user-attachments/assets/a07ee52d-3dfe-473f-ba1c-be59e51fd512)


---

## ✨ Features

### ☀️ Weather
- Real-time weather from **OpenWeather API**
- Beautiful icons loaded with **Glide**
- Smart **clothing recommendations**
- **Offline cache** using Room Database

### ⚙️ Settings
- Change app language: 🇬🇧 English, 🇿🇦 isiZulu, 🇿🇦 Afrikaans  
- Switch between Celsius ↔ Fahrenheit  
- Toggle daily weather notifications  
- Animated Material 3 dropdowns and cards  

### 👤 Profile
- Firebase Authentication (Email / Google)
- Firestore user data sync
- Offline fallback + auto sync  

### 🔔 Notifications
- Daily reminders via **Firebase Cloud Messaging (FCM)**  
- Android 13+ notification permission support  

---

## 🧱 Tech Stack

| Layer | Technology |
|-------|-------------|
| **Language** | Kotlin |
| **UI** | Jetpack Compose + Material 3 |
| **Architecture** | MVVM (ViewModel + StateFlow) |
| **Network** | Retrofit + Gson |
| **Database** | Room (SQLite) |
| **Backend** | Firebase Auth + Firestore + FCM |
| **Persistence** | DataStore Preferences |
| **Images** | Glide |
| **Localization** | Multi-language (English, isiZulu, Afrikaans) |

---


---

## 🔑 Setup

### 1️⃣ Clone
git clone https://github.com/<your-username>/Weatherly.git
cd Weatherly

2️⃣ Add API Key
In your local.properties file:
OPENWEATHER_API_KEY=your_openweather_api_key_here

3️⃣ Connect Firebase
Add your google-services.json to /app
Enable Auth, Firestore, and FCM in Firebase Console

4️⃣ Build & Run
./gradlew assembleDebug

---

🧭 **Architecture**

UI (Compose)
 ↓ observes
ViewModel (StateFlow)
 ↓ uses
Repository (WeatherRepository, SettingsRepository)
 ↓ interacts with
Local (Room, DataStore) + Remote (Retrofit, Firebase)

---
⚡ **Offline & Sync**

Weather cached locally via Room
Firestore sync when online
Works gracefully in offline mode

🧩 **Settings Page (Modern Material 3)**

Rounded cards with icons
Animated dropdowns
Notification permission handling
Smooth transitions with AnimatedContent

![WhatsApp Image 2025-10-14 at 21 10 59_847cfc97](https://github.com/user-attachments/assets/f1ad5d3f-aa3a-45a9-b6a1-4033487fea0a)

---

🚀 **Future Features**

🌙 Dark mode toggle

🌧 Weekly forecast

📍 Location-based weather

💬 Feedback system

Video Presantions: https://youtu.be/8_BHoq8xnOI
