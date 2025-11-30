# 系統後端程式規範

## 1. 程式分類表

| 資料夾                   | 用途                       |
| ------------------------ | -------------------------- |
| **controller**           | 接 API，把請求交給 service |
| **service**              | 後端核心邏輯               |
| **repository**           | 操作 MySQL                 |
| **model**                | JPA entity，資料表欄位     |
| **config**               | 系統設定（CORS、安全性等） |
| **Application.kt**       | Spring Boot 啟動入口       |
---

## 2. 各分類預期檔案及功能

---

# 1. controller

## 用途
1. Controller 代表網站 API 的入口
2. Vue3 使用 Axios 呼叫 Controller
3. Controller 接收請求 → 交給 Service → 回傳 JSON

## 預期功能
- `POST /upload`：上傳影音
- `GET /records/{userId}`：查詢使用者歷史紀錄
- `GET /transcript/{id}`：查詢字幕結果
- `POST /login`：使用者登入
- `POST /register`：使用者註冊
- `DELETE /record/{id}`：刪除紀錄
- `GET /health`：健康檢查

---

# 2. service

## 用途
1. 語音辨識主要邏輯
2. 呼叫 Whisper / Pyannote 模型
3. 影片解析（抽音軌、切片）
4. 存取資料庫（透過 Repository）
5. 回傳組合好的 JSON 給 Controller
6. 處理登入驗證、密碼加密
7. 檔案儲存（影片、音檔、字幕）

## 預期功能
- processUpload(file, userId)
- getRecords(userId)
- getTranscript(recordId)
- deleteRecord(id)
- registerUser(data)
- loginUser(email, password)
- speakerAnalysis(audio)
- mergeTranscript(whisper + pyannote)
- saveFileToDisk()

---

# 3. repository

## 用途
Spring Data JPA 操作 MySQL 的 CRUD 介面

## 預期功能
### UserRepository
- findByEmail  
- findById  
- save  

### UploadRecordRepository
- findByUserId  
- findById  
- save  
- deleteById  

### TranscriptRepository
- findByRecordId  
- save  

### SpeakerInfoRepository
- findByTranscriptId  
- save  

---

# 4. model

## 用途
定義資料表的欄位對應（@Entity）

## 預期功能（資料欄位）

### User
- id  
- email  
- passwordHash  
- createdAt  

### UploadRecord
- id  
- userId  
- filename  
- uploadTime  
- duration  
- status  

### Transcript
- id  
- recordId  
- text  
- jsonData  

### SpeakerInfo
- id  
- transcriptId  
- speaker  
- start  
- end  

---

# 5. config

## 用途
設定整個 Spring Boot 系統的運作環境

## 預期功能

### CORSConfig
- 允許 Vue3（http://localhost:5173）訪問 API
- 設定允許 method / header / credential

### SecurityConfig
- 登入、註冊開放
- 其他 API 可配置 Token 機制
- 密碼加密（BCrypt）

### SwaggerConfig
- 自動產生 API docs
- Swagger UI 提供 API 測試頁

### FileConfig
- 設定上傳檔案最大容量（例如 1GB）

### DatabaseConfig
- 設定 HikariCP 連線池
- MySQL URL / 帳密

---

# 6. Application.kt

Spring Boot 專案的啟動主程式

---

