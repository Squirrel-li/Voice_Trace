<template>
    <div class="modal-overlay" @click.self="$emit('close')">
        <div class="modal-content">
            <h2>👤 更改使用者資訊</h2>
            <form @submit.prevent="handleUploadUserinfo">
                <div class="form-group">
                    <label for="login-username">使用者名稱:</label>
                    <input type="text" id="login-username" v-model="username">
                </div>
                <div class="form-group">
                    <label for="login-email">電子郵件:</label>
                    <input type="email" id="login-email" v-model="email">
                </div>
                <div class="form-group">
                    <label for="login-password">密碼:</label>
                    <input type="password" id="login-password" v-model="password">
                </div>
                <button type="submit" class="submit-btn">更新資訊</button>
            </form>
            <button class="close-btn" @click="$emit('close')">X</button>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';

const emit = defineEmits(['close', 'loginSuccess', 'switchToRegister']);

const username = ref('');
const email = ref('');
const password = ref('');

const handleUploadUserinfo = async () => {
        console.log(username.value, email.value, password.value);
        const token = localStorage.getItem('token');
        
        if (!token) {
                alert('請重新登入');
                return;
        }

        fetch('/api/upload/userinfo', {
                method: 'POST',
                headers: {
                        "Content-Type": "application/json",
                        "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify({
                        email: email.value,
                        username: username.value,
                        password: password.value
                })
        })
        .then(response => {
                if (!response.ok) {
                        throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
        })
        .then(data => {
                console.log('更新成功:', data);
                localStorage.setItem('token', data.token);
                emit('loginSuccess', data); // 更新成功通知父層
        })
        .catch(error => {
                console.log('更新失敗:', error);
                alert('更新失敗: ' + error.message);
        });
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
    background-color: #1677ff;
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
    color: #1677ff;
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