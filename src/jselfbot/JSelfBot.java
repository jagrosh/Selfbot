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

import javax.security.auth.login.LoginException;
import jselfbot.entities.Config;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class JSelfBot {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        Config config;
        try{
            config = new Config();
        } catch(Exception e) {
            LoggerFactory.getLogger("Config").error(""+e);
            return;
        }
        try {
            new JDABuilder(config.isForBot() ? AccountType.BOT : AccountType.CLIENT)
                    .setToken(config.getToken())
                    .addEventListener(new Bot(config))
                    .setStatus(config.getStatus())
                    .setIdle(true)
                    .buildAsync();
        } catch(LoginException | IllegalArgumentException | RateLimitedException e) {
            LoggerFactory.getLogger("Config").error(""+e);
        }
    }
    
}
