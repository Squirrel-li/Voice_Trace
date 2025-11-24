<template>
  <div class="modal-overlay" @click.self="$emit('close')">
    <div class="modal-content">
      <h2>👤 使用者登入</h2>
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="login-email">電子郵件:</label>
          <input type="email" id="login-email" v-model="email" required>
        </div>
        <div class="form-group">
          <label for="login-password">密碼:</label>
          <input type="password" id="login-password" v-model="password" required>
        </div>
        <button type="submit" class="submit-btn">登入</button>
      </form>
      <p class="switch-link">
        還沒有帳號? 
        <a href="#" @click.prevent="$emit('switchToRegister')">立即註冊</a>
      </p>
      <button class="close-btn" @click="$emit('close')">X</button>
    </div>
  </div>
</template>

<script setup>
import { ref, defineEmits } from 'vue';

const emit = defineEmits(['close', 'loginSuccess', 'switchToRegister']);

const email = ref('');
const password = ref('');

const handleLogin = () => {
  // 這裡僅處理畫面，實際登入邏輯應呼叫後端 API
  console.log('嘗試登入:', email.value);
  alert(`登入中... (模擬成功)`);
  emit('loginSuccess'); // 模擬登入成功，關閉彈窗並更新 HomeView 狀態
};
</script>

<style scoped>
/* 樣式與 App.vue 中的 .modal-overlay, .modal-content 配合 */
.form-group {
  margin-bottom: 15px;
  text-align: left;
}
.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}
.form-group input {
  width: 100%;
  padding: 10px;
  box-sizing: border-box;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.submit-btn {
  width: 100%;
  padding: 10px;
  background-color: #3f51b5;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 10px;
}
.switch-link {
  margin-top: 20px;
  font-size: 0.9em;
}
.switch-link a {
  color: #3f51b5;
  text-decoration: none;
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