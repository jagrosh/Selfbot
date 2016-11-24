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
public class SetCmd extends Command {
    private final Emojis emojis;
    public SetCmd(Emojis emojis)
    {
        this.emojis = emojis;
        this.name = "set";
        this.description = "sets a :custom: emoji";
        this.arguments = "<name> <replacement>";
    }
    
    @Override
    protected void execute(String args, MessageReceivedEvent event) {
        String[] parts = args.split("\\s+",2);
        if(parts.length<2)
        {
            tempReply("Must include an emoji name and its content", event);
            return;
        }
        emojis.setEmoji(parts[0], parts[1]);
        tempReply("Added emoji `:\u200E"+parts[0]+":` that will convert to `"+parts[1]+"`", event);
    }
    
}
