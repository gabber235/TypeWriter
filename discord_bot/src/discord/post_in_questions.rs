use poise::{serenity_prelude::Mentionable, CreateReply};
use rand::prelude::*;
use rand::seq::SliceRandom;
use rand::SeedableRng;
use std::time::{SystemTime, UNIX_EPOCH};

use crate::{check_is_contributor, Context, WinstonError, QUESTIONS_CHANNEL};

#[poise::command(
    context_menu_command = "Post In Questions",
    ephemeral,
    check = "check_is_contributor"
)]
pub async fn post_in_questions(
    ctx: Context<'_>,
    #[description = "The message to reply to"] message: poise::serenity_prelude::Message,
) -> Result<(), WinstonError> {
    let seed = SystemTime::now()
        .duration_since(UNIX_EPOCH)
        .expect("Time went backwards")
        .as_nanos();
    let mut rng = StdRng::seed_from_u64(seed as u64);
    let reply = REPLIES
        .choose(&mut rng)
        .unwrap_or(&"Please use the questions channel.")
        .replace("{channel}", &QUESTIONS_CHANNEL.mention().to_string());

    message.reply_ping(ctx, reply).await?;

    ctx.reply("Replied to the question with").await?;
    Ok(())
}

const REPLIES: &[&str] = &[
"Oops! Looks like your question took a wrong turn. The {channel} is just around the corner! 🧭",
"Hold up! Your awesome question deserves a spotlight in our dedicated {channel}. It's like VIP treatment for curiosity! ✨",
"Psst... I heard the {channel} is throwing a party, and your question is the guest of honor! 🎉",
"Alert! Question detected in a question-free zone. Quick, teleport it to the {channel} before it gets lonely! 🚀",
"Beep boop! My question sensors are tingling. May I suggest a cozy new home for your query in our dedicated {channel}? 🏠",
"Whoa there, curious cat! Your question is purr-fect for our special {channel}. It's feline fine over there! 🐱",
"Holy guacamole! Your question is so good, it deserves its own red carpet in the {channel}. Shall we roll it out? 🥑",
"Great Scott! Your question has traveled through time and space. Quick, let's redirect it to its true destination: the {channel}! ⏰",
"Woah, easy there, speed racer! Your question's burning rubber in the wrong lane. Want to cruise over to the {channel}? 🏎️",
"Shiver me timbers! Ye be asking questions in strange waters. How about we set sail for the {channel}, matey? ☠️",
"Bazinga! Your question is so smart, it deserves to hang out with its brainy buddies in the {channel}. Wadda ya say? 🧠",
"Kaboom! Your question just exploded with awesomeness. Let's clean up the glitter and move this party to the {channel}! 🎆",
"Abracadabra! Your question's magical, but it needs the special enchanted grounds of the {channel} to truly shine! ✨",
"Cowabunga, dude! Your question's surfing the wrong waves. Let's catch the perfect curl over in the {channel}! 🏄‍♂️",
"Great galaxies! Your question's out of this world, but it's landed in the wrong solar system. Shall we warp to the {channel}? 🚀",
"Jeepers creepers! Your question's giving me the heebie-jeebies... of excitement! Let's exorcise it over to the {channel}! 👻",
"Holy moly guacamole! Your question's spicier than a jalapeño popper. Let's cool it off in the refreshing waters of the {channel}! 🌶️",
"Gadzooks! Your question's got more buzz than a beehive. Let's guide this busy bee to its proper flower in the {channel}! 🐝",
"Jumpin' Jehoshaphat! Your question's bouncing off the walls. Let's give it a proper trampoline in the {channel}! 🏃‍♂️",
"Lickety-split! Your question's faster than greased lightning. Let's race it over to the {channel} for a photo finish! 🏁",
"Mama mia! Your question's spicier than a pepperoni pizza. Let's serve this hot slice in the {channel}! 🍕",
"Quibble me timbers! Your question's stirring up a storm in a teacup. Let's sail these choppy waters to the {channel}! ☕",
"Tally-ho! Your question's on a fox hunt in the wrong field. Let's guide the hounds to the {channel} for the real chase! 🦊",
"Uber-cool! Your question's chillin' like a villain in the wrong hood. Let's cruise over to the {channel} in style! 😎",
"Voila! Your question's appeared like magic, but in the wrong hat! Let's pull this rabbit out in the {channel} instead! 🎩",
"Xtra! Xtra! Read all about it! Your question's making headlines in the wrong paper. Let's print the edition in the {channel}! 📰",
"Zoinks! Your question's got more mystery than a Scooby-Doo episode. Let's unmask this villain in the {channel}! 🕵️‍♂️",
"Zing! Your question's sharper than a tack. Let's pin this brilliant idea in the {channel} board! 📌",
"Aye aye, captain! Your question's sailing in uncharted waters. Let's navigate to the safe harbor of the {channel}! ⚓",
"Bada bing, bada boom! Your question's explosive, but it's in the wrong fireworks show. Let's light it up in the {channel}! 💥",
"Cha-ching! Your question's worth its weight in gold. Let's cash in this treasure in the {channel}! 💰",
"Gee whiz! Your question's fizzier than a shaken soda. Let's pop the top in the {channel}! 🥤",
"Ipso facto! Your question's presenting its case in the wrong court. Let's adjourn to the {channel} for the verdict! ⚖️",
"Mamma mia! Your question's saucier than a pizza with extra toppings. Let's slice and dice in the {channel}! 🍕",
"Oh my stars and garters! Your question's more shocking than static electricity. Let's ground ourselves in the {channel}! ⚡",
"Pish posh! Your question's fancier than a teacup at the Queen's garden party. Let's sip and chat in the {channel}! ☕👑",
"Querulous quails! Your question's ruffling feathers in the wrong coop. Let's migrate to the {channel} for nesting! 🐦",
"Razzle dazzle! Your question's shinier than a disco ball. Let's boogie on over to the {channel}! 🕺",
"Upsy-daisy! Your question's tumbled into the wrong ball pit. Let's bounce over to the fun zone in the {channel}! 🏀",
"Vroom vroom! Your question's revving its engine at the wrong starting line. Let's zoom to the real race in the {channel}! 🏎️",
"Whoop-de-doo! Your question's doing cartwheels in the wrong circus. Let's flip over to the big top in the {channel}! 🎪",
"Xylophone! Your question's playing a beautiful tune, but in the wrong orchestra pit. Let's conduct this symphony in the {channel}! 🎼",
"Yum yum! Your question's deliciously intriguing, but it's on the wrong menu. Let's serve this gourmet dish in the {channel}! 🍽️",
"Bazinga! Your question's got more twists than a pretzel factory. Let's unravel this mystery in the {channel}! 🥨",
"Cowabunga! Your question's riding a gnarly wave, dude. Let's catch the perfect curl in the {channel}! 🏄‍♂️",
"Dagnabbit! Your question's digging for gold in the wrong mine. Let's strike it rich in the {channel}! ⛏️💰",
"Eureka! Your question's a golden discovery, but it's floating in the wrong bathtub. Let's make a splash in the {channel}! 🛁",
"Fiddlesticks! Your question's playing a solo when we need a full orchestra. Let's tune up in the {channel}! 🎻",
"Hocus pocus! Your question's casting spells in the wrong wizard's tower. Let's find the right potion in the {channel}! 🧙‍♂️",
"Inconceivable! Your question's scaling the wrong castle wall. Let's storm the correct fortress in the {channel}! 🏰",
"Jiminy Cricket! Your question's chirping up a storm, but in the wrong garden. Let's find the right leaf in the {channel}! 🦗",
"Merlin's beard! Your question's more magical than a wizard's duel. Let's cast this spell in the {channel}! 🧙‍♂️✨",
"Noodle noggin! Your question's got my brain tied up like spaghetti. Let's untangle this pasta in the {channel}! 🍝",
"Ubiquitous unicorns! Your question's sprouting rainbows in the wrong meadow. Let's prance to the magical {channel}! 🦄",
"Vertigo! Your question's got me spinning like a top. Let's find our balance in the {channel}! 💫",
"Xenon! Your question's glowing brighter than a noble gas. Let's light up the {channel} with this brilliance! 💡",
"Egad! Your question's more surprising than a jack-in-the-box. Let's pop this lid in the {channel}! 🎁",
"Great Scott! Your question's generating 1.21 gigawatts of curiosity. Let's time travel to the {channel} for answers! ⚡🚗",
"Indubitably! Your question's more mysterious than a Sherlock Holmes case. Let's deduce the answer in the {channel}, Watson! 🕵️‍♂️",
"Leaping Leprechauns! Your question's luckier than a four-leaf clover. Let's find the pot of gold in the {channel}! 🍀",
];
