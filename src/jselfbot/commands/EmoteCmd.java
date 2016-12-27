/*
 * Copyright 2016 John Grosh (jagrosh).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jselfbot.commands;

import jselfbot.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class EmoteCmd extends Command {

    public EmoteCmd()
    {
        this.name = "emote";
        this.description = "checks info about an emote";
        this.arguments = "<:emote:>";
    }
    
    @Override
    protected void execute(String args, MessageReceivedEvent event) {
        String id = args.replaceAll("<:.+:(\\d+)>", "$1");
        Emote emote = event.getJDA().getEmoteById(id);
        if(emote==null)
            tempReply("Invalid emote or emote ID", event);
        else
        {
            EmbedBuilder builder = new EmbedBuilder();
            if(event.getGuild()!=null)
                builder.setColor(event.getGuild().getSelfMember().getColor());
            builder.setImage(emote.getImageUrl());
            builder.setDescription(emote.getAsMention()+" **:"+emote.getName()+":**\nID: **"+emote.getId()+"**\nGuild: **"+emote.getGuild().getName()+"**");
            reply(builder.build(), event);
        }
    }
    
}
