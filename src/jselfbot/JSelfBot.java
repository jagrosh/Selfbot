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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.utils.SimpleLog;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class JSelfBot {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        /**
         * Config:
         * Line 1 - User Token
         * Line 2 - Prefix
         */
        try{
            List<String> config = Files.readAllLines(Paths.get("config.txt"));
            if(config.size()<2)
                throw new Exception("You must provide a token and a prefix!");
            new JDABuilder(AccountType.CLIENT)
                    .setToken(config.get(0))
                    .addListener(new Bot(config))
                    .setStatus(OnlineStatus.IDLE)
                    .buildAsync();
        } catch(Exception e) {
            SimpleLog.getLog("Login").fatal(e);
        }
    }
    
}
