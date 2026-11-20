<div align="center">

# YORU

**Reproductor de música para Android con motor de audio de alta calidad, Liquid Glass y fondos personalizados.**

[![Licencia: Apache 2.0](https://img.shields.io/badge/licencia-Apache%202.0-1b1418?style=for-the-badge)](LICENSE)
[![Android 8.0+](https://img.shields.io/badge/Android-8.0%2B-3ddc84?style=for-the-badge&logo=android&logoColor=white)](#descargar)
[![Kotlin](https://img.shields.io/badge/Kotlin-Compose-7f52ff?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org)

</div>

## Qué es YORU

Un reproductor que une en una sola biblioteca la música de tu dispositivo con tus servidores (Navidrome, Subsonic, Jellyfin, Plex) y servicios de streaming. Debajo tiene un motor de audio de 64 bits con ecualizador paramétrico, rack de efectos, correcciones para auriculares y salida bit-perfect por USB. Sin cuenta, sin anuncios y sin rastreo.

## Novedades de YORU

- Tema **Liquid Glass** por defecto, con paneles translúcidos.
- **Fondos personalizados:** imagen o video en loop detrás de la app (Apariencia → Temas → Fondo de pantalla), con control de oscurecimiento.
- Reproductor más transparente y marca propia.
- Al elegir una carpeta de música se pide el permiso de audio y se escanea al instante.

## Descargar

Cada vez que se publica un cambio, GitHub Actions compila el APK. Entrá a la pestaña **Actions** → último run de **YORU APK** → descargá el artefacto **YORU-debug-apk**.

## Compilar desde el código

Necesitás JDK 21, Android SDK 36 y NDK 27.0.12077973.

```bash
./gradlew :app:assembleDebug
# APK en app/build/outputs/apk/debug/
```

## Licencia

Apache License 2.0 (ver [LICENSE](LICENSE)). YORU es una obra derivada de un proyecto de código abierto con esa misma licencia; los avisos de licencia originales se conservan, como exige la licencia. Los cambios están descritos en [YORU_CHANGES.md](YORU_CHANGES.md).
