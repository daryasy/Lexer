public class Symbol {
    public static boolean isLetter(char c){
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <='Z') || c == '_';
    }

    public static boolean isDigit(char c){
        return (c >= '0' && c <= '9');
    }

    public static boolean isSomeOperator(char c){
        return (c == '#'
        || c == '@' || c == '^' ); //for pointers
    }

    public static boolean isValidHex(char c){
        return (c >= 'a' && c <= 'f') ||
                (c >= 'A' && c <='F') ||
                (c >= '0' && c <='9') ;
    }


}
