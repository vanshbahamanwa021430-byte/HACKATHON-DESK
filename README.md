# HackForge AI

Turns a hackathon problem statement into labeled analysis, 3–5 solution options, a scoresheet you can re-weight,
a clickable architecture, a MUST/SHOULD/CUT plan, a hostile judge and a pitch that flags its own unsupported claims.

## Run the tests
```bash
npm install
npx tsc -p .          # typecheck
npx vitest run        # 54 tests: scoring, AI stages, claims, winners, UI (jsdom)
```

## Build the publishable page
```bash
npx vite build && node scripts/assemble.mjs   # → publish/hackforge.html
node test/built.smoke.mjs                      # boots the built page and walks the demo
```
The page expects to run inside a Claude artifact: live AI uses the `sample` capability and file export uses `downloads`.
Outside Claude, the cached demo, scoring, selection, plan view and local storage all still work; live AI shows as NOT IMPLEMENTED.

## Android app
See **ANDROID.md**. In short: `npm ci`, then `npm run android:sync` and open `android/` in Android Studio,
or push the folder to GitHub and run the **Android APK** workflow to get an installable APK.
The app uses your own AI provider key (Anthropic, OpenAI, Groq, OpenRouter or Gemini), stored encrypted with the Android Keystore.
`npm run test:e2e` checks the built app in Chromium at phone width.

## Where things are
- `src/scoring.ts` deterministic rubric scoring (v10.1 §8)
- `src/ai.ts`, `src/schemas.ts` AI stages with schema validation and one repair
- `src/pitch.ts` claim check · `src/winners.ts` curated winners and matching
- `src/store.ts` LOCAL ONLY storage with schema version, export and reset
- `src/demo.ts` fictional CACHED DEMO project
- `src/providers.ts`, `src/keys.ts` your-own-key AI providers and key custody (Android)
- `src/main.app.tsx`, `app/index.html`, `capacitor.config.ts`, `android/` the Android app
- `android/app/src/main/java/com/hackforge/app/SecureKeysPlugin.java` Keystore-encrypted key storage
- `STATUS.md` gate table and evidence · `decisions.md` decision log

## Honest limits
Live AI output quality and latency, screen-reader behavior and anything on a physical Android device have not been verified.
No APK has been compiled yet (see ANDROID.md). Data lives only in the browser or app that created it. Full list: KNOWN_LIMITATIONS.md.
