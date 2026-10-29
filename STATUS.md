# STATUS — HackForge AI (website + Android app)

Published as a private Claude artifact (Version 1). Storage mode: **LOCAL ONLY**. Live AI: the page's `sample` capability
(the viewer's own Claude account; no API key exists in the page or the repo).

## Evidence (all commands run 2026-10-29, Node v22.23.4)
| Command | Result |
|---|---|
| `npx tsc -p .` | exit 0 |
| `npx vitest run` | 54 passed / 54 (29 core · 15 AI, claims, winners · 10 UI in jsdom) |
| Mutation check (5 injected bugs, one at a time) | 5/5 caught: tie tolerance 0 · missing rating not INCOMPLETE · negative weight accepted · weight change clears selection · CUT task counts as support |
| `npx vite build` + `node scripts/assemble.mjs` | publish/hackforge.html, 150.7 KB |
| `node test/built.smoke.mjs` (built page + identical UMD libraries, jsdom) | 9/9 pass, after fixing a crash it found (see Defects) |

## Gate table
| Gate | Status | Evidence / limit |
|---|---|---|
| G1 Intake | PASS | unit + UI test: limits enforced, errors name field and limit |
| G2 Analysis | PASS (fake AI) | schema + labels enforced; demo outputs validate; live quality NOT RUN |
| G3 Winners | PASS | RESEARCH UNAVAILABLE / NO VERIFIED HISTORICAL MATCH / matches; unverified excluded; no records shipped |
| G4 Scoring | PASS | all 16 §8 cases + mutation check + live re-rank in UI and built page |
| G5 Architecture | PASS | selection-gated tabs; node details; React Flow renders in built page |
| G6 Plan | PASS (demo) | owner, hours, deps, acceptance test, fallback; MUST-hours vs time check |
| G7 Judge | PASS (demo) | 3 perspectives, hostile list, HEURISTIC — NOT OFFICIAL |
| G8 Pitch | PASS | deterministic claim check flags null, unknown and CUT tasks |
| G9 Persistence | PASS (jsdom) | remount restores weights + selection; corrupt/older data kept until confirmed reset |
| G10 Cached demo | PASS | works with AI absent; labeled CACHED DEMO; no live button |
| G11 Failures/secrets | PASS (fake AI) | repair once, no retry on platform errors, Stop, Retry; no keys anywhere |
| G12 Full suite | PARTIAL | typecheck, unit, UI, build, smoke recorded; no lint config; real-browser E2E NOT RUN |
| G13 Repo mode | NOT IMPLEMENTED | out of scope for this build |
| G14 A11y + telemetry | PARTIAL | keyboard tabs, labels, focus styles, reduced motion; per-stage ms logged, cost UNKNOWN; 360 px and screen reader NOT RUN in a real browser |

## Defects found by testing and fixed
- Built page crashed on the Architecture tab: React Flow's CDN build exposes its component differently from npm (React error #130). Fixed with a named import; only the built-page smoke test could see it.
- Stage runner logged the wrong stage when the second step of "analyze + solutions" failed (stale React state). Fixed with a local variable.
- Tests ran React in production mode (act() unsupported). Build-only setting now.
- **Version 2 (reported by user): every live run failed with "temporary problem".** Root cause: the platform's `sample` is a
  function, and `setSample(s)` made React call it as a state updater, storing a Promise instead of the handle. The page then
  failed before reaching Claude, and the catch-all mislabeled it as a service outage. Fixed with `setSample(() => s)`.
  Tests had used a plain-object fake; they now use the platform's real shape (callable + `.json`) and assert the bare function
  is never called. Reverting the fix fails 2 tests. Page-side exceptions are now reported as `page_error` with Technical details.
  Evidence: `npx vitest run` → 56/56; built-page smoke test 9/9.

## Not verified
Live AI output quality and latency (needs a real key or viewer), screen readers, the website's downloads prompt, and everything on a physical Android device.
360 px layout is now verified in Chromium for the app bundle (same CSS as the website).

## Android app (Capacitor 8.6) — added 2026-10-29
| Area | Status | Evidence / limit |
|---|---|---|
| Offline app bundle (fonts, React, React Flow bundled) | PASS (Chromium) | E2E: demo flow with every external request blocked made 0 external requests |
| Phone layout at 360 px | PASS (Chromium) | E2E checks against `screen.width`; screenshots reviewed |
| BYOK providers (A10) | PASS (simulated) | 25 provider/key tests + E2E live flow through a routed Anthropic API; real providers NOT RUN |
| AI settings, consent, Test connection (A11) | PASS (simulated) | jsdom + E2E; key never in storage or exports; 429 echo redacted |
| Android Keystore key storage | COMPILES (stubs) | runtime NOT RUN on a device |
| Native export via share sheet | NOT RUN | needs a device |
| Back button, system bars, splash, icon | NOT RUN | configured; needs a device |
| APK build | NOT RUN / CONFIGURATION NEEDED | see ANDROID.md (GitHub Actions or Android Studio) |
