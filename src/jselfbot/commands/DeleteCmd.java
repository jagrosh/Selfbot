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
import jselfbot.emojis.Emojis;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class DeleteCmd extends Command {
    private final Emojis emojis;
    public DeleteCmd(Emojis emojis)
    {
        this.emojis = emojis;
        this.name = "delete";
        this.description = "deletes a :custom: emoji";
        this.arguments = "<name>";
    }
    
    @Override
    protected void execute(String args, MessageReceivedEvent event) {
        if(args.isEmpty())
        {
            tempReply("Please include an emoji name.", event);
            return;
        }
        String[] parts = args.split("\\s+",2);
        boolean deleted = emojis.deleteEmoji(parts[0]);
        if(deleted)
            tempReply("Deleted emoji `:\u200E"+parts[0]+":`", event);
        else
            tempReply("Emoji `"+parts[0]+"` was not found.", event);
    }
    
}
