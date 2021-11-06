# Full Features

The full feature list contains all the features across all of our official or officially accepted modules.

To gain more information about the individual features, please check our developer wiki.

**WARNING** This list may be outdated at certain times! This list is **effective as of the 1st of November, 2021**

## Core

- Beans (almost like in Spring)
    - Constructor auto-wiring (field auto-wiring is not supported)
    - Circular dependency handling
    - Support for creating custom annotations
        - Support for custom auto-wiring logic
        - Support for custom auto-creation logic
    - Support for creating custom method interceptors
- Kotlin support (for beans, additional Kotlin features can be obtained via the icicle-kotlin module)
- Configuration
    - Easy default values
    - Comments and headers supported
    - Ability to auto-wire configuration properties (ready-only!)
    - Ability to search for a property in every config file (ConfigurationEnvironment)
- Method Interceptors
    - @Async - run method asynchronously
    - @Sync - run method synchronously
    - @Meauser - measure method's execution time in ms
- Module loading
    - Automatic module downloading (to save space)
    - Automatic module updating
- Performance Log
    - Logged on a per-project basis
- Translations
    - Customizable Language- and TranslationString providers
    - GeoLocation language provider
    - [StringCode]() parser

## Serialization

- ObjectMapper
    - Handles the objects themselves, converts them into an intermediate form: ObjectDescriptors
    - The actual serializers use ObjectDescriptors
- Converters
- Built-in formats:
    - JSON
    - YAML
    - NBT
    - ProtoBuf (planned)
    - HOCON (planned)

## Utilities

- Datastores
    - Heap
    - Tuple
    - Triple
- File utilities
    - AdvancedFile (to access every file related utilities without calling the utilitiy classes)
    - FileUtils
    - FileZipper
- Generic utilities
    - GenericUtils
    - TypeAccessors
- Updater
    - VersionComparator
- List utilities
- Reflection utilities
- String utilities
- TriConsumer
- Web utilities