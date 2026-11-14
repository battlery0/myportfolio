# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Calendar Versioning](https://calver.org/) of
the following form: YYYY.0M.0D.

## [2026.11.13]

### Added

- Designed a Playlist component
- Designed a Budget component
- Designed a StockPortfolio component

### Updated

- Changed Playlist design to include song themes (K-pop idols, K-bands, J-pop, EDM, game OST) and theme-based secondary methods

### Added

- Designed kernel and enhanced interfaces for Playlist component

### Updated

- Changed design so `removeFirst` requires a non-empty playlist instead of throwing an exception
- Changed design so `Song` is an immutable type and `Theme` is a nested enum in the kernel interface
- Changed design so `appendPlaylist` clears its argument

### Added

- Designed abstract class for Playlist component

### Updated

- Implemented `reverse` iteratively to avoid deep recursion
- Implemented `shuffle` using rotation so it only needs kernel methods

### Added

- Designed kernel implementation for Playlist component

### Added

- Designed test suite for Playlist component
- Designed two different use cases for Playlist component