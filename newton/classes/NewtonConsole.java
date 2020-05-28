package classes;
import java.io.*;

public class NewtonConsole{

  public static void main (String [] args){
    NewtonConsole nrConsole = new NewtonConsole();
    while(true){
      nrConsole.mainMenu();
    }
  }

  public void compute(boolean shouldShowSteps){
    System.out.println("Пожалуйста, введите выражение.");
    try{
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      Operation.setOperation(InfixToPostfix.infixToPostfix(new NumericalTokenizer().convertToSpacedNumericalFormat(in.readLine().replaceAll("x", "X"))));
    }
    catch(IOException e){}
    System.out.println("Пожалуйста, введите ответ: ");
    double guess = getInput();
    double ans = Operation.compute(guess);
    if (ans == Integer.MAX_VALUE){
      System.out.println("Ошибка: не удалось вычислить корень.");
    }
    else{
      System.out.println("Ответ:" + ans);
    }
    pause();
  }

  public int getInput(){
    while(true){
      try{
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int option = Integer.parseInt(in.readLine());
        return option;
      }
      catch(IOException e){
        System.out.println("Ошибка: Пожалуйста, повторите попытку, произошла ошибка ввода.");
      }
      catch(NumberFormatException e){
        System.out.println("Ошибка: пожалуйста, введите номер.");
      }
    }
  }

  public void pause(){
    System.out.println("Нажмите любую клавишу, чтобы продолжить.");
    try{
      new BufferedReader(new InputStreamReader(System.in)).readLine();
      for(int x = 0; x < 100; x++)
        System.out.println("");
    }
    catch(IOException e){}
  }

  public void mainMenu(){
    System.out.println("Main menu: " );
    System.out.println("1. Compute (with steps)");
    System.out.println("2. Compute (without steps)");
    System.out.println("3. Exit");
    int option = getInput();
    if (option == 3){
      System.exit(0);
    }
    else if (option == 2){
      compute(false);
    }
    else if (option == 1){
      compute(true);
    }
  }
}