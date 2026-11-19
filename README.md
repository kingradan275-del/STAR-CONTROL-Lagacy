# STAR CONTROL Legacy 1.0

Starter Android project targeting Android 4.2.2 (API 17) and newer.

## Features in this starter
- Offline device dashboard
- Model, manufacturer, Android version and API level
- Battery percentage
- Available RAM
- Available external storage
- Dark navy / cyan interface

## Build APK with GitHub Actions
1. Create a new GitHub repository.
2. Upload the contents of this folder to the repository root (not the ZIP file itself).
3. Open the repository's **Actions** tab.
4. Select **Build STAR CONTROL Legacy APK**.
5. Click **Run workflow**.
6. When the workflow finishes, open the run and download the artifact named
   `STAR-CONTROL-Legacy-APK`.

The APK is a debug build for testing. It is not signed as a Play Store release.

## Compatibility notes
- `minSdkVersion 17` targets Android 4.2.2.
- This project uses Java and the platform's built-in Android UI toolkit to support
  older Android versions; it does not use Jetpack Compose.
- The build uses an older Android Gradle Plugin and Android SDK level for legacy
  compatibility. Hosted build environments can change, so the workflow may need
  maintenance if those old build tools are removed.
