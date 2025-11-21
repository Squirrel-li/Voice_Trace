// 當網頁載入完成後，啟動全選功能
document.addEventListener('DOMContentLoaded', () => {
    initializeSelectAllFeature();
});

/**
 * ============================================================
 * 功能：全選 / 取消全選所有文件
 * ============================================================
 */
function initializeSelectAllFeature() {
    // 1. 獲取標題欄的「全選」勾選框
    const selectAllCheckbox = document.getElementById('select-all-checkbox');
    
    // 2. 獲取文件列表的表格容器 (確保只選到這裡面的檔案)
    const fileListBody = document.getElementById('file-list-body');

    // 安全檢查：如果找不到元素就停止，避免報錯
    if (!selectAllCheckbox || !fileListBody) {
        console.warn("⚠️ 全選功能無法啟動：請確認 HTML 中有 id='select-all-checkbox' 和 id='file-list-body'");
        return;
    }

    console.log("✅ 全選功能已就緒");

    // 3. 監聽「全選」框的點擊 (改變狀態) 事件
    selectAllCheckbox.addEventListener('change', function() {
        // 取得目前全選框是「勾選 (true)」還是「未勾選 (false)」
        const isChecked = this.checked;

        // 找出表格內所有每一列的 Checkbox
        // 使用 querySelectorAll 鎖定在 fileListBody 範圍內
        const allFileCheckboxes = fileListBody.querySelectorAll('input[type="checkbox"]');

        // 4. 將所有檔案的 Checkbox 狀態設為跟全選框一樣
        allFileCheckboxes.forEach(checkbox => {
            checkbox.checked = isChecked;
        });

        console.log(isChecked ? `已勾選 ${allFileCheckboxes.length} 個檔案` : "已取消全選");
    });
}