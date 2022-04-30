
<div align="center">
  <img src="https://user-images.githubusercontent.com/36101494/111905318-4e8d5e80-8a4b-11eb-8e9c-666b3f3d49a3.png" width="725" />
  <div id="links">
    <a href="https://github.com/IceyLeagons/Icicle/actions/workflows/gradle.yml">
      <img src="https://github.com/IceyLeagons/Icicle/actions/workflows/gradle.yml/badge.svg?branch=master" />
    </a>
    <a href="https://www.codacy.com/gh/IceyLeagons/Icicle/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=IceyLeagons/Icicle&amp;utm_campaign=Badge_Grade">
      <img src="https://app.codacy.com/project/badge/Grade/5b12166b807048cebf3dd5e94a45e4e3" />
    </a>
    <a href="https://github.com/IceyLeagons/Icicle/blob/master/LICENSE">
      <img src="https://img.shields.io/github/license/IceyLeagons/Icicle" />
    </a>
    <a href="">
      <img src="https://img.shields.io/badge/Made%20with-%E2%9D%A4-red">
    </a>
    <a href="https://discord.iceyleagons.net/">
      <img alt="Discord" src="https://img.shields.io/badge/discord-IceyLeagons-738bd7.svg?style=square" />
    </a>
    <a href="https://discord.iceyleagons.net/">
      <img alt="Discord Online" src="https://img.shields.io/discord/489789322300620801.svg" />
    </a>
    <a href="https://github.com/IceyLeagons/Icicle/blob/master/CODE_OF_CONDUCT.md">
      <img alt="Contributor Covenant" src="https://img.shields.io/badge/Contributor%20Covenant-2.0-4baaaa.svg" />
    </a>
  </div>
  <br>
  <div id="description">
    🧊 Icicle is a Spring inspired framework designed mainly for Bukkit/Spigot plugin development. 🧊
  </div>
  <div id="links">
    <a href="https://docs.iceyleagons.net/icicle/get-started">Getting Started</a>
    |
    <a href="https://icicle.iceyleagons.net">Website</a>
    |
    <a href="https://https://github.com/IceyLeagons/Icicle/">JavaDoc</a>
    |
    <a href="https://docs.iceyleagons.net/icicle">Documentation</a>
    |
    <a href="https://github.com/IceyLeagons/Icicle/releases">Releases</a> <!-- Yeah clear trace of where the idea came from, shoutout to Reposilite, great software! -->
 </div>
 <br><br>
 <strong>
  ⛔ This framework is in heavy development, get ready for loads of bad code and heaps of bugs. ⛔<br>
   Until the framework's first release please do not contribute or create issues!
 </strong>
</div>

# ✨ Features
#### Icicle is loaded to the brim with features off-the-shelf, but for simplicity sake, here are some of the main features:

- <details><summary>💨 Lightweight,  easy-to-use design</summary><br>Icicle was designed to be as easy to use as possible, meanwhile maintaining the least amount of code needed to be written. Exactly because of this, we have implemented an "annotation-first" style into Icicle to minimize on the number of lines needed to be written by the end-users, the developers. There ARE exceptions to this, especially when annotations don't suit a task, for instance calculations, or accessing somethings properties. In those instances, we use "normal object-style."</details>
- <details><summary>➿ Auto-wiring and beans (or in other words: <a href="https://spring.io/">Spring</a> features in <a href="https://www.spigotmc.org/">Spigot</a>)</summary><br>Resolving one of the quirks of object-oriented programming languages, with yet another quirk of its own. Auto-wiring makes sure there is ONLY one instance of a given class, and that every class has access to it. No more bulky constructors and long argument lines.</details>
- <details><summary>📖 Easy configuration management</summary><br>Write configurations in a single class with a few annotations that have self-describing names. Currently only YAML is supported, but a rewrite of the configuration system is imminent and as such, will include support for most formats that make sense to be included. Few key features that make it worthwhile include, but are not limited to:<br>&nbsp;&nbsp;&nbsp;&nbsp;💬 Comments!<br>&nbsp;&nbsp;&nbsp;&nbsp;🛸 Object conversion out-of-the-box!</details>
- <details><summary>🖨 &nbsp;Serialization</summary><br>Instead of spending development time on saving the structure of an important class, and then loading it back in, you can instead focus on the logic of your project. Built on speed and size, rather than format support, our serialization module is extremely agile. Serialization handles most of the formats needed for a normal Minecraft developer, and probably some more on top of that. It is by no means a perfect module, though it should work in most, if not all cases.</summary>
- <details><summary>📋 Extending base classes</summary><br>Reflections are good'n'all, but then again, wouldn't it be reaaaal good, if you could just edit parts of the source code? Icicle has many ways of doing such things, and as such should be a good starting point for those looking to fiddle with the source.</summary>
- <details><summary>♾️ and so on...</summary><br>Many more are features yet to be explored, yet alone listed, so I'd advise you check out what we have at our repertoire!<br>Anything missing? Create an issue, or contribute.</summary>

