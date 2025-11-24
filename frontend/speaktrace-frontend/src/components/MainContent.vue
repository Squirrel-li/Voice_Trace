<template>
  <main class="main-content-container">
    <div class="intro-section">
      <h2>多人語音辨識服務</h2>
      <p>結合語音辨識、說話者分離與自然語言摘要的 AI 服務 [cite: 8]。</p>
      <button 
        @click="$emit('openUpload')" 
        class="action-btn large primary"
      >
        開始上傳辨識
      </button>
    </div>

    <section class="history-section">
      <h3>我的辨識紀錄 (對應功能 F4)</h3>
      <button @click="$emit('viewHistory')" class="action-btn">
        查詢歷史紀錄
      </button>
      
      <TranscriptList :items="recognitionHistory" />
    </section>
  </main>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';
import TranscriptList from './TranscriptList.vue';

const props = defineProps({
  recognitionHistory: {
    type: Array,
    default: () => []
  }
});

const emit = defineEmits(['openUpload', 'viewHistory']);
</script>

<style scoped>
.main-content-container {
  max-width: 1000px;
  margin: 20px auto;
  padding: 20px;
  text-align: left;
}
.intro-section {
  text-align: center;
  padding: 40px;
  background-color: white;
  border-radius: 8px;
  margin-bottom: 30px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}
.action-btn {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 15px;
  transition: background-color 0.3s;
  background-color: #ccc;
  color: #333;
}
.action-btn.primary {
  background-color: #3f51b5;
  color: white;
}
.action-btn.large {
  padding: 15px 30px;
  font-size: 1.2em;
}
.history-section {
  background-color: white;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}
</style>