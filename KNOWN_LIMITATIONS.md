# Known limitations

## Android app
- **No APK was compiled here.** The build environment couldn't reach Google's Android repository, Maven or the
  Gradle distribution server. `npx cap add/sync` ran; `gradlew assembleDebug` is NOT RUN. Use ANDROID.md.
- **Not tested on a device or emulator.** All app checks ran on the same web bundle in desktop Chromium at phone width.
- `SecureKeysPlugin.java` and `MainActivity.java` were compiled only against signature stubs of the Android and
  Capacitor APIs (javac with all warnings as errors). That checks syntax and types, not runtime behavior.
- System-bar insets use Capacitor's `native` handling; how they look depends on the phone's WebView version.
- Debug APKs are signed with a debug key and are for testing only. Play Store release signing is not set up.

## AI
- Live output quality and real latency are unmeasured; tests use simulated provider replies.
- Suggested model IDs may be retired by providers; the user can type any model ID, and a 404 shows "model unavailable".
- Cost is always UNKNOWN: no provider reports cost to the app. Token counts are shown only when the provider reports them.
- A timed-out request may still finish on the provider side; the app stops waiting and keeps saved steps.

## Data
- Projects are LOCAL ONLY: in the browser's or app's storage. Uninstalling the app or clearing its data deletes them.
- No import yet: an export can be saved and shared but not loaded back.

## Spec coverage (HACKATHON desk feature prompts v2.0)
This build covers the Android part of A16 plus the parts of A10/A11 that an app needs. It is not a full rebuild of
A1–A16: the spec targets a Next.js 15 / Tailwind v4 / Three.js / pptxgenjs codebase that isn't part of this project.
Not implemented: team members and deadlines (A1, A6), stack recommendations (A2), selection-change confirmation (A4),
manual architecture edits and exports (A5), the debug CLI (A12), PPTX and Markdown exports (A13), the Three.js
background (A15), Ollama/Kimi providers and ordered failover (A10), PWA install, and all of Parts B and C.
