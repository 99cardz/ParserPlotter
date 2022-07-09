package parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    public static List<Token> lex(final String string) throws SyntaxException {
        List<Token> tokens = new ArrayList<Token>();

        // The matcher for numbers
        final Pattern numberPattern = Pattern.compile("([0-9]*[.])?[0-9]+");
        final Matcher numberMatcher = numberPattern.matcher(string);

        // Current position in the string
        int pos = 0;

        while(pos < string.length()) {

            // Skip all blank characters
            if(" \t\n".indexOf(string.charAt(pos)) != -1) {
                pos++;
                continue;
            }

            switch(string.charAt(pos)){
                case '+':
                    tokens.add(new Token("+", pos, ++pos, TokenType.PLUS));
                    continue;
                case '-':
                    tokens.add(new Token("-", pos, ++pos, TokenType.MINUS));
                    continue;
                case '*':
                    tokens.add(new Token("*", pos, ++pos, TokenType.MUL));
                    continue;
                case '/':
                    tokens.add(new Token("/", pos, ++pos, TokenType.DIV));
                    continue;
                case '^':
                    tokens.add(new Token("^", pos, ++pos, TokenType.POW));
                    continue;
                case 'x':
                    tokens.add(new Token("-", pos, ++pos, TokenType.VARIABLE));
                    continue;
                case '(':
                    tokens.add(new Token("(", pos, ++pos, TokenType.PAR_OPEN));
                    continue;
                case ')':
                    tokens.add(new Token(")", pos, ++pos, TokenType.PAR_CLOSE));
                    continue;
            }

            // Read number literal
            if(Character.isDigit(string.charAt(pos))) {
                // Get the number!
                numberMatcher.find();

                int start = numberMatcher.start();
                int end = numberMatcher.end();
                String numberString = string.substring(start, end);
                tokens.add(new Token(numberString, start, end, TokenType.NUMBER, Double.parseDouble(numberString)));

                pos = end;
                continue;
            }

            // Else, check for function names
            boolean found = false;
            for(FunctionType ft : FunctionType.values()) {
                String funcName = ft.name().toLowerCase();
                if(string.indexOf(funcName) == pos) {
                    found = true;

                    tokens.add(new Token(funcName, pos, pos + funcName.length(), TokenType.FUNC_ID, ft));
                    pos += funcName.length();
                }
            }
            if(found) continue;

            throw new SyntaxException(string.substring(pos, pos + 1), pos, pos + 1);
        }

        return tokens;
    }

    List<Token> tokens = null;
    int tokenIndex = 0;
    Token nextToken = null;

    // Get the next token from the list
    private void scanToken() {
        try {
            nextToken = tokens.get(tokenIndex++);
        }
        catch (Exception e) {
            nextToken = null;
        }
    }
}
