# 1. 常用語法

## 1 使用 Token 存取受保護的 API

### 1.1 後端設定
在 `backend\src\main\kotlin\com\SpeakTrace\backend\config\SecurityConfig.kt`中，設定哪些API需認證
```kotlin
.requestMatchers(
    "/index.html", "/", "/css/**", "/js/**", "/images/**", "/webjars/**"
).permitAll() // 靜態資源與首頁允許
.requestMatchers("/api/auth/**", "/api/test_sql/**").permitAll() // 公開API
.anyRequest().authenticated() // 其他都要驗證
```

### 1.2 前端使用
在前端中使用API時，需在標頭中加入Token，例如：
```javaScript
async need_token_protect_api() {
	// 從 localStorage 取得 Token
    const token = localStorage.getItem("token"); 
    const res = await fetch("/api/protected", {
        method: "GET",
        headers: {
			// 添加 Authorization 標頭
            "Authorization": `Bearer ${token}` 
        }
    });

    if (res.ok) {
        const data = await res.text();
        console.log("受保護的資料：", data);
    } else {
        console.error("無法存取受保護的資料");
    }
}
```

# 2. API 文件
## 2.1 測試 API

| Method | Endpoint                         | Description        |
| ------ | -------------------------------- | ------------------ |
| GET    | /api/test_sql/show/all           | 顯示資料庫所有內容 |
| GET    | /api/test_sql/show/user          | 顯示使用者資訊     |
| GET    | /api/test_sql/show/uploadRecord  | 顯示上傳紀錄       |
| GET    | /api/test_sql/show/transcript    | 顯示逐字稿         |
| GET    | /api/test_sql/show/speakerInfo   | 顯示講者資訊       |
| POST   | /api/test_sql/add/user           | 新增使用者         |
| POST   | /api/test_sql/add/uploadRecord   | 新增上傳紀錄       |
| POST   | /api/test_sql/add/transcript     | 新增逐字稿         |
| POST   | /api/test_sql/add/speakerInfo    | 新增講者資訊       |
| POST   | /api/test_sql/clear/all          | 清除資料庫所有內容 |
| POST   | /api/test_sql/clear/user         | 清除使用者資訊     |
| POST   | /api/test_sql/clear/uploadRecord | 清除上傳紀錄       |
| POST   | /api/test_sql/clear/transcript   | 清除逐字稿         |
| POST   | /api/test_sql/clear/speakerInfo  | 清除講者資訊       |


## 2.2 公開 API 列表

| Method | Endpoint                         | Description        |
| ------ | -------------------------------- | ------------------ |
| POST   | /api/auth/login                  | 使用者登入         |
| POST   | /api/auth/signup                 | 使用者註冊         |

## 2.3 受保護 API 列表

| Method | Endpoint             | Description    |
| ------ | -------------------- | -------------- |
| POST   | /api/upload/userinfo | 更新使用者資訊 |
| POST   | /api/upload/media    | 上傳媒體資料   |

## 2.4 語音處理 API 列表
| Method | Endpoint    | Description       |
| ------ | ----------- | ----------------- |
| POST   | /api/tts    | 語音轉文字處理    |