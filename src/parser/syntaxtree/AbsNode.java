package parser.syntaxtree;

public class AbsNode extends UnarySyntaxNode {

    public AbsNode(SyntaxNode left) {
        super(left);
    }

    @Override
	public void print() {
        System.out.print("abs(");
        left.print();
        System.out.print(")");
    }

    @Override
	public double eval(double prev) {
        return Math.abs(left.eval(prev));
    }

	@Override
	public double[] evalAll(double[] values) {
		double[] result = left.evalAll(values);

		for (int i = 0; i < values.length; i++)
			result[i] = Math.abs(result[i]);

		return result;
	}
}