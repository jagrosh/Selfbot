/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jselfbot.entities;

import java.awt.Color;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collection;
import me.jagrosh.jagtag.Method;
import net.dv8tion.jda.core.EmbedBuilder;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class JagTagMethods {
    
    public static Collection<Method> getMethods()
    {
        return Arrays.asList(
                new Method("title", (env, in) -> {
                    EmbedBuilder eb = env.get("builder");
                    String[] parts = in[0].split("\\|",2);
                    eb.setTitle(parts[0], parts.length>1 ? parts[1] : null);
                    return "";}),
                
                new Method("author", (env, in) -> {
                    EmbedBuilder eb = env.get("builder");
                    String[] parts = in[0].split("\\|",3);
                    eb.setAuthor(parts[0], parts.length>2 ? parts[2] : null, parts.length>1 ? parts[1] : null);
                    return "";}),
                
                new Method("thumbnail", (env, in) -> {
                    EmbedBuilder eb = env.get("builder");
                    eb.setThumbnail(in[0]);
                    return "";}),
                
                new Method("field", (env, in) -> {
                    EmbedBuilder eb = env.get("builder");
                    String[] parts = in[0].split("\\|",3);
                    eb.addField(parts[0], parts[1], parts.length>2 ? parts[2].equalsIgnoreCase("true") : true);
                    return "";}),
                
                new Method("image", (env, in) -> {
                    EmbedBuilder eb = env.get("builder");
                    eb.setImage(in[0]);
                    return "";}),
                
                new Method("color", (env, in) -> {
                    EmbedBuilder eb = env.get("builder");
                    switch(in[0].toLowerCase()) {
                        //standard
                        case "red": eb.setColor(Color.RED); break;
                        case "orange": eb.setColor(Color.ORANGE); break;
                        case "yellow": eb.setColor(Color.YELLOW); break;
                        case "green": eb.setColor(Color.GREEN); break;
                        case "cyan": eb.setColor(Color.CYAN); break;
                        case "blue": eb.setColor(Color.BLUE); break;
                        case "magenta": eb.setColor(Color.MAGENTA); break;
                        case "pink": eb.setColor(Color.PINK); break;
                        case "black": eb.setColor(Color.decode("#000001")); break;
                        case "dark_gray": 
                        case "dark_grey": eb.setColor(Color.DARK_GRAY); break;
                        case "gray":
                        case "grey": eb.setColor(Color.GRAY); break;
                        case "light_gray":
                        case "light_grey": eb.setColor(Color.LIGHT_GRAY); break;
                        case "white": eb.setColor(Color.WHITE); break;
                        //discord
                        case "blurple": eb.setColor(Color.decode("#7289DA")); break;
                        case "greyple": eb.setColor(Color.decode("#99AAB5")); break;
                        case "darktheme": eb.setColor(Color.decode("#2C2F33")); break;
                        default: eb.setColor(Color.decode(in[0]));
                    }
                    return "";}),
                
                new Method("footer", (env, in) -> {
                    EmbedBuilder eb = env.get("builder");
                    String[] parts = in[0].split("\\|",2);
                    eb.setFooter(parts[0], parts.length>1 ? parts[1] : null);
                    return "";}),
                
                new Method("timestamp", (env) -> {
                    EmbedBuilder eb = env.get("builder");
                    eb.setTimestamp(OffsetDateTime.now());
                    return "";}, (env, in) -> {
                    EmbedBuilder eb = env.get("builder");
                    eb.setTimestamp(OffsetDateTime.parse(in[0]));
                    return "";})
        );
    }
}
