import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LangPatterns {

    public static final String[] VALUES_KEY =  new String[]{"and", "array", "asm", "begin", "case", "const", "constructor", "destructor", "div", "do", "downto", "else", "end", "file", "for", "function", "goto", "if",
            "in", "implementation", "inherited", "inline", "interface", "label", "mod", "nil", "not", "of", "or", "packed", "procedure", "program", "record", "repeat", "set", "shl", "shr", "string", "then", "to",
            "type","unit", "uses", "until", "var", "while", "with", "integer", "real", "boolean", "char", "string", "writeln", "write", "read", "readln", "xor",
    "absolute", "assembler", "external", "far", "forward", "interrupt", "near", "private", "public", "virtual"};  // directives

    public static final Character[] VALUES_PUN = new Character[] {'.',' ', '-','+','=','[',']','\\',
            ';','\'',':','"','<','>', ',','/', '\r', '\n', '\t', '^','&','*','(',')'};
    public static final String[] VALUES_LIT = new String[]{"true", "false", "null"};


    private static Set<String> keywords = new HashSet<String>(Arrays.asList(VALUES_KEY));
    private static Set<Character> punctuation = new HashSet<Character>(Arrays.asList(VALUES_PUN));
    private static Set<String> literals = new HashSet<String>(Arrays.asList(VALUES_LIT));

    public static boolean isKeyword(String word)
    {
        return keywords.contains(word);
    }

    public static boolean isPunctuation(char word)
    {
        return punctuation.contains(word);
    }
    public static boolean isLiteral(String word)
    {
        return literals.contains(word);
    }
}