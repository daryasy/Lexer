public class Token {
    private String content;
    private TokenType tokenType;
    public Token(){}

    public Token(TokenType tokenType, String content){
        this.content = content;

        if(tokenType.equals(TokenType.IDENTIFIER)){
            if(LangPatterns.isLiteral(content.toLowerCase())){
                this.tokenType = TokenType.LITERAL;
            }
            else
            if(LangPatterns.isKeyword(content.toLowerCase())){
                this.tokenType = TokenType.KEYWORD;
            }
            else
                this.tokenType = tokenType;
        }  else
            this.tokenType = tokenType;

        if(tokenType.equals(TokenType.PUNCTUATION)){
            switch (content) {
                case "\n":
                    this.content = "\\" + "n";
                    break;
                case "\t":
                    this.content = "\\" + "t";
                    break;
            }

        }

    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getContent() {
        return content;
    }
}