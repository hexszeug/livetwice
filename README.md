# livetwice
## This is a bukkit plugin
See its page on dev.bukkit.org/projects/livetwice

## LiveTwice is a Minigame
If the plugin is enabled, you cannot do anything else than playing it.

In LiveTwice you fight against each other with two characters. Both characters have their own kits, health and position. The players can switch freely between their characters during the game. If one character dies, you cannot switch anymore but you can still win with the one left.

The kits define the items and some status effects that are applied to the player.
There are 15 kits:

    Flying Bowman
    Knight
    Turtle Master
    Elf
    Assassin
    Squire
    Tank
    Ghost
    Giant
    Dwarf
    Healer
    Smart Guy
    Builder
    Prince
    Unicorn

If you join the server you get a pickaxe with which you can open an inventory-interface to choose your kits. After that, with at least two players, you can start the game with an inbuilt starting queue (join the queue with a given glas pane).

This is my first plugin. I welcome comments and suggestions for improvement.

Have fun!

## For server admins: (admins, please read it's essential)
### The easy way to setup:
1. Put the plugin in the plugins-folder
2. Start the server with any world (not your survival world with no backup, it would overwrite your items)
3. Make you self an op
4. type "/m setup"

### The advanced way to setup:
1. Put the plugin in the plugins-folder
2. Start the server with any world
3. Find a position in the world where you want the minigame to be and note the coords
4. The minigames size is 50 x 90 x 50 (X Y Z) Blocks
5. The coords you need are the of the edge with the smalles coord on each axis
6. Stop the server
7. Open plugins/LiveTwice_Plugin/config.yml and change the x-pos, y-pos and z-pos
8. Start the server again
9. type /m setup in the chat (only works if you are op)

### The config.yml
    plugins/LiveTwice_Plugin/config.yml
    min-players: 2            The minimum of players required to start a game
    start-timer: 15           The length (seconds) of the timer which start when min-players are ready
    moderated: false          Toggle moderated mode (see Moderated Mode)
    x-pos: -1                 The x-pos of the minigame (see advanced setup)
    y-pos: 61                 The y-pos of the minigame (see advanced setup)
    z-pos: -1                 The z-pos of the minigame (see advanced setup)

 
### Moderated Mode
If moderaded is enabeld (see config.yml) the game only starts if a op type /s or /start.
The "moderator" also can type /q or /queue to see which players are not ready and if the havnt choosen their kit.
He cant see which kits the other players have!
