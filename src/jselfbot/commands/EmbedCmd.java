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
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class EmbedCmd extends Command {

    public EmbedCmd()
    {
        this.name = "embed";
        this.description = "puts text in an embed";
        this.arguments = "<stuff>";
    }
    
    @Override
    protected void execute(String args, MessageReceivedEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        if(event.getGuild()!=null)
            builder.setColor(event.getGuild().getSelfMember().getColor());
        builder.setDescription(args);
        reply(builder.build(), event);
    }
    
}
