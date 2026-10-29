import type { CapacitorConfig } from "@capacitor/cli";

const config: CapacitorConfig = {
  appId: "com.hackforge.app",
  appName: "HackForge",
  webDir: "dist-app",
  backgroundColor: "#050507",
  android: {
    // Left at Capacitor's default: WebView inspection is on in debug builds only, off in release builds.
    allowMixedContent: false,
  },
  plugins: {
    // Route fetch through native HTTP: provider APIs are called directly with no browser CORS limits.
    CapacitorHttp: { enabled: true },
    // Pad the WebView below the status and navigation bars (index.html does not use viewport-fit=cover).
    SystemBars: { insetsHandling: "native", style: "DARK" },
  },
};

export default config;
