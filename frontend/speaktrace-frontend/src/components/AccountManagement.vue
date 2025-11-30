<template>
	<div
		v-if="true"
		class="dropdown-menu"
		style="position: absolute; right: 10px; top: 50px; background: #fff; border: 1px solid #eee; box-shadow: 0 2px 8px rgba(0,0,0,0.1); border-radius: 24px; z-index: 10; min-width: 120px; padding: 8px 0;"
	>
		<!--未登入-->
		<button 
			v-if="!props.isLoggedIn" 
			@click="$emit('openLogin')"
			class="menu-btn toolbar-btn" style="padding: 8px 8px; cursor: pointer; margin: 5px 5px;"
		>登入</button>
		<button 
			v-if="!props.isLoggedIn" 
			@click="$emit('openRegister')"
			class="menu-btn toolbar-btn" style="padding: 8px 8px; cursor: pointer; margin: 5px 5px;"
		>註冊</button>
		<!--已登入-->
		<button
			v-if="props.isLoggedIn"
			@click="$emit('openUploadUserinfo')"
			class="menu-btn toolbar-btn" style="padding: 8px 8px; cursor: pointer; margin: 5px 5px;"
		>帳號管理</button>
		<button 
			v-if="props.isLoggedIn"
			@click="openLogout" 
			class="menu-btn toolbar-btn" style="padding: 8px 8px; cursor: pointer; margin: 5px 5px;"
		>登出</button>
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
	</div>
</template>

<script setup>
    import { ref, onMounted, onBeforeUnmount, defineProps, defineEmits } from 'vue';
	const props = defineProps({
		isLoggedIn: {
			type: Boolean,
			required: true
		}
	});

	const emit = defineEmits([
		'openLogin',
		'openRegister',
		'updateLogout',
		'openUploadUserinfo'
	]);

	const openLogout = () => {
		localStorage.removeItem('token');
		emit('updateLogout', false);
	};

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
.card,
.file-list-card {
  overflow: visible;
}
.dropdown-menu {
    display: flex;
    flex-direction: column;
    align-items: stretch; /* 讓按鈕寬度一致 */
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
    margin: 0; /* 移除水平 margin，讓 flex 控制 */
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