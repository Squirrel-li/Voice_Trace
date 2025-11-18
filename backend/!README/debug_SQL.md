# 資料庫測試

## 1. 查看資料庫內容

1. 顯示全部的user
   [http://localhost:8080/api/test_sql/show/user](http://localhost:8080/api/test_sql/show/user)
2. 顯示全部的uploadRecord
   [http://localhost:8080/api/test_sql/show/uploadRecord](http://localhost:8080/api/test_sql/show/uploadRecord)
3. 顯示全部tranScription
   [http://localhost:8080/api/test_sql/show/tranScription](http://localhost:8080/api/test_sql/show/tranScription)
4. 顯示全部Speakerinfo
   [http://localhost:8080/api/test_sql/show/Speakerinfo](http://localhost:8080/api/test_sql/show/Speakerinfo)
5. 顯示全部資料表內容
   [http://localhost:8080/api/test_sql/show/all](http://localhost:8080/api/test_sql/show/all)

## 2. 新增資料庫內容

### 1. 新增 User：

```powershell
Invoke-RestMethod -Method POST -Uri http://localhost:8080/api/test_sql/add/user `
  -Headers @{ "Content-Type"="application/json" } `
  -Body '{"username":"demo","email":"demo@example.com","password":"secret"}'
```

### 2. 新增 UploadRecord：

```powershell
Invoke-RestMethod -Method POST -Uri http://localhost:8080/api/test_sql/add/uploadRecord `
  -Headers @{ "Content-Type"="application/json" } `
  -Body '{"userId":1,"fileName":"sample.wav","filePath":"/tmp/sample.wav"}'
```

### 3. 新增 SpeakerInfo：

```powershell
Invoke-RestMethod -Method POST -Uri http://localhost:8080/api/test_sql/add/speakerinfo `
  -Headers @{ "Content-Type"="application/json" } `
  -Body '{"uploadRecordId":1,"speakerLabel":"S1","displayName":"Speaker 1"}'
```

### 4. 新增 Transcript：

```powershell
Invoke-RestMethod -Method POST -Uri http://localhost:8080/api/test_sql/add/transcript `
  -Headers @{ "Content-Type"="application/json" } `
  -Body '{"uploadRecordId":1,"speakerInfoId":1,"content":"sample","startTime":0.0,"endTime":3.2}'
```

有時候出500erro還是有作用

## 3. 刪除資料庫內容

### 1. 刪除全部 User：

```powershell
Invoke-RestMethod -Method POST -Uri http://localhost:8080/api/test_sql/clear/user
```

### 2. 刪除全部 UploadRecord：

```powershell
Invoke-RestMethod -Method POST -Uri http://localhost:8080/api/test_sql/clear/uploadRecord
```

### 3. 刪除全部 SpeakerInfo：

```powershell
Invoke-RestMethod -Method POST -Uri http://localhost:8080/api/test_sql/clear/speakerInfo
```

### 4. 刪除全部 Transcript：

```powershell
Invoke-RestMethod -Method POST -Uri http://localhost:8080/api/test_sql/clear/transcript
```

### 5. 刪除全部資料表內容：

```powershell
Invoke-RestMethod -Method POST -Uri http://localhost:8080/api/test_sql/clear/all
```

## 4. 測試用的 PowerShell 指令範例

### 1. 新增資料範例

```powershell
Invoke-RestMethod -Method POST -Uri http://localhost:8080/api/test_sql/add/user `
  -Headers @{ "Content-Type"="application/json" } `
  -Body '{"username":"demo","email":"demo@example.com","password":"secret"}'

Invoke-RestMethod -Method POST -Uri http://localhost:8080/api/test_sql/add/uploadRecord `
  -Headers @{ "Content-Type"="application/json" } `
  -Body '{"userId":1,"fileName":"sample.wav","filePath":"/tmp/sample.wav"}'

Invoke-RestMethod -Method POST -Uri http://localhost:8080/api/test_sql/add/speakerinfo `
  -Headers @{ "Content-Type"="application/json" } `
  -Body '{"uploadRecordId":1,"speakerLabel":"S1","displayName":"Speaker 1"}'

Invoke-RestMethod -Method POST -Uri http://localhost:8080/api/test_sql/add/transcript `
  -Headers @{ "Content-Type"="application/json" } `
  -Body '{"uploadRecordId":1,"speakerInfoId":1,"content":"sample","startTime":0.0,"endTime":3.2}'
```

### 2. 刪除全部資料範例

```powershell
Invoke-RestMethod -Method POST -Uri http://localhost:8080/api/test_sql/clear/all
```

### 3. 分別刪除各資料表內容範例

```powershell
Invoke-RestMethod -Method POST -Uri http://localhost:8080/api/test_sql/clear/user
Invoke-RestMethod -Method POST -Uri http://localhost:8080/api/test_sql/clear/uploadRecord
Invoke-RestMethod -Method POST -Uri http://localhost:8080/api/test_sql/clear/speakerInfo
Invoke-RestMethod -Method POST -Uri http://localhost:8080/api/test_sql/clear/transcript
```

<!--- END OF FILE --->
