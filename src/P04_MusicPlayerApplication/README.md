# Music Player Application

## Problem Statement

You are building a music player application (like Winamp or a simplified Spotify).

Users maintain a song library, organize songs into playlists, and play music through external audio hardware. The system must satisfy the following:

1. **Multiple output devices** — The player must support Bluetooth speakers, wired speakers, and headphones. Each vendor provides its own SDK with a different method: `playSoundViaBluetooth(String)`, `playSoundViaCable(String)`, `playSoundViaJack(String)`. New vendors will be onboarded over time.

2. **No duplicate songs** — A playlist cannot contain the same song more than once.

3. **Three playback modes** — Sequential (playlist order), shuffle (random, no repeats until all songs have played), and custom queue (user picks what plays next; falls back to sequential when the queue is empty). New modes may be added later.

4. **Shuffle has memory** — Going "previous" during shuffle replays songs in the reverse order they were randomly selected — not in playlist order.

5. **Pause resumes** — Playing a paused song resumes it, not restarts.

**Domain operations:**

- Add a song to the library (title, artist, file path)
- Create a playlist (name) · Add a song to a playlist (playlist name, song title)
- Connect a device (device type)
- Play / pause a song
- Set playback mode (mode type)
- Load a playlist · Play all · Next · Previous
- Queue a song to play next (song title)

**Your task:**
Design and implement this system. Think about how the player handles device differences cleanly, how playback ordering stays flexible, and how the client drives everything without getting tangled in internals.
