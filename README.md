# 🌤️ Weatherly  
**A Modern Weather & Lifestyle App built with Jetpack Compose + Firebase**

## 📱 **Overview**

**Weatherly** is a weather and lifestyle app that provides **real-time weather forecasts**, **clothing suggestions**, and **multi-language support**.  
It’s designed with **Material 3**, powered by **Firebase**, and optimized for **South African users** 🇿🇦.

![IMG-20251108-WA0009](https://github.com/user-attachments/assets/93b4b5f3-457a-4fd7-98bc-87822dc8d772)



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

  ![IMG-20251108-WA0004](https://github.com/user-attachments/assets/ad085046-8fc0-45fa-80c5-3799930e466e)


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

Video Presantions: https://youtu.be/S3wU45MVwGs

![IMG-20251108-WA0007](https://github.com/user-attachments/assets/6de9f8f2-b615-49ff-a6a4-bd9e93bac5d8)
![IMG-20251108-WA0008](https://github.com/user-attachments/assets/cb66659e-3e01-4830-8fe0-a6c37ea03416)
![IMG-20251108-WA0005](https://github.com/user-attachments/assets/da12ac56-5ce6-40ac-9f77-1fc96e264a97)

