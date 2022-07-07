package parser.syntaxtree;

public abstract class BinarySyntaxNode extends SyntaxNode {
    SyntaxNode left, right;

    public BinarySyntaxNode(SyntaxNode left, SyntaxNode right) {
        this.left  = left;
        this.right = right;
    }
}
