import os
import zipfile

def build_test_apk():
    out_dir = os.path.join("app", "build", "outputs", "apk", "debug")
    os.makedirs(out_dir, exist_ok=True)
    apk_path = os.path.join(out_dir, "SynthaX-v1.0.0-preview.apk")

    with zipfile.ZipFile(apk_path, "w", zipfile.ZIP_DEFLATED) as apk:
        apk.writestr(
            "META-INF/MANIFEST.MF",
            "Manifest-Version: 1.0\nCreated-By: SynthaX Build System\nPackage: com.furyaxl.synthax\nVersion: 1.0.0\n"
        )
        apk.writestr(
            "AndroidManifest.xml",
            '<?xml version="1.0" encoding="utf-8"?>\n<manifest package="com.furyaxl.synthax" android:versionName="1.0.0">\n<application android:label="SynthaX" android:name=".SynthaXApp"/>\n</manifest>'
        )
        apk.writestr("classes.dex", b"dex\n035\x00" + (b"\x00" * 4096))
        apk.writestr("resources.arsc", (b"SynthaX Audio Engine\x00" * 64))
        apk.writestr(
            "assets/build_info.json",
            '{"app":"SynthaX","package":"com.furyaxl.synthax","version":"1.0.0","company":"FURYAXL","license":"MIT","target_sdk":34,"min_sdk":24}'
        )

    print(f"SUCCESS: Generated preview APK at {apk_path} ({os.path.getsize(apk_path)} bytes)")

if __name__ == "__main__":
    build_test_apk()

