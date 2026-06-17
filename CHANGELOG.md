# Changelog

## [Current - 2026-06-16]
- Created the "Pixel Experiment" screen/tab for both Android and Web applications to demonstrate the recursive upscaling and fractal harmonics within a single quantum pixel (holographic similarities).
- Configured recursive fractal rendering algorithms using native Canvas APIs (Kotlin DrawScope for Android and HTML5 Context2D for React Web).
- Placed the experiment as a dedicated 5th global tab within both apps to test resolution scaling interactively as described in the White Paper.
- Fixed Web build errors (`TS6133: 'React' is declared but its value is never read`) by removing unused `React` imports in `FirstSource.tsx`, `MainTab.tsx`, `WhitePaper.tsx`, and `Criticism.tsx`.
- Synced Android content (FirstSourceData, CosmologyData, EssayData, WhitePaperData, CriticismData) to the React Web version.
- Ported the native Android application's UI structure to the Web version.
- Added animated expanding/collapsing sections (accordion) to `Criticism.tsx` to match the Android experience.
- Implemented a tabbed interface for `WhitePaper.tsx` to replicate the horizontal tabs from Android.
- Replaced the globe ("Language") icon with the Share icon in the Android app. 
- Removed the duplicated "Map" button (Карта смыслов) from the web and Android app.
- Ported all 15 native Canvas animations (Visualizations.kt) over to Web (Visualizations.tsx) using the `requestAnimationFrame` loop, accurately mirroring math, effects, colors, bounding boxes, logic, and component layouts for all sub-sections (Nodes, Criticism, WhitePaper, Essays).
- Verified seamless React integration with 60fps local performance. Ready for deployment.
