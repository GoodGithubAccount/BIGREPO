/// <reference types="vitest" />
import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import viteBasicSslPlugin from "@vitejs/plugin-basic-ssl";

export default defineConfig({
  plugins: [react(), viteBasicSslPlugin()],
  test: {
    environment: "jsdom",
    setupFiles: ["vitest.setup.ts"],
  },
});