# Norse Community Plugin - WIP

A comprehensive server-side MMORPG overhaul plugin for Minecraft that transforms the vanilla experience into a full-featured RPG game with custom systems, classes, abilities, items, and progression. No client-side mods required!

## Overview

Norse Community Plugin completely reimagines Minecraft gameplay by replacing vanilla mechanics with custom RPG systems including:

- Class-based character progression
- Custom health and mana systems
- Rich item system with upgrades and rarities
- Special class abilities
- Custom damage calculation based on stats
- Nation and clan systems
- Experience and leveling

## Features

### Class System
Choose from four unique classes, each with different playstyles and abilities:
- **Warrior**: Tank class with high HP and melee damage
- **Assassin**: Stealth-based class with high damage but low HP
- **Mage**: Magic-focused class with powerful abilities
- **Priest**: Support class with healing capabilities

### Custom Health System
- Replaces vanilla health with a class-based HP system
- Health regeneration over time
- Death and respawn handling
- HP scales with level and class

### Item System
- **Weapons**: Custom damage values, attack speeds, and special damage types
- **Armor**: Protection values and stat bonuses
- **Scrolls**: Used to upgrade items with various success rates
- **Item Grades**: Low, Medium, High, and Blessed quality tiers
- **Item Rarity**: Common, Uncommon, Rare, Epic, and Legendary
- **Item Upgrading**: Enhance your gear using scrolls with a risk/reward system

### Stats System
Develop your character with various stats:
- **Strength**: Increases physical damage
- **Dexterity**: Improves critical hits and dodge
- **Intelligence**: Enhances magic damage
- **Wisdom**: Improves mana regeneration

### Custom Abilities
Each class has unique abilities:
- **Warrior**: Stomp ability to knock back enemies
- **Assassin**: Stealth ability for invisibility and speed
- **Mage**: Various magic abilities (in development)
- **Priest**: Healing abilities (in development)

### Experience and Leveling
- Gain XP from combat and activities
- 100 levels of progression
- Each level increases stats and unlocks new abilities
- Custom XP requirements per level

### Nation and Clan Systems
- Join nations and clans for group gameplay
- Social structure for cooperative play

## Commands

### Player Commands
- `/spawn` - Teleport to spawn point
- `/nationmenu` - Open the nation selection menu
- `/classmenu` - Open the class selection menu
- `/stats [player]` - View your stats or another player's stats
- `/dagger` - Get Loki's Dagger (test item)

### Admin Commands
- `/setspawn` - Set the server spawn point
- `/sethp <player> <amount>` - Set a player's HP
- `/setdamage <player> <amount>` - Set a player's damage stat
- `/setprotection <player> <amount>` - Set a player's protection stat
- `/setdexterity <player> <amount>` - Set a player's dexterity stat
- `/setclass <player> <class>` - Set a player's class
- `/giveitem <item_name> <amount>` - Give custom items to a player
- `/getarmor <item_name>` - Get custom armor items

## Item Upgrading

Items can be upgraded at crafting tables, which now open a custom upgrading interface:
1. Place your item in the left slots
2. Place a scroll in the right slots
3. If successful, your item will be upgraded to the next level (+1, +2, etc.)
4. Higher-level upgrades have lower success rates
5. Failed upgrades may result in item destruction or stat penalties

Success rates vary based on:
- Item grade (Low, Medium, High)
- Scroll grade (Low, Medium, High, Blessed)
- Current item level

## Configuration

The plugin creates several configuration files:
- `config.yml` - Main configuration including upgrade rates and level stats
- `PlayerData.yml` - Stores player progression data
- `PlayerClassData.yml` - Class-specific multipliers
- `ItemData.yml` - Item blueprints and definitions

## For Developers

The plugin uses a modular architecture:
- `Abilities` package - Class-specific abilities
- `Configs` package - Configuration handling
- `HealthSystem` package - Custom health implementation
- `Items` package - Item system implementation
- `LevelingSystem` package - XP and level handling
- `managers` package - Various system managers

## Future Development

- Additional class abilities
- Quests and missions
- Dungeons and raids
- Custom mobs and bosses
- Economy system
- Skills and professions
