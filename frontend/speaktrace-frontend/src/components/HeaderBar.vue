<template>
  <header class="header">
    <h1>🎧 SpeakTrace 多人語音辨識平台</h1>
    <nav>
      <button 
        v-if="!isLoggedIn" 
        @click="$emit('openLogin')"
        class="nav-btn primary"
      >
        登入 / 註冊
      </button>
      <div v-else class="logged-in-controls">
        <button @click="$emit('openUpload')" class="nav-btn primary">上傳檔案</button>
        <button @click="logout" class="nav-btn">登出</button>
      </div>
    </nav>
  </header>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';

// 定義組件接收的屬性
const props = defineProps({
  isLoggedIn: {
    type: Boolean,
    required: true
  }
});

// 定義組件發出的事件
const emit = defineEmits(['openLogin', 'openRegister', 'openUpload']);

const logout = () => {
  // 實際應用中應清除登入狀態和 token
  alert('登出成功 (模擬)');
  emit('openLogin'); // 登出後導回登入畫面或主頁
};
</script>

<style scoped>
.header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  background-color: #3f51b5; /* 主要品牌色 */
  color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  z-index: 500;
}
h1 {
  font-size: 1.5em;
  margin: 0;
}
.nav-btn {
  background: none;
  color: white;
  border: 1px solid white;
  padding: 8px 15px;
  margin-left: 10px;
  cursor: pointer;
  border-radius: 4px;
  transition: background-color 0.3s;
}
.nav-btn.primary {
  background-color: #ff9800; /* 突顯色 */
  border-color: #ff9800;
}
.nav-btn:hover {
  opacity: 0.8;
}
</style>