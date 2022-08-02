package parser.syntaxtree;

public class NegateNode extends UnarySyntaxNode {

    public NegateNode(SyntaxNode left) {
        super(left);
    }

    public void print() {
        System.out.print("(-");
        left.print();
        System.out.print(")");
    }

    public double eval(double prev) {
        return -left.eval(prev);
    }

	public double[] evalAll(double[] values) {
		
		double[] result = left.evalAll(values);
		
		for (int i = 0; i < values.length; i++)
			result[i] = -result[i];
			
		return result;
	}
}
