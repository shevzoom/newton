package classes;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;

@SuppressWarnings("serial")
public class NewtonApp extends JFrame implements ActionListener, MouseListener{
  private JPanel buttonPanel;
  private static final int UPPER_BUFFER = 100;
  private Image backgroundImg;
  private Image display;
  private Image highlight;
  private Image displayInitialize;
  private Image displayOff;
  private Image maxChar;
  private boolean drawHighlight = false;
  private int highlightX = 0;
  private int highlightY = 0;
  private boolean drawZero = false;
  private boolean turnedOn = false;
  private boolean turnedOff = true;
  private boolean off = true;
  private static String command = "";
  private static String expression = "";
  private double guess;
  private KeyMapping keyMapping;

  public NewtonApp(){
    super("Метод Ньютона");
    keyMapping = new KeyMapping();
    setSize(465,351);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setResizable(false);
    
    buttonPanel = new JPanel();
    add(buttonPanel);
    addMenuBar();
    
    addMouseListener(this);
    setVisible(true);
  }

  private void fetchImages(){
    try{
      backgroundImg = ImageIO.read((NewtonApp.class.getClassLoader().getResource("images/CalculatorBackground.jpg")));
      //backgroundImg = ImageIO.read(new File("../images/CalculatorBackground.jpg"));
      display = ImageIO.read((NewtonApp.class.getClassLoader().getResource("images/monitor.jpg")));
      highlight = ImageIO.read((NewtonApp.class.getClassLoader().getResource("images/highlight.png")));
      displayInitialize = ImageIO.read((NewtonApp.class.getClassLoader().getResource("images/initializing.jpg")));
      displayOff = ImageIO.read((NewtonApp.class.getClassLoader().getResource("images/off.jpg")));
      maxChar = ImageIO.read((NewtonApp.class.getClassLoader().getResource("images/monitor_max.jpg")));
    }
    catch(IOException e){
      JOptionPane.showMessageDialog(this, "Error");
    }
  }

  private void addMenuBar(){
    JMenuBar menu = new JMenuBar();
    setJMenuBar(menu);
  }

  private void drawInterface(Graphics g){
    Graphics2D g2d = (Graphics2D) g;
    for(int i = 0; i < 6; i++)
      g2d.drawLine(0,UPPER_BUFFER + i * 50, 464, UPPER_BUFFER + i * 50);
    
    for(int i = 0; i < 9; i++)
      g2d.drawLine(i * 58, UPPER_BUFFER, i * 58, 350);
  }

  private void drawHighLight(Graphics g){
    if (!drawHighlight)
      return;
    if (!drawZero)
      g.drawImage(highlight, 58 * highlightX,  100 + 50 * highlightY,  null);
    
  }

  private void updateScreen(Graphics g){
    g.setFont(new Font("Helvetica", Font.PLAIN, 30));
    g.setColor(Color.white);
    g.drawString(command, 113, 85);
  }

  public void paint(Graphics g){
    super.paint(g);
    fetchImages();
    
    g.drawImage(backgroundImg, 0, 100, null);
    if (!turnedOff && !turnedOn && !off){
      if (command.length() <= 18)
        g.drawImage(display,  0,  44,  490,  57,  null);
      else
        g.drawImage(maxChar, 0, 44, 490, 57, null);
    }
    else if (turnedOn){
      off = false;
      command = "";
      expression = "";
      g.drawImage(displayInitialize, 0, 44, 490, 57, null);
      try{
        Thread.sleep(100);
      }
      catch(Exception e){}
      g.drawImage(display,  0,  44,  490,  57,  null);
    }
    else{
      off = true;
      g.drawImage(displayOff, 0, 44, 490, 57, null);
      command = "";
      expression = "";
    }

    drawInterface(g);
    updateScreen(g);
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    String cmd = arg0.getActionCommand();
    if (cmd.equals("Выход"))
      System.exit(0);
    else
      return;
  }

  private void outputNotSupported(){
    JOptionPane.showMessageDialog(this,"Обратите внимание: эта операция не поддерживается в этой версии!","Обратите Внимание: Неподдерживаемая Операция",JOptionPane.ERROR_MESSAGE);
  }

