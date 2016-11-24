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

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import jselfbot.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class EvalCmd extends Command {

    public EvalCmd()
    {
        this.name = "eval";
        this.description = "evaluates using Nashorn";
        this.arguments = "<script>";
        this.type = Type.EDIT_ORIGINAL;
    }
    
    @Override
    protected void execute(String args, MessageReceivedEvent event) {
        ScriptEngine se = new ScriptEngineManager().getEngineByName("Nashorn");
        se.put("event", event);
        se.put("jda", event.getJDA());
        se.put("guild", event.getGuild());
        se.put("channel", event.getChannel());
        try
        {
            reply("```java\n"+args+" ```"+"Evaluated Successfully:\n```\n"+se.eval(args)+" ```", event);
        } 
        catch(Exception e)
        {
            reply("```java\n"+args+" ```"+"An exception was thrown:\n```\n"+e+" ```", event);
        }
    }
    
}
