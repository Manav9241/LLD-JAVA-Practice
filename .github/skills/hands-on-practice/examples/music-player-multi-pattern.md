# Example: Music Player Application (Multiple Patterns)

This is a reference example showing how a hands-on exercise should look when the problem naturally involves multiple design patterns (Adapter, Factory, Strategy, Facade, Singleton) rather than a single one.

It also documents two lessons learned from a failed generation attempt:

- How to separate **constraints** (requirements) from **features** (domain operations) — see [Lesson: Requirements vs. Domain Operations](#lesson-requirements-vs-domain-operations).
- How to generate a problem statement from existing code without reverse-engineering it — see [Lesson: Generating from Existing Code](#lesson-generating-from-existing-code).

---

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

---

## Hints

**Hint 1:** Your playback engine shouldn't know whether sound is going to Bluetooth or a headphone jack. What contract could all devices share so the engine talks to one thing?

**Hint 2:** Each vendor SDK speaks its own language. Who translates between the contract your engine expects and each vendor's method?

**Hint 3:** "Give me the right device for this type" is a creation decision. Where should it live so adding a fourth vendor means touching one place?

**Hint 4:** Sequential, shuffle, and up-next all answer "what song comes next?" differently. What do they have in common that makes them swappable?

**Hint 5:** Shuffle needs to track what hasn't been played _and_ what order songs were picked (for going backward). What data structures serve each need?

**Hint 6:** Up-next has two sources of "next song" — the user's queue and playlist order. Which takes priority, and what happens when it's empty?

**Hint 7:** The client shouldn't wire up the audio engine, pick devices, manage playlists, _and_ drive track ordering. What single entry point could coordinate all of this?

---

## Review Focus Areas

After implementation, check:

- Does the playback engine import any concrete device classes? (It shouldn't.)
- Can you add a fourth device vendor by writing one new class and touching one creation point — without modifying the engine or any existing device class?
- Can you add a fourth playback mode without modifying the playback loop or any existing mode?
- Is the client code making simple, high-level calls, or is it coordinating subsystems directly?
- Are managers and application-level objects that should only exist once actually enforced as such?

---

## Lesson: Requirements vs. Domain Operations

This problem statement was generated from an existing implementation. The first several attempts failed because requirements and domain operations said the same thing in two different formats:

| Bad requirement (feature dressed as requirement) | Where it actually belongs                                       |
| ------------------------------------------------ | --------------------------------------------------------------- |
| "User can play and pause songs"                  | Domain operations: Play / pause a song                          |
| "User can create playlists and add songs"        | Domain operations: Create a playlist · Add a song to a playlist |
| "App should support multiple output devices"     | Domain operations: Connect a device (device type)               |

The fix: **requirements should state rules and constraints that force design decisions.** If a requirement reads like a line in domain operations, it's a feature — move it. What remains in requirements are things that aren't obvious from the verb list alone:

- Incompatible vendor SDKs with different method signatures (forces an adaptation decision)
- No duplicate songs in a playlist (forces a validation rule)
- Shuffle must remember selection order for backward navigation (forces a data structure decision)
- Pause resumes, not restarts (forces state tracking in the playback engine)

## Lesson: Generating from Existing Code

When this problem statement was first generated from a completed branch, the result described what each class and interface did — adapter interfaces, factory methods, strategy contracts — and called those descriptions "requirements." That's reverse-engineering, not problem writing.

What worked instead:

1. Set the code aside. Ask: "What does a **user** of this music player need?"
2. Write from that perspective first.
3. Cross-check against the code to catch any behavioral rule or constraint you missed.
4. If a requirement sounds like it's describing a class, an interface, or an architectural decision, it's solution language. Rewrite it as the user-facing need it serves.

Signs you're reverse-engineering instead of writing a problem:

- Requirements mention "uniform interface," "centralized place," or "interchangeable at runtime"
- Requirements describe what the code _does_ rather than what the user _needs_
- The problem statement reads like a summary of the implementation
