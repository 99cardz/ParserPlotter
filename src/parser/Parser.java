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

    public SyntaxNode buildSyntaxTree(final String string) {
        try {
            tokens = lex(string);
        }
        catch (SyntaxException e) {
            System.out.println("Syntax error: '" + e.getString() + "' at index " + e.getStartIndex() + " to " + e.getEndIndex());
            return null;
        }
        tokenIndex = 0;

        scanToken();

        try {
            SyntaxNode root = parseExpr();

            return root;
        }
        catch(SyntaxException e) {
            System.out.println("Error while building syntax tree!");
            return null;
        }
    }

    private SyntaxNode parseExpr() throws SyntaxException {
        SyntaxNode a = parseTerm();

        while(true) {
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
        SyntaxNode a = parsePow();

        while(true) {
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


        // Check for negation
        if(nextToken.getType() == TokenType.MINUS) {
            scanToken();
            return new NegateNode(parsePow());
        }
        else {
            SyntaxNode a = parseFact();

            if (nextToken.getType() == TokenType.POW) {
                scanToken();
                return new PowNode(a, parsePow());
            } else {
                return a;
            }
        }
    }

    private SyntaxNode parseFact() throws SyntaxException {
        if(nextToken.getType() == TokenType.NUMBER) {
            return new NumberNode(nextToken.getValue());
        }
        else if(nextToken.getType() == TokenType.FUNC_ID) {
            // Keep record which function it is
            FunctionType funcType = nextToken.getFuncType();

            // Check for opening parenthesis
            scanToken();
            if(nextToken.getType() != TokenType.PAR_OPEN) {
                throw new SyntaxException(nextToken.getString(), nextToken.getStartIndex(), nextToken.getEndIndex());
            }

            // Parse the function argument
            SyntaxNode a = parseExpr();

            // Check for closing parenthesis
            if(nextToken.getType() != TokenType.PAR_CLOSE) {
                throw new SyntaxException(nextToken.getString(), nextToken.getStartIndex(), nextToken.getEndIndex());
            }

            // Build and return the node
            switch(funcType) {
                case COS: return new CosNode(a);
                case LOG: return new LogNode(a);
                case SIN: return new SinNode(a);
                case TAN: return new TanNode(a);
                case SQRT: return new SqrtNode(a);
            }
        }
        else if(nextToken.getType() == TokenType.PAR_OPEN) {
            scanToken();
            SyntaxNode a = parseExpr();
            scanToken();

            return a;
        }
        else
        {
            return new VariableNode();
        }

        // This should never happen
        return null;
    }
}
