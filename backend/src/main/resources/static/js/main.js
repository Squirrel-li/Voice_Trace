const { createApp } = Vue;

createApp({
    data() {
        return {
            title: "歡迎",
            content: "這是首頁。",
            error: ""
        };
    },
    methods: {
        async updateContent() {
            try {
                this.error = "";
                const res = await fetch("/api/time");
                if (!res.ok) throw new Error("Fetch failed");
                const data = await res.json();
                this.content = data.now ?? "時間取得失敗";
            } catch (err) {
                this.error = "無法取得時間";
            }
        }
    }
}).mount("#app");