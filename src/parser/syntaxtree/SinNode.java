package parser.syntaxtree;

public class SinNode extends UnarySyntaxNode {

    public SinNode(SyntaxNode left) {
        super(left);
    }

    @Override
	public void print() {
        System.out.print("sin(");
        left.print();
        System.out.print(")");
    }

    @Override
	public double eval(double prev) {
        return Math.sin(left.eval(prev));
    }

	@Override
	public double[] evalAll(double[] values) {

		double[] result = left.evalAll(values);

		for (int i = 0; i < values.length; i++)
			result[i] = Math.sin(result[i]);

		return result;
	}
}
