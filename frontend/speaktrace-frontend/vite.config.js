import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',   // Spring Boot port
        changeOrigin: true,
      }
    },
    host: '0.0.0.0',
    port: 5173,
  }
})
//10.244.178.140:5173
