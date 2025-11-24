<template>
  <HomeView />
</template>

<script setup>
import HomeView from './views/HomeView.vue';
// App.vue 僅負責引入主要的視圖
</script>

<style>
/* 基礎全域樣式，確保頁面佈局正常 */
body {
  font-family: "Noto Sans TC", -apple-system, BlinkMacSystemFont, "Segoe UI",
    sans-serif;
  background: #f5f6fa;
  color: #1f2933;
}
#app {
  text-align: center;
  color: #2c3e50;
}
/* 彈窗背景疊層的通用樣式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}
.modal-content {
  background: white;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  width: 400px;
  max-width: 90%;
}
</style>