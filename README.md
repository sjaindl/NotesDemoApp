# NotesDemoApp

A small Android app used as a teaching example for the **SOLID** principles. The app itself is intentionally simple — create a note, save it to either a Room database or a JSON file, mark it as shareable or unshareable, and share shareable notes via the system share sheet. The interesting part is the code structure, which is shaped to make SOLID trade-offs visible and discussable.

## Features

- Create text notes with a title and body
- Persist notes to one of two backends:
  - **Room** database (`Note.DatabaseNote`)
  - **JSON files** in app-private storage (`Note.FileNote`)
- Mark notes as `Shareable` or `Unshareable`
- Share shareable notes through `Intent.ACTION_SEND`
- Delete notes
- List all notes (loaded from both backends)

## Tech stack

- **Language:** Kotlin
- **UI:** Jetpack Compose, Material 3
- **Persistence:** Room 2.7, `kotlinx.serialization` (JSON) for file-backed notes
- **Architecture:** `ViewModel` + `StateFlow`
- **Build:** Android Gradle Plugin 9.0.1, Kotlin 2.3.21
- **SDK:** `compileSdk` 36, `minSdk` 29, `targetSdk` 36

## Project layout

```
app/src/main/java/com/sjaindl/notesdemoapp/
├── MainActivity.kt              # Compose entry point
├── NotesScreen.kt               # List + FAB to add
├── AddNoteScreen.kt             # Form to create a note
├── NotesAppBar.kt
├── SingleNote.kt
├── NotesViewModel.kt            # State + dispatch to the right manager
├── NoteAction.kt                # share / load / save / delete
├── ShareableNotesManager.kt     # NoteAction impl, supports share()
├── UnshareableNotesManager.kt   # NoteAction impl, share() is a no-op
├── model/
│   ├── Note.kt                  # sealed interface: DatabaseNote | FileNote
│   └── ShareType.kt
└── db/
    ├── AppDatabase.kt           # Room DB
    ├── NoteEntity.kt
    └── NotesDao.kt
```

## SOLID, by example

The code is structured so each principle has something concrete to point at — including a couple of deliberate smells that make good discussion material.

- **S — Single Responsibility.** `NotesViewModel` only coordinates state; persistence lives in the managers; the Compose screens only render. Each `*Manager` separates file I/O from database I/O into private helpers.
- **O — Open/Closed.** Adding a new note kind (e.g. a remote note) means adding a new `Note` subtype and a new `NoteAction` implementation, without changing existing managers.
- **L — Liskov Substitution.** `Note.DatabaseNote` and `Note.FileNote` are interchangeable wherever a `Note` is expected — same contract, same fields.
- **I — Interface Segregation.** `UnshareableNotesManager.share()` is an empty no-op, which is a textbook ISP violation: clients are forced to depend on a method they cannot use.
- **D — Dependency Inversion.** `NotesViewModel` depends on the `NoteAction` abstraction rather than concrete managers, and the managers are injected via `NotesViewModelFactory`.

The codebase is small enough to read end-to-end in a sitting, which is the point — students can refactor it and watch the principles tighten or loosen.

## Build and run

Requirements: Android Studio (recent stable), JDK 17+, an emulator or device running Android 10 (API 29) or newer.

```bash
./gradlew :app:assembleDebug      # build a debug APK
./gradlew :app:installDebug       # install on a connected device/emulator
./gradlew test                    # JVM unit tests
./gradlew connectedAndroidTest    # instrumentation tests (requires a device)
```

Or open the project in Android Studio and run the `app` configuration.

## License

[MIT](LICENSE) © Stefan Jaindl
