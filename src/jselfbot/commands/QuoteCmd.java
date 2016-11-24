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
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class QuoteCmd extends Command {

    public QuoteCmd()
    {
        this.name = "quote";
        this.description = "quotes a message";
        this.arguments = "<message id>";
    }
    
    @Override
    protected void execute(String args, MessageReceivedEvent event) {
        if(!args.matches("\\d{17,22}"))
        {
            tempReply("Not a valid message ID", event);
            return;
        }
        event.getChannel().getHistory().retrievePast(100).queue(messages -> {
            try {
                Message msg = messages.stream().filter(m -> m.getId().equals(args)).findAny().get();
                EmbedBuilder builder = new EmbedBuilder();
                builder.setAuthor(msg.getAuthor().getName()+" #"+msg.getAuthor().getDiscriminator(), null, 
                        msg.getAuthor().getAvatarUrl()==null ? msg.getAuthor().getDefaultAvatarUrl() : msg.getAuthor().getAvatarUrl());
                if(event.getGuild()!=null)
                {
                    Member member = event.getGuild().getMemberById(msg.getAuthor().getId());
                    if(member!=null)
                        builder.setColor(member.getColor());
                }
                if(msg.isEdited())
                {
                    builder.setFooter("Edited", null);
                    builder.setTimestamp(msg.getEditedTime());
                }
                else
                {
                    builder.setFooter("Sent", null);
                    builder.setTimestamp(msg.getCreationTime());
                }
                builder.setDescription(msg.getRawContent());
                reply(builder.build(), event);
            } catch(Exception e) {
                tempReply("Message with id `"+args+"` not found in the past 100", event);
            }
        }, t -> {
            tempReply("Failed to retrieve history", event);
        });
    }
    
}
