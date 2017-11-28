/*
 * Copyright (C) 2017 Artu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package jselfbot.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import jselfbot.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runs a shell command.
 * @author Spotlight
 * @author Artu
 */
public class BashCmd extends Command {
    
    private final static Logger LOG = LoggerFactory.getLogger("Bash");
    public BashCmd() {
        this.name = "bash";
        this.description = "run a command on a shell";
    }

    @Override
    protected void execute(String args, MessageReceivedEvent event) {
        if(args.isEmpty()) {
            reply("Cannot execute a empty command!", event);
            return;
        }

        StringBuilder output = new StringBuilder();
        String finalOutput;
        try {
            ProcessBuilder builder = new ProcessBuilder(args.split(" "));
            Process p = builder.start();

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String runningLineOutput;
            while ((runningLineOutput = reader.readLine())!= null) {
                output.append(runningLineOutput).append("\n");
            }

            if (output.toString().isEmpty()) {
                reply("Done, with no output!", event);
                return;
            }

            // Remove linebreak
            finalOutput = output.substring(0, output.length() - 1);
            reader.close();
        } catch (IOException e) {
            reply("I wasn't able to find the command `" + args + "`!", event);
            return;
        } catch (Exception e) {
            LOG.warn("An unknown error occurred!");
            e.printStackTrace();
            reply("An unknown error occurred! Check the bot console.", event);
            return;
        }

        // Actually send
        try {
            reply("Input: ```\n" + args + "``` Output: \n```\n" + finalOutput + "```", event);
        } catch (IllegalArgumentException e) {
            LOG.info("Input: " + args + "\nOutput: " + finalOutput);
            reply("Command output too long! Output sent in console.", event);
        }
    }
}
