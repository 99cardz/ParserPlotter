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
		double[] inner = left.evalAll(values);
		double[] result = new double[values.length];
		
		result[0] = Math.sqrt(inner[0]);
		int len = values.length-1;
		result[len] = Math.sqrt(inner[len]);
		for(int i = 1; i < len; i++)
			result[i] = 
				inner[i-1] <= 0 && inner[i] >= 0 || inner[i] >= 0 && inner[i+1] <= 0 
				? 0 : Math.sqrt(inner[i]);
			
		return result;
	}
}
