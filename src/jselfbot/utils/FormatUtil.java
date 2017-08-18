/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jselfbot.utils;

import java.util.List;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class FormatUtil
{
    
    public static String join(List<String> strings, String joiner)
    {
        if(strings.isEmpty())
            return "";
        StringBuilder sb = new StringBuilder(strings.get(0));
        for(int i=1; i<strings.size(); i++)
            sb.append(joiner).append(strings.get(i));
        return sb.toString();
    }
}
