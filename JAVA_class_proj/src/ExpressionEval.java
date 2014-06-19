import java.util.Scanner;


/**
this class is used to solve mathematic expressions
and can be execute directly as a calculator
**/
public class ExpressionEval{
		
	/**
	the muthod solves expression in standard mathematic expression form
	containing adding, substraction, mutiply ,division and power operator
	the answer is given in a double number
	**/
	public static double solve(String expression){
		String[] postfixArray;
		StringBuilder prescanBuilder = new StringBuilder(expression);
		StringBuilder postFixBuilder = new StringBuilder();
		CharStack processStack = new CharStack();
		DoubleStack solveStack;
		int current = 0;
		double temp;
		double answer = 0;
		
		
		//prescan the expression
		//replace the unuery "-" with "_"
		for(current = 0;current < expression.length();current++){
			if(current == 0 && prescanBuilder.charAt(current) == '-'){
				prescanBuilder.replace(0,1,"_");
				//if the first character is minus, than it must be unary
			}else if(prescanBuilder.charAt(current) == '-' && isOperator(prescanBuilder.charAt(current - 1))){
				prescanBuilder.replace(current,current + 1,"_");
				//a minus immiediately after an operator is unary
			}else if(prescanBuilder.charAt(current) == '-' && prescanBuilder.charAt(current - 1) == '('){
				prescanBuilder.replace(current,current + 1,"_");
				//a minus immiediately after an left parenthesis is unary
			}
		}
		current = 0;
		expression = prescanBuilder.toString();
		//update the expression
		

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
			}else if(isOperator(expression.charAt(current)) && isUnary(expression.charAt(current)) && (precedenceOf(expression.charAt(current)) == precedenceOf(processStack.peek()))){
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
				System.out.println("error, system will exit");
				System.exit(0);
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
		solveStack = new DoubleStack();
		for(current = 0;current < postfixArray.length;current++){
			if(isOperand(postfixArray[current])){
				solveStack.push(Double.parseDouble(postfixArray[current]));
				//if the string is operand, convert it to double and push it onto the stack
			}else if(isOperator(postfixArray[current]) && !(solveStack.empty())){
				temp = solveStack.pop();
				if(isUnary(postfixArray[current])){
					switch(postfixArray[current]){
						case "_":
							temp = -temp;
					}
				}else{
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
					}
				}
				solveStack.push(temp);
				//if the string is unary operator, pop one number from stack
				//if the string is binary operator, pop two number from stack (there should be at least two numbers)
				//evaluate them, and push the answer back to the stack
			}else{		
				System.out.println("error, system will exit");
				System.exit(0);
			}
		}
		if(!solveStack.empty()){
			answer = solveStack.pop();
		}else{
			System.out.println("error, system will exit");
			System.exit(0);
		}
		//after all object scanned, there should be only one number left
		//and that is the answer
		if(!solveStack.empty()){
			System.out.println("error, system will exit");
			System.exit(0);
			}
		return answer;
	}
	
	
	public static boolean isOperator(char scanned){
		switch(scanned){
			case '+':
			case '-':
			case '*':
			case '/':
			case '^':
			case '_':
				return true;
			default:
				return false;
		}
	}
	
	
	public static boolean isOperator(String scanned){
		switch(scanned){
			case "+":
			case "-":
			case "*":
			case "/":
			case "^":
			case "_":
				return true;
			default:
				return false;
		}
	}
	
	
	public static boolean isUnary(char scanned){
		switch(scanned){
			case '_':
				return true;
			default:
				return false;
		}
	}
	
	
	public static boolean isUnary(String scanned){
		switch(scanned){
			case "_":
				return true;
			default:
				return false;
		}
	}
	
	
	public static boolean isOperand(char scanned){
		if(Character.isDigit(scanned)){
			return true;
		}else if(scanned == '.'){
			return true;
		}else{
			return false;
		}
	}
	
	
	public static boolean isOperand(String scanned){
		if(Character.isDigit(scanned.charAt(0))){
			return true;
			//any given number should be positive, and the first number is always digit
		}else{
			return false;
		}
	}
	
	
	public static int precedenceOf(char scanned){
		switch(scanned){
			case '+':
			case '-':
				return 1;
			case '*':
			case '/':
				return 2;
			case '^':
				return 3;
			case '_':
				return 4;
			case '(':
			default:
				return 0;
				//left parenthesis is seen as operator here (for convenience), and has the lowest precedence
		}
	}
	
	
}
	
	
