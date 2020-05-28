package classes;
import java.io.*;

public class Settings{

  public static boolean shouldDisplay(){
    try{
      BufferedReader in = new BufferedReader(new FileReader(new File(NewtonApp.class.getClassLoader().getResource("classes/settings.txt").toURI())));
      if (in.readLine().equals("display= false"))
        return false;
      return true;
    }
    catch(Exception e){
      System.out.println("Файл не найден");
    }
    return true;
  }
}