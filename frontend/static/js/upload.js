// upload.js - 完美對齊修正版

document.addEventListener('DOMContentLoaded', () => {
    // 1. 優先執行：強制修正 HTML 標題列的樣式
    fixHeaderAlignment();

    // 2. 啟動上傳功能
    initializeUploadFeature();
});

/**
 * 自動修復標題列樣式的函數
 * 確保標題文字與內容完美對齊
 */
function fixHeaderAlignment() {
    const headerRow = document.querySelector('.file-header-row');
    if (headerRow) {
        headerRow.style.setProperty('display', 'flex', 'important');
        headerRow.style.setProperty('align-items', 'center', 'important');
        headerRow.style.setProperty('padding', '10px 18px', 'important');
    }

    // 第一欄：Checkbox
    const checkboxCol = document.querySelector('.file-header-row .file-col-checkbox');
    if (checkboxCol) {
        checkboxCol.style.setProperty('flex', '0 0 80px', 'important');
        checkboxCol.style.setProperty('display', 'flex', 'important');
        checkboxCol.style.setProperty('align-items', 'center', 'important');
        checkboxCol.style.setProperty('justify-content', 'center', 'important');
    }

    // 第二欄：名稱
    const nameCol = document.querySelector('.file-header-row .file-col-name');
    if (nameCol) {
        nameCol.style.setProperty('flex', '1 1 auto', 'important');
        nameCol.style.setProperty('padding-left', '12px', 'important');
    }

    // 第三欄：模式（關鍵修正）
    const modeCol = document.querySelector('.file-header-row .file-col-mode');
    if (modeCol) {
        modeCol.style.setProperty('flex', '0 0 170px', 'important');
        modeCol.style.setProperty('display', 'flex', 'important');
        modeCol.style.setProperty('align-items', 'center', 'important');
        modeCol.style.setProperty('justify-content', 'flex-start', 'important');
        modeCol.style.setProperty('padding-left', '35px', 'important'); // 往右移 35px
        modeCol.style.setProperty('text-align', 'left', 'important');
    }

    // 第四欄：狀態（關鍵修正）
    const statusCol = document.querySelector('.file-header-row .file-col-status');
    if (statusCol) {
        statusCol.style.setProperty('flex', '0 0 150px', 'important');
        statusCol.style.setProperty('display', 'flex', 'important');
        statusCol.style.setProperty('align-items', 'center', 'important');
        statusCol.style.setProperty('justify-content', 'flex-start', 'important');
        statusCol.style.setProperty('padding-left', '35px', 'important'); // 往右移 35px
        statusCol.style.setProperty('text-align', 'left', 'important');
    }

    // 第五欄：更多
    const moreCol = document.querySelector('.file-header-row .file-col-more');
    if (moreCol) {
        moreCol.style.setProperty('flex', '0 0 40px', 'important');
        moreCol.style.setProperty('justify-content', 'flex-end', 'important');
    }
}

function initializeUploadFeature() {
    const uploadTrigger = document.getElementById('upload-trigger-btn');
    const uploadModal = document.getElementById('upload-modal');
    const closeUploadBtn = document.getElementById('close-upload-btn');
    const confirmBtn = document.getElementById('confirm-upload-btn');
    const fileInput = document.getElementById('file-input');
    const fileListBody = document.getElementById('file-list-body');

    if (!uploadTrigger || !uploadModal) return;

    // 彈窗顯示
    uploadTrigger.addEventListener('click', (e) => {
        e.preventDefault();
        Object.assign(uploadModal.style, {
            position: 'fixed', top: '0', left: '0', width: '100vw', height: '100vh',
            backgroundColor: 'rgba(0,0,0,0.5)', zIndex: '9999',
            display: 'flex', justifyContent: 'center', alignItems: 'center'
        });
        const contentBox = uploadModal.querySelector('.modal-content');
        if(contentBox) {
            Object.assign(contentBox.style, {
                backgroundColor: '#fff', borderRadius: '12px', padding: '30px', width: '500px'
            });
        }
    });

    // 彈窗關閉
    const closeUpload = () => {
        uploadModal.style.display = 'none';
        if (fileInput) fileInput.value = '';
    };
    if (closeUploadBtn) closeUploadBtn.addEventListener('click', closeUpload);
    uploadModal.addEventListener('click', (e) => { if (e.target === uploadModal) closeUpload(); });

    // 確認上傳
    if (confirmBtn) {
        confirmBtn.addEventListener('click', () => {
            if (!fileInput || fileInput.files.length === 0) {
                alert("請先選擇檔案！");
                return;
            }
            if (!fileListBody) {
                alert("❌ 找不到檔案列表容器 (id='file-list-body')");
                return;
            }

            const fileName = fileInput.files[0].name;
            const today = new Date().toLocaleDateString('zh-TW', { year: 'numeric', month: '2-digit', day: '2-digit' });

            // 生成與標題列完美對齊的內容列
            const newRowHTML = `
                <div class="file-row" style="display: flex; align-items: center; padding: 10px 15px; border-top: 1px solid #f3f4f6;">
                    
                    <!-- 第一欄：Checkbox (80px) -->
                    <div style="flex: 0 0 80px; display: flex; align-items: center; justify-content: center; gap: 10px;">
                        <input type="checkbox" class="file-checkbox" style="cursor: pointer; width: 16px; height: 16px; margin-left: 15px;">
                        <div style="width: 24px; height: 24px;"></div>
                    </div>

                    <!-- 第二欄：名稱 -->
                    <div style="flex: 1 1 auto; padding-left: 12px; display: flex; align-items: center; min-width: 0;">
                        <span style="font-weight: 500; color: #333; font-size: 14px; margin-right: 8px;">${fileName}</span>
                        <span style="color: #9ca3af; font-size: 12px;">| ${today}</span>
                    </div>

                    <!-- 第三欄：模式 (170px，往右移 15px 對齊標題) -->
                    <div style="flex: 0 0 170px; display: flex; align-items: center; justify-content: flex-start; padding-left: 12px;">
                        <span style="display: inline-flex; align-items: center; background-color: #eff6ff; color: #1d4ed8; padding: 3px 8px; border-radius: 999px; font-size: 12px;">語音轉文字</span>
                    </div>

                    <!-- 第四欄：狀態 (150px，往右移 15px 對齊標題) -->
                    <div style="flex: 0 0 150px; display: flex; align-items: center; justify-content: flex-start; gap: 6px; padding-left: 25px;">
                        <span style="width: 8px; height: 8px; background-color: #16a34a; border-radius: 50%;"></span>
                        <span style="color: #374151; font-size: 14px;">完成</span>
                    </div>

                    <!-- 第五欄：更多 (40px，往右移 5px) -->
                    <div style="flex: 0 0 40px; display: flex; justify-content: flex-end; align-items: center; color: #9ca3af; padding-right: 20px;">
                        <i class="fas fa-ellipsis-h" style="cursor: pointer; font-size: 16px;"></i>
                    </div>
                </div>
            `;

            fileListBody.insertAdjacentHTML('afterbegin', newRowHTML);
            closeUpload();
        });
    }
}