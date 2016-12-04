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
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class PollCmd extends Command {
    private final int REGIONAL_A = "\uD83C\uDDE6".codePointAt(0);
    public PollCmd()
    {
        this.name = "poll";
        this.description = "makes a poll";
        this.arguments = "<question> or <question>|<option1>|<option2>...";
        this.type = Type.EDIT_ORIGINAL;
    }
    
    @Override
    protected void execute(String args, MessageReceivedEvent event) {
        String[] parts = args.split("\\|");
        if(parts.length==1)
        {
            reply(formatQuestion(args), event, m -> 
            {
                m.addReaction("\uD83D\uDC4D").queue();
                m.addReaction("\uD83D\uDC4E").queue();
            });
        }
        else
        {
            StringBuilder builder = new StringBuilder(formatQuestion(parts[0]));
            for(int i=1; i<parts.length; i++)
            {
                String r = String.copyValueOf(Character.toChars(REGIONAL_A+i-1));
                builder.append("\n").append(r).append(" ").append(parts[i].trim());
            }
            reply(builder.toString(), event, m -> 
            {
                for(int i=1; i<parts.length; i++)
                    m.addReaction(String.copyValueOf(Character.toChars(REGIONAL_A+i-1))).queue();
            });
        }
    }
    
    private static String formatQuestion(String str)
    {
        return "\uD83D\uDDF3 **"+str+"**";
    }
}
