package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SimulationFrame extends JFrame {

    private JTextField noClientsInput = new JTextField(5);
    private JTextField noServersInput = new JTextField(5);
    private JTextField simulationInterval = new JTextField(5);
    private JTextField minArrivalTime = new JTextField(5);
    private JTextField maxArrivalTime = new JTextField(5);
    private JTextField minServiceTime = new JTextField(5);
    private JTextField maxServiceTime = new JTextField(5);
    private JButton startBtn = new JButton("Start");
    private JButton stopBtn = new JButton("Stop");

    public JTextArea textArea;

    public SimulationFrame()
    {
        JPanel inputPanel1 = new JPanel();          // pt clients si server
        JLabel clientsLabel = new JLabel("Clients");
        JLabel serversLabel = new JLabel("Servers");
        inputPanel1.add(clientsLabel);
        inputPanel1.add(noClientsInput);
        inputPanel1.add(serversLabel);
        inputPanel1.add(noServersInput);
        inputPanel1.setLayout(new FlowLayout());

        JPanel inputPanel2 = new JPanel();          // pt arrival time
        JLabel minArrivalLabel = new JLabel("Minimum arrival time");
        JLabel maxArrivalLabel = new JLabel("Maximum arrival time");
        inputPanel2.add(minArrivalLabel);
        inputPanel2.add(minArrivalTime);
        inputPanel2.add(maxArrivalLabel);
        inputPanel2.add(maxArrivalTime);
        inputPanel2.setLayout(new FlowLayout());

        JPanel inputPanel3 = new JPanel();          // pt service time
        JLabel minServiceLabel = new JLabel("Minimum service time");
        JLabel maxServiceLabel = new JLabel("Maximum service time");
        inputPanel3.add(minServiceLabel);
        inputPanel3.add(minServiceTime);
        inputPanel3.add(maxServiceLabel);
        inputPanel3.add(maxServiceTime);
        inputPanel3.setLayout(new FlowLayout());

        JPanel inputPanel4 = new JPanel();          // pt timp
        JLabel simulationTimeLabel = new JLabel("Simulation Time");
        inputPanel4.add(simulationTimeLabel);
        inputPanel4.add(simulationInterval);
        inputPanel4.setLayout(new FlowLayout());

        JPanel inputPanel5 = new JPanel();          // butoane
        inputPanel5.add(startBtn);
        inputPanel5.add(stopBtn);
        inputPanel5.setLayout(new FlowLayout());

        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new BorderLayout(0,0));
        textArea=new JTextArea(10,10);
        textArea.setEditable(false);
        outputPanel.add(textArea);

        JScrollPane scrollPane = new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        outputPanel.add(scrollPane);
        setContentPane(outputPanel);

        JPanel content=new JPanel();      // panel principal
        content.add(inputPanel1);
        content.add(inputPanel2);
        content.add(inputPanel3);
        content.add(inputPanel4);
        content.add(inputPanel5);
        content.add(outputPanel);
        content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));

        this.setContentPane(content);
        this.setTitle("Queue Simulation");
        this.setSize(800,600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public String getNoOfClients() { return noClientsInput.getText(); }
    public String getNoOfServers() { return noServersInput.getText(); }
    public String getMinArrivalTimeInput() { return minArrivalTime.getText(); }
    public String getMaxArrivalTimeInput() { return maxArrivalTime.getText(); }
    public String getMinServiceTimeInput() { return minServiceTime.getText(); }
    public String getMaxServiceTimeInput() { return maxServiceTime.getText(); }
    public String getSimulationTimeInput() { return simulationInterval.getText(); }

    public void showError(String errorMessage) { JOptionPane.showMessageDialog(this,errorMessage); }

    public void addStartListener(ActionListener sal) { startBtn.addActionListener(sal); }
}
