/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sju.ee98.daq.ui.menu;

import edu.sju.ee98.daq.ui.Manager;
import edu.sju.ee98.daq.ui.screen.ScreenPanel;
import edu.sju.ee98.daq.ui.screen.grid.Axis;
import edu.sju.ee98.daq.ui.screen.grid.SampleGrid;
import edu.sju.ee98.daq.ui.text.Format;
import edu.sju.ee98.ni.daqmx.data.WaveData;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

/**
 *
 * @author Leo
 */
public class EditMenu extends JMenu implements ActionListener {

    private static final String LABEL = "edit";
    public final JMenuItem fftForwardItem;
    public final JMenuItem fftInverseItem;

    public EditMenu(ResourceBundle resource) {
        this.setText(Format.text(resource.getString(LABEL)));
        this.setMnemonic(Format.mnemonic(resource.getString(LABEL)));
        this.fftForwardItem = new DMenuItem(resource.getString(LABEL + ".fftf"), this);
        this.add(fftForwardItem);
        this.fftInverseItem = new DMenuItem(resource.getString(LABEL + ".ffti"), this);
        this.add(fftInverseItem);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(fftForwardItem)) {
            Logger.getLogger(EditMenu.class.getName()).log(Level.FINER, fftForwardItem.getText());
            Component selectedComponent = Manager.MANAGER.getMainFrame().work.getSelectedComponent();
            if (selectedComponent instanceof ScreenPanel) {
                ScreenPanel screen = (ScreenPanel) selectedComponent;
                System.out.println(screen.getWave());
                Complex[] transform = transform(screen.getWave().getData(), TransformType.FORWARD);
                System.out.println(Arrays.toString(transform));
                ScreenPanel fft = new ScreenPanel();
                fft.setLocation(0, 0);
                fft.setGrid(new SampleGrid(new Axis(0, 2), new Axis(0, 1)));
                fft.setWave(new FFTWave(transform));

                fft.setDropTarget(null);
                fft.repaint();
                Manager.MANAGER.getMainFrame().work.addTab(fft);
                Manager.MANAGER.getMainFrame().work.setSelectedComponent(fft);
            }
        } else if (e.getSource().equals(fftInverseItem)) {
            Logger.getLogger(EditMenu.class.getName()).log(Level.FINER, fftInverseItem.getText());
            Component selectedComponent = Manager.MANAGER.getMainFrame().work.getSelectedComponent();
            if (selectedComponent instanceof ScreenPanel) {
                ScreenPanel screen = (ScreenPanel) selectedComponent;
                System.out.println(screen.getWave());
                Complex[] transform = transform(screen.getWave().getData(), TransformType.INVERSE);
                System.out.println(Arrays.toString(transform));
                ScreenPanel fft = new ScreenPanel();
                fft.setLocation(0, 0);
                fft.setGrid(new SampleGrid(new Axis(0, 2), new Axis(0, 1)));
                fft.setWave(new FFTWave(transform));

                fft.setDropTarget(null);
                fft.repaint();
                Manager.MANAGER.getMainFrame().work.addTab(fft);
                Manager.MANAGER.getMainFrame().work.setSelectedComponent(fft);
            }
        }
    }

    public class FFTWave implements WaveData {

//        private double[] data;
        private Complex[] data;

        public FFTWave(Complex[] data) {
            this.data = data;
        }

        @Override
        public Complex[] getData() {
            return data;
        }

        @Override
        public double[] getDoubleArray() {

            double[] tempConversion = new double[data.length];
            for (int i = 0; i < data.length; i++) {
                System.out.println(data[i]);
                double rr = (data[i].getReal());
                double ri = (data[i].getImaginary());

//                tempConversion[i] = Math.sqrt((rr * rr) + (ri * ri));
                tempConversion[i] = rr;
            }
            return tempConversion;
        }

        public void setData(Complex[] data) {
            this.data = data;
        }
    }

    private static Complex[] transform(Object input, TransformType type) {

//        double[] tempConversion = new double[input.length];

        FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
        try {
            Complex[] complx = null;
            if (input instanceof double[]) {
                double[] data = (double[]) input;
//                Double[] in = (Double[]) input;
//                double[] data = new double[in.length];
//                for (int i = 0; i < in.length; i++) {
//                    data[i] = in[i];
//                }
                complx = transformer.transform(data, type);
            } else if (input instanceof Complex[]) {
                Complex[] data = (Complex[]) input;
                complx = transformer.transform(data, type);
            }
//            Complex[] complx2 = transformer.transform(complx, TransformType.INVERSE);

//            for (int i = 0; i < complx.length; i++) {
//                System.out.println(complx[i]);
//                double rr = (complx[i].getReal());
//                double ri = (complx[i].getImaginary());
//
//                tempConversion[i] = Math.sqrt((rr * rr) + (ri * ri));
//            }
//            System.out.println(Arrays.toString(complx2));
            return complx;
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }
        return null;
    }
}
