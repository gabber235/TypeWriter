---
sidebar_position: 2
---

# Installation Guide

:::danger
Typewrite only works on **Paper** Spigot servers. It will not work on Spigot or Bukkit servers.
:::

:::caution
Typewriter is currently in beta. This means that the plugin is still in development and may contain bugs. If you find
any bugs, please report them [here](https://discord.gg/p7WH9VvdMQ)
:::

## Installing the Plugin

To install the Typewriter plugin on your paper spigot Minecraft server, follow these steps:

1. Download the plugin from [here](https://github.com/gabber235/TypeWriter/releases).
2. Place the downloaded .jar file in your server's `plugins` folder.
3. Download and install `ProtocolLib` from [here](https://ci.dmulloy2.net/job/ProtocolLib/lastSuccessfulBuild/).
4. Restart your server to complete the installation.

:::caution
The `ProtocolLib` link on spigotmc.org is outdated and will not work with Typewriter. Make sure to use the link above.
:::

### Basic Adapter

The basic adapter contains the base entries for any server.

To install the basic adapter (or any for that matter), follow these steps:

1. Download the `BasicAdapter.jar` file from [here](https://github.com/gabber235/TypeWriter/releases).
2. Place it in the `server/plugins/Typewriter/adapters` folder.
3. Restart your server to complete the installation.

#### Pre-Made Adapters

In addition to the basic adapter, Typewriter also offers several pre-made adapters that can be easily installed and
configured. For a list of available adapters and instructions on how to install them, see [here](pre-made-adapters).

## Configuring the Web Panel

The web panel to configure your server's interactions. The web panel is preinstalled inside the plugin, though it is
disabled by default for security and performance reasons.

:::caution
The web should only be used on a development server and **NOT** on a production server.
As it uses precious resources to both host a website and web sockets.
:::

The web panel needs two ports to be open. These can be changed, but it does need at least two new ports to be open. The
default ports are `8080` and `9092`. If you are not able to open these ports, the easiest way is to use typewriter
locally and copying the files in `server/plugins/Typewriter/pages` to your production server.

To enable the web panel, follow these steps:

1. Open the `server/plugins/Typewriter/config.yml` file.
2. Change the settings to the following:

```yml
# Whether the web panel and web sockets are enabled.
enabled: true
# The hostname of the server. CHANGE THIS to your servers ip.
hostname: localhost
# The panel uses web sockets to sync changes to the server and it allows you to work with multiple people at the same time.
websocket:
  # The port of the websocket server. Make sure this port is open.
  port: 9092
  # The authentication that is used. Leave unchanged if you don't know what you are doing.
  auth: session

panel:
  # The panel can be disabled while the sockets are still open. Only disable this if you know what you are doing.
  # If the web sockets are disabled then the panel wil always be disabled.
  enabled: true
  # The port of the web panel. Make sure this port is open.
  port: 8080
```

3. Restart your server to complete the installation.
4. To connect to the web panel. Run `/typewriter connect` in game. This will give you a link to the web panel.

:::info Note
If the web panel is not working, make sure that the ports are open and that the `hostname` is set to your servers IP.
If it still doesn't work, ask for help in the [Discord](https://discord.gg/HtbKyuDDBw).
:::

Once opened, you can use the web panel to create and configure quests, NPC dialogues, and more. The panel also allows
you to view and edit your server's existing player interactions.

## What's Next?

Try to create your first interaction by following the [First Interaction](First-interaction) guide. If you have any
questions, feel free to ask them in the [Discord](https://discord.gg/HtbKyuDDBw).
