const { createApp } = Vue;

createApp({
    data() {
        return {
            view: "login",
            session: { loggedIn: false, token: "" },
            messages: { error: "", success: "" },
            loginForm: { email: "", password: "" },
            registerForm: { name: "", email: "", password: "", confirm: "" },
            profileForm: { name: "", email: "", password: "" },
            uploadForm: { title: "", file: null },
            library: []
        };
    },
    methods: {
        resetMessages() {
            this.messages.error = "";
            this.messages.success = "";
        },
        async login() {
            this.resetMessages();
            if (!this.loginForm.email || !this.loginForm.password) {
                this.messages.error = "請輸入帳密";
                return;
            }
            // TODO: call backend /api/auth/login
            this.session.loggedIn = true;
            this.session.token = "demo-token";
            this.profileForm.email = this.loginForm.email;
            this.profileForm.name = "使用者";
            this.view = "library";
            this.messages.success = "登入成功";
        },
        async register() {
            this.resetMessages();
            if (this.registerForm.password !== this.registerForm.confirm) {
                this.messages.error = "兩次密碼不一致";
                return;
            }
            // TODO: call backend /api/auth/register
            this.messages.success = "註冊成功，請登入";
            this.view = "login";
            this.loginForm.email = this.registerForm.email;
        },
        async updateProfile() {
            this.resetMessages();
            // TODO: call backend /api/users/me
            this.messages.success = "個人資料已更新";
            this.profileForm.password = "";
        },
        handleFile(event) {
            const [file] = event.target.files ?? [];
            this.uploadForm.file = file ?? null;
        },
        async uploadMedia(event) {
            this.resetMessages();
            if (!this.uploadForm.file) {
                this.messages.error = "請選擇檔案";
                return;
            }
            // TODO: call backend /api/media with FormData
            const id = crypto.randomUUID();
            const type = this.uploadForm.file.type.startsWith("video") ? "video" : "audio";
            this.library.unshift({
                id,
                title: this.uploadForm.title || this.uploadForm.file.name,
                type,
                uploadedAt: new Date().toLocaleString(),
                downloadUrl: URL.createObjectURL(this.uploadForm.file)
            });
            this.messages.success = "上傳完成";
            this.uploadForm = { title: "", file: null };
            event.target?.reset?.();
        },
        download(item) {
            if (!item.downloadUrl) return;
            const anchor = document.createElement("a");
            anchor.href = item.downloadUrl;
            anchor.download = `${item.title}.${item.type === "video" ? "mp4" : "mp3"}`;
            anchor.click();
        },
        remove(item) {
            this.library = this.library.filter(entry => entry.id !== item.id);
        }
    }
}).mount("#app");