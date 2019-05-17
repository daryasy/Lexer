import java.util.List;

 class OperatorRecognizer {
    private StateWrapper curState;
    private List tokenList;

    OperatorRecognizer (StateWrapper curState, List tokenList){
        this.curState = curState;
        this.tokenList = tokenList;
    }

     private void endOfLexeme(){
        curState.setState(State.START);
    }

     boolean recognizeCompilerDirective(char symbol, String buffer){
        if(symbol == '$') {
            curState.setState(State.COMPILER_DIRECTIVE_NEXT);
            return true;
        }
        else {
            curState.setState(State.COMMENT_2);
            return false;}
    }

     boolean recognizeCompilerDirectiveNext(char symbol, String buffer, boolean endOfFile){
        if(symbol == '}'){
            buffer += symbol;
            tokenList.add(new Token(TokenType.DIRECTIVE, buffer));
            endOfLexeme();
        }
        if(Symbol.isDigit(symbol) && buffer.length()==2){
            curState.setState(State.COMMENT_2);
            return false;}
        else if(endOfFile){
            tokenList.add(new Token(TokenType.ERROR, buffer));
        }
        return true;
    }

     boolean recognizeComment2(char symbol, String buffer, boolean endOfFile){
        if(symbol == '}'){
            buffer += symbol;
            tokenList.add(new Token(TokenType.COMMENT, buffer));
            endOfLexeme();
        }
        else if(endOfFile){
            tokenList.add(new Token(TokenType.ERROR, buffer));
        }
       // else recognizeCommentEnd(symbol,buffer);
        return true;
    }
     boolean recognizeComment3(char symbol){
        if(symbol == '*'){
            curState.setState(State.COMMENT_3NEXT);
            return true;//false
        }
        else{
            tokenList.add(new Token(TokenType.PUNCTUATION, Character.toString('(')));
            endOfLexeme();
            return false;
        }
    }

     boolean recognizeComment3next(char symbol, String buffer, boolean endOfFile){
        if(symbol == '*'){
            curState.setState(State.COMMENT_3END);
            return false; // true
        }
        else if(endOfFile){
            tokenList.add(new Token(TokenType.ERROR, buffer));
        }
        return true;
    }

     boolean recognizeComment3end(char symbol, String buffer, boolean endOfFile){
        if(buffer.toCharArray()[buffer.length() - 1]=='*' && symbol == ')'){
            buffer += symbol;
            tokenList.add(new Token(TokenType.COMMENT, buffer));
            endOfLexeme();
        }
        else if(endOfFile){
            tokenList.add(new Token(TokenType.ERROR, buffer));
        }

        return true;
    }
     boolean recognizeMultiplication (char symbol, String buffer){
        if(symbol == '=' || symbol == '*'){                              // "*=", "**" - pow
            buffer += symbol;
            tokenList.add(new Token(TokenType.OPERATOR, buffer));
            endOfLexeme();
            return true;
        }
        else {
            tokenList.add(new Token(TokenType.OPERATOR, buffer)); // "*"
            endOfLexeme();
            return false; // not to miss the next char
        }


    }

     boolean recognizeAssign(char symbol, String buffer){
        if(symbol == '='){                              // "+=", "-=", ":="
            buffer += symbol;
            tokenList.add(new Token(TokenType.OPERATOR, buffer));
            endOfLexeme();
            return true;
        }
        else {
            tokenList.add(new Token(TokenType.OPERATOR, buffer)); // "+", "-", ":"
            endOfLexeme();
            return false;
        }
    }

     boolean recognizeRelop(char symbol, String buffer){
        if(symbol == '='){                              // "<=", ">="
            buffer += symbol;
            tokenList.add(new Token(TokenType.OPERATOR, buffer));
            endOfLexeme();
            return true;
        }
        if(symbol == '<' || symbol == '>'){             // "<<", "<>", ">>", "><"- sym difference
            buffer += symbol;
            tokenList.add(new Token(TokenType.OPERATOR, buffer));
            endOfLexeme();
            return true;
        }
        else {
            tokenList.add(new Token(TokenType.OPERATOR, buffer)); // "<", ">"
            endOfLexeme();
            return false;
        }
    }

    boolean recognizeMinus(char symbol, String buffer) {
        if (Symbol.isDigit(symbol)) {
            curState.setState(State.NUMBER);
            return false;
        }
        else return recognizeAssign(symbol, buffer);

    }
}
