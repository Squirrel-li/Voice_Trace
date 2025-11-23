// delete.js - 專責處理刪除功能

document.addEventListener('DOMContentLoaded', () => {
    initializeDeleteFeature();
});

function initializeDeleteFeature() {
    const deleteBtn = document.getElementById('delete-trigger-btn');
    const selectAllCheckbox = document.getElementById('select-all-checkbox');

    // 安全檢查
    if (!deleteBtn) return;

    deleteBtn.addEventListener('click', () => {
        // 1. 找出頁面上所有「被勾選」的 checkbox
        const allChecked = document.querySelectorAll('input[type="checkbox"]:checked');
        
        // 2. 過濾掉「全選框」本身 (只留下檔案的 checkbox)
        const filesToDelete = Array.from(allChecked).filter(cb => cb.id !== 'select-all-checkbox');

        // 3. 如果沒選，跳出提示
        if (filesToDelete.length === 0) {
            alert("請先勾選要刪除的檔案！");
            return;
        }

        // 4. 確認刪除
        if (confirm(`確定要刪除這 ${filesToDelete.length} 個檔案嗎？`)) {
            let count = 0;
            filesToDelete.forEach(checkbox => {
                // 往上找最近的 .file-row (我們在 upload.js 裡生成的 class)
                // 同時相容舊的 .file-item 或 tr，確保通殺
                const row = checkbox.closest('.file-row, .file-item, tr');
                
                if (row) {
                    row.remove(); // 從 HTML 移除
                    count++;
                }
            });

            // 5. 刪除後，把全選框取消勾選 (避免誤會)
            if (selectAllCheckbox) {
                selectAllCheckbox.checked = false;
            }
            
            // console.log(`已刪除 ${count} 筆資料`);
        }
    });
}