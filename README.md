<!--suppress ALL -->
<div align="center">

<img src="assets/logo.png" alt="Logo" width="200" height="200">

# I have slept

This mod resets all players' sleep counters after skipping the night in multiplayer mode, ensuring no phantoms spawn for
those who didn't sleep.

</div>

> [!NOTE]  
> This mod works on the server side only. Clients do not need to install the mod for it to function.

## What does this mod do?

In Minecraft's multiplayer mode, the `playersSleepingPercentage` gamerule allows a percentage of players to sleep in
order to skip the night. However, even when the night is skipped, the sleep counter for players who didn't sleep remains
unchanged. This leads to a situation where players who didn't participate in skipping the night will still be targeted
by phantoms in subsequent nights, despite the night being skipped.

This mod fixes that by resetting the sleep counters for all players after the night is skipped, ensuring no one is
haunted by phantoms. It makes multiplayer gameplay smoother by ensuring that all players’ sleep statuses are properly
synced after skipping the night.

## How to use?

This mod works seamlessly with the `playersSleepingPercentage` gamerule. To use it, simply set the percentage of players
required to sleep in order to skip the night by using the following command:

```shell
/gamerule playersSleepingPercentage <value>
```

For example, setting it to `50` means that when half of the players are sleeping, the night will be skipped. Once the
night is skipped, this mod will automatically reset the sleep counters for all players, including those who didn't
sleep, ensuring phantoms won't spawn for anyone in the following nights. No additional configuration is needed—just set
the percentage, and the mod will take care of the rest!

Additionally, the mod introduces a new gamerule called `doBetterServerSleep`, which allows you to enable or disable the
sleep counter reset feature. By default, this feature is enabled. To toggle it, use the following command:

```shell
/gamerule doBetterServerSleep <true|false>
```

Set it to `true` to enable the sleep counter reset, or `false` to disable it.

## Sponsors

[![Sponsors](https://afdian-connect.deno.dev/sponsor.svg)](https://afdian.com/a/gizmo)

## License

Code is distributed under [MIT](./LICENSE) license, feel free to use it in your proprietary projects as well.
