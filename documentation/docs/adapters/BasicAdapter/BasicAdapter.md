# Basic Adapter

The Basic Adapter contains all of the essential entries for Typewriter. In most cases, it should be installed with
Typewriter. If you haven't installed Typewriter or the adapter yet, please follow
the [Installation Guide](/docs/Installation-Guide) first.

## Entries

### Action

| Name                                                                       | Description                                  |
|----------------------------------------------------------------------------|----------------------------------------------|
| [Console Command Action](BasicAdapter/entries/action/ConsoleCommandAction) | Run command from console                     |
| [Delayed Action](BasicAdapter/entries/action/DelayedAction)                | Delay an action for a certain amount of time |
| [Drop Item Action](BasicAdapter/entries/action/DropItemAction)             | Drop an item at location, or on player       |
| [Give Item Action](BasicAdapter/entries/action/GiveItemAction)             | Give an item to the player                   |
| [Player Command Action](BasicAdapter/entries/action/PlayerCommandAction)   | Make player run command                      |
| [Play Sound Action](BasicAdapter/entries/action/PlaySoundAction)           | Play sound at player, or location            |
| [Simple Action](BasicAdapter/entries/action/SimpleAction)                  | Simple action to modify facts                |
| [Spawn Particle Action](BasicAdapter/entries/action/SpawnParticleAction)   | Spawn particles at location                  |

### Dialogue

| Name                                                                           | Description                                              |
|--------------------------------------------------------------------------------|----------------------------------------------------------|
| [Message Dialogue](BasicAdapter/entries/dialogue/MessageDialogue)              | Display a single message to the player                   |
| [Option Dialogue](BasicAdapter/entries/dialogue/OptionDialogue)                | Display a list of options to the player                  |
| [Random Message Dialogue](BasicAdapter/entries/dialogue/RandomMessageDialogue) | Display a random message from a list to a player         |
| [Random Spoken Dialogue](BasicAdapter/entries/dialogue/RandomSpokenDialogue)   | Display a random selected animated message to the player |
| [Spoken Dialogue](BasicAdapter/entries/dialogue/SpokenDialogue)                | Display a animated message to the player                 |

### Speaker

| Name                                                         | Description            |
|--------------------------------------------------------------|------------------------|
| [Simple Speaker](BasicAdapter/entries/speaker/SimpleSpeaker) | The most basic speaker |

### Event

| Name                                                                         | Description                            |
|------------------------------------------------------------------------------|----------------------------------------|
| [Block Break Event](BasicAdapter/entries/event/BlockBreakEvent)              | When the player breaks a block         |
| [Block Place Event](BasicAdapter/entries/event/BlockPlaceEvent)              | When the player places a block         |
| [Detect Command Ran Event](BasicAdapter/entries/event/DetectCommandRanEvent) | When a player runs an existing command |
| [Interact Block Event](BasicAdapter/entries/event/InteractBlockEvent)        | When the player interacts with a block |
| [Pickup Item Event](BasicAdapter/entries/event/PickupItemEvent)              | When the player picks up an item       |
| [Player Death Event](BasicAdapter/entries/event/PlayerDeathEvent)            | When a player dies                     |
| [Player Hit Entity Event](BasicAdapter/entries/event/PlayerHitEntityEvent)   | When a player hits an entity           |
| [Player Join Event](BasicAdapter/entries/event/PlayerJoinEvent)              | When the player joins the server       |
| [Player Kill Entity Event](BasicAdapter/entries/event/PlayerKillEntityEvent) | When a player kills an entity          |
| [Player Kill Player Event](BasicAdapter/entries/event/PlayerKillPlayerEvent) | When a player kills a player           |
| [Run Command Event](BasicAdapter/entries/event/RunCommandEvent)              | When a player runs a custom command    |

### Fact

| Name                                                                       | Description                                       |
|----------------------------------------------------------------------------|---------------------------------------------------|
| [Cron Fact](BasicAdapter/entries/fact/CronFact)                            | Saved until a specified date, like (0 0 \* \* 1)  |
| [Permanent Fact](BasicAdapter/entries/fact/PermanentFact)                  | Saved permanently, it never gets removed          |
| [Number Placeholder Fact](BasicAdapter/entries/fact/NumberPlaceholderFact) | Computed fact for placeholders returning a number |
| [Value Placeholder Fact](BasicAdapter/entries/fact/ValuePlaceholderFact)   | Computed fact for placeholders returning anything |
| [Session Fact](BasicAdapter/entries/fact/SessionFact)                      | Saved until a player logouts of the server        |
| [Timed Fact](BasicAdapter/entries/fact/TimedFact)                          | Saved for a specified duration, like 20 minutes   |
| [Random Trigger Gate](BasicAdapter/entries/action/RandomTriggerGate)       | Randomly selects its connected triggers           |
