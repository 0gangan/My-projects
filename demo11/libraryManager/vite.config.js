import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server: {
    proxy: {
      // 关键配置：代理 /api 到 Spring Boot 后端
      '/api': {
        target: 'http://localhost:8080', // 你的后端地址
        changeOrigin: true,
        rewrite: (path) => path.replace(/api/, '') // 可选：去掉 /api 前缀
      }
    }
  }
})
