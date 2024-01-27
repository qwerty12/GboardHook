# GboardHook

A crappily-written Xposed module for recent versions of Gboard to not have the new, horrible layout for tablets on fresh installs
 \- while the (internal) option to toggle it continues to exist.

I use Gboard with its internet access cut off and on a tablet with no GMS. I haven't really tried to neuter any mechanisms that are used to treat you like a lab rat.

If you're rooted, just use [Rboard](https://github.com/DerTyp7214/RboardThemeManagerV3).

## Installation

No ready-built APKs; take the code, change the options you don't want and build it yourself.

1. `git clone https://github.com/qwerty12/GboardHook.git`

2. `notepad GboardHook\app\src\main\java\pk\q12\gboardhook\Hook.java` and modify to your heart's content

3. `gradlew assembleRelease`

4. Get a [Gboard APK](https://www.apkmirror.com/apk/google-inc/gboard/gboard-13-7-12-582282722-release/gboard-the-google-keyboard-13-7-12-582282722-release-arm64-v8a-2-android-apk-download/) (not a bundle)

5. Get LSPatch, built from [Git `master`](https://nightly.link/JingMatrix/LSPatch/workflows/main/master/lspatch-release)

6. `java -jar jar-v0.6-412-release.jar --sigbypasslv 1 -m "GboardHook\app\build\outputs\apk\release\app-release-unsigned.apk" --force "%PATH_TO_GBOARD_APK%"`

7. `adb install -r -i "com.android.vending" com.google.android.inputmethod.latin*-lspatched.apk`
