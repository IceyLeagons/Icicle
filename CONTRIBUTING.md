# Contributing

When contributing to this repository, please first discuss the change you wish to make via issue, on our Discord, or any
other method with the owners of this repository before making a change.

Please note we have a code of conduct, please follow it in all your interactions with the project.

## 🐛 Reporting Bugs

Before reporting a bug or issue, please make sure that the issue is actually being caused by or related to Icicle.
Please also make sure, that you have the latest version of the framework in

- your development environment (aka. Gradle/Maven) - **DEVS**
- the server you're running - **OWNERS**

If you're unsure about the above, feel free to ask our community at [our Discord](https://discord.iceyleagons.net/)
BEFORE making a report. If all of the above check out, then feel free to continue by creating an issue.

## 📝 Contributing code

Icicle loosely follows the Google Java Style Guide, meaning we've adopted some concepts, but we have our own prefrences,
that go against Google's guid.
(In the future we will create a proper styling guide) Generally, try to copy the style of code found in the class you're
editing.

If you're considering submitting a substantial pull request, please open an issue so we can discuss the change before
starting work on the contribution.
(for ex.: You don't have to do it for fixing typos, but you should absolutely do it when deprecating stuff)

Most pull requests are happily accepted, but larger changes may have an impact on the maintainability of the project,
and require more consideration.

Our project uses the [gitmoji](https://gitmoji.dev/) standard for commit messages, please apply the respective emoji to your commit message as well!

### Pull Request Process (checklist)

1. Ensure that your code is clean of unused imports, dependencies and so on.
2. Increase the version numbers in any changed files to the new version that this Pull Request would represent. The
   versioning scheme we use is [SemVer](http://semver.org/).
3. Please write proper JavaDoc for your additions. If your changes alter interactions with the framework (ex.:
   deprecation, method refactoring, etc.), then edit the respective GitBook page, and do a pull request there as well.
4. You may merge the Pull Request in once you have the sign-off of two other developers, or if you do not have
   permission to do that, you may request the second reviewer to merge it for you.
