import { defineConfig } from "vite";

// Library build to one IIFE. React, ReactDOM and React Flow come from pinned CDN UMD builds (globals),
// so they are external here. JSX uses the classic runtime because react/jsx-runtime has no UMD global.
export default defineConfig(({ command }) => ({
  esbuild: { jsx: "transform", jsxFactory: "React.createElement", jsxFragment: "React.Fragment" },
  // Production mode only for the build; tests need React's development build (act() support).
  define: command === "build" ? { "process.env.NODE_ENV": JSON.stringify("production") } : {},
  build: {
    outDir: "dist",
    emptyOutDir: true,
    cssCodeSplit: false,
    minify: true,
    lib: { entry: "src/main.tsx", formats: ["iife"], name: "HackForge", fileName: () => "app.js" },
    rollupOptions: {
      external: ["react", "react-dom", "react-dom/client", "reactflow"],
      output: {
        globals: { react: "React", "react-dom": "ReactDOM", "react-dom/client": "ReactDOM", reactflow: "ReactFlow" },
        assetFileNames: "app.[ext]",
      },
    },
  },
  test: {
    environment: "node",
    environmentMatchGlobs: [["test/ui.test.tsx", "jsdom"]],
    setupFiles: ["test/setup.ts"],
  },
}));
