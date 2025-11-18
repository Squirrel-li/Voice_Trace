# 1. git 規範說明文件

## 1.1 常用指令

### 1.1.1 本地端操作

- `git clone <repository_url>`：複製遠端儲存庫到本地端。
- `git status`：查看目前工作目錄的狀態。
- `git add <file>`：將檔案加入暫存區。
- `git commit -m "commit message"`：提交暫存區的變更並"附上訊息"。
- `git pull origin <branch>`：從遠端儲存庫的指定分支拉取最新的變更並合併到本地端。
- `git checkout -b <branch>`：建立並切換到新的分支。

### 1.1.2 遠端操作

- `git push origin <branch>`：將本地端的變更推送到遠端儲存庫的指定分支。

### 1.2 規則
1. 分支命名規則：
   - 修改功能分支：`modify/<功能名稱>`，例如：`modify/login_logic`
   - 新增功能分支：`add/<功能名稱>`，例如：`add/signup_feature`
   - 修正分支：`fix/<修正內容>`，例如：`fix/signup_bug`

1. Commit 訊息規範：
   - 描述清楚變更內容。
   - 範例：`add basic login & signup function`、`Add MIT License to the project`

## Pull Request 流程

### 1.1 開發中

1. 在本地建立一個新的分支，並在該分支上進行開發。
2. 暫時上傳未完成的工作到遠端分支（可選）。 `git push origin <branch>`。
3. Pull Request 選項使用 "Draft" 狀態，以表示工作尚未完成。

### 1.2 開發完成

1. 完成開發後，提交並推送變更到遠端儲存庫。
2. 在 GitHub 上開啟一個 Pull Request，選擇目標分支並描述變更內容。
3. 進行程式碼審查，確保變更符合專案標準。
4. 經過審查後，將 Pull Request 合併到目標分支。

# 2. 特殊語法

## 2.1 使用 Token 存取受保護的 API

### 2.1.1 後端設定
在 `backend\src\main\kotlin\com\SpeakTrace\backend\config\SecurityConfig.kt`中，設定哪些API需認證
```
.requestMatchers(
    "/index.html", "/", "/css/**", "/js/**", "/images/**", "/webjars/**"
).permitAll() // 靜態資源與首頁允許
.requestMatchers("/api/auth/**", "/api/test_sql/**").permitAll() // 公開API
.anyRequest().authenticated() // 其他都要驗證
```

### 2.1.2 前端使用
在前端中使用API時，需在標頭中加入Token，例如：
```
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

# 3. API 文件
## 3.1 測試 API

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


## 3.2 公開 API 列表

| Method | Endpoint                         | Description        |
| ------ | -------------------------------- | ------------------ |
| POST   | /api/auth/login                  | 使用者登入         |
| POST   | /api/auth/signup                 | 使用者註冊         |

## 3.3 受保護 API 列表

| Method | Endpoint             | Description    |
| ------ | -------------------- | -------------- |
| POST   | /api/upload/userinfo | 更新使用者資訊 |
| POST   | /api/upload/media    | 上傳媒體資料   |
