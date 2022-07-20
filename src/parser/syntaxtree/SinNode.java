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

    public double eval(double prev, double x, double next) {
        return Math.sin(left.eval(prev, x, next));
    }

	public double eval(double x) {
		return Math.sin(left.eval(x));
	}
}
