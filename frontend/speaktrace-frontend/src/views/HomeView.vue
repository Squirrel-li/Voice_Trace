<template>
  <div class="home-page">
    <HeaderBar 
      @open-login="openModal('login')"
      @open-register="openModal('register')"
      @open-upload="openModal('upload')"
      :is-logged-in="isLoggedIn" 
    />

    <MainContent 
      @open-upload="openModal('upload')"
      @view-history="fetchHistory"
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
      />
    </teleport>

  </div>
</template>

<script setup>
import { ref } from 'vue';
import HeaderBar from '../components/HeaderBar.vue';
import MainContent from '../components/MainContent.vue';
import LoginModal from '../components/LoginModal.vue';
import RegisterModal from '../components/RegisterModal.vue';
import UploadModal from '../components/UploadModal.vue';

// 狀態管理
const isLoggedIn = ref(false); // 模擬登入狀態
const activeModal = ref(null); // 當前開啟的彈窗 (null, 'login', 'register', 'upload')
const uploadrecord = ref([
    { id: 1, filename: '會議記錄001.mp4', time: '2025-11-20 10:00', language: '中文', status: '完成' },
    { id: 2, filename: '訪談錄音.wav', time: '2025-11-19 15:30', language: '中文', status: '處理中' },
  ]); // 模擬歷史紀錄數據

// 彈窗控制邏輯
const openModal = (modalName) => {
  activeModal.value = modalName;
};

const closeModal = () => {
  activeModal.value = null;
};

// 登入成功處理
const handleLoginSuccess = () => {
  isLoggedIn.value = true;
  closeModal();
};

// 模擬查詢歷史紀錄 (對應功能 F4)
const fetchHistory = () => {
  // 這裡應是 API 呼叫，目前為模擬數據
  uploadrecord.value = [
    { id: 1, filename: '會議記錄001.mp4', time: '2025-11-20 10:00', language: '中文', status: '完成' },
    { id: 2, filename: '訪談錄音.wav', time: '2025-11-19 15:30', language: '中文', status: '處理中' },
  ];
};


</script>

<style scoped>
.home-page {
  padding-top: 60px; /* 為 HeaderBar 留出空間 */
}
</style>