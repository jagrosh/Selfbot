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
import jselfbot.Command;
import jselfbot.entities.GoogleSearcher;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class GoogleCmd extends Command {
    private final GoogleSearcher google;
    public GoogleCmd()
    {
        google = new GoogleSearcher();
        this.name = "google";
        this.description = "search google";
        this.arguments = "[num] <query>";
        this.type = Type.EDIT_ORIGINAL;
    }
    
    @Override
    protected void execute(String args, MessageReceivedEvent event) {
        String[] parts = args.split("\\s+", 2);
        int num = 1;
        String query;
        if(parts.length >1 && parts[0].matches("\\d+"))
        {
            num = Integer.parseInt(parts[0]);
            query = parts[1];
        }
        else
            query = args;
        if(num<1 || num>10)
        {
            tempReply("Can only search 1 to 10 results at once!", event);
            return;
        }
        ArrayList<String> results = google.getDataFromGoogle(query);
        if(results == null)
        {
            tempReply("Error, could not search google", event);
        }
        else if(results.isEmpty())
        {
            tempReply("No results found for `"+query+"`", event);
        }
        else
        {
            StringBuilder builder = new StringBuilder("`"+query+"` \uD83D\uDD0E "+results.get(0));
            if(num>1 && results.size()>1)
            {
                builder.append("\nSee also:");
                for(int i=1; i<num && i<results.size(); i++)
                    builder.append("\n<").append(results.get(i)).append(">");
            }
            reply(builder.toString(), event);
        }
    }
    
}
