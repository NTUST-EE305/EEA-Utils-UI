/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sju.ee98.daq.ui.menu.file;

import edu.sju.ee98.daq.ui.fft.DialogPanel;
import edu.sju.ee98.ni.daqmx.NIAnalogConfig;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Leo
 */
public class FrequencyResponsePane extends DialogPanel {

    private NIAnalogConfig config;
    private JInput physicalChannel = new JInput("Physical Channel");
    private JInput minVoltage = new JInput("Min Voltage");
    private JInput maxVoltage = new JInput("Max Voltage");
    private JInput rate = new JInput("Rate");
    private JInput length = new JInput("Length");

    public FrequencyResponsePane() {
        super(null);
        this.setSize(300, 400);
        initComponents();
    }

    private void testValue() {
        physicalChannel.setText("Dev1/ai0");
        minVoltage.setText("-20");
        maxVoltage.setText("20");
        rate.setText("10000");
        length.setText("1024");
    }

    private void initComponents() {
        testValue();
        physicalChannel.setLocation(50, 50);
        this.add(physicalChannel);
        minVoltage.setLocation(50, 100);
        this.add(minVoltage);
        maxVoltage.setLocation(50, 150);
        this.add(maxVoltage);
        rate.setLocation(50, 200);
        this.add(rate);
        length.setLocation(50, 250);
        this.add(length);

        JButton finishButton = new JButton("Finish");
        finishButton.setBounds(100, 300, 100, 30);
        finishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    config = new NIAnalogConfig(
                            physicalChannel.getText(),
                            Double.parseDouble(minVoltage.getText()), Double.parseDouble(maxVoltage.getText()),
                            Double.parseDouble(rate.getText()), Long.parseLong(length.getText()));
                } catch (java.lang.NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Please input a Integer!", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                dialog.dispose();
            }
        });
        this.add(finishButton);

    }

    public NIAnalogConfig getConfig() {
        return config;
    }

    private void add(JInput input) {
        this.add(input.label);
        this.add(input.textField);
    }

    private class JInput {

        protected JLabel label = new JLabel();
        protected JTextField textField = new JTextField();

        private JInput(String text) {
            this.label.setSize(100, 25);
            this.textField.setSize(100, 25);
            label.setText(text);
        }

        private void setLocation(int x, int y) {
            this.label.setLocation(x, y);
            this.textField.setLocation(x + 100, y);
        }

        private void setText(String text) {
            this.textField.setText(text);
        }

        private String getText() {
            return this.textField.getText();
        }
    }
}
