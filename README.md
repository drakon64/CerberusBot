# DynamisBot

[![Kotlin Alpha](https://kotl.in/badges/alpha.svg)](https://kotlinlang.org/docs/components-stability.html)
[![Kotlin](https://img.shields.io/badge/kotlin-1.9.0-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![License](https://img.shields.io/github/license/drakon64/DynamisBot)](https://www.gnu.org/licenses/agpl-3.0.en.html)

_DynamisBot_ is a Discord bot that runs on AWS Lambda.

---

## Features

- Eorzea Database: Search for an item in the Eorzea Database.
  - Provides rich results for:
    - Arms/Tools
    - Armor/Accessories
    - Medicines & Meals
  - Supports English, Japanese, German, and French
- Lodestone: Link your Discord account to a Final Fantasy XIV character and display your characters
  card/portrait/profile. Uses XIVAPI and https://github.com/xivapi/XIV-Character-Cards.
- Universalis: Search the Final Fantasy XIV market board for an item in a given world, datacenter, or region.
  Uses https://github.com/Universalis-FFXIV/Universalis.

As _DynamisBot_ uses the Discord Interactions API, features can be selectively enabled or disabled within your guilds
settings.

## Adding to Discord

_DynamisBot_ can be added to your Discord server using [this link](https://discord.com/api/oauth2/authorize?client_id=1130903004657229904&permissions=0&scope=bot).
