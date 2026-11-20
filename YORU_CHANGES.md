# Cambios de YORU respecto al proyecto base

## Marca
- Nombre **YORU**, package `com.yoru.music` (se instala aparte de cualquier otra app), icono propio en todas las variantes.
- Todos los textos visibles en los 17 idiomas, el logo interno y la carpeta de descargas (`Music/YORU`) usan la marca YORU.
- Actualizaciones automáticas, enlaces de autor y de comunidad del proyecto base: desactivados / eliminados.

## Apariencia
- Tema por defecto: **Liquid Glass**. Live glass (blur en tiempo real) desactivado por defecto para evitar cierres al cambiar de tema (`ENABLE_LIVE_GLASS` en `ui/theme/LiquidGlass.kt`).
- **Fondos personalizados** (imagen o video en loop, con oscurecimiento): Apariencia → Temas → Fondo de pantalla. Código en `ui/wallpaper/` y `ui/screens/settings/WallpaperSettings.kt`.
- Reproductor más transparente (paneles glass ~50 %).

## Arreglos
1. **Carpetas de música:** al elegir una carpeta se pide el permiso de audio si falta y se escanea al instante (`SourcesSettingsScreen.kt`).
2. **Carátulas:** la authority del proveedor de carátulas ahora sigue al package real (`ArtworkUrls.kt`, `AuroraApplication.kt`).

## Compilar
JDK 21 + Android SDK 36 + NDK 27.0.12077973:
```bash
./gradlew :app:assembleDebug
```
O con GitHub Actions: workflow **YORU APK** (`.github/workflows/yoru-apk.yml`).

## Licencia
Apache 2.0. Se conservan los avisos de licencia del código original.
