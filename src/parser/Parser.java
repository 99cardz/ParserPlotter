package parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import parser.syntaxtree.*;

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
                    tokens.add(new Token("x", pos, ++pos, TokenType.VARIABLE));
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

    public SyntaxNode buildSyntaxTree(final String string) throws SyntaxException {

        tokens = lex(string);
        tokenIndex = 0;

        scanToken();

        SyntaxNode root = parseExpr();

        // Make sure all the tokens got used!
        if(nextToken != null) {
            throw new SyntaxException(nextToken.getString(), nextToken.getStartIndex(), nextToken.getEndIndex());
        }

        return root;
    }

    private SyntaxNode parseExpr() throws SyntaxException {
        if(nextToken == null)
            throw new SyntaxException("", -1, -1);

        SyntaxNode a = parseTerm();

        while(true) {
            if(nextToken == null)
                return a;

            if(nextToken.getType() == TokenType.PLUS) {
                scanToken();
                SyntaxNode b = parseTerm();
                a = new AddNode(a, b);
            }
            else if(nextToken.getType() == TokenType.MINUS) {
                scanToken();
                SyntaxNode b = parseTerm();
                a = new SubNode(a, b);
            }
            else {
                return a;
            }
        }
    }

    private SyntaxNode parseTerm() throws SyntaxException {
        if(nextToken == null)
            throw new SyntaxException("", -1, -1);

        SyntaxNode a = parsePow();

        while(true) {
            if(nextToken == null)
                return a;

            if(nextToken.getType() == TokenType.MUL) {
                scanToken();
                SyntaxNode b = parsePow();
                a = new MultNode(a, b);
            }
            else if(nextToken.getType() == TokenType.DIV) {
                scanToken();
                SyntaxNode b = parsePow();
                a = new DivNode(a, b);
            }
            else {
                return a;
            }
        }
    }

    private SyntaxNode parsePow() throws SyntaxException {

        if(nextToken == null)
            throw new SyntaxException("", -1, -1);

        // Check for negation
        if(nextToken.getType() == TokenType.MINUS) {
            scanToken();
            return new NegateNode(parsePow());
        }
        else {
            SyntaxNode a = parseFact();

            if (nextToken != null && nextToken.getType() == TokenType.POW) {
                scanToken();
                return new PowNode(a, parsePow());
            } else {
                return a;
            }
        }
    }

    private SyntaxNode parseFact() throws SyntaxException {

        if(nextToken == null)
            throw new SyntaxException("", -1, -1);

        if(nextToken.getType() == TokenType.NUMBER) {
            SyntaxNode a = new NumberNode(nextToken.getValue());
            scanToken();
            return a;
        }
        else if(nextToken.getType() == TokenType.FUNC_ID) {
            // Keep record which function it is
            FunctionType funcType = nextToken.getFuncType();


            scanToken();
            // Null check
            if(nextToken == null)
                throw new SyntaxException("", -1, -1);

            // Make sure its a left parenthesis else
            if(nextToken.getType() != TokenType.PAR_OPEN) {
                throw new SyntaxException(nextToken.getString(), nextToken.getStartIndex(), nextToken.getEndIndex());
            }

            scanToken();
            // Parse the function argument
            SyntaxNode a = parseExpr();

            // Null check
            if(nextToken == null)
                throw new SyntaxException("", -1, -1);
            // Check for closing parenthesis
            if(nextToken.getType() != TokenType.PAR_CLOSE) {
                throw new SyntaxException(nextToken.getString(), nextToken.getStartIndex(), nextToken.getEndIndex());
            }
            scanToken();

            // Build and return the node
            switch(funcType) {
                case COS:  return new CosNode(a);
                case LOG:  return new LogNode(a);
                case SIN:  return new SinNode(a);
                case TAN:  return new TanNode(a);
                case SQRT: return new SqrtNode(a);
            }
        }
        else if(nextToken.getType() == TokenType.PAR_OPEN) {
            scanToken();
            SyntaxNode a = parseExpr();

            if(nextToken.getType() != TokenType.PAR_CLOSE) {
                throw new SyntaxException(nextToken.getString(), nextToken.getStartIndex(), nextToken.getEndIndex());
            }
            scanToken();

            return a;
        }
        else if(nextToken.getType() == TokenType.VARIABLE)
        {
            SyntaxNode a = new VariableNode();
            scanToken();
            return a;
        }

        // Otherwise, syntax is incorrect
        throw new SyntaxException(nextToken.getString(), nextToken.getStartIndex(), nextToken.getEndIndex());

    }
}
