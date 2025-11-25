<template>
    <header class="header">
        <h1>🎧 SpeakTrace 多人語音辨識平台</h1>
        <div class="topbar-right">
            <button 
                v-if="!isLoggedIn" 
                @click="$emit('openLogin')"
                class="auth-btn"
            >登入</button>
            <button 
                v-if="!isLoggedIn" 
                @click="$emit('openRegister')"
                class="auth-btn auth-btn--primary"
            >註冊</button>
            <div v-else class="logged-in-controls">
            <button @click="logout" class="auth-btn">登出</button>
            </div>
        </div>
    </header>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';

const props = defineProps({
    isLoggedIn: {
        type: Boolean,
        required: true
    }
});

const emit = defineEmits(['openLogin', 'openRegister', 'openUpload', 'logout']);

const logout = () => {
    alert('登出成功 (模擬)');
    emit('logout'); // 通知父組件要登出
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
    background-color: #1677ff; /* 主要品牌色 */
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