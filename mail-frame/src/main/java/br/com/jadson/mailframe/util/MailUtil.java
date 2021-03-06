package br.com.jadson.mailframe.util;

import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.UUID;

/**
 * Utility class for email send
 */
@Component
public class MailUtil {

    /**
     * Format the text with html style
     * @param subject
     * @param text
     * @param application
     * @param noReply
     * @return
     */
    public String formatEmailText(String subject, String text, String application, UUID id, boolean noReply){

        StringBuilder message  = new StringBuilder("");

        message.append("<html>");
        message.append("<body>");

        if(noReply) {
            message.append("<div style=\"font-weight: bold; padding-top: 30px; padding-bottom: 30px; text-align: center;\"> " +
                    " THIS E-MAIL WAS AUTOMATICALLY GENERATED BY THE APPLICATION \"" + application.toUpperCase() + "\"." +
                    " PLEASE DO NOT REPLY. </div>");
        }

        message.append("<table style=\"width: 90%; margin-left: auto; margin-right: auto; border: 1px solid #343a40;\">");
        message.append("<caption style=\"font-weight: bold; color: #FFFFFF; background-color: #065c90\" > "+subject+" </caption>");

        message.append("<tbody>");
        message.append("<tr>");
            message.append("<td>");
                message.append(text);
            message.append("</td>");
        message.append("</tr>");

        message.append("</tbody>");

        message.append("<tfoot>");
            message.append("<tr>");
                message.append("<td style=\"font-style: italic; font-weight: bold; padding-top: 30px; text-align: center; font-size: small;\">");
                    message.append("We are not responsible for not receiving this email for any technical reason   <br>");
                    message.append("The \""+application.toUpperCase()+"\" does not send emails asking for passwords or personal data <br>");
                message.append("</td>");
            message.append("</tr>");
        message.append("</tfoot>");

        message.append("</table>");

        message.append("<div style=\"padding-top: 30px; padding-bottom: 30px;  text-align: center; width: 90%; font-size: x-small;\"> "+application +" | "+ Year.now().getValue()+" | "+ id +" </div>");

        message.append("</body>");
        message.append("</html>");

        return message.toString();

    }
}
