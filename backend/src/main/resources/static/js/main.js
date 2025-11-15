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
            try {
                // 呼叫後端 /api/auth/login
                const res = await fetch("/api/auth/login", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({
                        email: this.loginForm.email, // 如果後端用 email，這裡要填 email
                        password: this.loginForm.password
                    })
                });
                if (!res.ok) throw new Error("登入失敗");
                const user = await res.json();
                this.session.loggedIn = true;
                this.session.token = "demo-token"; // 可改為後端回傳的 token
                this.profileForm.email = user.email;
                this.profileForm.name = user.username;
                this.view = "library";
                this.messages.success = "登入成功";
            } catch (e) {
                this.messages.error = "登入失敗";
            }
        },
        async register() {
            this.resetMessages();
            if (this.registerForm.password !== this.registerForm.confirm) {
                this.messages.error = "兩次密碼不一致";
                return;
            }
            try {
                // 呼叫後端 /api/auth/signup
                const res = await fetch("/api/auth/signup", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({
                        username: this.registerForm.name,
                        email: this.registerForm.email,
                        password: this.registerForm.password
                    })
                });
                if (!res.ok) throw new Error("註冊失敗");
                this.messages.success = "註冊成功，請登入";
                this.view = "login";
                this.loginForm.email = this.registerForm.email;
            } catch (e) {
                this.messages.error = "註冊失敗";
            }
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