# First Cinematic
This guide will lead you through making your first cinematic.

:::info
This guide uses the [Basic Adapter](../adapters/BasicAdapter), hence it must be installed before following this guide.
:::

:::info
This guide builds upon the [First Interaction](03-first-interaction.mdx) guide.
<br>We strongly recommend reading through it before following this guide.
:::

## What are Cinematics?
Cinematics take control of the player's point of view to show them cutscenes from a different point of view.
<br>This can be used in a lot of different situations, with some examples being:
- A cutscene in a quest
- As part of a tutorial
- A cutscene on player teleporting
- And a lot more

## Creating a Cinematic
We'll start off by making a simple cinematic with a cinematic camera entry.

### 1. Creating the Page
Start off by clicking add page. Here, choose the **Cinematic** type in the dropdown menu, and name the page `first_cinematic`.

(gif showing the page creation)

### 2. Adding a Camera Entry
Let's now add an entry to the newly created page. Click the `Add Entry` button or the `+` in the top right corner,
then search for `add: camera`, and add the `Add Camera Cinematic` entry. You should now see the entry on your screen.
Let's rename the entry to `camera_view`.

You'll now see a lot of new fields that aren't in the other pages. Let's go through those.

**Segments**
<br>A segment is what the entry does in that specific time frame. Entries can have multiple segments, but an entry can't
have overlapping segments. For that, create another entry.

**Track Duration**
<br>The track duration field is where you decide the duration of the entire cinematic.
It's important to note that the duration is in amount of frames, and not in seconds.
You can however easily convert from seconds to frames, as 20 frames is equal to 1 second.

**Segments Track**
<br>This is where the segments of entries are displayed to you. Here you can change when a segment starts and when it ends.
Keep in mind you can also do this on each segment's sidebar by modifying the `Start Frame` & `End Frame` fields.

(Image with arrows pointing to the things described above)

<br>
Now that that's explained, we need to add a segment to the camera_view entry.
<br>Make sure you have the camera view entry selected, and then click `Add Segment` on the entry sidebar.
This creates a new segment that spans the entire track.

We now want to make this segment do something, and to do that, we need to add paths.
For the camera entry, this will be the locations it moves the player's POV between.
Let's start with making two paths. To do that, click the `+` symbol next to Path on the sidebar twice.
We then expand `Path #1`, and enter the location of this path. We then expand `Path #2` and add the location of that path.
Remember to add two different locations. You can see example paths in the screenshot below.

(Screenshot of example paths)

You should now have a functioning cinematic!
Let's `Publish` and check it out. To check it out right now, type `/tw cinematic start first_cinematic`, and
the cinematic should start playing!

(Cinematic gif)

### Triggering the Cinematic
Now, you probably want the cinematic to be triggered when a player does something, and not through a command.
Let's do that by modifying the flower example from [First Interaction](03-first-interaction.mdx) so that one of the
options causes the cinematic to play.

Let's head over to the Flower page by clicking on it in the Pages sidebar. We then simply want to add a new entry.
Search for `add: cinematic` when adding a new entry, and select `Add Cinematic` entry. We'll then rename this entry to
`play_first_cinematic`, and in the entry sidebar, click on the `Page` field. Here you should see the `First Cinematic` page
that we made earlier. Select that page, and then select the `Inspect Flower` entry. Here we want to modify one of
the options so that when a player selects it, the `first_cinematic` plays. Let's select the option `Smell the flower`.
In that option, add a new Trigger, and select `Play First Cinematic` option in the searh box.

(Gif going through the steps above)

:::info
I strongly recommend picking a specific red tulip and changing the cinematic paths to be around it.
:::
You should now be able to select the option `Smell the flower` when clicking a red tulip, and it'll play the cinematic.

## Adding dialogue
Let's make the cinematic have dialogue when it's playing. To do this, head back to the `First Cinematic` page.
Here, add a new entry and search for `add: actionbar`, and select the `Add Actionbar Dialogue Cinematic` entry.
We'll then rename this entry to `flower_dialogue`. Now, let's first add a speaker to the entry. Let's use the Flower Speaker.
After that, click `Add Segment` to add a segment to the dialogue entry, and select it.

Let's change the time frame of the dialogue to start after 1 second. To do this, we can either change the Start Frame field, or drag the segment on the track
to our desired starting point. We also have to remember that duration is in frames, not seconds, and remembering from earlier
that 1 second equals 20 frames, change the Start Frame to 20.

We then have to add the text to be displayed. Insert `That flower smells <red><bold>fantastic` to the Text field.
<br>Aand we're done! Dialogue should now appear 1 second into the cinematic.

(Cinematic gif)

## Adding Particles
You can also add particles to be displayed during the cinematic. Let's do so hearts appear around the red tulip.
To do this, add a new entry and search `add: particle`, and add the `Add Particle Cinematic` entry, and rename it to
`flower_particles`
For this entry, we modify the particle effect on the entry itself and not on the segments.
Let's start by selecting the particle type. Click the `Particle` field in the entry sidebar, and scroll down until
you find the `HEART` particle and then select it. Then, enter the location of your Red Tulip in the location field above.
We will then define the particle offsets, speed & spawnCountPerTick. Think of the offset fields as a box around the location
you've specified, and particles can spawn anywhere inside the box.
We will set offsetX and offsetZ to 0.25, and leave offsetY at 0. We'll then set the speed to 0.1 as to not get a lot of
hearts spawning at the same time, and we will set spawnCountPerTick to 1, meaning that 1 heart will spawn every tick.

(Gif going through the steps above)

Now, we will add a segment to the particle entry, and we're finished! If you now play your cinematic, heart particles
should spawn around the red tulip.

## Next steps
You should now know how to make simple cinematics, but remember, cinematics have a lot more to offer!
Try messing about with the other options like NPCs that walk around, playing sounds, blinding cinematic & more! 