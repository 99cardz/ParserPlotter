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

    public double eval(double prev) {
        return left.eval(prev) + right.eval(prev);
    }

	public double[] evalAll(double[] values) {
		
		double[] result = left.evalAll(values);
		double[] other = right.evalAll(values);
		
		for (int i = 0; i < values.length; i++)
			result[i] += other[i];
			
		return result;
	}
}
