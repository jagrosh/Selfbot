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

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import jselfbot.Command;
import jselfbot.entities.AnimatedGifEncoder;
import jselfbot.utils.WrapUtil;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class SpoilerCmd extends Command {
    private final Color DARKTHEME = new Color(54, 57, 62);
    private final Color HOVERCOLOR = new Color(155,155,155);
    private final Color TEXTCOLOR = Color.WHITE;
    private final Font FONT = new Font("Arial", Font.PLAIN, 15);
    private final int BUFFER = 3;
    public SpoilerCmd()
    {
        this.name = "spoiler";
        this.description = "creates a spoiler image";
        this.arguments = "<spoiler text>";
        this.type = Type.DELETE_AND_RESEND;
    }
    
    @Override
    protected void execute(String input, MessageReceivedEvent event) {
        if(input==null || input.isEmpty())
        {
            tempReply("Please include spoiler text", event);
            return;
        }
        try{
            Canvas c = new Canvas();
            List<String> lines = WrapUtil.wrap(input.replace("\n", " "), c.getFontMetrics(FONT), 400-(2*BUFFER)-2);
            if(lines.size()>8)
            {
                tempReply("Too many lines (>8)", event);
                return;
            }
            BufferedImage text = new BufferedImage(400,lines.size()*(FONT.getSize()+BUFFER)+2*BUFFER+2,BufferedImage.TYPE_INT_RGB);
            BufferedImage hover = new BufferedImage(400,lines.size()*(FONT.getSize()+BUFFER)+2*BUFFER+2,BufferedImage.TYPE_INT_RGB);
            Graphics2D textG = text.createGraphics();
            Graphics2D hoverG = hover.createGraphics();
            textG.setFont(FONT);
            hoverG.setFont(FONT);
            textG.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            hoverG.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            textG.setColor(DARKTHEME);
            hoverG.setColor(DARKTHEME);
            textG.fillRect(1, 1, text.getWidth()-2, text.getHeight()-2);
            hoverG.fillRect(1, 1, text.getWidth()-2, text.getHeight()-2);
            textG.setColor(TEXTCOLOR);
            hoverG.setColor(HOVERCOLOR);
            for(int i=0; i<lines.size(); i++)
                textG.drawString(lines.get(i), BUFFER+1, (i+1)*(FONT.getSize()+BUFFER)-1);//text.getHeight()-BUFFER);
            hoverG.drawString("[ Hover to View Spoiler ]", BUFFER+1, FONT.getSize()+BUFFER-1);
            //ImageIO.write(text,"png", new File("text.png"));
            //ImageIO.write(hover,"png", new File("hover.png"));
            AnimatedGifEncoder e = new AnimatedGifEncoder();
            e.setRepeat(0);
            e.start("spoiler.gif");
            e.setDelay(1);
            e.addFrame(hover);
            e.setDelay(60000);
            e.addFrame(text);
            e.setDelay(60000);
            e.finish();
            event.getChannel().sendFile(new File("spoiler.gif"), null).queue();
            textG.dispose();
            hoverG.dispose();
        }catch(IOException e){
            tempReply("Failed to generate spoiler image", event);
            //e.printStackTrace();
        }
    }
    
}
