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

import java.util.List;
import jselfbot.commands.*;
import jselfbot.emojis.Emojis;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class Bot extends ListenerAdapter {
    private final String prefix;
    private final String prefixLower;
    private final Command[] commands;
    private final Emojis emojis;
    
    public Bot(List<String> config)
    {
        prefix = config.get(1);
        prefixLower = prefix.toLowerCase();
        emojis = new Emojis();
        commands = new Command[]{
            new ClearCmd(),
            new DeleteCmd(emojis),
            new EvalCmd(),
            new GameCmd(),
            new ListCmd(emojis),
            new MeCmd(),
            new QuoteCmd(),
            new SetCmd(emojis)
        };
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // the selfbot only replies to the user account it is running on
        if (!event.getAuthor().equals(event.getJDA().getSelfUser()))
            return;

        boolean isCommand = false;
        if(event.getMessage().getRawContent().toLowerCase().startsWith(prefixLower))
        {
            String[] parts = event.getMessage().getRawContent()
                    .substring(prefixLower.length()).trim().split("\\s+",2);
            if(parts.length<2)
                parts = new String[]{parts[0], ""};
            
            if("help".equalsIgnoreCase(parts[0]))
            {
                event.getMessage().deleteMessage().queue();
                EmbedBuilder builder = new EmbedBuilder();
                builder.setAuthor(event.getJDA().getSelfUser().getName()+" Self-Help", null, 
                        event.getJDA().getSelfUser().getAvatarUrl()==null ? event.getJDA().getSelfUser().getDefaultAvatarUrl() : event.getJDA().getSelfUser().getAvatarUrl());
                if(event.getGuild()!=null)
                    builder.setColor(event.getGuild().getSelfMember().getColor());
                StringBuilder sbuilder = new StringBuilder("Commands:");
                for(Command command: commands)
                {
                    sbuilder.append("\n`").append(prefix).append(command.name);
                    if(command.arguments!=null)
                        sbuilder.append(" ").append(command.arguments);
                    sbuilder.append("` ").append(command.description);
                }
                builder.setDescription(sbuilder.toString());
                event.getChannel().sendMessage(new MessageBuilder().setEmbed(builder.build()).build()).queue();
            }
            else
                for(Command command : commands)
                    if(command.name.equalsIgnoreCase(parts[0]))
                    {
                        isCommand = true;
                        command.run(parts[1], event);
                    }
        }
        if(!isCommand)
        {
            StringBuilder builder = new StringBuilder();
            String content = event.getMessage().getRawContent();
            while(true)
            {
                int index1 = content.indexOf(":");
                int index2 = content.indexOf(":", index1+1);
                if(index2==-1)
                    break;
                String emoji = emojis.getEmoji(content.substring(index1+1, index2));
                if(emoji==null)
                {
                    builder.append(content.substring(0, index2));
                    content = content.substring(index2);
                }
                else
                {
                    builder.append(content.substring(0, index1));
                    builder.append(emoji);
                    content = content.substring(index2+1);
                }
            }
            builder.append(content);
            content = builder.toString();
            if(!content.equals(event.getMessage().getRawContent()))
                event.getMessage().editMessage(content).queue();
        }
    }
    
}
