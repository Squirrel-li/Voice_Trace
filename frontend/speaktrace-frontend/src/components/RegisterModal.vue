<template>
  <div class="modal-overlay" @click.self="$emit('close')">
    <div class="modal-content">
      <h2>✏️ 註冊新帳號</h2>
      <form @submit.prevent="handleRegister">
        <div class="form-group">
          <label for="reg-username">使用者名稱:</label>
          <input type="text" id="reg-username" v-model="username" required>
        </div>
        <div class="form-group">
          <label for="reg-email">電子郵件:</label>
          <input type="email" id="reg-email" v-model="email" required>
        </div>
        <div class="form-group">
          <label for="reg-password">密碼:</label>
          <input type="password" id="reg-password" v-model="password" required>
        </div>
        <button type="submit" class="submit-btn register">註冊</button>
      </form>
      <p class="switch-link">
        已有帳號? 
        <a href="#" @click.prevent="$emit('switchToLogin')">立即登入</a>
      </p>
      <button class="close-btn" @click="$emit('close')">X</button>
    </div>
  </div>
</template>

<script setup>
import { ref, defineEmits } from 'vue';
import axios from 'axios';

const emit = defineEmits(['close', 'switchToLogin']);

const username = ref('');
const email = ref('');
const password = ref('');

const handleRegister = async () => {
    console.log(username.value, email.value, password.value);
    const result = await axios.post('/api/auth/signup', {
      username: username.value,
      email: email.value,
      password: password.value,
    })
    .then(response => {
      console.log('註冊成功:', response.data);
      emit('switchToLogin'); // 註冊成功後，引導使用者去登入
    })
    .catch(error => {
      alert(error.response?.data?.message || '註冊失敗，請稍後再試。');
    });
};
</script>

<style scoped>
/* 使用與 LoginModal 相同的樣式 */
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
  background-color: #4CAF50; /* 綠色，區分登入 */
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
  color: #4CAF50;
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