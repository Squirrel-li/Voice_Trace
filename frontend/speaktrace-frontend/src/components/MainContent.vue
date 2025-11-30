<template>
    <main class="layout">
        <!--
        <aside class="left-panel">
            <section class="card">
                <div class="section-label">快捷方式</div>
                <div class="shortcut-item">
                    <span class="shortcut-dot"></span>
                    <span>最近的文件</span>
                </div>
                <div class="section-label section-label--spaced">資料夾</div>
                <button class="folder-new">
                   ➕ 新建資料夾
                </button>
            </section>
        </aside>-->
             
        <section class="content-panel">
            <div class="content-header">
                <div class="content-header-left">
                    <span class="icon">☰</span>
                    <span>最近的文件</span>
                </div>
                <div class="content-header-right">
                    <!--<button class="icon-btn" aria-label="搜尋" @click="openSearchModal">🔍</button>-->
                    <button class="toolbar-btn" @click="$emit('openUpload')">⬆️ 上傳語音檔</button>
                </div>
            </div>
                
            
            <section class="card file-list-card">
                <div class="file-header-row">
                <div class="file-col file-col-checkbox file-row-checkbox">
                    <div class="header-action-wrapper">
                    <input
                      type="checkbox"
                      id="select-all-checkbox"
                      :checked="allSelected"
                      @change="toggleSelectAll"
                    >
                    <button @click="deleteSelected()" id="delete-trigger-btn" class="delete-trigger-btn">
                        <i class="fas fa-trash-alt"></i>
                    </button>
                    </div>
                </div>
                <div class="file-col file-col-name file-col-name-header">名稱</div>
                <!--<div class="file-col file-col-len">語言</div>-->
                <div class="file-col file-col-status">狀態</div>
                <div class="file-col file-col-more"></div>
                </div>
            
                <table class="file-table">
                    <tbody id="file-list-body">
                        <tr v-for="file in props.uploadrecord" :key="file.id" class="file-row file-row-body">
                            <td class="file-col file-col-checkbox file-row-checkbox" style="margin-left: 0.5%;">
                                <input
                                    type="checkbox"
                                    class="file-checkbox"
                                    :value="file.id"
                                    v-model="selectedIds"
                                >
                                <div class="file-row-checkbox-spacer"></div>
                            </td>
                            <td class="file-col file-col-name file-row-name" style="margin-left: 0%; min-width: 40%;">
                                <span class="file-row-name-text">{{ file.filename }}</span>
                                <span v-if="file.time" class="file-row-name-date">| {{ file.time }}</span>
                            </td>
                            <!--<td class="file-col file-col-len file-row-len" style="margin-left: 10px;">
                                <span class="file-row-len-text">{{ file.language || '中文' }}</span>
                            </td>-->
                            <td class="file-col file-col-status file-row-status" style="min-width: 10%;">
                                <span class="file-row-status-dot"
                                    :style="{ backgroundColor: file.statusColor || '#16a34a' }"></span>
                                <span class="file-row-status-text">{{ file.status || '完成' }}</span>
                            </td>
                            <td class="file-col file-col-more file-row-more" style="position: relative;">
                                <i
                                    class="fas fa-ellipsis-h file-row-more-icon"
                                    style="font-size: 20px;color: #717781; cursor: pointer;"
                                    @click="openMenu(file.id)"
                                ></i>
                                <!-- 小選單 -->
                                <actionMenuModal 
                                    v-if="activeMenuId === file.id"
                                    @transcribe="transcribe(file.id)"
                                    @download="download(file.id)"
                                    @delete-file="deleteFile(file.id)"
                                    :file="file"
                                />
                            </td>
                        </tr>
                        <tr v-if="props.uploadrecord.length === 0">
                            <td colspan="5" class="file-empty-row">
                                未上傳檔案
                            </td>
                        </tr>
                    </tbody>
                </table>
            </section>
        </section>
    </main>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<link rel="stylesheet" href="../../style.css" />
</template>

<script setup>
    import { ref, onMounted, onBeforeUnmount, computed } from 'vue';
    import actionMenuModal  from "./ActionMenuModal.vue";

    const props = defineProps({
        uploadrecord: {
            type: Array,
            default: () => []
        }
    });

    const emit = defineEmits(['openUpload', 'flashHistory']);

    const activeMenuId = ref(null);
    const selectedIds = ref([]);

    const transcribe = (id) => {
        const token = localStorage.getItem('token');
        if (!token) {
            alert('請先登入以進行轉錄');
            return;
        }

        console.log('取得 token:', token);
        console.log('開始轉錄，檔案ID:', id);

        fetch('/api/speech/tts', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ id: id })
        })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log('轉錄成功:', data);
            emit('flashHistory');
        })
        .catch(error => {
            alert(error.message || '轉錄失敗，請稍後再試。');
        });
    };

    // 全選 checkbox 狀態
    const allSelected = computed(() => 
        props.uploadrecord.length > 0 &&
        selectedIds.value.length === props.uploadrecord.length
    );

    // 點擊全選
    const toggleSelectAll = () => {
    if (allSelected.value) {
        selectedIds.value = [];
    } else {
        selectedIds.value = props.uploadrecord.map(file => file.id);
    }
    };

    const download = (id) => {
        const token = localStorage.getItem('token');
        if (!token) {
            alert('請先登入以下載檔案');
            return;
        }

        fetch(`/api/record/download?id=${id}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.blob();
        })
        .then(blob => {
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = `record_${id}.txt`; // 可根據需要修改檔名
            document.body.appendChild(a);
            a.click();
            a.remove();
            window.URL.revokeObjectURL(url);
        })
        .catch(error => {
            alert(error.message || '下載失敗，請稍後再試。');
        });
    };


    const deleteFile = (id) => {
        selectedIds.value = [id];
        deleteSelected();
    }

    const deleteSelected = () => {
        if (selectedIds.value.length === 0) {
            alert('請先選擇要刪除的檔案');
            return;
        }
        const token = localStorage.getItem('token');
        if (!token) {
            alert('請先登入以刪除檔案');
            return;
        }

        fetch ('/api/record/delete', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ 
                ids:selectedIds.value 
            })
        })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log('刪除成功:', data);
            emit('flashHistory');
            selectedIds.value = [];
        })
        .catch(error => {
            alert(error.message || '刪除失敗，請稍後再試。');
        });
    };

    const openMenu = (id) => {
        activeMenuId.value = activeMenuId.value === id ? null : id;
    };

    const closeMenu = () => {
        activeMenuId.value = null;
    };

    // 點擊外部關閉選單
    const handleClickOutside = (event) => {
        // 判斷點擊是否在選單或 ⋯ 按鈕上
        if (
            !event.target.closest('.file-row-more-icon') &&
            !event.target.closest('.dropdown-menu')
        ) {
            closeMenu();
        }
    };

    onMounted(() => {
        document.addEventListener('click', handleClickOutside);
    });
        onBeforeUnmount(() => {
        document.removeEventListener('click', handleClickOutside);
    });
</script>

<style >
.card,
.file-list-card {
  overflow: visible;
}
.menu-btn {
    color: #374151;
    text-align: center;
    background: none;
    border: none;
    font-size: 16px;
    cursor: pointer;
    transition: background 0.2s;
    padding: 8px 16px;
    margin: 5px 5px;
}
.menu-btn:hover {
    background: #f5f6fa;
}
.menu-divider {
    height: 1px;
    background: #eee;
    margin: 4px 0;
}
</style>