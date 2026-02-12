import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

public class TimerExample extends JFrame implements ActionListener {
    private JLabel timerLabel1;
    private JLabel timerLabel2;
    private JButton switchButton;
    private Timer timer1;
    private Timer timer2;
    private boolean isTimer1Active;
    int time1=0;
    public TimerExample() {
        setTitle("Timer Example");
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 200);

        timerLabel1 = new JLabel();
        timerLabel2 = new JLabel();
        switchButton = new JButton("Switch");

        timer1 = new Timer();
        timer2 = new Timer();
        isTimer1Active = true;

        switchButton.addActionListener(this);

        add(timerLabel1);
        add(timerLabel2);
        add(switchButton);

        setVisible(true);
        startTimers();
    }

    private void startTimers() {
        timer1.scheduleAtFixedRate(new TimerTask() {


            @Override
            public void run() {
                if (isTimer1Active) {
                    timerLabel1.setText("Timer 1: " + time1 + " seconds");
                    time1++;
                }
            }
        }, 0, 1000);

        timer2.scheduleAtFixedRate(new TimerTask() {
            public   int secondsPassed ;

            @Override
            public void run() {
                if (!isTimer1Active) {
                    timerLabel2.setText("Timer 2: " + secondsPassed + " seconds");
                    secondsPassed++;
                }
            }
        }, 0, 1000);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == switchButton) {
            isTimer1Active = !isTimer1Active;
            switchTimers();
        }
    }

    private void switchTimers() {
        if (isTimer1Active) {
            timer1 = new Timer();
            timer2.cancel();
        } else {
            timer2 = new Timer();
            timer1.cancel();
        }
        startTimers();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TimerExample();
            }
        });
    }
}
