package parser.syntaxtree;

public abstract class UnarySyntaxNode extends SyntaxNode {
    SyntaxNode left;

    public UnarySyntaxNode(SyntaxNode left) {
        this.left = left;
    }
}