  private void updateCommand(int x, int y){
    String cmdBefore = command;
    
    if (keyMapping.getCommandAppending.get(x + "|" + y) != null){
      command += keyMapping.getCommandAppending.get(x + "|" + y);
    }
    else if (x == 0 && (y > 0)){
      if (!InputVerification.isValidExponent(command)){
        JOptionPane.showMessageDialog(this, "Обратите внимание: вы не можете применить экспоненциальную функцию без выражения, заключенного в квадратные скобки.","Notice: Invalid Operation", JOptionPane.ERROR_MESSAGE);
        command = command.substring(0, command.length()-3);
        return;
      }
      if (!InputVerification.hasRepeatedInvalidOperators(command)){
        JOptionPane.showMessageDialog(this,"Фатальная Ошибка: Неверный Ввод! Вы не можете иметь повторяющиеся / недопустимые операторы.","Фатальная Ошибка: Неверный Ввод", JOptionPane.ERROR_MESSAGE);
        command = command.substring(0, command.length() -3);
        return;
      }
    }
    else if ((x == 1 && y == 3) || (x == 1 && y == 4) || (x == 1 && y == 0)){
      outputNotSupported();
      return;
    }
    else if (x == 3 && y == 4){
      JOptionPane.showMessageDialog(this,"Обратите внимание: десятичные дроби не поддерживаются в этой версии!","Обратите Внимание: Неподдерживаемая Операция", JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    verifyCommandValidity(false);
  }

  private void removeLastOperation(){
    command = command.substring(0, command.length()-1);
  }

  private boolean verifyCommandValidity(boolean isFinal){
    if (!InputVerification.hasBalancedBrackets(command, isFinal)){
      JOptionPane.showMessageDialog(this,"ошибка: у вас неверные пропорции скобок!","Ошибка: Неверный Ввод",JOptionPane.ERROR_MESSAGE);
      removeLastOperation();
      return false;
    }
    if (!InputVerification.hasRepeatedInvalidOperators(command)){
      JOptionPane.showMessageDialog(this,"Ошибка: Неверный Ввод! Вы не можете иметь повторяющиеся / недопустимые операторы.","Ошибка: Неверный Ввод",JOptionPane.ERROR_MESSAGE);
      removeLastOperation();
      return false;
    }
    return true;
  }

  public static String getCommand(){
    return new NumericalTokenizer().convertToSpacedNumericalFormat(command); //expression;
  }

  public static String getExpression(){
    return command; 
  }

  private void calculate(){
    if (!verifyCommandValidity(true))
      return;
    if (command.equals("")){
      JOptionPane.showMessageDialog(this,"Ошибка: вы не можете вычислить без ввода уравнения!","Фатальная Ошибка: Неверный Ввод",JOptionPane.ERROR_MESSAGE);
      return;
    }
    if (Settings.shouldDisplay())
      new StartValueSelection();
    else{
      while(true){
        String s = JOptionPane.showInputDialog("Пожалуйста, введите свое предположение. Только цифры!");
        try{
          if (s == null)
            return;
          guess = Double.parseDouble(s);
          Operation.setOperation(NewtonApp.getCommand());
          //if (Operation.derivative(guess) <= Operation.ACCURACY){ //ignore
          //  JOptionPane.showMessageDialog(null, "Error: The slope of the tangent is zero! Enter a valid guess point, or enter an expression with a possible root.", "Error: Slope is zero.", JOptionPane.PLAIN_MESSAGE);
          //}else{
          Operation.setOperation(NewtonApp.getCommand());
          double ans = Operation.compute(guess);
          
          if (ans == Integer.MAX_VALUE){
            JOptionPane.showMessageDialog(this, "Извините, не смог найти корень😞. Пожалуйста, введите допустимое разрешимое выражение.");
          }
          else{
            JOptionPane.showMessageDialog(this, "Решение найдено, есть корень: x =" + (ans));
          }
          break;
          
          //}
        }
        catch(NumberFormatException e){
          JOptionPane.showMessageDialog(null, "Ошибка", "Ошибка: Неверный Ввод Данных", JOptionPane.PLAIN_MESSAGE);
        }
        catch(NullPointerException e){
          return;
        }
      }
    }
  }

  @Override
  public void mousePressed(MouseEvent arg0) {
    int x = (arg0.getX()) / 58;
    int y = (arg0.getY() - 100) / 50;
    
    if (x < 0 || x > 7 || y < 0 || y > 4)
      return;
    
    turnedOn = ((x == 4 && y == 0) ? (true) : (false));
    turnedOff = ((x == 5 &&
                  y == 0) ? (true) : (false));
    drawHighlight = true;
    
    if (x >=4 && x <= 6 && y == 4)
      drawZero = true;
    else
      drawZero = false;
    
    highlightX = x;
    highlightY = y;
    
    if (off)
      return;
    
    if (x == 7 && y == 4){ //calculate button
      calculate();
      return; 
    }
    
    if (x == 6 && y == 0){
      command = "";
      expression = "";
    }
    
    if (command.length() > 18)
      return;
    
    updateCommand(x, y);
  }

  public void mouseReleased(MouseEvent arg0) {
    drawHighlight = false;
    repaint();
  }

  private boolean isNumber(String a){
    try{Integer.parseInt(a);return true;} catch(NumberFormatException e){return false;}
  }    

  public static void main(String[] args) {
    new NewtonApp();
  }

  public void mouseClicked(MouseEvent arg0) {}

  public void mouseEntered(MouseEvent arg0) {}

  public void mouseExited(MouseEvent arg0) {}
}
