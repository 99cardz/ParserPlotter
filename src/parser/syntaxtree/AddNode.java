package parser.syntaxtree;

public class AddNode extends BinarySyntaxNode {
    public AddNode(SyntaxNode left, SyntaxNode right) {
        super(left, right);
    }

    public void print() {
        System.out.print("(");
        left.print();
        System.out.print(" + ");
        right.print();
        System.out.print(")");
    }

    public double eval(double prev, double stride) {
        return left.eval(prev, stride) + right.eval(prev, stride);
    }
}
