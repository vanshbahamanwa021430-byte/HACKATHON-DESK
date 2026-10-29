# Test evidence

Environment: Linux container, Node v22.23.4, Chromium via Playwright, JDK "21.0.12.1". Date: 2026-10-29T10:16Z.
Git: not a repository (no commit hash).

| Command | Exit | Result |
|---|---|---|
| `npm run typecheck` | 0 | no errors |
| `npm test` (Vitest) | 0 | 84 passed / 84: scoring 29 · AI stages, claims, winners, layout 17 · providers and keys 25 · UI in jsdom 13 |
| Mutation check, scorer (M1–M3) and UI/claims (M4–M5) | — | 5/5 caught (earlier run, still covered) |
| Mutation check, provider adapter: 401 unclassified (M6), redaction off (M7), no timeout race (M8) | — | 3/3 caught: 5, 1 and 2 failing tests; restored code 84/84 |
| `npm run build:app` | 0 | dist-app: one JS bundle, CSS, bundled woff/woff2 fonts |
| `node test/e2e.app.mjs` (built app, Chromium 360×800, real CSP, simulated provider) | 0 | 19/19 checks |
| `npx cap add android`, `npx cap sync android` | 0 | project generated; 3 plugins (app, filesystem, share) |
| `javac --release 21 -Xlint:all -Werror` on SecureKeysPlugin + MainActivity against API stubs | 0 | compiles; stubs mirror real Capacitor 8.6 signatures |
| `npm run build:web` + `node test/built.smoke.mjs` | 0 | 9/9; website republished (version 3) |
| `./gradlew assembleDebug` | NOT RUN | Android SDK, Maven and Gradle downloads are blocked in this environment |
| Install and run on an Android device or emulator | NOT RUN | no device or emulator available |

## Defects found by these tests and fixed
- At 360 px the whole page scrolled sideways to 724 px: a screen-reader-only table label escaped its scroll box. This also affected the published website.
  The first overflow check missed it because mobile emulation widens `innerWidth`; the check now compares with `screen.width`.
- Checkboxes inherited text-field styles and rendered as dots.
- The architecture diagram was a single unreadable row on phones; edge labels had white backgrounds.
