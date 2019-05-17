import java.util.List;

public class NumberRecognizer {
    StateWrapper curState;
    List tokenList;

    public NumberRecognizer(StateWrapper curState, List tokenList){
        this.curState = curState;
        this.tokenList = tokenList;
    }

    public void endOfLexeme(){
        curState.setState(State.START);
    }
    public void checkEndOfNum(char symbol, String buffer){
        if(LangPatterns.isPunctuation(symbol)){
        //if (symbol == ' '){
           tokenList.add(new Token(TokenType.NUMBER, buffer));
           endOfLexeme();
        }
        else curState.setState(State.ERROR);
    }

    public boolean recognizeNumber(char symbol,  String buffer){
        if(symbol == '.') {
            curState.setState(State.FRACTIONAL_NUM);
            return true;
        }
        if(symbol == 'e' || symbol == 'E') {
            curState.setState(State.NUM_E);
            return true;
        }
        if(!Symbol.isDigit(symbol)){ // check for punctuation symbol
            checkEndOfNum(symbol, buffer);
            return false;
        }
        else return true;
    }

    public boolean recognizeNumE(char symbol, String buffer){
        if(!Symbol.isDigit(symbol)){
            if(symbol == '+' || symbol == '-'){
                curState.setState(State.NUM_E_NEXT); return true;
            }
           else {
               checkEndOfNum(symbol, buffer);
               return false;
           }
        }
        else return true;
    }
    public boolean recognizeNumENext(char symbol, String buffer){
        if(!Symbol.isDigit(symbol)){
            checkEndOfNum(symbol, buffer);
            return false;
        }
        else return true;
    }


    public boolean recognizeFractionalNum(char symbol, String buffer){
        if(Symbol.isDigit(symbol)){ curState.setState(State.FRACTIONAL_NUM_AFTER_DOT); return true;}
        if (symbol == '.'){
                buffer = buffer.substring(0, buffer.length() - 1);
                String str = "..";
                tokenList.add(new Token(TokenType.NUMBER, buffer));
                tokenList.add(new Token(TokenType.PUNCTUATION, str));
                endOfLexeme();
                return true;
        }
        else {
           // checkEndOfNum(symbol, buffer);
            curState.setState(State.ERROR); return true;
        }// check for punctuation symbol
    }

    public boolean recognizeFractionalNumAfterDot(char symbol, String buffer){
        if(!Symbol.isDigit(symbol)){
            if(symbol == 'e' || symbol == 'E') {
                curState.setState(State.NUM_E);
                return true;
            }
            else {
                checkEndOfNum(symbol, buffer);
                return false;
            }
        } // check for punctuation symbol
        else return true;
    }

    public boolean recognizeHexNum(char symbol, String buffer){
        if(!(Symbol.isValidHex(symbol)) || buffer.length()>8){
            checkEndOfNum(symbol, buffer);
            return false;
        }
        else return true;
    }

    public boolean recognizeError(char symbol, String buffer){
        if (LangPatterns.isPunctuation(symbol)){
           tokenList.add(new Token(TokenType.ERROR, buffer));
           endOfLexeme();
           return false;
        }
        else{ curState.setState(State.ERROR);return true;} // curstate remove
    }
}
