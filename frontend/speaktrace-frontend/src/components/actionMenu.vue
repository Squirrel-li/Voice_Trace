<template>
	<div
		class="dropdown-menu"
		style="position: absolute; right: 0; top: 30px; background: #fff; border: 1px solid #eee; box-shadow: 0 2px 8px rgba(0,0,0,0.1); border-radius: 24%; z-index: 10; min-width: 120px; padding: 8px 0;"
	>
		<button v-if="file.status!=='完成'" class="toolbar-btn menu-btn" style="padding: 8px 16px; cursor: pointer; margin: 5px 5px;">
			<i style="margin-right: 8px; color: #374151; text-align: center;"> 轉錄</i>
		</button>
		<button v-else class="toolbar-btn menu-btn" style="padding: 8px 16px; cursor: pointer; margin: 5px 5px;">
			<i style="margin-right: 8px; color: #374151; text-align: center;"> 下載</i>
		</button>
		<button class="toolbar-btn menu-btn" style="padding: 8px 16px; cursor: pointer; margin: 5px 5px;">
			<i style="margin-right: 8px; color: #374151; text-align: center;"> 刪除</i>
		</button>
	</div>
</template>

<script setup>
	import { defineProps } from 'vue';

	const props = defineProps({
	file: {
		type: Object,
		required: true
	}
	});
</script>