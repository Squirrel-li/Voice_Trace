<template>
    <div class="modal-overlay" @click.self="$emit('close')">
        <div class="modal-content display">
            <h2>觀看文本</h2>
            <div class="horizontal-layout">
                <div class="video-container">
                    <video v-if="displayupload!=null" controls class="responsive-video" style="width: 100%;">
                        <source :src="displayupload.videoUrl" type="video/mp4">
                        您的瀏覽器不支援影片播放。
                    </video>
                    <p v-else>未找到影片</p>
                </div>
                <div class="transcript-list">
                    <div class="horizontal-layout" v-for="(transcript, index) in displayTranscriptList" :key="index">
                        <div class="speaker">
                            <p>{{ transcript.speaker }}</p>
                            <p>{{ Math.floor(transcript.time / 60) }}:{{ (transcript.time % 60).toFixed(0).padStart(2, '0') }}</p>
                        </div>
                        <div class="text">
                            <p>{{ transcript.text }}</p>
                        </div>
                    </div>
                </div>
            </div>
			<div v-for="(speaker, index) in displayspeakers" :key="index" class="speaker-container">
				<div class="horizontal-layout">
					<p class="speaker-info">{{ speaker.name }} (ID: {{ speaker.id }})</p>
					<input type="text" class="speaker-input" v-model="speaker.newLabel" :placeholder="`為 ${speaker.name} 指定新標籤`" />
					<button class="update-btn" @click="changeSpeakerLabel(index)">更新標籤</button>
				</div>
			</div>
            <button class="close-btn" @click="$emit('close')">X</button>
        </div>
    </div>
</template>

<script setup>
	import { onMounted } from 'vue';
	import { ref } from 'vue';

	const emit = defineEmits(['close', 'loginSuccess', 'switchToRegister']);

	const props = defineProps({
        displayID: {
            type: Array,
            default: null
        }
    });

	
    const displayTranscriptList = ref([]);
    const displayupload = ref(null);
    const displayspeakers = ref([]);

	const uploadDisplayData = async () => {
        const token = localStorage.getItem('token');
		console.log('Fetching display data for ID:', props.displayID);
		fetch(`/api/record/display/${props.displayID}`, {
			method: 'GET',
			headers: {
				'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
			},
		})
		.then(response => response.json())
		.then(data => {
			console.log('Fetched display data:', data.transcriptList);
			console.log('Fetched display data:', data.uploadrecord);
			displayTranscriptList.value = data.transcriptList;
			displayupload.value = data.uploadrecord;
			uniqueSpeakers();
		})
		.catch(error => {
			console.error('Error fetching display data:', error);
		});
	};

	const uniqueSpeakers = () => {
		for (let i = 0; i < displayTranscriptList.value.length; i++) {
			const speakerID = displayTranscriptList.value[i].speakerID;
			const speakerName = displayTranscriptList.value[i].speaker;

			if (!displayspeakers.value.some(speaker => speaker.id === speakerID)) {
				displayspeakers.value.push({ id: speakerID, name: speakerName });
			}
		}
	};

	const changeSpeakerLabel = async (index) => {
        const speaker = displayspeakers.value[index];
        if (speaker.newLabel && speaker.newLabel.trim() !== '') {
            // 更新前端資料
            speaker.name = speaker.newLabel;
            displayTranscriptList.value.forEach(transcript => {
                if (transcript.speakerID === speaker.id) {
                    transcript.speaker = speaker.newLabel;
                }
            });
			
			const token = localStorage.getItem('token');
			fetch(`/api/record/update/${speaker.id}`, {
				method: 'PUT',
				headers: {
					'Content-Type': 'application/json',
					'Authorization': `Bearer ${token}`
				},
				body: JSON.stringify({ newLabel: speaker.newLabel })
			})
			.then(async response => {
				console.log(`Speaker ID ${speaker.id} 的標籤已更新為: ${speaker.name}`);
			})
			.catch(error => {
				console.error('更新標籤時發生錯誤:', error);
            });
            speaker.newLabel = '';
        } else {
            console.error('標籤不能為空');
        }
    };

	onMounted(() => {
        uploadDisplayData();
    });
	
