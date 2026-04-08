# TestExstension

Test extension for the ApexStaff Exstension system.

## Building

After building ApexStaff main plugin:

1. Make sure ApexStaff is installed to your local Maven repository:
   ```bash
   cd ../../
   mvn install
   ```

2. Build the test extension:
   ```bash
   cd exstensions/TestExstension
   mvn clean package
   ```

3. Copy the generated JAR to your server's plugins folder:
   ```bash
   cp target/TestExstension-1.0.0.jar ../../../plugins/
   ```

## Usage

Once installed, use the following commands:

- `/testext` - Main command
- `/testext help` - Show help
- `/testext info` - Show extension info
- `/testext item` - Give test item
- `/testext message` - Test message system
- `/testext task` - Test task scheduler
- `/testext world` - Test world manager
- `/testext location` - Test location manager
- `/testext scoreboard` - Test scoreboard
- `/testext gui` - Test GUI manager

## Permissions

- `testexstension.admin` - Admin access
- `testexstension.use` - Use features

## API Usage

This extension demonstrates usage of all Exstension API managers:

- `CommandManager` - Command registration
- `GUIManager` - Custom GUI creation
- `EventManager` - Event handling
- `TaskManager` - Task scheduling
- `MessageManager` - Message formatting
- `ItemManager` - Item manipulation
- `PlayerManager` - Player utilities
- `WorldManager` - World management
- `LocationManager` - Location saving/loading
- `ScoreboardManager` - Scoreboard creation
- `PermissionManager` - Permission handling
- `ConfigManager` - Configuration handling
