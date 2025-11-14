# SpeakTrace – 多人語音辨識平台

本專案為一套前後端分離的多人語音辨識網站，使用者可上傳影音檔並由系統自動執行語音分離、逐字稿產生與歷史紀錄查詢。

---

## 📌 技術架構

- **Frontend：** Vue 3
- **Backend：** Spring Boot（Kotlin）
- **Database：** MySQL
- **AI Models：** Whisper STT + Pyannote Speaker Diarization（外部服務或自建）

---

## 📁 專案結構

```
SpeakTrace/
│
├── backend/          # Kotlin + Spring Boot 後端
├── frontend/         # Vue 3 前端
└── database/         # MySQL 資料表與初始化腳本
```

---

## 🚀 功能簡介

- 上傳影片 / 音訊檔
- Whisper 語音辨識
- 說話者分離（Speaker Diarization）
- 自動生成逐字稿
- 歷史紀錄查詢與下載
- 使用者登入/註冊（可選）

---

## 🔧 開發環境需求

- Node.js 18+
- JDK 17+
- MySQL 8+
- IntelliJ IDEA（後端）
- VS Code（前端）

---

## ▶️ 啟動專案

### 1. 啟動後端

```bash
cd backend
./gradlew bootRun
```

### 2. 啟動前端

```bash
cd frontend
npm install
npm run dev
```

---

## 📄 資料庫初始化

```bash
mysql -u root -p < database/create_tables.sql
```

---

## 📬 聯絡

若有問題可向開發者詢問：SpeakTrace 開發團隊

email: **t112360221@ntut.org.tw**
