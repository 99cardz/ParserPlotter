package parser.syntaxtree;

public class SqrtNode extends UnarySyntaxNode {

    public SqrtNode(SyntaxNode left) {
        super(left);
    }

    public void print() {
        System.out.print("sqrt(");
        left.print();
        System.out.print(")");
    }

    public double eval(double prev, double stride) {
        return Math.sqrt(left.eval(prev, stride));
    }
}
