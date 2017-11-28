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
package jselfbot.entities;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.util.List;
import net.dv8tion.jda.core.OnlineStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class Config {
    private final Logger LOG = LoggerFactory.getLogger("Config");
    private String userToken;
    private String prefix;
    private ZoneId zone = null;
    private OnlineStatus status;
    private boolean bot = false;
    
    public Config() throws Exception
    {
        List<String> lines = Files.readAllLines(Paths.get("config.txt"));
        for(String str : lines)
        {
            String[] parts = str.split("=",2);
            String key = parts[0].trim().toLowerCase();
            String value = parts.length>1 ? parts[1].trim() : null;
            switch(key) 
            {
                case "token":
                    userToken = value;
                    break;
                case "prefix":
                    if(value==null)
                    {
                        prefix = "";
                        LOG.warn("The prefix was defined as empty!");
                    }
                    else
                        prefix = value;
                    break;
                case "timezone":
                    if(value==null)
                    {
                        zone = ZoneId.systemDefault();
                        LOG.warn("An empty timezone was provided; using the system default!");
                    }
                    else
                    {
                        try {
                            zone = ZoneId.of(value);
                        } catch(Exception e) {
                            zone = ZoneId.systemDefault();
                            LOG.warn("\""+value+"\" is not a valid timezone; using the system default!");
                        }
                    }
                    break;
                case "status":
                    status = OnlineStatus.fromKey(value);
                    if(status == OnlineStatus.UNKNOWN)
                    {
                        status = OnlineStatus.IDLE;
                        LOG.warn("\""+value+"\" is not a valid status; using the default IDLE! Valid statuses are ONLINE, IDLE, DND, and INVISIBLE.");
                    }
                    break;
                case "bot":
                    bot = "true".equalsIgnoreCase(value);
            }
        }
        if(userToken==null)
            throw new Exception("No token provided int he config file!");
        if(prefix==null)
            throw new Exception("No prefix provided in the config file!");
        if(zone==null)
        {
            zone = ZoneId.systemDefault();
            LOG.warn("No timezone provided; using the system default!");
        }
        if(status==null)
        {
            status = OnlineStatus.IDLE;
            LOG.warn("No status provided; using IDLE!");
        }
    }
    
    public String getToken()
    {
        return userToken;
    }
    
    public String getprefix()
    {
        return prefix;
    }
    
    public ZoneId getZoneId()
    {
        return zone;
    }
    
    public OnlineStatus getStatus()
    {
        return status;
    }
    
    public boolean isForBot()
    {
        return bot;
    }
}
