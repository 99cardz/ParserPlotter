package parser.syntaxtree;

public class SqrtNode extends UnarySyntaxNode {

    public SqrtNode(SyntaxNode left) {
        super(left);
    }

    public void print() {
        System.out.print("sqrt(");
        left.print();
        System.out.print(")");
    }

    public double eval(double x) {
        return Math.sqrt(left.eval(x));
    }

	public double[] evalAll(double[] values) {
		double[] result = left.evalAll(values);
		
		result[0] = Math.sqrt(result[0]);
		for(int i = 1, len = result.length; i < len; i++)
			result[i] = (result[i-1] > 0 && result[i] <= 0) ? 0 : Math.sqrt(result[i]);
			
		return result;
	}
}
