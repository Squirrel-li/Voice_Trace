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
                const res = await fetch("/api/auth/login", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({
                        email: this.loginForm.email,
                        password: this.loginForm.password
                    })
                });
                if (!res.ok) {
                    // 如果回應不是 200，解析錯誤訊息
                    const errorData = await res.json();
                    this.messages.error = errorData.message;
                    return;
                }
                
                const successData = await res.json();
                this.session.loggedIn = true;
                this.session.token = successData.token; // 使用後端回傳的 token
                this.profileForm.email = successData.email;
                this.profileForm.name = successData.username;
                this.view = "library";
                this.messages.success = "登入成功";
                //localStorage.setItem("token", successData.token);
            } catch (e) {
                this.messages.error = "伺服器錯誤，請稍後再試";
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
            if (!this.profileForm.name || !this.profileForm.email) {
                this.messages.error = "名稱與電子郵件不可為空";
                return;
            }
            try {
                const res = await fetch("/api/upload/userinfo", {
                    method: "POST",
                    headers: { 
                        "Content-Type": "application/json",
                        "Authorization": `Bearer ${this.session.token}`
                    },
                    body: JSON.stringify({
                        email: this.profileForm.email,
                        username: this.profileForm.name,
                        password: this.profileForm.password
                    })
                });
                if (!res.ok) {
                    const res_text = await res.text();
                    this.messages.error = res_text;
                    return;
                }
                const res_json = await res.json();
                const res_text = res_json.message;
                this.session.token = res_json.token;
                this.messages.success = res_text;
                this.profileForm.password = "";
            } catch (e) {
                this.messages.error = e.message || "伺服器錯誤，請稍後再試";
            }
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
