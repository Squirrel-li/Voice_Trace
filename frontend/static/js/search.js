document.addEventListener('DOMContentLoaded', () => {
    initializeSearchFeature();
});

function initializeSearchFeature() {
    // 1. 獲取搜尋框與彈窗元素
    const searchInput = document.getElementById('modal-search-input');
    const searchTrigger = document.getElementById('search-icon-btn'); // 如果您有把放大鏡 ID 加回去
    const modalOverlay = document.getElementById('full-screen-search-modal');
    const closeModalBtn = document.getElementById('close-modal-btn');

    // 如果找不到搜尋框，就停止 (避免報錯)
    if (!searchInput) return;

    // === 搜尋核心邏輯 ===
    searchInput.addEventListener('input', (e) => {
        const searchTerm = e.target.value.toLowerCase().trim();

        // 1. 抓取所有「檔案行」
        // 注意：這裡我們抓取 class 為 "file-row" 的元素 (排除 header)
        const allRows = document.querySelectorAll('.file-row');

        console.log(`正在搜尋: ${searchTerm}，共找到 ${allRows.length} 個檔案`);

        allRows.forEach(row => {
            // 2. 在每一行裡面找到「檔案名稱」
            const nameCol = row.querySelector('.file-col-name');
            
            if (nameCol) {
                // 取得檔名文字
                const fileName = nameCol.textContent.toLowerCase();

                // 3. 比對搜尋詞
                if (searchTerm === '' || fileName.includes(searchTerm)) {
                    // 匹配成功：顯示 (恢復 flex 排版)
                    row.style.display = 'flex';
                } else {
                    // 匹配失敗：隱藏
                    row.style.display = 'none';
                }
            }
        });
    });

    // === 彈窗開關邏輯 (如果您還沒設定好的話) ===
    // 開啟 (如果您有放大鏡按鈕)
    if (searchTrigger && modalOverlay) {
        searchTrigger.addEventListener('click', () => {
             // 這裡建議加上強制樣式函數 (參考之前的 applySearchModalStyles)
             modalOverlay.style.display = 'flex'; 
             searchInput.focus();
        });
    }

    // 關閉
    const closeSearch = () => {
        if (modalOverlay) modalOverlay.style.display = 'none';
        searchInput.value = ''; // 清空搜尋
        // 關閉時恢復所有檔案顯示
        const allRows = document.querySelectorAll('.file-row');
        allRows.forEach(row => row.style.display = 'flex');
    };

    if (closeModalBtn) closeModalBtn.addEventListener('click', closeSearch);
    if (modalOverlay) {
        modalOverlay.addEventListener('click', (e) => {
            if (e.target === modalOverlay) closeSearch();
        });
    }
}