import { defineConfig } from "vite";

// Installable app build (Android/Capacitor): a normal multi-file build with every library and font bundled.
export default defineConfig({
  root: "app",
  base: "./",
  esbuild: { jsx: "transform", jsxFactory: "React.createElement", jsxFragment: "React.Fragment" },
  build: { outDir: "../dist-app", emptyOutDir: true, target: "es2020", sourcemap: false, assetsInlineLimit: 0 },
});
