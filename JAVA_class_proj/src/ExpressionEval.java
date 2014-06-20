import java.util.Stack;

/**
this class is used to solve mathematics expressions
**/
public class ExpressionEval{	
	
	//functions map to one-character operator
	private static final String ASIN = "A";
	private static final String ACOS = "B";
	private static final String ATAN = "C";
	private static final String SINH = "D";
	private static final String COSH = "E";
	private static final String TANH = "F";
	private static final String SIN = "G";
	private static final String COS = "H";
	private static final String TAN = "I";
	private static final String LN = "J";
	private static final String LOG = "K";
	private static final String SQRT = "L";
	private static final String ROOT = "M";
	private static final String ABS = "N";
	
	private static double lastAns = 0;				
		
	/**
	the method solves expression in standard mathematics expression form
	containing adding, subtraction, multiply ,division and power operator
	the answer is given in a double number
	**/
	public static double solve(String expression,boolean useDeg){
		String[] postfixArray;
		StringBuilder prescanBuilder;
		StringBuilder postFixBuilder = new StringBuilder();
		Stack<Character> processStack = new Stack<Character>();
		Stack<Double> solveStack;
		int current = 0;
		double temp;
		double answer = 0;
		
		
		//prescan the expression
		//replace functions
		expression = expression.toLowerCase();
		expression = expression.replaceAll("asin",ASIN);
		expression = expression.replaceAll("acos",ACOS);
		expression = expression.replaceAll("atan",ATAN);
		expression = expression.replaceAll("sinh",SINH);
		expression = expression.replaceAll("cosh",COSH);
		expression = expression.replaceAll("tanh",TANH);
		expression = expression.replaceAll("sin",SIN);
		expression = expression.replaceAll("cos",COS);
		expression = expression.replaceAll("tan",TAN);
		expression = expression.replaceAll("ln",LN);
		expression = expression.replaceAll("log",LOG);
		expression = expression.replaceAll("sqrt",SQRT);
		expression = expression.replaceAll("root",ROOT);
		expression = expression.replaceAll("abs",ABS);
		expression = expression.replaceAll("pi",Double.toString(Math.PI));
		expression = expression.replaceAll("e",Double.toString(Math.E));
		expression = expression.replaceAll("ans",Double.toString(lastAns));
		//replace the unuery "-" with "_"
		prescanBuilder = new StringBuilder(expression);
		for(current = 0;current < expression.length();current++){
			if(current == 0 && prescanBuilder.charAt(current) == '-'){
				prescanBuilder.replace(0,1,"_");
				//if the first character is minus, than it must be unary
			}else if(prescanBuilder.charAt(current) == '-' && isOperator(prescanBuilder.charAt(current - 1))){
				prescanBuilder.replace(current,current + 1,"_");
				//a minus immediately after an operator is unary
			}else if(prescanBuilder.charAt(current) == '-' && prescanBuilder.charAt(current - 1) == '('){
				prescanBuilder.replace(current,current + 1,"_");
				//a minus immiediately after an left parenthesis is unary
			}
		}		
		current = 0;
		expression = prescanBuilder.toString();	
		//after replacement ,all operator should be one char
		

		//infix to postfix
		//every element of postfix should be seperated with a whitespace
		for(current = 0;current < expression.length();current++){
			if(isOperand(expression.charAt(current))){
				postFixBuilder.append(expression.charAt(current));
					if(current >= (expression.length() -1)){
						postFixBuilder.append(" ");
						//add a wehitespace after the operand at the end of expression (if any)
					}else if(!(isOperand(expression.charAt(current+1)))){
						postFixBuilder.append(" ");
						//add a wehitespace after each set of operand add to postfix
					}
				//if the scanned character is operand, add it to postfix directly
			}else if(processStack.empty() || expression.charAt(current) == '('){
				processStack.push(expression.charAt(current));
				//if the stack is empty or the character is left parenthesis, push it onto the stack
			}else if(expression.charAt(current) == ')'){
				while(!(processStack.empty()) && processStack.peek() != '('){
					//note that the emptystack exception is avoided by lazy evaluation
					if(processStack.peek() == '(' || processStack.peek() == ')'){
						processStack.pop();
						//discard both parenthesis
					}else{
						postFixBuilder.append(processStack.pop());
						postFixBuilder.append(" ");
						//add a wehitespace after each operator pop to the postfix
					}
				}
				if(!(processStack.empty()) && processStack.peek() == '('){
					//note that the emptystack exception is avoided by lazy evaluation
					processStack.pop();
					//if there is still a left parenthesis in the stack, discard it
				}
				//if the character is right parenthesis, pop the stack until the top of the stack is left parenthesis (or empty)
				//all parenthesis, including the left parenthesis on top of the stack is discard in th process
			}else if(isOperator(expression.charAt(current)) && (precedenceOf(expression.charAt(current)) > precedenceOf(processStack.peek()))){
				processStack.push(expression.charAt(current));
				//if the character is operator and has higher precedence than the top of stack, push it onto the stack
			}else if(isOperator(expression.charAt(current)) && isRightAssoc(expression.charAt(current)) && (precedenceOf(expression.charAt(current)) == precedenceOf(processStack.peek()))){
				processStack.push(expression.charAt(current));
				//since unary operators is compute from right to left, unuaries with same precedence should be pushed on the stack
			}else if(isOperator(expression.charAt(current)) && (precedenceOf(expression.charAt(current)) <= precedenceOf(processStack.peek()))){
				do{
					postFixBuilder.append(processStack.pop());
					postFixBuilder.append(" ");
					//add a wehitespace after each operator pop to the postfix
				}while(!(processStack.empty()) && precedenceOf(expression.charAt(current)) <= precedenceOf(processStack.peek()));
				//note that the emptystack exception is avoided by lazy evaluation
				processStack.push(expression.charAt(current));
				//if the character is operator and has lower or equal precedence than the top of stack,
				//pop the stack and repeat the test with new element on top
				//finally, push the character onto the stack
			}else{
				throw new IllegalArgumentException("illegal input in expression");
			}
		}
		while(!processStack.empty()){
			if(processStack.peek() == '(' || processStack.peek() == ')'){
				processStack.pop();
				//discard both parenthesis
			}else{
				postFixBuilder.append(processStack.pop());
				postFixBuilder.append(" ");
				//add a wehitespace after each operator pop to the postfix
			}
		}
		//after the infix is scanned, pop the stack until empty
		//all parenthesis is discarded
		
		
		//Postfix evaluation
		postfixArray = postFixBuilder.toString().split(" ");
		current = 0;
		temp = 0;
		solveStack = new Stack<Double>();
		for(current = 0;current < postfixArray.length;current++){
			if(isOperand(postfixArray[current])){
				solveStack.push(Double.parseDouble(postfixArray[current]));
				//if the string is operand, convert it to double and push it onto the stack
			}else if(isOperator(postfixArray[current]) && !(solveStack.empty())){
				temp = solveStack.pop();
				if(isRightAssoc(postfixArray[current])){
					switch(postfixArray[current]){
						case "_":
							temp = -temp;
							break;
						case ASIN:
							temp = degRadOutput(Math.asin(temp),useDeg);
							break;
						case ACOS:
							temp = degRadOutput(Math.acos(temp),useDeg);
							break;
						case ATAN:
							temp = degRadOutput(Math.atan(temp),useDeg);
							break;
						case SINH:
							temp = Math.sinh(temp);
							break;
						case COSH:
							temp = Math.cosh(temp);
							break;
						case TANH:
							temp = Math.tanh(temp);
							break;
						case SIN:
							temp = Math.sin(degRadInput(temp,useDeg));
							break;
						case COS:
							temp = Math.cos(degRadInput(temp,useDeg));
							break;
						case TAN:
							temp = Math.tan(degRadInput(temp,useDeg));
							break;
						case LN:
							temp = Math.log(temp);
							break;
						case LOG:
							temp = Math.log(temp)/Math.log(solveStack.pop());
							break;
						case SQRT:
							temp = Math.sqrt(temp);
							break;
						case ROOT:
							temp = Math.pow(temp,1/solveStack.pop());
							break;
						case ABS:
							temp = Math.abs(temp);
							break;
						default:
							throw new UnsupportedOperationException("undefined operators");
					}
				}else{//left assoc
					switch(postfixArray[current]){
						case "+":
							temp = solveStack.pop() + temp;
							break;
						case "-":
							temp = solveStack.pop() - temp;
							break;
						case "*":
							temp = solveStack.pop() * temp;
							break;
						case "/":
							temp = solveStack.pop() / temp;
							break;
						case "^":
							temp = Math.pow(solveStack.pop(),temp);
							break;
						case ",":
							break;	//split for multi-arg function ,does nothing
						default:
							throw new UnsupportedOperationException("undefined operators");
					}
				}
				solveStack.push(temp);
				//if the string is unary operator, pop one number from stack
				//if the string is binary operator, pop two number from stack (there should be at least two numbers)
				//evaluate them, and push the answer back to the stack
			}else{		
				throw new IllegalArgumentException("syntex error in expression");
			}
		}
		if(!solveStack.empty()){
			answer = solveStack.pop();
		}else{
			throw new IllegalArgumentException("syntex error in expression");
		}
		//after all object scanned, there should be only one number left
		//and that is the answer
		if(!solveStack.empty()){
			throw new IllegalArgumentException("syntex error in expression");
		}
		lastAns = answer;//recoard ans
		return answer;
	}
	
