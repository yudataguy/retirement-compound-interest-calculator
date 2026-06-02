# Releasing a reproducible build for F-Droid

F-Droid ships *your* signed APK only if it can rebuild the same bytes from
source. That requires three things to line up: a release signing key, a signed
APK published at the `Binaries` URL, and that key's fingerprint in
`AllowedAPKSigningKeys`. Do these once per signing key, then once per release.

## One-time: create a signing key

Keep this keystore and its passwords safe and backed up — if you lose them you
can never publish an update under the same signature.

```sh
keytool -genkey -v \
  -keystore release.jks \
  -alias upload \
  -keyalg RSA -keysize 4096 \
  -validity 10000
```

Create `keystore.properties` in the repo root (it is git-ignored):

```properties
storeFile=/absolute/path/to/release.jks
storePassword=<store password>
keyAlias=upload
keyPassword=<key password>
```

## Per release: build, fingerprint, publish

> **Toolchain:** build with **JDK 17** (AGP 8.7.3's supported JDK; newer JDKs
> break lint). The committed Gradle wrapper pins **Gradle 8.11.1**, so always
> invoke `./gradlew` (never a system `gradle`) — this is the same version
> F-Droid uses to rebuild, which is what makes the build reproducible.
>
> ```sh
> export JAVA_HOME="$(/usr/libexec/java_home -v 17)"
> ```

1. Build the signed release APK:

   ```sh
   ./gradlew assembleRelease
   # output: app/build/outputs/apk/release/app-release.apk
   ```

2. Get the SHA-256 fingerprint (lowercase, no colons) for
   `AllowedAPKSigningKeys` in the F-Droid metadata template:

   ```sh
   apksigner verify --print-certs app/build/outputs/apk/release/app-release.apk \
     | sed -n 's/.*SHA-256 digest: //p' | tr -d ' '
   ```

   Paste the value into `dev.samyu.compoundcalculator.yml.template`, replacing
   `REPLACE_WITH_SHA256_FINGERPRINT`. This only changes if you change keys.

3. Tag the release and publish the APK to GitHub Releases so the `Binaries`
   URL resolves. The tag must be `v<versionName>` and the asset must be named
   `app-release.apk`:

   ```sh
   git tag v0.1.0 && git push origin v0.1.0
   gh release create v0.1.0 \
     app/build/outputs/apk/release/app-release.apk \
     --title v0.1.0 --notes "Release 0.1.0"
   ```

   This produces:
   `https://github.com/yudataguy/retirement-compound-interest-calculator/releases/download/v0.1.0/app-release.apk`
   which matches `Binaries` with `%v` = `0.1.0`.

## Verifying reproducibility locally (optional but recommended)

Before submitting, confirm F-Droid will be able to reproduce your APK:

```sh
fdroid build dev.samyu.compoundcalculator
fdroid verify dev.samyu.compoundcalculator
```

`fdroid verify` downloads the `Binaries` APK, compares it byte-for-byte to the
build from source, and reports whether they match.
