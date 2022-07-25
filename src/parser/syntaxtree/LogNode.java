package parser.syntaxtree;

public class LogNode extends UnarySyntaxNode {

    public LogNode(SyntaxNode left) {
        super(left);
    }

    public void print() {
        System.out.print("log(");
        left.print();
        System.out.print(")");
    }

    public double eval(double x) {
        return Math.log(left.eval(x));
    }

	public double[] evalAll(double[] values) {
		
		double[] inner = left.evalAll(values);
		double[] result = new double[values.length];
	
		result[0] = Math.log(inner[0]);
		result[values.length-1] = Math.log(inner[values.length-1]);
		for (int i = 1, len = values.length-1; i < len; i++)
			result[i] = (inner[i-1] <= 0 && inner[i+1] > 0 || inner[i+1] <= 0 && inner[i-1] > 0) 
					? Double.NEGATIVE_INFINITY : Math.log(inner[i]);
			
		return result;
	}
}