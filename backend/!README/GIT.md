# git 規範說明文件

## 1 常用指令

### 1.1 本地端操作

-`git clone <repository_url>`：複製遠端儲存庫到本地端。

-`git status`：查看目前工作目錄的狀態。

-`git add <file>`：將檔案加入暫存區。

-`git commit -m "commit message"`：提交暫存區的變更並"附上訊息"。

-`git pull origin <branch>`：從遠端儲存庫的指定分支拉取最新的變更並合併到本地端。

-`git checkout -b <branch>`：建立並切換到新的分支。

### 1.2 遠端操作

-`git push origin <branch>`：將本地端的變更推送到遠端儲存庫的指定分支。

### 2 規則

1. 分支命名規則：

   - 修改功能分支：`modify/<功能名稱>`，例如：`modify/login_logic`
   - 新增功能分支：`add/<功能名稱>`，例如：`add/signup_feature`
   - 修正分支：`fix/<修正內容>`，例如：`fix/signup_bug`
2. Commit 訊息規範：

   - 描述清楚變更內容。
   - 範例：`add basic login & signup function`、`Add MIT License to the project`

## 3 分支管理與開發流程

### 3.1 開始開發

1. 從主分支 `main` 拉取

   ```bash

   git checkout main

   git pull origin main

   ```
2. 建立新的分支

   ```bash

   git checkout -b <branch_name>

   ```

### 3.2 開發中

1. 每次開始開發前，先從主分支拉取最新變更並合併到目前分支：

   ```bash

   git checkout main

   git pull origin main

   git checkout <branch_name>

   git merge main

   ```
2. 完成部分功能後，在該分支中使用 `git add` 和 `git commit` 提交變更。
3. 定期將變更推送到遠端儲存庫：

   ```bash

   git push origin <branch_name>

   ```
4. 建立 Pull Request 選項使用 "Draft" 狀態，以表示工作尚未完成。

### 3.3 開發完成

1. 完成開發後，提交並推送變更到遠端儲存庫。
2. 進行程式碼審查，處理衝突(建議在github中操作)。
3. 經過審查後，將 Pull Request 合併到目標分支。
