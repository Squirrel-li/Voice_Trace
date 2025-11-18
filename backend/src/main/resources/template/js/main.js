const { createApp } = Vue;

createApp({
    data() {
        return {
            view: "dashboard",
            session: { loggedIn: false, token: "" },
            uploadRecordList : [
                {id: 1, filename: "testAudio1.m4a", status: "PENDING", uploadTime: "2024-06-01 10:00"},
                {id: 1, filename: "testAudio1.m4a", status: "PENDING", uploadTime: "2024-06-01 10:00"},
                {id: 1, filename: "testAudio2.wav", status: "CONFIRMED", uploadTime: "2024-06-01 10:00"}
            ],
            messages: {
                trans: {error: "", success: ""},
                tests: {error: "", success: ""},
            },
            usage: {
                totalLanguageUsage: 100,
                LanguageStatistics: [
                    {Language: "English", NumOfFiles: 200},
                    {Language: "Chinese", NumOfFiles: 100},
                    {Language: "Japanese", NumOfFiles: 100}
                ]
            }
        };
    },
    methods: {
        resetMessages() 
        {
            this.messages.error = "";
            this.messages.success = "";
        },
        resetUploadRecordList() 
        {
            this.usage = {
                totalLanguageUsage: 0,
                LanguageStatistics: [
                    {Language: "English", NumOfFiles: 200},
                    {Language: "Chinese", NumOfFiles: 100},
                    {Language: "Japanese", NumOfFiles: 100}
                ]
            }
            this.reflash_usage();
        },
        reflash_usage()
        {
            result = 0;
            for (let i = 0; i < this.usage.LanguageStatistics.length; i++) {
                result += this.usage.LanguageStatistics[i].NumOfFiles;
            }
            this.usage.totalLanguageUsage = result;
        },
        switchView(viewName) {
            this.view = viewName;
        },
        async login() {
            
        }
    },
    mounted() {
        this.resetUploadRecordList();
    }
}).mount("#app");
