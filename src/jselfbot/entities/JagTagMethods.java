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
                    eb.setColor(Color.decode(in[0]));
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
