---
sidebar_position: 5
---

# Basic Adapter

The Basic Adapter contains all the essential entries for Typewriter. In most cases, it should be installed with Typewriter. If you haven't installed Typewriter or the adapter yet, please follow the [Installation Guide](Installation-guide) first.

## Entries

All entries have a `triggers` field which is what entries will be triggered after the current entry is finished. This field can be a single entry or a list of entries.

### Events

When something happens. The player that triggers the event is passed through all other triggers connected to it.

| Entry Name               | Description                                                                                              | Fields                                                                                                                                                                                     |
| ------------------------ | -------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| On Player Hit Entity     | Triggered when a player hits an entity.                                                                  | `entityType` - The entity that must be hit.                                                                                                                                                |
| On Player Death          | Triggered when a player dies.                                                                            | None                                                                                                                                                                                       |
| On Player Kill Entity    | Triggered when a player kills and entity.                                                                | `entityType` - The entity that must be hit.                                                                                                                                                |
| On Place Block           | Triggered when a user places a block.                                                                    | `block` - The block that must be placed down. <br /> `location` (optional) - Where the block must be placed.                                                                               |
| On Item Pickup           | Triggered when an item is picked up.                                                                     | `material` - The item that must be picked up.                                                                                                                                              |
| On Player Join           | Triggered when a player joins.                                                                           | None                                                                                                                                                                                       |
| On Run Command           | Triggered when a user runs a command. This command will be registered to the server.                     | `command` - The command to run. Must not include the slash.                                                                                                                                |
| On Detect Command Ran    | Triggered when an already existing command is run. The command must already be registered to the server. | `command` - The command to run. Must not include the slash.                                                                                                                                |
| On Message Contains Text | Triggered when a chat message from a player contains a certain message.                                  | `text` - The text to match. <br /> `exactSame` - If the text should exactly match what the player said.                                                                                    |
| On Interact With Block   | Triggered when a player interacts (Right-Click) with a block.                                            | `block` - The block that must be interacted with. <br /> `location` (optional) - Where the block should be. <br /> `itemInHand` (optional) - The item that must be in the player's hand.   |
| On Block Break           | Triggers when a player breaks a block.                                                                   | `block` (optional) - The block that must be broken. <br /> `location` (optional) - Where the block should be. <br /> `itemInHand` (optional) - The item that must be in the player's hand. |
| On Player Kill Player    | Triggered when a player kills another player.                                                            | `targetTriggers` (optional) - The triggers to activate with the killed player's context.                                                                                                   |

### Actions

To make things happen. All actions come with a `criteria` and `modifier` field. The `criteria` field makes the action only run if the condition(s) are met. The `modifier` field is used to modify a [fact](facts) when the action successfully runs.

| Entry Name          |                 Description                  | Fields                                                                                                                                                                                                                                                                                                                                                              |
| ------------------- | :------------------------------------------: | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Random Trigger      |   Triggers one of it's triggers randomly.    | None                                                                                                                                                                                                                                                                                                                                                                |
| Console Run Command |       Run a command from the console.        | `command` - The command to run. Must not include the slash.                                                                                                                                                                                                                                                                                                         |
| Player Run Command  |        Run a command from the player.        | `command` - The command to run. Must not include the slash.                                                                                                                                                                                                                                                                                                         |
| Play Sound          |           Plays a Minecraft sound.           | `sound` - The sound to play. <br /> `volume` - The volume of the sound. <br />`pitch` - The pitch of the sound <br />`location` (optional) - Where to play the sound from.                                                                                                                                                                                          |
| Spawn Particles     |              Spawns particles.               | `particle` - The type of particle to show. <br /> `count` - The amount of particles to spawn. <br /> `offsetX` - The offset X of the particles from `location`. `offsetY` - The offset Y of the particles from `location`. `offsetZ` - The offset Z of the particles from `location`. `location` (optional) - Where to spawn the particles.                         |
| Simple Action       | An action to modify a [fact](facts)'s value. | None                                                                                                                                                                                                                                                                                                                                                                |
| Give Item           |          Gives the player an item.           | `material` - The item to give. <br /> `amount` - The size of the stack. <br /> `displayName` (optional) - Sets the display name for the item. If not set, the item will have its normal name. <br /> `lore` (optional) - The lore of the item. If not set, the item will have its normal lore if any.                                                               |
| Drop Item           |         Drops an item to the player.         | `material` - The item to drop. <br /> `amount` - The size of the stack. <br /> `displayName` (optional) - Sets the display name for the item. If not set, the item will have its normal name. <br /> `lore` (optional) - The lore of the item. If not set, the item will have its normal lore if any. <br /> `location` (optional) - The location to drop the item. |
| Delayed Action      |           Delays the next trigger.           | `duration` - The time to wait.                                                                                                                                                                                                                                                                                                                                      |
### Dialogue

Text that is shown to the player.

| Entry Name     | Description                                                   | Fields                                                                                                                                                  |
| -------------- | ------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Message        | Displays a single message to the player.                      | `text` - The message to display. <br /> `speaker` - The speaker of the message.                                                                         |
| Random Message | Displays a random message from a list to the player.          | `messages` - A list of messages to display. <br /> `speaker` - The speaker of the message.                                                              |
| Spoken         | Displays an animated message to the player.                   | `text` - The message to display. <br /> `speaker` - The speaker of the message. <br /> `duration` - he time for the dialogue to be written.             |
| Random Spoken  | Displays a random animated message from a list to the player. | `messages` - A list of messages to display. <br /> `speaker` - The speaker of the message. <br /> `duration` - The time for the dialogue to be written. |
| Option         | Displays a list of options to the player.                     | `text` - The message to display. <br /> `options` - A list of options to display. <br /> `speaker` - The speaker of the message.                        |  |

### Static

Static entries are for holding data. They are used as parameters for other entries. All static entries contain a field called `comment`. It does nothing, and serves as a tool for you to remember what the entry is for.

| Entry Name     | Description                                               | Fields                                                                                                                |
| -------------- | --------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------- |
| Permanent Fact | A [fact](facts) that never gets reset.                    | None                                                                                                                  |
| Session Fact   | A [fact](facts) that is saved until the player logs out.  | None                                                                                                                  |
| Cron Fact      | A [fact](facts) that is saved until a specific date.      | `cron` - The [cron](https://docs.oracle.com/cd/E12058_01/doc/doc.1014/e12030/cron_expressions.htm) expression to use. |
| Timed Fact     | A [fact](facts) that is saved for the specified duration. | `duration` - The duration to save the fact for.                                                                       |
| Simple Speaker | A basic speaker. Used in any `speaker` parameter.         | `displayName` - The name of the speaker. <br /> `sound` (optional) - The sound the speaker makes.                     |