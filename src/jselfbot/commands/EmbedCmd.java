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
import jselfbot.Constants;
import jselfbot.entities.JagTagMethods;
import me.jagrosh.jagtag.Parser;
import me.jagrosh.jagtag.ParserBuilder;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class EmbedCmd extends Command {

    private final Parser parser;
    public EmbedCmd()
    {
        this.name = "embed";
        this.description = "puts text in an embed";
        this.arguments = "<stuff>";
        this.parser = new ParserBuilder()
                .addMethods(JagTagMethods.getMethods())
                .setMaxOutput(2048)
                .setMaxIterations(1000)
                .build();
    }
    
    @Override
    protected void execute(String args, MessageReceivedEvent event) {
        if(args==null || args.isEmpty())
        {
            reply("Embeds:\n"
                    + "`{title:`TEXT`|`URL`}` or `{title:`TEXT`}`\n"
                    + "`{author:`NAME`|`IMAGE`|`URL`}` or `{author:`NAME`|`IMAGE`}` or `{author:`NAME`}`\n"
                    + "`{thumbnail:`IMAGE`}`\n"
                    + "`{field:`NAME`|`VALUE`|`true/false`}` or `{field:`NAME`|`VALUE`}` *can include multiple fields\n"
                    + "`{image:`IMAGE`}`\n"
                    + "`{color:`#HEX`}`\n"
                    + "`{footer:`TEXT`|`IMAGE`}` or `{footer:`TEXT`}`\n"
                    + "`{timestamp:`ISO`}` or `{timestamp}` *current time if nothing included\n"
                    + "Any remaining text goes into the description", event);
            return;
        }
        EmbedBuilder builder = new EmbedBuilder();
        if(event.getGuild()!=null)
            builder.setColor(event.getGuild().getSelfMember().getColor());
        parser.put("builder", builder);
        try {
            String descr = parser.parse(args).trim();
            if(!descr.isEmpty())
                builder.setDescription(descr);
            reply(builder.build(), event);
        } catch(Exception e) {
            reply(Constants.FAILURE+" Error: "+e, event);
        }
    }
    
}