	private static double degRadInput(double in,boolean useDeg){
		if(useDeg){
			return Math.toRadians(in);
		}else{
			return in;
		}		
	}
	
	private static double degRadOutput(double in,boolean useDeg){
		if(useDeg){
			return Math.toDegrees(in);
		}else{
			return in;
		}		
	}
	
	private static boolean isOperator(char scanned){
		if(Character.isAlphabetic(scanned)){
			return true;
		}
		switch(scanned){
			case '+':
			case '-':
			case '*':
			case '/':
			case '^':
			case '_':
			case ',':	//split for multi-arg function ,does nothing
				return true;
			default:
				return false;
		}
	}
	
	
	private static boolean isOperator(String scanned){
		return isOperator(scanned.charAt(0));		
	}
	
	
	private static boolean isRightAssoc(char scanned){
		if(Character.isAlphabetic(scanned)){
			return true;
		}
		switch(scanned){
			case '_':
				return true;
			default:
				return false;
		}
	}
	
	
	private static boolean isRightAssoc(String scanned){
		return isRightAssoc(scanned.charAt(0));		
	}
	
	
	private static boolean isOperand(char scanned){
		if(Character.isDigit(scanned)){
			return true;
		}else if(scanned == '.'){
			return true;
		}else{
			return false;
		}
	}
	
	
	private static boolean isOperand(String scanned){
		if(Character.isDigit(scanned.charAt(0))){
			return true;
			//any given number should be positive, and the first number is always digit
		}else{
			return false;
		}
	}
	
	
	private static int precedenceOf(char scanned){
		if(Character.isAlphabetic(scanned)){
			return 5;
		}
		switch(scanned){
			case '+':
			case '-':
				return 2;
			case '*':
			case '/':
				return 3;
			case '^':
				return 4;
			case '_':
				return 5;
			case ',':	//split for multi-arg function ,does nothing
				return 1;
			case '(':
			default:
				return 0;
				//left parenthesis is seen as operator here (for convenience), and has the lowest precedence
		}
	}
	
}
	
	
