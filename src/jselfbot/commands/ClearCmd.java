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

import java.util.List;
import java.util.stream.Collectors;
import jselfbot.Command;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class ClearCmd extends Command {

    public ClearCmd()
    {
        this.name = "clear";
        this.description = "deletes self messages within limit";
        this.arguments = "<number> (default 100, max 100)";
    }
    
    @Override
    protected void execute(String args, MessageReceivedEvent event) {
        int num = 100;
        try {
        if(!args.isEmpty())
            num = Integer.parseInt(args);
        } catch(NumberFormatException e) {
            tempReply("`"+args+"` is not a valid number between 1 and 100.", event);
            return;
        }
        if(num<1 || num>100)
        {
            tempReply("`"+args+"` is not a valid number between 1 and 100.", event);
            return;
        }
        try {
            event.getChannel().getHistory().retrievePast(num==100 ? 100 : num+1).queue(success -> {
                List<Message> list = success.stream().filter(m -> event.getJDA().getSelfUser().equals(m.getAuthor()) && !event.getMessage().equals(m)).collect(Collectors.toList());
                list.forEach(m -> m.deleteMessage().queue());
                tempReply("Deleting "+list.size()+" messages.", event);
            }, failure -> {
                tempReply("Failed retrieve messages.", event);
            });
        } catch(Exception e) {
            tempReply("Could not retrieve messages.", event);
        }
    }
    
}
