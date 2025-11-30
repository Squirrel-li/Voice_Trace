<template>
    <header class="header">
        <h1>🎧 SpeakTrace 多人語音辨識平台</h1>
        
        <button @click.stop="openAccountManagement" class="gear-btn topbar-right">
            <i class="fa-solid fa-gear"></i>
        </button>
        <AccountManagement
            v-if="showAccount"
            class="account-management"
            :is-logged-in="isLoggedIn"
            @openLogin="$emit('openLogin')"
            @openRegister="$emit('openRegister')"
            @openUploadUserinfo="$emit('openUploadUserinfo')"
            @updateLogout="$emit('updateLogout', $event)"
        />
    </header>
</template>

<script setup>
    import { ref, onMounted, onBeforeUnmount, defineProps, defineEmits } from 'vue';
    import AccountManagement from './AccountManagement.vue';

    const props = defineProps({
        isLoggedIn: {
            type: Boolean,
            required: true
        }
    });
    const emit = defineEmits([
        'openLogin', 
        'openRegister', 
        'openUploadUserinfo', 
        'updateLogout'
    ]);

    const showAccount = ref(false);

    const openAccountManagement = () => {
        showAccount.value = true;
    };
    const closeAccountManagement = () => {
        showAccount.value = false;
    };

    // 點擊外部關閉
    const handleClickOutside = (event) => {
        if (
            !event.target.closest('.topbar-right') &&
            !event.target.closest('.account-management')
        ) {
            closeAccountManagement();
        }
    };

    onMounted(() => {
        document.addEventListener('click', handleClickOutside);
    });
    onBeforeUnmount(() => {
        document.removeEventListener('click', handleClickOutside);
    });
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
.gear-btn {
    background-color: #1677ff; /* 你想要的底色 */
    color: white;
    border: none;
    padding: 8px 16px;
    border-radius: 6px;
    cursor: pointer;
    transition: background 0.2s;
}
.gear-btn:hover {
    background-color: #145cc4; /* hover 時的底色 */
}
</style>