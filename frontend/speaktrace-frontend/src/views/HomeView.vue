<template>
    <div class="home-page">
        <HeaderBar 
            @open-login="openModal('login')"
            @open-register="openModal('register')"
            @open-upload-userinfo="openModal('uploadUserinfo')"
            @update-logout="logout($event)"
            :is-logged-in="isLoggedIn" 
        />

        <MainContent 
            @open-upload="openModal('upload')"
            @flash-history="fetchHistory"
            :uploadrecord="uploadrecord"
        />

        <teleport to="body">
            <LoginModal 
                v-if="activeModal === 'login'" 
                @close="closeModal" 
                @login-success="handleLoginSuccess"
                @switch-to-register="openModal('register')"
            />
            
            <RegisterModal 
                v-if="activeModal === 'register'" 
                @close="closeModal" 
                @switch-to-login="openModal('login')"
            />

            <UploadModal 
                v-if="activeModal === 'upload'" 
                @close="closeModal"
                @upload-success="fetchHistory"
                :uploadrecord="uploadrecord"
            />
            <UploadUserinfoModal
                v-if="activeModal === 'uploadUserinfo'"
                @close="closeModal"
            />
        </teleport>

    </div>
</template>

<script setup>
    import { ref, onMounted, onBeforeUnmount } from 'vue';
    import HeaderBar from '../components/HeaderBar.vue';
    import MainContent from '../components/MainContent.vue';
    import LoginModal from '../components/LoginModal.vue';
    import RegisterModal from '../components/RegisterModal.vue';
    import UploadModal from '../components/UploadModal.vue';
    import UploadUserinfoModal from '../components/UploadUserinfoModal.vue';

    // 狀態管理
    const isLoggedIn = ref(false); // 模擬登入狀態
    const activeModal = ref(null); // 當前開啟的彈窗 (null, 'login', 'register', 'upload', 'uploadUserinfo')
    const uploadrecord = ref([]); // 模擬歷史紀錄數據

    let intervalId = null; // 定時器 ID

    // 彩窗控制邏輯
    const openModal = (modalName) => {
        activeModal.value = modalName;
    };

    const closeModal = () => {
        activeModal.value = null;
    };

    // 登入成功處理
    const handleLoginSuccess = () => {
        isLoggedIn.value = true;
        fetchHistory();
        closeModal();
    };

    const logout = () => {
        isLoggedIn.value = false;
        clearHistory();
    };

    const clearHistory = () => {
        uploadrecord.value = [];
    };
    const fetchHistory = () => {
        const token = localStorage.getItem('token');
        
        if (isLoggedIn.value && !token) {
            alert('請先登入以查看歷史紀錄');
            return;
        }

        fetch('/api/record/list', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
        .then(response => response.json())
        .then(data => {
            uploadrecord.value = data;
        })
        .catch(error => {
            console.error('Error fetching history:', error);
        });
    };

    // 啟動定時器
    onMounted(() => {
        intervalId = setInterval(fetchHistory, 5000); // 每 5 秒執行一次 fetchHistory
    });

    // 清除定時器
    onBeforeUnmount(() => {
        if (intervalId) {
            clearInterval(intervalId);
        }
    });
</script>

<style scoped>
.home-page {
    padding-top: 60px; /* 為 HeaderBar 留出空間 */
}
</style>