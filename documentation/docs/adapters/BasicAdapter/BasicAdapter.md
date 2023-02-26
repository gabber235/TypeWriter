# Basic Adapter

The Basic Adapter contains all of the essential entries for Typewriter. In most cases, it should be installed with
Typewriter. If you haven't installed Typewriter or the adapter yet, please follow
the [Installation Guide](Installation-guide) first.

## Entries

### Action

| Name                                                                                  | Description                                  |
|---------------------------------------------------------------------------------------|----------------------------------------------|
| [Console Command Action Entry](BasicAdapter/entries/action/ConsoleCommandActionEntry) | Run command from console                     |
| [Delayed Action Entry](BasicAdapter/entries/action/DelayedActionEntry)                | Delay an action for a certain amount of time |
| [Drop Item Action Entry](BasicAdapter/entries/action/DropItemActionEntry)             | Drop an item at location, or on player       |
| [Give Item Action Entry](BasicAdapter/entries/action/GiveItemActionEntry)             | Give an item to the player                   |
| [Player Command Action Entry](BasicAdapter/entries/action/PlayerCommandActionEntry)   | Make player run command                      |
| [Play Sound Action Entry](BasicAdapter/entries/action/PlaySoundActionEntry)           | Play sound at player, or location            |
| [Simple Action Entry](BasicAdapter/entries/action/SimpleActionEntry)                  | Simple action to modify facts                |
| [Spawn Particle Action Entry](BasicAdapter/entries/action/SpawnParticleActionEntry)   | Spawn particles at location                  |

### Dialogue

| Name                                                                                    | Description                                              |
|-----------------------------------------------------------------------------------------|----------------------------------------------------------|
| [Message Dialogue](BasicAdapter/entries/dialogue/MessageDialogue)                       | Display a single message to the player                   |
| [Option Dialogue Entry](BasicAdapter/entries/dialogue/OptionDialogueEntry)              | Display a list of options to the player                  |
| [Random Message Dialogue](BasicAdapter/entries/dialogue/RandomMessageDialogue)          | Display a random message from a list to a player         |
| [Random Spoken Dialogue Entry](BasicAdapter/entries/dialogue/RandomSpokenDialogueEntry) | Display a random selected animated message to the player |
| [Spoken Dialogue Entry](BasicAdapter/entries/dialogue/SpokenDialogueEntry)              | Display a animated message to the player                 |

### Speaker

| Name                                                                    | Description            |
|-------------------------------------------------------------------------|------------------------|
| [Simple Speaker Entry](BasicAdapter/entries/speaker/SimpleSpeakerEntry) | The most basic speaker |

### Event

| Name                                                                                    | Description                            |
|-----------------------------------------------------------------------------------------|----------------------------------------|
| [Block Break Event Entry](BasicAdapter/entries/event/BlockBreakEventEntry)              | When the player breaks a block         |
| [Block Place Event Entry](BasicAdapter/entries/event/BlockPlaceEventEntry)              | When the player places a block         |
| [Detect Command Ran Event Entry](BasicAdapter/entries/event/DetectCommandRanEventEntry) | When a player runs an existing command |
| [Interact Block Event Entry](BasicAdapter/entries/event/InteractBlockEventEntry)        | When the player interacts with a block |
| [Pickup Item Event Entry](BasicAdapter/entries/event/PickupItemEventEntry)              | When the player picks up an item       |
| [Player Death Event Entry](BasicAdapter/entries/event/PlayerDeathEventEntry)            | When a player dies                     |
| [Player Hit Entity Event Entry](BasicAdapter/entries/event/PlayerHitEntityEventEntry)   | When a player hits an entity           |
| [Player Join Event Entry](BasicAdapter/entries/event/PlayerJoinEventEntry)              | When the player joins the server       |
| [Player Kill Entity Event Entry](BasicAdapter/entries/event/PlayerKillEntityEventEntry) | When a player kills an entity          |
| [Player Kill Player Event Entry](BasicAdapter/entries/event/PlayerKillPlayerEventEntry) | When a player kills a player           |
| [Run Command Event Entry](BasicAdapter/entries/event/RunCommandEventEntry)              | When a player runs a custom command    |

### Fact

| Name                                                                            | Description                                      |
|---------------------------------------------------------------------------------|--------------------------------------------------|
| [Cron Fact Entry](BasicAdapter/entries/fact/CronFactEntry)                      | Saved until a specified date, like (0 0 \* \* 1) |
| [Permanent Fact Entry](BasicAdapter/entries/fact/PermanentFactEntry)            | Saved permanently, it never gets removed         |
| [Placeholder Fact Entries](BasicAdapter/entries/fact/PlaceholderFactEntries)    | Computed Fact for a placeholder number           |
| [Session Fact Entry](BasicAdapter/entries/fact/SessionFactEntry)                | Saved until a player logouts of the server       |
| [Timed Fact Entry](BasicAdapter/entries/fact/TimedFactEntry)                    | Saved for a specified duration, like 20 minutes  |
| [Random Trigger Gate Entry](BasicAdapter/entries/action/RandomTriggerGateEntry) | Randomly selects its connected triggers          |
