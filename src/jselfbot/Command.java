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
package jselfbot;

import java.util.function.Consumer;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.requests.RestAction;

/**
 *
 * @author John Grosh (jagrosh)
 */
public abstract class Command {
    protected String name = "null";
    protected String description = "No description provided";
    protected String arguments = null;
    protected Type type = Type.DELETE_AND_RESEND;
    
    protected abstract void execute(String args, MessageReceivedEvent event);
    
    public void run(String args, MessageReceivedEvent event)
    {
        try {
            if(type == Type.DELETE_AND_RESEND)
                event.getMessage().delete().queue();
            execute(args, event);
        } catch(Exception e) {
            failure(event);
            throw e;
        }
    }
    
    protected void reply(String reply, MessageReceivedEvent event)
    {
        if(type == Type.EDIT_ORIGINAL)
            event.getMessage().editMessage(reply).queue();
        else
            event.getChannel().sendMessage(reply).queue();
    }
    
    protected void reply(String reply, MessageReceivedEvent event, Consumer<Message> consumer)
    {
        if(type == Type.EDIT_ORIGINAL)
            event.getMessage().editMessage(reply).queue(consumer);
        else
            event.getChannel().sendMessage(reply).queue(consumer);
    }
    
    protected void reply(MessageEmbed embed, MessageReceivedEvent event)
    {
        if(type == Type.EDIT_ORIGINAL)
            event.getMessage().editMessage(new MessageBuilder().setEmbed(embed).build()).queue();
        else
            event.getChannel().sendMessage(new MessageBuilder().setEmbed(embed).build()).queue();
    }
    
    protected void reply(MessageEmbed embed, MessageReceivedEvent event, Consumer<Message> consumer)
    {
        if(type == Type.EDIT_ORIGINAL)
            event.getMessage().editMessage(new MessageBuilder().setEmbed(embed).build()).queue(consumer);
        else
            event.getChannel().sendMessage(new MessageBuilder().setEmbed(embed).build()).queue(consumer);
    }
    
    protected void failure(MessageReceivedEvent event)
    {
        tempReply(Constants.FAILURE, event);
    }
    
    protected void tempReply(String message, MessageReceivedEvent event)
    {
        RestAction<Message> action;
        if(type == Type.EDIT_ORIGINAL)
            action = event.getMessage().editMessage(message);
        else
            action = event.getChannel().sendMessage(message);
        action.queue(m -> {
            try{Thread.sleep(2000);}catch(Exception e){}
            m.delete().queue();
        });
    }
    
    public enum Type {
        DELETE_AND_RESEND, EDIT_ORIGINAL, KEEP_AND_RESEND
    }
}
