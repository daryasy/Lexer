import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Output {

    public static void generateHtml(String path, @NotNull List<Token> tokenList){
        String defaultHtml = "<html><head>"
                +"<link href =\"style.css\" rel=\"stylesheet\" type=\"text/css\">"
                + "</head><body><div>";
        for (Token token : tokenList){
            if (token.getTokenType() == TokenType.PUNCTUATION && token.getContent().equals("\\n"))
                defaultHtml += "</div>\n<div>";
            else
                defaultHtml += String.format("<p class=\"%s\">%s</p>", token.getTokenType(), token.getContent());
        }
        defaultHtml += "</div></body></html>";

        File file = new File(path);
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(defaultHtml);
            fileWriter.close();

        } catch (IOException e) {
            System.err.println("File error:" + e);
        }
    }
}
