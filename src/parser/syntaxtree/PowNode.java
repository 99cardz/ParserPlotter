package parser.syntaxtree;

public class PowNode extends BinarySyntaxNode {
    public PowNode(SyntaxNode left, SyntaxNode right) {
        super(left, right);
    }

    @Override
	public void print() {
        System.out.print("(");
        left.print();
        System.out.print("^");
        right.print();
        System.out.print(")");
    }

    @Override
	public double eval(double prev) {
        return Math.pow(left.eval(prev), right.eval(prev));
    }

	@Override
	public double[] evalAll(double[] values) {
		double[] base = left.evalAll(values);
		double[] exponent = right.evalAll(values);

		for (int i = 0; i < values.length; i++)
			base[i] = Math.pow(base[i], exponent[i]);

		return base;
	}
}