## 🚀 Modules

Icicle is split into separate modules, which all add additional functionality to the core. These modules don't need to
be downloaded: when the core reads the metadata of a project that is built around Icicle it will download and load the necessary Icicle modules for it automagically.

Icicle currently has the following official modules (if the "Dependencies" column is empty, then the module can be used
standalone):
|✅|⚠️|☢️|📝|💀|
|:-:|:-:|:-:|:-:|:-:|
|**RELEASE**<br>Ready for use.|**BETA**<br>Unstable.|**ALPHA**<br>Use with caution!|**PLANNED**<br>Stubs, or just plans.|**DROPPED**<br>Support is non-existent.|

| Status | Name | Description | Recommended Version |
|-:|:-:|:-|-:|
| ✅ | icicle-core | This is the base of the operations, the almighty overlord of the whole project. | 1.0.0-beta |
| ✅ | icicle-serialization | This module contains everything you need for serializing objects. | 1.0.0-beta |
| ✅ | icicle-utilities | Utility classes containing useful methods for development. | 1.0.0 |
| ✅ | icicle-commands | Module containing all the stuff needed to create a good user-experience with just commands. | 1.0.0 |
| ⚠️ | icicle-gradle | Our Gradle plugin to assist in the creation of Icicle-based projects. | 1.0.0 |
| ⚠️ | icicle-bukkit | Implementation of Icicle for Bukkit environments. | 1.0.0-beta |
| ☢️ | icicle-nms | Wrapping of NMS and CraftBukkit. | 0.0.1-alpha |
| ☢️ | icicle-gui | Easy creation of complex and simple guis. | 0.1.0-alpha |
| ☢️ | icicle-protocol | Features for messing with the Minecraft protocol & packets. | 0.0.1-alpha |
| 📝 | icicle-kotlin | Utilities for Kotlin to speed up your workflow even more! | - |
| 📝 | icicle-database | This module is used to interact with many database engines with a common interface. | - |
| 📝 | icicle-intellij | Our IntelliJ Idea plugin to speed up development further, and to bring syntax highlighting. | - |
| 📝 | icicle-entity-ai | A library to simplify entity movement generation and traversing. | - |
| 📝 | icicle-ocl | OpenCL API implementation. A solution to problems that can be broken up into multiple smaller problems. | - |
| 💀 | icicle-addon-server | This module is responsible for serving the modules to the instance of Icicle. | 0.0.1 |

## 🛠️ Installation & Setup

Please read our wiki on GitBook, [here.][docs]

## 🎁 Contributing

Everyone is welcome to contribute to Icicle. If you want to do so, please start, by reading our [Contribution Guidelines.][contributing]
![Alt](https://repobeats.axiom.co/api/embed/b9ee7f8c42f8ce9f0a34070044ea739e23bfe662.svg "Repobeats analytics image")

## 🧾 License

Icicle is licensed under the permissive **MIT License**. You can read ours [here.][license]
## 🤝🏼 Our Supporters
|||
|:--:|:-----------:|
| <img src="https://user-images.githubusercontent.com/36101494/110477295-47795e80-80e3-11eb-9c3e-bf57776e3680.png" width="300" align="left"> | GitBook happily hooked us up with their powerful documentation tool/service.<br>Check them out [here!][gitbook] |
| <img src="https://user-images.githubusercontent.com/36101494/110478780-fd917800-80e4-11eb-9358-fcc8de4baa99.png" align="center" width="130"> | This project is developed with the help of JetBrains' products. We have access to such tools with an [open source license.][jetbrains license]<br>Check them out [here!][jetbrains] |

[contributing]:https://github.com/IceyLeagons/Icicle/blob/master/CONTRIBUTING.md
[docs]:https://docs.iceyleagons.net/icicle/
[license]:https://github.com/IceyLeagons/Icicle/blob/master/LICENSE
[gitbook]:https://www.gitbook.com/?utm_source=content&utm_medium=trademark&utm_campaign=iceyleagons
[jetbrains]:https://jb.gg/OpenSource
[jetbrains license]:https://www.jetbrains.com/community/opensource
