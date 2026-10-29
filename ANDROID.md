# HackForge for Android — build and install

The `android/` folder is a complete Capacitor 8 Android project. It wasn't compiled into an APK where it was
prepared, because that environment can't download the Android SDK or Gradle. Pick one of the two routes below.

## Route A: let GitHub build the APK (no installs on your PC)

1. Create a new **private** repository on github.com.
2. Upload this whole folder to it (GitHub Desktop, or `git init`, `git add .`, `git commit`, `git push`).
   `node_modules/`, build output and signing keys are already excluded by `.gitignore`.
3. Open the repository's **Actions** tab, choose **Android APK**, then **Run workflow**.
   It also runs on every push to `main`.
4. When the run finishes (about 5–10 minutes), open it and download **hackforge-debug-apk**.
   Unzip it to get `app-debug.apk`.
5. Copy the APK to your phone and open it. Android will ask you to allow installs from that app (Files, Chrome, etc.).

The workflow runs the typecheck and all unit tests before building, so a broken change fails before an APK is made.

## Route B: build on your Windows PC with Android Studio

Requirements: **Node.js 22+**, **Android Studio** (Otter or newer; it includes JDK 21 and SDK 36).

```powershell
cd path\to\hackforge
npm ci
npm run android:sync     # builds the app and copies it into android/
npx cap open android     # opens the project in Android Studio
```

Then in Android Studio:
- **Run on your phone:** turn on *Developer options → USB debugging* on the phone, plug it in, press Run ▶.
- **Make an APK file:** *Build → Build App Bundle(s) / APK(s) → Build APK(s)*. The file appears in
  `android\app\build\outputs\apk\debug\app-debug.apk`.

From a terminal instead: `cd android` then `gradlew.bat assembleDebug`.

## First run on the phone

1. Open **AI** in the top bar.
2. Choose a provider (Anthropic Claude, OpenAI, Groq, OpenRouter or Google Gemini) and check the model ID.
3. Paste your API key, keep **Remember on this device** on, and press **Save key**.
   The key is encrypted with the Android Keystore and is never included in exports or backups.
4. Tick **I allow HackForge to send…** for that provider.
5. Press **Test connection**. You should see the provider, model and the measured response time.

The demo, scoring, selection, saved projects and the plan view all work offline and without a key.

## Publishing to Google Play (not done; needs your decision)

A Play Store release needs a signed **App Bundle** (`.aab`) and a Google Play developer account.
In Android Studio use *Build → Generate Signed App Bundle*, create an upload key, and keep the
`.jks` file and its passwords safe and **out of the repository** (`.gitignore` already excludes them).
Nothing has been published or signed on your behalf.

## What still needs a real phone

These were not tested on a device and must be checked by hand:
- The APK installs and opens on your phone (Android 7.0 / API 24 or newer).
- Content sits below the status bar and above the navigation bar, with black bars.
- **Save key** with *Remember on this device*: close the app fully, reopen, and the key is still set.
- A live analysis with your real key, and **Stop** during a long step.
- **Export all as JSON** opens the Android share sheet.
- The hardware back button steps back through screens and leaves the app from the home screen.
