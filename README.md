<img src="https://user-images.githubusercontent.com/36101494/111905318-4e8d5e80-8a4b-11eb-8e9c-666b3f3d49a3.png" width="725" />

# [![Codacy Badge](https://api.codacy.com/project/badge/Grade/7ac8f720a0804656af1515bb878efe1c)](https://app.codacy.com/gh/IceyLeagons/Icicle?utm_source=github.com&utm_medium=referral&utm_content=IceyLeagons/Icicle&utm_campaign=Badge_Grade) ![GitHub](https://img.shields.io/github/license/IceyLeagons/Icicle) ![Love](https://img.shields.io/badge/Made%20with-%E2%9D%A4-red)

Icicle is a Spring inspired framework designed for Bukkit/Spigot plugin development.

# Features

Icicle is so feature-rich, that we cannot list everything here, but to get an idea, here are the main features:

- lightweight, easy-to-use design 
- Auto-wiring and beans (or in other words: Spring features in Spigot)
- Easy configuration management

# Modules

Icicle is split into separate modules, which all add additional functionality to the core.
These modules don't need to be downloaded: when the core reads the metadata of an Icicle-based project it
will download and load the necessary Icicle modules for it automagically.

Icicle currently has the following official modules (if the "Dependencies" column is empty, then the module can be used standalone):

| Name | Description | Version | Dependencies |
|:----:|:-----------:|:-------:|:------------:|
| icicle-core | This is the base of the operations, the almighty overlord of the project. | 1.0.0-beta | icicle-utilities |
| icicle-gradle | Our Gradle plugin to create Icicle-based projects easily. | 1.0.0 | - |
| icicle-serialization | This module contains everything you need for serializing objects. | 1.0.0-beta | icicle-core, icicle-utilities |
| icicle-database | This module is used to interact with many database engines with a common interface. | 0.0.1 | icicle-core, icicle-utilities, icicle-serialization |
| icicle-spigot | Implementation of Icicle for Spigot environments | planned | icicle-core |
| icicle-kotlin | Utilities for Kotlin to speed up your workflow even more! | planned | N/A |
| icicle-utilities | Utility classes containing useful methods for development. | 1.0.0 | - |

# Installation & Setup

Please read our wiki on GitBook, [here]().

# Contributing

Everyone is welcome to contribute to Icicle. If you want to do so, please start, by reading our [Contribution Guidelines]().

# License

Icicle is licensed under the permissive **MIT License**. You can read ours [here](https://github.com/IceyLeagons/Icicle/blob/master/LICENSE).

# Supporters

<img src="https://user-images.githubusercontent.com/36101494/110477295-47795e80-80e3-11eb-9c3e-bf57776e3680.png" width="280">

- GitBook happily hooked us up with their powerful documentation tool/service.
- Check them out [here](https://www.gitbook.com/?utm_source=content&utm_medium=trademark&utm_campaign=iceyleagons)!


<img src="https://user-images.githubusercontent.com/36101494/110478780-fd917800-80e4-11eb-9358-fcc8de4baa99.png" width="120">

- This project is developed with JetBrains products. We have access to such tools with an [open source license](https://www.jetbrains.com/community/opensource).
- Check them out [here](https://jb.gg/OpenSource)!
