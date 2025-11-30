<template>
    <div class="modal-overlay" @click.self="$emit('close')">
        <div class="modal-content upload-modal">
            <h2>📂 上傳影音檔案</h2>
            <p>請上傳您要進行多人語音辨識的影片或音訊檔案 [cite: 6, 79]。</p>
            
            <div class="drop-zone">
                <p>拖曳檔案到此處，或點擊選擇</p>
                <input type="file" @change="handleFileSelect" accept="video/*,audio/*" style="display: none;" ref="fileInput">
                <button @click="openFileDialog" class="upload-btn primary">選擇檔案</button>
            </div>

            <div v-if="fileName" class="file-info">
                已選取檔案: <strong>{{ fileName }}</strong>
            </div>
            
            <button 
                @click="startUpload"
                :disabled="!fileName" 
                class="submit-btn"
            >
                開始辨識 (上傳)
            </button>

            <button class="close-btn" @click="$emit('close')">X</button>
        </div>
    </div>
</template>

<script setup>  
    import { ref } from 'vue';

    const emit = defineEmits(['close', 'uploadSuccess']);

    const fileInput = ref(null);
    const fileName = ref('');
    const fileType = ref('');
    const allowFileType = ['video/mp4', 'audio/wav', 'audio/mp3', 'video/quicktime', 'video/x-msvideo'];

    const openFileDialog = () => {
        fileInput.value.click();
    };

    const handleFileSelect = (event) => {
        // 僅模擬獲取檔名，檔案上傳/驗證邏輯（File Validation）在後端處理 [cite: 63]
        const file = event.target.files[0];
        if (file) {
            fileName.value = file.name;
            fileType.value = file.type;
            console.log('選取的檔案:', fileType.value);
            if (allowFileType.includes(fileType.value)) {
                console.log('檔案類型允許');
            } else {
                alert('不支援的檔案類型，請上傳 mp4, wav, mp3, mov, avi 格式的檔案。');
                fileName.value = '';
                fileType.value = '';
            }
        }
    };

    const startUpload = () => {
        if (!fileName.value) {
            alert('請先選擇一個檔案');
            return;
        }

        const file = fileInput.value.files[0];  // 取得選取的檔案
        const formData = new FormData();
        formData.append('file', file);  // 正確 append 檔案

        const token = localStorage.getItem('token');
        console.log('Token:', token);
        
        fetch('/api/record/upload', {
            method: 'POST',
            headers: {
                "Authorization": `Bearer ${token}`  // 移除 Content-Type
            },
            body: formData  // 直接用 formData
        })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log('上傳成功:', data);
            // 成功後關閉彈窗
            emit('close');
            emit('uploadSuccess');  // 傳遞上傳成功的資料
        })
        .catch(error => {
            alert(error.response?.data?.message || '上傳失敗，請稍後再試。');
        });
    };
</script>

<style scoped>
/* 使用與 LoginModal 相似的樣式基礎 */
.upload-modal {
    width: 500px;
}
.drop-zone {
    border: 2px dashed #ccc;
    padding: 30px;
    text-align: center;
    margin: 20px 0;
    border-radius: 8px;
    background-color: #fafafa;
}
.upload-btn {
    background-color: #00bcd4; /* 青色，用於上傳 */
    color: white;
    border: none;
    padding: 10px 20px;
    border-radius: 4px;
    cursor: pointer;
    margin-top: 10px;
}
.file-info {
    margin-top: 15px;
    padding: 10px;
    border: 1px solid #eee;
    background-color: #f0f8ff;
    border-radius: 4px;
}
.submit-btn {
    width: 100%;
    padding: 10px;
    background-color: #ff9800;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    margin-top: 20px;
}
.submit-btn:disabled {
    background-color: #ccc;
    cursor: not-allowed;
}
.close-btn {
    position: absolute;
    top: 10px;
    right: 10px;
    background: none;
    border: none;
    font-size: 1.2em;
    cursor: pointer;
    color: #666;
}
</style>