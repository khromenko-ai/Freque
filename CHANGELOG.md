# Changelog

## [Current]
- Fixed Web build errors (`TS6133: 'React' is declared but its value is never read`) by removing unused `React` imports in `FirstSource.tsx`, `MainTab.tsx`, `WhitePaper.tsx`, and `Criticism.tsx`.
- Synced Android content (FirstSourceData, CosmologyData, EssayData, WhitePaperData, CriticismData) to the React Web version.
- Ported the native Android application's UI structure to the Web version.
