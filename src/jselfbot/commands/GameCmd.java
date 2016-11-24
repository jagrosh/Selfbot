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
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class GameCmd extends Command {

    public GameCmd()
    {
        this.name = "setgame";
        this.description = "sets or clears your current game";
        this.arguments = "[game]";
    }
    
    @Override
    protected void execute(String args, MessageReceivedEvent event) {
        String result;
        if(args.isEmpty())
        {
            event.getJDA().getPresence().setGame(null);
            result = "Game cleared.";
        }
        else
        {
            try {
            event.getJDA().getPresence().setGame(Game.of(args));
            result = "Game set to `"+args+"`";
            } catch(Exception e) {
                result = "Game could not be set to `"+args+"`";
            }
        }
        tempReply(result, event);
    }
    
}
