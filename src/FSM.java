import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FSM {
    private String inPath;
    private String outPath;
    boolean endOfFile = false;
    StateWrapper currentState = new StateWrapper();
    List<Token> tokenList = new ArrayList<>();
    String buffer = "";

    private NumberRecognizer numberRecognizer = new NumberRecognizer(currentState, tokenList);
    private IdentifierRecognizer identifierRecognizer = new IdentifierRecognizer(currentState, tokenList);
    private OperatorRecognizer operatorRecognizer = new OperatorRecognizer(currentState, tokenList);

    public FSM(String inPath, String outPath){
        this.inPath = inPath;
        this.outPath = outPath;
        readfile();
    }

    public void readfile(){
        currentState.setState(State.START);
        int currentSymbol;
        File file = new File(inPath);
        FileReader fileReader;
        try {
            fileReader = new FileReader(file);
            while((currentSymbol = fileReader.read()) != -1) {
                recognize((char)currentSymbol);
            //recognize((char)Character.toLowerCase(currentSymbol)); //  recognize((char)currentSymbol);
            }
        } catch (IOException e) {
            System.err.println("File error:" + e);
        }
        endOfFile = true;
        recognize(' ');

        Output.generateHtml(outPath, tokenList);
    }


    public void recognize(char symbol){
        boolean symbolChecked = false;

        while (!symbolChecked) {
            switch (currentState.getState()) {
                case START: {
                    buffer = "";
                    if (Symbol.isDigit(symbol)) {
                        currentState.setState(State.NUMBER);
                        numberRecognizer = new NumberRecognizer(currentState, tokenList);
                    }
                    else if (Symbol.isLetter(symbol)) {
                        currentState.setState(State.IDENTIFIER);
                        identifierRecognizer = new IdentifierRecognizer(currentState, tokenList);
                    }
                    else if(Symbol.isSomeOperator(symbol) || symbol == '=') {
                        tokenList.add(new Token(TokenType.OPERATOR, Character.toString(symbol)));
                    }
                    else if(symbol == '$'){
                        currentState.setState(State.HEX_NUM);
                        numberRecognizer = new NumberRecognizer(currentState, tokenList);
                    }
                    else if(symbol == ':' || symbol == '+' || symbol == '/'){
                         currentState.setState(State.ASSIGN);
                         operatorRecognizer = new OperatorRecognizer(currentState, tokenList);
                    }
                    else if( symbol == '-' ){
                        currentState.setState(State.MINUS);
                    }
                    else if(symbol == '*'){
                        currentState.setState(State.MULTIPLY);
                        operatorRecognizer = new OperatorRecognizer(currentState, tokenList);
                    }

                    else if(symbol == '{'){
                        currentState.setState(State.COMPILER_DIRECTIVE);
                        operatorRecognizer = new OperatorRecognizer(currentState, tokenList);
                    }
                    else if(symbol == '('){
                        currentState.setState(State.COMMENT_3);
                        operatorRecognizer = new OperatorRecognizer(currentState, tokenList);
                    }
                    else if(symbol == '>' || symbol == '<'){
                        currentState.setState(State.RELOP);
                        operatorRecognizer = new OperatorRecognizer(currentState, tokenList);
                    }
                    else if (symbol == '\"' || symbol == '\'')
                        currentState.setState(State.STRINGLITERAL);
                    else {
                        identifierRecognizer = new IdentifierRecognizer(currentState, tokenList);
                        identifierRecognizer.checkTerminateSymbol(symbol);
                    }

                    symbolChecked = true;
                }
                break;
                case IND_ERROR:{
                    symbolChecked = identifierRecognizer.recognizeErrInd(symbol, buffer);
                }
                break;
                case HEX_NUM: {
                    symbolChecked = numberRecognizer.recognizeHexNum(symbol, buffer);
                }
                break;

                case NUM_E: {
                    symbolChecked = numberRecognizer.recognizeNumE(symbol, buffer);
                }
                break;
                case NUM_E_NEXT: {
                    symbolChecked = numberRecognizer.recognizeNumENext(symbol, buffer);
                }
                break;

                case MINUS: {
                    symbolChecked = operatorRecognizer.recognizeMinus(symbol, buffer);
                }
                break;

                case RELOP: {
                    symbolChecked = operatorRecognizer.recognizeRelop(symbol, buffer);
                }
                break;

                case MULTIPLY: {
                    symbolChecked = operatorRecognizer.recognizeMultiplication(symbol, buffer);
                }
                break;

                case STRINGLITERAL: {
                    symbolChecked = identifierRecognizer.recognizeStringLiteral(symbol, buffer);
                }
                break;

                case COMPILER_DIRECTIVE: {
                    symbolChecked = operatorRecognizer.recognizeCompilerDirective(symbol, buffer);
                }
                break;

                case COMPILER_DIRECTIVE_NEXT: {
                    symbolChecked = operatorRecognizer.recognizeCompilerDirectiveNext(symbol, buffer, endOfFile);
                }
                break;

                case COMMENT_2: {
                    symbolChecked = operatorRecognizer.recognizeComment2(symbol, buffer, endOfFile);
                }
                break;

                case COMMENT_3: {
                    symbolChecked = operatorRecognizer.recognizeComment3(symbol);
                }
                break;

                case COMMENT_3END: {
                    symbolChecked = operatorRecognizer.recognizeComment3end(symbol, buffer, endOfFile);
                }
                break;

                case COMMENT_3NEXT: {
                    symbolChecked = operatorRecognizer.recognizeComment3next(symbol, buffer, endOfFile);
                }
                break;

                case ASSIGN: {
                    symbolChecked = operatorRecognizer.recognizeAssign(symbol, buffer);
                }
                break;

                case NUMBER: {
                    symbolChecked = numberRecognizer.recognizeNumber(symbol, buffer);
                }
                break;

                case FRACTIONAL_NUM: {
                    symbolChecked = numberRecognizer.recognizeFractionalNum(symbol, buffer);
                }
                break;

                case FRACTIONAL_NUM_AFTER_DOT: {
                    symbolChecked = numberRecognizer.recognizeFractionalNumAfterDot(symbol, buffer);
                }
                break;

                case IDENTIFIER: {
                    symbolChecked = identifierRecognizer.recognizeIdentifier(symbol, buffer);
                }
                break;

                case ERROR: {
                    symbolChecked = numberRecognizer.recognizeError(symbol, buffer);
                }
                default: {
                    System.out.println("no state specified");
                }
                break;
            }
        }
        if(currentState.getState() != State.START){buffer += symbol;}
    }



}
