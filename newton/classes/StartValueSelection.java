package classes;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.text.*;
// НЕ РАБОТАЕТ ВЫВОД ПРОИЗВОДНОЙ НА ЭКРАН ЮЗЕРА//
@SuppressWarnings("serial")
public class StartValueSelection extends JFrame implements MouseListener{
  JTable panel = new JTable();
  Image backgroundImg;
  double guess;
  double ans = Double.MAX_VALUE;
  static int iterations = Integer.MAX_VALUE;

  public StartValueSelection(){
    super("Начальные Значения");
    addMouseListener(this);
    setResizable(false);
    useGivenValue();
  }

  private void useGivenValue(){
    while(true){
      String s = JOptionPane.showInputDialog("Пожалуйста, введите свое предположение. Только цифры!");
      try{
        if (s == null){
          setVisible(false);
          return;
        }
        guess = Double.parseDouble(s);
        Operation.setOperation(NewtonApp.getCommand());
        if (Operation.derivative(guess) <= Operation.ACCURACY){
          JOptionPane.showMessageDialog(null, "Ошибка: наклон касательной равен нулю! Введите допустимую точку предположения или введите выражение с возможным корнем.", "Ошибка: наклон равен нулю.", JOptionPane.PLAIN_MESSAGE);
        }else
          break;
      }
      catch(NullPointerException e){
        setVisible(false);
        return;
      }
    }
    setSize(500, 300);
    setVisible(true);
  }

  public void drawHighlight(Graphics g){}

  public void adjustGuess(){
    while(true){
      String s = JOptionPane.showInputDialog("Пожалуйста, введите свое предположение. Только цифры!");
      try{
        if (s == null){
          setVisible(false);
          return;
        }
        guess = Double.parseDouble(s);
        Operation.setOperation(NewtonApp.getCommand());
        if (Operation.derivative(guess) <= Operation.ACCURACY){
          JOptionPane.showMessageDialog(null, "Ошибка: наклон касательной равен нулю! Введите допустимую точку предположения или введите выражение с возможным корнем.", "Ошибка: наклон равен нулю.", JOptionPane.PLAIN_MESSAGE);
        }else
          break;
      }
      catch(NumberFormatException e){
        
        JOptionPane.showMessageDialog(null, "Ошибка", "Ошибка: Неверный Ввод Данных", JOptionPane.PLAIN_MESSAGE);
      }
      catch(NullPointerException e){
        setVisible(false);
        return;
      }
    }
    
  }

  public void compute(){
    Operation.setOperation(NewtonApp.getCommand());
    ans = Operation.compute(guess);
    
    if (ans == Integer.MAX_VALUE)
      JOptionPane.showMessageDialog(this, "Ошибка: тайм-аут. Пожалуйста, попробуйте еще раз угадать выражение.");
    else
      JOptionPane.showMessageDialog(this, "Решение найдено, есть корень по адресу: x =" + (Operation.compute(guess)));
    
  }

  public void fetchImage(){
    try{
      backgroundImg = ImageIO.read(new File("images/StartStopValue.png"));
    }
    catch(IOException e){
      JOptionPane.showMessageDialog(this, "Ошибка: не удалось найти файл изображения!");
    }
  }

  public void drawText(Graphics g){
    g.setFont(new Font("Helvetica", Font.PLAIN, 30));
    g.setColor(Color.white);
    DecimalFormat dF = new DecimalFormat("0.00000000");
    g.drawString((ans == Integer.MAX_VALUE) ? ("Unknown") : (dF.format(ans)), 255, 103);
    g.drawString(guess+ "", 255, 148);
    
    Operation.setOperation(NewtonApp.getCommand());
    dF = new DecimalFormat("0.000000");
    g.drawString(dF.format(Operation.derivative(guess)), 338, 57);
    
    g.drawString((iterations == Integer.MAX_VALUE) ? ("Unknown") : (iterations + ""), 255, 192);
    
    if (NewtonApp.getCommand().length() > 10)
      g.drawString((NewtonApp.getCommand()+ "").substring(0, 10), 80, 57);
    else
      g.drawString((NewtonApp.getCommand()+ ""), 80, 57);
    
  }

  public void paint(Graphics g){
    super.paint(g);
    fetchImage();
    
    g.drawImage(backgroundImg, 0, 0, null);
    drawHighlight(g);
    drawText(g);
  }

  private void identifyStartingValue(){
    System.out.println("Эта функция еще не доступна!");
  }

  public void mousePressed(MouseEvent e) {}

  public void mouseReleased(MouseEvent arg0) {
    repaint();
  }

  public void mouseClicked(MouseEvent e) {
    System.out.println(e.getX() + " " + e.getY());
    int x = e.getX();
    int y = e.getY();
    
    if (x >= 11 && x <= 186 && y >= 220 && y <= 284)
      adjustGuess();
    else if (x >= 220 && x <= 395 && y >= 220 && y <=284)
      compute();
    else if (x >= 426 && x <= 489 && y >= 219 && y <= 297)
      setVisible(false);
    
  }

  public void mouseEntered(MouseEvent arg0) {}

  public void mouseExited(MouseEvent arg0) {}
}
