# TempestBot

[![Kotlin Alpha](https://kotl.in/badges/alpha.svg)](https://kotlinlang.org/docs/components-stability.html)
[![Kotlin](https://img.shields.io/badge/kotlin-1.7.21-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![License](https://img.shields.io/github/license/TempestProject/TempestBot)](https://www.gnu.org/licenses/agpl-3.0.en.html)
[![Qodana](https://github.com/TempestProject/TempestBot/actions/workflows/code_quality.yml/badge.svg)](https://github.com/drakon64/TempestBot/actions/workflows/code_quality.yml)

_TempestBot_ is a Discord bot that runs on AWS Lambda.

---

## Features

- Citations: Save quotes from guild members and repost them. Requires users to opt-in.
- Translate: Translate messages using AWS Translate.
- ~~Subject Access Request: The bot will DM you with all of the information that it holds on your Discord account.~~
- Localisations: _TempestBot_ is fully localised for all Discord-supported locales.

### Final Fantasy XIV features

- Lodestone: Link your Discord account to a Final Fantasy XIV character and display your characters
  card/portrait/~~profile~~. Uses XIVAPI and https://github.com/xivapi/XIV-Character-Cards.
- Universalis: Search the Final Fantasy XIV market board for an item in a given world, datacenter, or region.
  Uses https://github.com/Universalis-FFXIV/Universalis.

As _TempestBot_ uses the Discord Interactions API, features can be selectively enabled or disabled within your guilds
settings.
