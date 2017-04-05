/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jselfbot.commands;

import jselfbot.Command;
import jselfbot.entities.Todolist;
import jselfbot.entities.Todolist.TodoItem;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class TodoCmd extends Command {

    private final Todolist todo;
    public TodoCmd(Todolist todo)
    {
        this.todo = todo;
        this.name = "todo";
        this.description = "manages your todo list";
        this.type = Type.DELETE_AND_RESEND;
    }
    
    @Override
    protected void execute(String args, MessageReceivedEvent event) {
        if(args==null || args.isEmpty() || args.equalsIgnoreCase("help"))
        {
            EmbedBuilder builder = new EmbedBuilder();
            if(event.getGuild()!=null)
                builder.setColor(event.getGuild().getSelfMember().getColor());
            builder.setAuthor("\uD83D\uDCDD Todo Help:", null, null);
            builder.setDescription("`todo help` this message"
                    + "\n`todo list` views your todo list"
                    + "\n`todo add <content>` adds a new item"
                    + "\n`todo edit <number> <newcontent>` edits an item"
                    + "\n`todo checkoff <number>` marks an item as completed"
                    + "\n`todo delete <number>` removes an item from the list"
                    + "\n`todo clean` removes all completed items from the list");
            reply(builder.build(), event);
            return;
        }
        String[] parts = args.split("\\s+", 2);
        switch(parts[0].toLowerCase()) {
            case "list":
                if(todo.getList().isEmpty())
                    tempReply("Your todo list is empty!", event);
                else
                {
                    EmbedBuilder builder = new EmbedBuilder();
                    if(event.getGuild()!=null)
                        builder.setColor(event.getGuild().getSelfMember().getColor());
                    builder.setAuthor("\uD83D\uDCDD Todo List:", null, null);
                    
                    StringBuilder sbuilder = new StringBuilder();
                    for(int i=0; i<todo.getList().size(); i++)
                    {
                        String next = "\n`"+(i+1)+".` "+todo.getList().get(i).toString();
                        if(sbuilder.length()+next.length()>2000)
                        {
                            reply(builder.setDescription(sbuilder.toString().trim()).build(),event);
                            builder.setAuthor(null,null,null);
                            sbuilder = new StringBuilder();
                        }
                        sbuilder.append(next);
                    }
                    builder.setDescription(sbuilder.toString().trim());
                    reply(builder.build(), event);
                }
                break;
            case "remove":
            case "delete":
                if(parts.length<2)
                    tempReply("Please include a number to remove!", event);
                else try {
                    int index = Integer.parseInt(parts[1]);
                    if(index<1 || index>todo.getList().size())
                        tempReply("Index cannot be less than 1 nor greater than "+todo.getList().size(),event);
                    else
                    {
                        String content = todo.remove(index-1).getContent();
                        tempReply("Completed item "+index+" (`"+content+"`)",event);
                    }
                } catch(NumberFormatException e) {
                    tempReply("`"+parts[1]+"` is not a valid integer", event);
                }
                break;
            case "checkoff":
            case "check":
                if(parts.length<2)
                    tempReply("Please include a number to checkoff!", event);
                else try {
                    int index = Integer.parseInt(parts[1]);
                    if(index<1 || index>todo.getList().size())
                        tempReply("Index cannot be less than 1 nor greater than "+todo.getList().size(),event);
                    else
                    {
                        todo.complete(index-1);
                        tempReply("Completed item "+index+" (`"+todo.getList().get(index-1).getContent()+"`)",event);
                    }
                } catch(NumberFormatException e) {
                    tempReply("`"+parts[1]+"` is not a valid integer", event);
                }
                break;
            case "add":
                if(parts.length<2)
                    tempReply("Please include content for the todo item!", event);
                else
                {
                    todo.addItem(parts[1]);
                    tempReply("Added `"+parts[1]+"` to the todo list!", event);
                }
                break;
            case "edit":
                if(parts.length<2)
                    tempReply("Please include a todo item number and the new content", event);
                else
                {
                    String[] parts2 = parts[1].split("\\s+", 2);
                    if(parts2.length<2)
                        tempReply("Please include a todo item number and the new content", event);
                    else
                    {
                        try {
                            int index = Integer.parseInt(parts2[0]);
                            if(index<1 || index>todo.getList().size())
                                tempReply("Index cannot be less than 1 nor greater than "+todo.getList().size(),event);
                            else
                            {
                                todo.editItem(index-1, parts2[1]);
                                tempReply("Edited item "+index+" to `"+parts2[1]+"`",event);
                            }
                        } catch(NumberFormatException e) {
                            tempReply("`"+parts2[0]+"` is not a valid integer", event);
                        }
                    }
                }
                break;
            case "clean":
                tempReply("Removed "+todo.clean()+" completed items from the todo list!", event);
                break;
            default:
                tempReply("Unknown todo command `"+parts[0]+"`; see `todo help` for commands.", event);
        }
    }
    
}
