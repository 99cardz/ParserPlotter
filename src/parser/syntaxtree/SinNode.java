package parser.syntaxtree;

public class SinNode extends UnarySyntaxNode {

    public SinNode(SyntaxNode left) {
        super(left);
    }

    public void print() {
        System.out.print("sin(");
        left.print();
        System.out.print(")");
    }

    public double eval(double prev, double stride) {
        return Math.sin(left.eval(prev, stride));
    }
}
