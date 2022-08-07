package parser.syntaxtree;

public class MultNode extends BinarySyntaxNode {
    public MultNode(SyntaxNode left, SyntaxNode right) {
        super(left, right);
    }

    @Override
	public void print() {
        System.out.print("(");
        left.print();
        System.out.print(" * ");
        right.print();
        System.out.print(")");
    }

    @Override
	public double eval(double x) {
        return left.eval(x) * right.eval(x);
    }

	@Override
	public double[] evalAll(double[] values) {

		double[] result = left.evalAll(values);
		double[] factor = right.evalAll(values);

		for (int i = 0; i < values.length; i++)
			result[i] *= factor[i];

		return result;
	}
}
