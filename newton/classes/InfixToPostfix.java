package classes;
import java.util.*;

public class InfixToPostfix{
  public static String infixToPostfix(String input){
    StringTokenizer st = new StringTokenizer(input);
    StringBuilder res = new StringBuilder("");
    Stack<String> stack = new Stack<String>();
    
    Operations ops = new Operations();
    while(st.hasMoreTokens()){
      String token = st.nextToken();
      if (ops.isOperator(token)){
        while(!stack.isEmpty() && !stack.peek().equals("(") && ops.hasHigherPrecendece(stack.peek(), token)){
          res.append(stack.pop() + " "); 
        } 
        stack.push(token);
      }
      else if (token.equals("(")){
        stack.push("("); 
      }
      else if (token.equals(")")){
        while(!stack.isEmpty() && !stack.peek().equals("(")){
          res.append(stack.pop() + " "); 
        } 
        stack.pop();
      }
      else{
        res.append(token + " ");  //append the token normally
      }
    } 
    while (!stack.isEmpty())
      res.append((stack.pop()) + " ");
    return res.toString().trim(); //trim it nicely :)
  }
}