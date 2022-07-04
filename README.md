# TechnoBlade Memorial Plugin Core

#### In the minecraft community, we recently lost a hero of many people. TechnoBlade was a role model for us all, and he brought so much joy to everyone who watched his videos.

#### TechnoBlade had Sarcoma Cancer, and it had finally taken his life in june 2022. In honor of TechnoBlade and his legacy he left on the Minecraft/Hypixel community, myself and a few others have decided to make a memorial server for raising awareness for Sarcoma cancer, as well as raising funds and money for the Sarcoma Foundation of America.

#### To honor our pledge of FULL transparency, we have made the plugin behind the server public and open source for all to see.

## How the memorial server works.

#### The thought behind how the server works is as follows:

#### Players can log onto the server, and they will be welcomed with a custom terrain, open field where each player can purchase a pig spawn egg through Tebex/Buycraft.

#### There would be 4 tiers of donations, Iron, Gold, Diamond, and Custom. Iron would be $1 USD, gold $5 USD, diamond $20 USD, and custom would be any amount above $20 USD

#### 100% of profits would go directly to the Sarcoma Foundation of America, after we pay for server hosting expenses.

#### How the memorial plugin works (for all the developers)

#### A Detailed step-by-step explanation of how the plugin works is as follows:

Player would purchase a tier online via Tebex. Tebex would then run a command in
game: ``/techno give <player> <tier> <amount>``

Once that command gets run in game, it would create a `QueuedDonation` object, and then save the data of that into
SQL storage The SQL storage table would go as follows:

| id  | donationUUID                         | tier  | amountUSD | donorUUID                            | timestamp     |
|-----|--------------------------------------|-------|-----------|--------------------------------------|---------------|
| 1   | d670ac2d-2f9d-4afc-899e-025efaaa5573 | IRON  | 5.0       | 5a5ff914-6777-400e-803a-e9f78fa8bda6 | 1656911822367 |

The player would then be able to run the command `/claimPig` in game, and it would go through every entry in the queued
donations table and give them a pig spawn egg that would correspond to the row in the table.

At first, the spawn egg would not have a message attached to it, the player can then add a message
with `/message <message>` which allows them to set the message on the egg and ultimately into the final donation note
for that pig. The message command would have a profanity filter and restrict players from putting inappropriate words,
In config there is a list of blacklisted words to filter out.

The player can then right-click either the air or a block, and it will spawn a pig in at their location. The pig will
have a custom name in the format of: `<donor>'s pig`, example: `i01's pig`, The pig would be color coded to match the
donation tier. Iron would be Light-Gray (&7), Gold would be Gold (&6), Diamond would be Light-Blue (&b), and custom
would be rainbow.

When the player places the pig down, it will remove the egg from their inventory and make an entry in
the `final_donations` table in SQL. This table will be full of all the final donations that players have made.

When someone Shifts + Right-Clicks on a pig it will send them information about that pig, including the custom message
that the donor wrote.

Donors will be able to ride their own pig by Right-Clicking on them. You cannot ride other donors pigs, only your own.
ALl players on first join will be given a Carrot On a Stick to be able to control their pigs while riding.

Throughout the entire server, all damage, block breaking & placing, and food loss will be fully disabled and the world
will be in peaceful difficulty.

In the background there will be a task to assure all pigs are properly spawned in with their corresponding donor
attached to them. On reboot this task will run as well. The task does as follows:

* Loops through all pigs in all worlds and removes any that are not linked to a donation ID.
* Loops through the final donations table, and checks if there is a mapped entity ID with the corresponding donation ID.
* If there is not, it will spawn a pig in at a random (pre-set) location. and will have all meta-data attached to it.
* There will be an admin command `/techno addpigspawn` that will add the location of the admin into the list of pre-set
  spawn locations to randomly pick between.

### With all that being said, I hope this readme is legible to everyone. If you notice any typos, or clarification issues please create a GitHub issue, and I will fix it ASAP.

### Sarcoma Foundation Information
https://www.curesarcoma.org/
https://twitter.com/CureSarcoma
https://www.instagram.com/curesarcoma/
https://www.facebook.com/CureSarcoma/
https://www.linkedin.com/company/sarcoma-foundation-of-america