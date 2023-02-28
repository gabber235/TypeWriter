# Basic Adapter
The Basic Adapter contains all of the essential entries for Typewriter. In most cases, it should be installed with Typewriter. If you haven't installed Typewriter or the adapter yet, please follow the [Installation Guide](/docs/Installation-Guide) first.

## Entries

### Action

| Name | Description |
| ---- | ----------- |
| [Console Command Action](BasicAdapter/entries/action/console_run_command) | Run command from console |
| [Delayed Action](BasicAdapter/entries/action/delayed_action) | Delay an action for a certain amount of time |
| [Drop Item Action](BasicAdapter/entries/action/drop_item) | Drop an item at location, or on player |
| [Give Item Action](BasicAdapter/entries/action/give_item) | Give an item to the player |
| [Player Command Action](BasicAdapter/entries/action/player_run_command) | Make player run command |
| [Play Sound Action](BasicAdapter/entries/action/play_sound) | Play sound at player, or location |
| [Show Title Action](BasicAdapter/entries/action/show_title) | Show a title to a player |
| [Simple Action](BasicAdapter/entries/action/simple_action) | Simple action to modify facts |
| [Spawn Particle Action](BasicAdapter/entries/action/spawn_particles) | Spawn particles at location |
### Dialogue

| Name | Description |
| ---- | ----------- |
| [Message Dialogue](BasicAdapter/entries/dialogue/message) | Display a single message to the player |
| [Option Dialogue](BasicAdapter/entries/dialogue/option) | Display a list of options to the player |
| [Random Message Dialogue](BasicAdapter/entries/dialogue/random_message) | Display a random message from a list to a player |
| [Random Spoken Dialogue](BasicAdapter/entries/dialogue/random_spoken) | Display a random selected animated message to the player |
| [Spoken Dialogue](BasicAdapter/entries/dialogue/spoken) | Display a animated message to the player |
### Speaker

| Name | Description |
| ---- | ----------- |
| [Simple Speaker](BasicAdapter/entries/speaker/simple_speaker) | The most basic speaker |
### Event

| Name | Description |
| ---- | ----------- |
| [Block Break Event](BasicAdapter/entries/event/on_block_break) | When the player breaks a block |
| [Block Place Event](BasicAdapter/entries/event/on_place_block) | When the player places a block |
| [Detect Command Ran Event](BasicAdapter/entries/event/on_detect_command_ran) | When a player runs an existing command |
| [Interact Block Event](BasicAdapter/entries/event/on_interact_with_block) | When the player interacts with a block |
| [Pickup Item Event](BasicAdapter/entries/event/on_item_pickup) | When the player picks up an item |
| [Player Death Event](BasicAdapter/entries/event/on_player_death) | When a player dies |
| [Player Hit Entity Event](BasicAdapter/entries/event/on_player_hit_entity) | When a player hits an entity |
| [Player Join Event](BasicAdapter/entries/event/on_player_join) | When the player joins the server |
| [Player Kill Entity Event](BasicAdapter/entries/event/on_player_kill_entity) | When a player kills an entity |
| [Player Kill Player Event](BasicAdapter/entries/event/on_player_kill_player) | When a player kills a player |
| [Run Command Event](BasicAdapter/entries/event/on_run_command) | When a player runs a custom command |
### Fact

| Name | Description |
| ---- | ----------- |
| [Cron Fact](BasicAdapter/entries/fact/cron_fact) | Saved until a specified date, like (0 0 * * 1) |
| [Permanent Fact](BasicAdapter/entries/fact/permanent_fact) | Saved permanently, it never gets removed |
| [Placeholder Fact Entries](BasicAdapter/entries/fact/number_placeholder) | Computed Fact for a placeholder number |
| [Session Fact](BasicAdapter/entries/fact/session_fact) | Saved until a player logouts of the server |
| [Timed Fact](BasicAdapter/entries/fact/timed_fact) | Saved for a specified duration, like 20 minutes |
| [Random Trigger Gate](BasicAdapter/entries/action/random_trigger) | Randomly selects its connected triggers |