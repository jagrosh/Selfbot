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
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class DiscrimCmd extends Command {

    public DiscrimCmd()
    {
        this.name = "discrim";
        this.description = "finds users with your/a discrim";
        this.arguments = "[discrim]";
        this.type = Type.EDIT_ORIGINAL;
    }
    
    @Override
    protected void execute(String args, MessageReceivedEvent event) {
        String discrim;
        if(args==null || args.isEmpty())
            discrim = event.getJDA().getSelfUser().getDiscriminator();
        else if (!args.matches("\\d{4}"))
        {
            tempReply("Invalid discrim! (Must be a 4-digit number)", event);
            return;
        }
        else
            discrim = args;
        StringBuilder builder = new StringBuilder();
        event.getJDA().getUsers().stream().filter(u -> u.getDiscriminator().equals(discrim)).forEach(u -> {
            if(builder.length()<1940)
                builder.append("`").append(u.getName()).append("` ");
        });
        if(builder.length()==0)
            tempReply("No users found with discrim #"+discrim, event);
        else
            reply("Users with discrim #"+discrim+":\n"+builder.toString(), event);
    }
    
}
