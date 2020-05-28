package classes;

public class Operation{
  private static String operation = "";
  static final double ACCURACY = 1e-10;
  final static int MAX_ATTEMPTS = 10000;
  final static double[] TEST_CASES = new double[]{-100, -50, 0, 1, 50, 100};

  public static double operate(double value){
    return PostfixEvaluater.evaluate(operation, value);
  }

  public static double compute(double guess){
    final double ACCEPTABLE_CHANGE = ACCURACY; //accuracy for the answer
    final double ACCEPTABLE_HORIZONTAL_SLOPE = ACCURACY; //acceptible horizontal slope to prevent timeouts
    double x = guess;
    double newX;
    
    StartValueSelection.iterations = 0; //reset iterations
    for(int o = 0; o < MAX_ATTEMPTS; o++){
      double y = operate(x);
      double slope = derivative(x);
      newX = (x-(y/slope));
      if (Math.abs(newX-x) < ACCEPTABLE_CHANGE){
        System.out.println("Решение найдено: x= " + newX);
        return newX;
      }
      if (ACCEPTABLE_HORIZONTAL_SLOPE > Math.abs(slope)){
        System.out.println("Ошибка: найден бесконечный наклон.");
        return Integer.MAX_VALUE;
      }
      x = newX;
      StartValueSelection.iterations++;
    }
    System.out.println("Не мог найти никакого решения!");
    return Integer.MAX_VALUE; //could not find!
    
  }

  public static double derivative(double value){
    double firstVal = PostfixEvaluater.evaluate(operation, value);
    double secondVal = (PostfixEvaluater.evaluate(operation, value + ACCURACY));
    double dx = ((secondVal - firstVal)/(ACCURACY));
    return dx;
  }

  public static void setOperation(String operation){
    Operation.operation = operation.trim();
  }
  
  
}