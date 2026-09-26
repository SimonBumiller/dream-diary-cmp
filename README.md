# Compose-Multiplatform Dream Diary
This is the repository for a compose-multiplatform Dream Diary app.

## Structure
The top-level project structure is as follows:
- `composeApp/` contains code for the root components of the app
- `core/` contains technology-specific implementation, e.g. Database or Datastore access
- `domain/` contains domain logic of the app
- `feature/` contains the user-visible feature declarations in the form of composable
- `ui/` contains helper code for the user-interface
- `gradle/` contains the `libs.versions.toml` file for global version declarations
