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

    public double eval(double prev, double x, double next) {
        return left.eval(prev, x, next) + right.eval(prev, x, next);
    }

	@Override
	public double eval(double x) {
		// TODO Auto-generated method stub
		return left.eval(x) + right.eval(x);
	}
}
