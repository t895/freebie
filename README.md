Freebie Music Player - README
===

# Freebie

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
Basic local music player

### App Evaluation
- **Category:** Entertainment
- **Mobile:** This app will be exclusively designed for mobile.
- **Market:** People who want to listen to music offline or avoid subscription music services

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

- [x] Now playing screen
    - [x] Album art
    - [x] Play/pause (shuffle, repeat?)

- [x] screen with list of songs (recycler view)
    - [] search songs

* [x] navigation buttons
    * [] Liked songs
    * [x] List of Albums accessible via NavBar
    * [x] List of Artists accessible via NavBar
    * [] List of Genres accessible via NavBar

**Optional Nice-to-have Stories**
* Progress bar (scrubbing/seeking)
* Queue for next song
* take average or primary color of the album art and apply it as the theme of the app
* Lyrics
* Settings menu
    * dark mode/light mode
    * block explicit songs
    * default queue order (alpha, chrono, last played)
    * Enable gapless playback
    * Default tab (tracks, albums, etc)
* Artist descriptions provided by last.fm
* shuffle/repeat buttons
* crossfade
* visualizer (waveform, frequency graph, colorful animation)
* progress bar

### 2. Screen Archetypes

* Home Screen - Stream
   * Screen with list of songs
* Now playing - detail
    * album art
    * play/pause
- Albums 
    - screen with grid of albums
- settings

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Home
* Albums
* Artists
* Settings

**Flow Navigation** (Screen to Screen)

* Home/Albums/Artists
   * transition to now playing

## Wireframes
[Add picture of your hand sketched wireframes in this section]
<img src="https://imgur.com/g2hgb1f.jpeg" width=600>

## Schema 
### Models
#### Song
   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | title         | String   |name of the song
   | artist        | String   |artist of the song
   | album         | String   |album of the song
   | length        | String   |length of the song
   | path          | String   |file path of the song
   | albumArt      | Bitmap   |album art of the song
   
#### Album
   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | title         | String   | holds name of the album |
   | artist        | String   | holds name of the artist |
   |highResAlbumArt| Bitmap   | holds image of the album art |
   |lowResAlbumArt| Bitmap   | holds image of the album art |
   
#### Artist
   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | artistName    | String   | holds name of the artist |
   | profilePicture| String   | holds name of the profile picture |


## Networking
### Discogs API 
### used to get artist images


** Demo **

<img src="Demo2.gif" width=200>