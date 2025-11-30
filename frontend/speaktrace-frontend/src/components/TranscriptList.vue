<template>
  <div class="transcript-list">
    <h4 v-if="items.length === 0" class="empty-message">
      尚未有任何辨識紀錄，請上傳檔案開始辨識。
    </h4>
    <table v-else>
      <thead>
        <tr>
          <th>ID</th>
          <th>檔案名稱</th>
          <th>上傳時間</th>
          <th>狀態</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in items" :key="item.id">
          <td>{{ item.id }}</td>
          <td>{{ item.filename }}</td>
          <td>{{ item.time }}</td>
          <td>
            <span :class="{'status-complete': item.status === '完成', 'status-processing': item.status === '處理中'}">
              {{ item.status }}
            </span>
          </td>
          <td>
            <button 
              v-if="item.status === '完成'" 
              @click="downloadTranscript(item)" 
              class="action-link download-btn"
            >
              下載文檔 (F3)
            </button>
            <span v-else>等待中...</span>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>

  const props = defineProps({
    items: {
      type: Array,
      required: true
    }
  });

  const downloadTranscript = (item) => {
    // 實際應用中，這會是 API 呼叫以下載 Word/txt 檔案 [cite: 81, 101]
    alert(`正在為 ${item.filename} 準備下載連結... (模擬)`);
  };
</script>

<style scoped>
.transcript-list {
  margin-top: 20px;
}
.empty-message {
  text-align: center;
  color: #666;
  padding: 20px;
  border: 1px solid #eee;
  border-radius: 4px;
}
table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px;
}
th, td {
  border: 1px solid #ddd;
  padding: 12px;
  text-align: left;
}
th {
  background-color: #f2f2f2;
  font-weight: bold;
}
.status-complete {
  color: green;
  font-weight: bold;
}
.status-processing {
  color: orange;
}
.action-link {
  background: none;
  border: none;
  color: #3f51b5;
  cursor: pointer;
  text-decoration: underline;
}
.download-btn {
  background-color: #8bc34a; /* 綠色，表示可下載 */
  color: white;
  padding: 6px 10px;
  border-radius: 4px;
  text-decoration: none;
}
</style>