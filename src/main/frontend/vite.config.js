import { defineConfig } from "vite";
import react from "@vitejs/plugin-react-swc";
import "dotenv/config";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  build: {
    outDir: "../resources/static",
  },
});
