# RPGRegions Adapter

The RPGRegions Adapter is an adapter for the RPGRegions plugin. It allows you to use RPGRegions's discovery system in your dialogue.

## Entries

### Action

| Name                                                                    | Description      |
|-------------------------------------------------------------------------|------------------|
| [Discover Region Action](RPGRegionsAdapter/entries/action/discover_region)   | Discover a region  |

### Facts

| Name                                                         | Description                       |
|--------------------------------------------------------------|-----------------------------------|
| [In Region Fact](RPGRegionsAdapter/entries/fact/in_region_fact) | If the player is in a region |

### Events

| Name                                                                  | Description                              |
|-----------------------------------------------------------------------|------------------------------------------|
| [Enter Region Event](RPGRegionsAdapter/entries/event/on_enter_region) | When a player enters a RPGRegions region |
| [Exit Region Event](RPGRegionsAdapter/entries/event/on_exit_region)   | When a player exits a RPGRegions region  |