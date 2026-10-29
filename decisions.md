# Decisions

| Decision | Alternatives | Reason | How to reverse |
|---|---|---|---|
| Live AI through the artifact `sample` capability | Next.js server route with an API key | Keeps keys out of the browser and repo with no backend to deploy; runs on the viewer's own Claude account | Implement `SampleFn` over a server endpoint; `src/ai.ts` takes any `SampleFn` |
| LOCAL ONLY storage with schema version 2 | Supabase cloud mode | No auth backend in this environment; spec allows local mode with export/reset | Add cloud mode with auth + RLS; keep `KV` interface |
| One automatic repair only on schema failure; no auto-retry on platform errors | Retry any failure once | Platform guidance forbids retry loops; a repair is a different prompt, so it is not a blind retry | Change `runStage` |
| No page-side timeout | Per-stage timer | The platform ends long calls itself; a page timer would also count the viewer's consent dialog. Stop button provided | Add an AbortController timer in `Workspace.run` |
| Cost shown as UNKNOWN | Estimate from tokens | The platform does not report cost to the page; estimates would be invented | Fill from a provider that reports cost |
| React, ReactDOM, React Flow from pinned CDN UMD builds; zod bundled | Bundle everything | Artifact guidance prefers CDN libraries; zod 3.25 has no UMD build | Remove externals in `vite.config.ts` |
| Winners are user-curated only, with a checked-against-organizer checkbox | Live search or scraping | v10.1 §10 forbids invented records; no authorized search API here | Add an authorized search integration that writes UNVERIFIED records to the review queue |
| Demo caches architecture for 2 of 3 options | Cache all | Keeps the demo honest and small; the third option shows an empty state | Add a cached plan for sol-predict |
| **Android:** Capacitor 8 shell around the same React app | React Native or Kotlin rewrite | Reuses every tested module and the scoring engine unchanged; spec A16 names Capacitor | Replace `android/` and `src/main.app.tsx` |
| **Android:** bring-your-own-key providers (Anthropic, OpenAI, Groq, OpenRouter, Gemini) | A HackForge backend holding one key | The Claude artifact runtime doesn't exist in an app; a shared server key would sit in a backend we'd have to run and secure (spec rule 4) | Implement `SampleFn` over a backend endpoint |
| **Android:** keys encrypted by a small app plugin on the Android Keystore | Third-party secure-storage plugin; plain Preferences | No extra dependency, standard platform API, plain Preferences isn't encrypted | Swap `nativeStore` for another `SecureStore` |
| **Android:** encrypted key store excluded from cloud backup and device transfer | Allow backup | The Keystore key can't leave the device, so a backed-up ciphertext is useless and only adds exposure | Remove the backup rules |
| **Android:** fetch routed through CapacitorHttp | Browser fetch | Provider APIs don't all allow browser CORS; native HTTP avoids it | Set `CapacitorHttp.enabled` to false |
| **Android:** own timeout raced against each request | Rely on AbortSignal | Native HTTP may ignore abort; the UI must never hang (mutation M8 proves the race is needed) | Remove the deadline race |
| **Android:** fonts and libraries bundled, no CDN | CDN like the web build | The app must open offline and the CSP blocks other hosts | Change `vite.app.config.ts` |
| **Android:** top-to-bottom diagram layout everywhere | Left-to-right | Readable at 360 px; matches React Flow's default top/bottom handles | Change `layout()` in Arch.tsx |
| **Android:** name kept as HackForge (`com.hackforge.app`) | Rename to "HACKATHON desk" from the v2.0 spec | The app being packaged is HackForge; renaming is a two-line change | `capacitor.config.ts` appName/appId, then `npx cap sync` |
