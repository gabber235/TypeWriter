<user>
# Bug with velocity
</user>
<user>
Helloo, I tried the new beta 129 version of typewriter on 1.21 with the extensions and it seems to have some issues with velocity when I interact with an NPC an error appears in the console and the Interaction doesn't work also.

It worked on typewriter v0.5.1 but unfortunately not on v0.6.0

This is the error:
```
Error sending packet clientbound/minecraft:system_chat (skippable? true)
io.netty.handler.codec.EncoderException: Failed to encode packet 'clientbound/minecraft:system_chat'
        at net.minecraft.network.codec.IdDispatchCodec.encode(IdDispatchCodec.java:53) ~[paper-1.21.1.jar:1.21.1-122-4430e96]
...
```
</user>
<assistant>
Sorry to hear you are having issues with Typerwriter! Please provide the following information:
 - **Upload logs** Go to your `logs` folder and upload the `latest.log` to [McLogs](https://mclo.gs/) and give us the generated URL.
 - **Expected behavior**: What did you expect to happen? And what actually happened?
 - **Reproduction steps**: Provide clear instructions on how to reproduce the issue.

Providing the above information is **mandatory** for the ticket to be answered and look by the support team.
</assistant>
<user>
Logs:
https://mclo.gs/y4NrASr

Expected behavior:
I'm trying to interact with an NPC with the Entity adapter, it should work like in the docs
https://docs.typewritermc.com/docs/creating-stories/entity-adapter

Reproduction steps:
1. Use a proxy for example velocity
2. Follow the docs https://docs.typewritermc.com/docs/creating-stories/entity-adapter/interacting
3. Interact with the NPC
(Might also crash minecraft)

For minecraft crash:
1. Use the citizens adapter
2. Add Add On NPC Interact
3. Link with new spoken & link the speaker to the citizens NPC
4. Interact with the NPC

Typewriter Version: v0.6.0 beta 129
Server Version: 1.21.1
</user>
<tool_call>
complete_intake({"provided_inquiry": true})
</tool_call>
