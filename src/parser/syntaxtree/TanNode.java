package parser.syntaxtree;

public class TanNode extends UnarySyntaxNode {

    public TanNode(SyntaxNode left) {
        super(left);
    }

    public void print() {
        System.out.print("tan(");
        left.print();
        System.out.print(")");
    }

    public double eval(double x) {
        return Math.tan(left.eval(x));
    }
}