</script>

<style scoped>
.display {
	width: 60%;
	height: 80%;
	gap: 20px;
}


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
.speaker {
    flex: 1; /* 左側部分占據一半空間 */
    padding: 10px;
    border-right: 1px solid #ccc; /* 添加分隔線 */
    text-align: right; /* 左對齊 */
    max-height: 400px; /* 設置最大高度，超過時啟用滾動條 */
    overflow-y: auto; /* 超過最大高度時顯示滾動條 */
}

.text {
    flex: 3; /* 右側部分占據一半空間 */
    padding: 10px;
    text-align: left; /* 左對齊 */
    max-height: 400px; /* 設置最大高度，超過時啟用滾動條 */
}

.video-container {
    flex: 1; /* 左側容器占據剩餘空間 */
    display: flex;
    justify-content: center; /* 內容居中 */
    align-items: center; /* 垂直居中 */
    padding: 10px;
    border: 1px solid #ccc; /* 添加邊框 */
    border-radius: 4px; /* 圓角 */
    background-color: #f9f9f9; /* 背景色 */
    height: 100%; /* 強制設置高度為父容器的高度 */
}

.transcript-list {
    flex: 1; /* 右側容器占據剩餘空間 */
    overflow-y: auto; /* 如果內容超過最大高度，添加垂直滾動條 */
    padding: 10px;
    border: 1px solid #ccc; /* 添加邊框 */
    border-radius: 4px; /* 圓角 */
    background-color: #fff; /* 背景色 */
    max-height: 500px; /* 設置最大高度，超過時啟用滾動條 */
}

/* 外層容器 */
.speaker-container {
    margin-bottom: 15px; /* 每個 speaker 之間的間距 */
    padding: 10px; /* 添加內邊距 */
    border: 1px solid #ccc; /* 添加邊框 */
    border-radius: 8px; /* 圓角 */
    background-color: #f9f9f9; /* 背景色 */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 添加陰影 */
}

/* 橫向佈局 */
.horizontal-layout {
    display: flex;
    flex-direction: row; /* 橫向排列 */
    gap: 20px; /* 元素之間的間距 */
    align-items: flex-start; /* 垂直方向對齊方式 */
}

/* Speaker 信息 */
.speaker-info {
    font-weight: bold; /* 加粗文字 */
    font-size: 1em; /* 調整字體大小 */
    color: #333; /* 字體顏色 */
    flex: 0 0 200px; /* 固定寬度，讓 speaker 信息占據左側 */
    text-align: right; /* 右對齊 */
}

/* 輸入框 */
.speaker-input {
    flex: 1; /* 右側輸入框占據剩餘空間 */
    padding: 10px; /* 添加內邊距 */
    font-size: 1em; /* 調整字體大小 */
    border: 1px solid #ccc; /* 邊框 */
    border-radius: 4px; /* 圓角 */
    box-sizing: border-box; /* 確保寬度包含邊框和內邊距 */
    background-color: #fff; /* 背景色 */
}
.speaker-input:focus {
    border-color: #1677ff; /* 聚焦時的邊框顏色 */
    outline: none; /* 移除默認的聚焦樣式 */
    box-shadow: 0 0 4px rgba(22, 119, 255, 0.5); /* 聚焦時的陰影效果 */
}

/* 更新按鈕樣式 */
.update-btn {
    padding: 10px 20px; /* 調整按鈕的內邊距 */
    background-color: #1677ff; /* 按鈕背景色 */
    color: white; /* 按鈕文字顏色 */
    border: none; /* 移除邊框 */
    border-radius: 4px; /* 圓角 */
    cursor: pointer; /* 鼠標移到按鈕上時顯示手型 */
    font-size: 1em; /* 調整字體大小 */
    transition: background-color 0.3s ease, box-shadow 0.3s ease; /* 添加過渡效果 */
}

.update-btn:hover {
    background-color: #125bcc; /* 滑鼠懸停時的背景色 */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 滑鼠懸停時的陰影效果 */
}

.update-btn:active {
    background-color: #0f4a9c; /* 按下時的背景色 */
    box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.2); /* 按下時的內陰影效果 */
}
</style>