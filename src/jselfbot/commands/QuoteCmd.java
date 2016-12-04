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
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.MessageHistory;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
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
        this.arguments = "<message id> [channel mention or id] [text]";
    }
    
    @Override
    protected void execute(String args, MessageReceivedEvent event) {
        String[] parts = args.split("\\s+", 2);
        String id1 = parts[0].replaceAll("<#(\\d+)>", "$1");
        if(!isId(id1))
        {
            tempReply("`"+id1+"` is not a valid message or channel ID", event);
            return;
        }
        MessageChannel channel = event.getJDA().getTextChannelById(id1);
        String messageId;
        String followingText = null;
        if(channel != null) //channel id found, need a message id now
        {
            if(parts.length==1)
            {
                tempReply("Channel provided but no message id!", event);
                return;
            }
            parts = parts[1].split("\\s+",2);
            if(!isId(parts[0]))
            {
                tempReply("`"+parts[0]+"` is not a valid message ID", event);
                return;
            }
            messageId = parts[0];
            followingText = parts.length>1 ? parts[1] : null;
        }
        else
        {
            messageId = id1;
            if(parts.length>1)
            {
                String[] parts2 = parts[1].split("\\s+", 2);
                String id2 = parts2[0].replaceAll("<#(\\d+)>", "$1");
                channel = event.getJDA().getTextChannelById(id2);
                if(channel == null)
                    followingText = parts[1];
                else
                    followingText = parts2.length>1 ? parts2[1] : null;
            }
        }
        String follow = followingText;
        if(channel==null)
            channel = event.getChannel();
        try
        {
            MessageHistory.getHistoryAround(channel, messageId, 2).queue(
                    mh -> {
                        if(mh.getCachedHistory().isEmpty())
                        {
                            tempReply("No message history around `"+messageId+"`", event);
                            return;
                        }
                        Message msg = mh.getCachedHistory().size()==1 ? mh.getCachedHistory().get(0) : mh.getCachedHistory().get(1);
                        EmbedBuilder builder = new EmbedBuilder();
                        builder.setAuthor(msg.getAuthor().getName()+" #"+msg.getAuthor().getDiscriminator(), null, 
                                msg.getAuthor().getAvatarUrl()==null ? msg.getAuthor().getDefaultAvatarUrl() : msg.getAuthor().getAvatarUrl());
                        if(msg.getGuild()!=null)
                        {
                            Member member = msg.getGuild().getMemberById(msg.getAuthor().getId());
                            if(member!=null)
                                builder.setColor(member.getColor());
                        }
                        if(!msg.getAttachments().isEmpty() && msg.getAttachments().get(0).isImage())
                            builder.setImage(msg.getAttachments().get(0).getUrl());
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
                        reply(builder.build(), event, follow==null ? null : s -> event.getChannel().sendMessage(follow).queue());
                    }, 
                    f -> tempReply("Failed to retrieve history around `"+messageId+"`", event));
        }
        catch(Exception e)
        {
            tempReply("Could not retrieve history", event);
        }
    }
    
    
    private static boolean isId(String id)
    {
        return id.matches("\\d{17,22}");
    }
}
