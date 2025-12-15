import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig(({ command, mode }) => {
  // During development use a root base so Vite resolves modules locally.
  // For production builds we want assets to be served from `/frontend/`.
  const isProd = mode === 'production'
  return {
    plugins: [react()],
    base: isProd ? '/frontend/' : '/',
  }
})
