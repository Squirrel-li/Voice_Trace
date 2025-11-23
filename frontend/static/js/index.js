const { createApp } = Vue;

createApp({
    data() {
        return {
			isUploadModalVisible: true,
            view: "t",
            uploadForm: { title: "", file: null },
            messages: { error: "", success: "" },
            upload_modal_style: { 
                display: "none", // 初始隱藏
                position: "fixed", 
                top: "0", left: "0", 
                width: "100vw", 
                height: "100vh",
                backgroundColor: "rgba(0,0,0,0.5)", 
                zIndex: 9999,
                justifyContent: "center", 
                alignItems: "center"
            }
        };
    },
    methods: {
        resetMessages() {
            this.messages.error = "";
            this.messages.success = "";
        },
        showUploadModal() {
            this.upload_modal_style.display = "flex"; // 顯示 modal
        },
        hideUploadModal() {
            this.upload_modal_style.display = "none"; // 隱藏 modal
        }
    },
    mounted() {
        this.hideUploadModal();
    }
}).mount("#upload_modal");