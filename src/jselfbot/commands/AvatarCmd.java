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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jselfbot.Command;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.impl.JDAImpl;
import net.dv8tion.jda.core.entities.impl.UserImpl;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class AvatarCmd extends Command {

    public AvatarCmd()
    {
        this.name = "avatar";
        this.description = "shows a user's (or your) avatar";
        this.arguments = "[user]";
        this.type = Type.EDIT_ORIGINAL;
    }
    
    @Override
    protected void execute(String args, MessageReceivedEvent event) {
        if(args==null || args.isEmpty())
        {
            reply(event.getJDA().getSelfUser().getEffectiveAvatarUrl()+"?size=1024", event);
        }
        else
        {
            Guild g = event.getJDA().getGuildById(args);
            if(g!=null)
            {
                reply(g.getIconUrl()+"?size=1024"+(g.getSplashId()==null ? "" : "\n"+g.getSplashUrl()+"?size=1024"),event);
                return;
            }
            List<User> users = findUsers(args, event.getJDA());
            if(users.isEmpty())
                tempReply("No users found!", event);
            else
                reply(users.get(0).getEffectiveAvatarUrl()+"?size=1024", event);
        }
    }
    
    public final static String USER_MENTION = "<@!?(\\d+)>";
    public final static String DISCORD_ID = "\\d{17,22}";
    
    public static List<User> findUsers(String query, JDA jda)
    {
        String id;
        String discrim = null;
        if(query.matches(USER_MENTION))
        {
            id = query.replaceAll(USER_MENTION, "$1");
            if(id.equals("1"))
            {
                UserImpl clyde = new UserImpl("1", (JDAImpl) jda);
                clyde.setDiscriminator("0000");
                clyde.setBot(true);
                clyde.setName("Clyde");
                return Collections.singletonList(clyde);
            }
            User u = jda.getUserById(id);
            if(u!=null)
                return Collections.singletonList(u);
        }
        else if(query.matches(DISCORD_ID))
        {
            id = query;
            User u = jda.getUserById(id);
            if(u!=null)
                return Collections.singletonList(u);
        }
        else if(query.matches("^.*#\\d{4}$"))
        {
            discrim = query.substring(query.length()-4);
            query = query.substring(0,query.length()-5).trim();
        }
        ArrayList<User> exact = new ArrayList<>();
        ArrayList<User> wrongcase = new ArrayList<>();
        ArrayList<User> startswith = new ArrayList<>();
        ArrayList<User> contains = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        for(User u: jda.getUsers())
        {
            if(discrim!=null && !u.getDiscriminator().equals(discrim))
                continue;
            if(u.getName().equals(query))
                exact.add(u);
            else if (exact.isEmpty() && u.getName().equalsIgnoreCase(query))
                wrongcase.add(u);
            else if (wrongcase.isEmpty() && u.getName().toLowerCase().startsWith(lowerQuery))
                startswith.add(u);
            else if (startswith.isEmpty() && u.getName().toLowerCase().contains(lowerQuery))
                contains.add(u);
        }
        if(!exact.isEmpty())
            return exact;
        if(!wrongcase.isEmpty())
            return wrongcase;
        if(!startswith.isEmpty())
            return startswith;
        return contains;
    }
}
