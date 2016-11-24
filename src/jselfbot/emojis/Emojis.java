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
package jselfbot.emojis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import net.dv8tion.jda.core.utils.SimpleLog;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class Emojis {
    private final HashMap<String, String> emojis;
    private final static String FILENAME = "emojis.json";
    private final static SimpleLog LOG = SimpleLog.getLog("Emojis");
    
    public Emojis()
    {
        emojis = new HashMap<>();
        JSONObject obj;
        try {
            obj = new JSONObject(new String(Files.readAllBytes(Paths.get(FILENAME))));
            obj.keySet().stream().forEach(name -> emojis.put(name.toLowerCase(), obj.getString(name)));
            LOG.info("Successfully loaded "+emojis.keySet().size()+" custom emojis!");
        } catch(IOException e) {
            LOG.warn(FILENAME+" was not found! This can be ignored if this is the first time running this.");
        } catch(JSONException e) {
            LOG.fatal("The emojis file, "+FILENAME+" is corrupted. Please fix the file before adding emojis or the file will be overwritten.");
        }
    }
    
    private void write()
    {
        JSONObject obj = new JSONObject();
        emojis.keySet().stream().forEach(name -> obj.put(name, emojis.get(name)));
        try {
            Files.write(Paths.get(FILENAME), obj.toString(2).getBytes());
        } catch(IOException e) {
            LOG.fatal("Failed to save emojis to "+FILENAME);
        }
    }
    
    public void setEmoji(String name, String content)
    {
        emojis.put(name.toLowerCase(), content);
        write();
    }
    
    public boolean deleteEmoji(String name)
    {
        boolean removed = emojis.remove(name.toLowerCase())!=null;
        if(removed)
            write();
        return removed;
    }
    
    public String getEmoji(String name)
    {
        return emojis.get(name.toLowerCase());
    }
    
    public Collection<String> getEmojiList()
    {
        return emojis.keySet();
    }
}
