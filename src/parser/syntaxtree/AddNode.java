package parser.syntaxtree;

public class AddNode extends BinarySyntaxNode {
    public AddNode(SyntaxNode left, SyntaxNode right) {
        super(left, right);
    }

    @Override
	public void print() {
        System.out.print("(");
        left.print();
        System.out.print(" + ");
        right.print();
        System.out.print(")");
    }

    @Override
	public double eval(double prev) {
        return left.eval(prev) + right.eval(prev);
    }

	@Override
	public double[] evalAll(double[] values) {

		double[] result = left.evalAll(values);
		double[] other = right.evalAll(values);

		for (int i = 0; i < values.length; i++)
			result[i] += other[i];

		return result;
	}
}
