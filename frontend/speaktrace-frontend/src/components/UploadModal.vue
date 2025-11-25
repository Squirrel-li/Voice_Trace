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
import { ref, defineEmits } from 'vue';

const emit = defineEmits(['close']);

const fileInput = ref(null);
const fileName = ref('');

const openFileDialog = () => {
  fileInput.value.click();
};

const handleFileSelect = (event) => {
  // 僅模擬獲取檔名，檔案上傳/驗證邏輯（File Validation）在後端處理 [cite: 63]
  const file = event.target.files[0];
  if (file) {
    fileName.value = file.name;
    // 這裡應有檔案格式驗證 [cite: 63]
  }
};

const startUpload = () => {
  if (!fileName.value) {
    alert('請先選擇一個檔案');
    return;
  }
  // 實際應用中，這裡會啟動上傳流程 (對應功能 F1) [cite: 101]
  alert(`檔案 ${fileName.value} 已送出，正在進行辨識... (模擬)`);
  
  // 成功後關閉彈窗
  emit('close');
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