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

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import net.dv8tion.jda.core.utils.SimpleLog;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class Todolist {
    private final List<TodoItem> todolist;
    private final static String FILENAME = "todo.json";
    private final static SimpleLog LOG = SimpleLog.getLog("Todo");
    
    public Todolist()
    {
        todolist = new ArrayList<>();
        JSONArray array;
        try {
            array = new JSONArray(StringUtils.join(Files.readAllLines(Paths.get(FILENAME)), "\n"));
            for(int i=0; i<array.length(); i++)
                todolist.add(new TodoItem(array.getJSONObject(i)));
            LOG.info("Successfully loaded "+todolist.size()+" todo entries!");
        } catch(IOException e) {
            LOG.warn(FILENAME+" was not found! This can be ignored if this is the first time running this.");
        } catch(JSONException e) {
            LOG.fatal("The todo file, "+FILENAME+" is corrupted. Please fix the file before editing the todo list or the file will be overwritten.");
        }
    }
    
    private void write()
    {
        JSONArray array = new JSONArray();
        todolist.stream().forEach(todoitem -> array.put(todoitem.toJSON()));
        try (Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILENAME), "UTF-8"))) {
            out.write(array.toString(2));
        }catch(IOException e) {
            LOG.fatal("Failed to save todolist to "+FILENAME);
        }
    }
    
    public void addItem(String content)
    {
        todolist.add(new TodoItem(content));
        write();
    }
    
    public void editItem(int index, String content)
    {
        todolist.get(index).editContent(content);
        write();
    }
    
    public TodoItem remove(int index)
    {
        TodoItem item = todolist.remove(index);
        write();
        return item;
    }
    
    public void complete(int index)
    {
        todolist.get(index).complete();
        write();
    }
    
    public List<TodoItem> getList()
    {
        return todolist;
    }
    
    public int clean()
    {
        List<TodoItem> del = todolist.stream().filter(item -> item.completed).collect(Collectors.toList());
        todolist.removeAll(del);
        write();
        return del.size();
    }
    
    public class TodoItem {
        private String content;
        private boolean completed;
        private TodoItem(String content)
        {
            this.content=content;
            this.completed = false;
        }
        private TodoItem(JSONObject obj)
        {
            this.content = obj.getString("c");
            this.completed = obj.getBoolean("d");
        }
        private void editContent(String newcontent)
        {
            this.content = newcontent;
        }
        private void complete()
        {
            this.completed = true;
        }
        private JSONObject toJSON()
        {
            return new JSONObject().put("c",content).put("d", completed);
        }
        public String getContent()
        {
            return content;
        }
        @Override
        public String toString()
        {
            return (completed ? "~~" : "") + content + (completed ? "~~" : "");
        }
    }
}
