# SpeakTrace — 多人語音辨識平台
一套支援 **多人語音分離、語音辨識、逐字稿產生與歷史紀錄查詢** 的網頁平台。  
使用者可上傳會議、訪談或課堂錄影，系統將自動：

1. 進行語音分離（Speaker Diarization）
2. 語音轉文字（Speech-to-Text）
3. 標示不同說話者
4. 產生逐字稿與摘要文件
5. 將紀錄儲存至資料庫，可隨時查詢下載

---

## 🚀 系統架構

本專案採用 **前後端分離架構**：

### 🟦 Frontend（前端）
- Vue 3
- Vue Router
- Axios
- Vite

### 🟧 Backend（後端）
- Spring Boot（Kotlin）
- Spring Web / Validation / JPA
- MySQL Connector
- ModelMapper
- Whisper + Pyannote（外部 AI 模型）

### 🟩 Database（資料庫）
- MySQL
- ERD包含：
  - `User`
  - `Upload_Record`
  - `Transcript`
  - `Speaker_Info`

---

## 📁 專案資料夾結構

