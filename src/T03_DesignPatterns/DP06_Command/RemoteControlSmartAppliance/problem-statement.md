# Problem Statement — Smart Home Remote Control

## The Problem

You're building a universal remote control for a smart home system. The remote has a fixed number of button slots. Each button controls a different appliance — lights, fans, ACs, etc.

**Requirements:**

1. Each button toggles its appliance — first press turns on, second press turns off
2. Adding a new appliance type (e.g., AC, TV) should not require modifying the remote control code
3. The remote has no knowledge of what each button does — it just triggers whatever is assigned

**The naive approach that breaks down:**

```java
void pressButton(int slot) {
    if (slot == 0) light.on();
    else if (slot == 1) fan.start();
    else if (slot == 2) ac.cool();
    // ... grows forever
}
```

Every new appliance means editing the remote. The remote is coupled to every device. Toggle logic (on vs. off) is tangled into the dispatcher. Undo becomes a nightmare of per-device `if` checks.

---

## Thought Process

**Step 1 — Identify the friction.** The remote (caller) is directly calling specific methods on specific devices. This means the remote needs to know about every device type and every method name. That's tight coupling.

**Step 2 — What do all button-presses have in common?** Regardless of the device, pressing a button always means "do something" and pressing again means "undo that thing." The _what_ changes, but the _shape_ of the operation is always the same: `execute()` / `undo()`.

**Step 3 — Extract the common shape into an interface.** If every button action has the same shape, make it an interface (`ICommand` with `execute()` and `undo()`). Now the remote just holds a list of `ICommand` references — it calls `execute()` or `undo()` without caring what happens behind it.

**Step 4 — Bridge each device to that interface.** A `LightCommand` holds a `Light` reference and maps `execute()` → `light.on()`, `undo()` → `light.off()`. The command is the adapter between the remote's generic interface and the device's specific methods.

---

## Intuition

The key insight is: **the remote shouldn't know what it's controlling.** It should only know _that_ it can trigger something and reverse it. By putting a command object between the remote and the device, you make the remote permanently closed to modification — new devices only require new command classes.

This is the simplest case of the Command pattern — no queueing, no audit, no history stack. Just clean decoupling through an interface.

---

## Solution — Role Mapping

| Role              | Class                        | What it does                                                                                 |
| ----------------- | ---------------------------- | -------------------------------------------------------------------------------------------- |
| Receiver          | `Light`, `Fan`               | Domain objects with device-specific methods (`on/off`, `start/stop`)                         |
| Command Interface | `ICommand`                   | `execute()` + `undo()` — the shape every button action follows                               |
| Concrete Commands | `LightCommand`, `FanCommand` | Hold a receiver reference, map `execute/undo` to receiver methods                            |
| Invoker           | `RemoteControlInvoker`       | Holds button slots (array of `ICommand`), tracks toggle state, calls `execute()` or `undo()` |
| Client            | `CommandPatternMain`         | Creates receivers, wraps them in commands, assigns commands to button slots                  |

**Why the Invoker is blind:** `RemoteControlInvoker` only imports `ICommand`. It never imports `Light`, `Fan`, or any concrete command. It stores commands in a list and toggles between `execute()` and `undo()` based on a boolean flag per slot.

**Extensibility test:** Adding a `DoorLock` requires: (1) `DoorLock.java` receiver, (2) `DoorLockCommand.java` concrete command, (3) one line in the client: `remote.setButton(2, new DoorLockCommand(lock))`. Zero changes to the remote.
