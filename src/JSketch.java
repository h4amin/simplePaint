import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.io.*;


public class JSketch {
    public static void main(String[] args) {

        Model model = new Model();

        Toolbar toolbar = new Toolbar(model);
        Canvas canvas = new Canvas(model);

        model.addObserver(canvas);
        model.addObserver(toolbar);


        JFrame frame = new JFrame("JSketch: A Simple Paint App");

        JFileChooser fileChoice = new JFileChooser();

        JMenuBar menubar = new JMenuBar();

        frame.setJMenuBar(menubar);



        JMenu file = new JMenu("File");
        JMenuItem new_ = new JMenuItem("New");
        JMenuItem load_ = new JMenuItem("Load");
        JMenuItem save_ = new JMenuItem("Save");

        JMenu menuview = new JMenu("View");

        // new
        new_.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.newSketch();
            }
        });

        //save
        save_.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int ret = fileChoice.showSaveDialog(frame);

                if (ret == JFileChooser.APPROVE_OPTION) {

                    File file = fileChoice.getSelectedFile();
                    String filePath = file.getAbsolutePath();
                    String str = filePath + ".sketch";
                    file = new File(str);

                    try {

                        ObjectOutputStream objectStream = new ObjectOutputStream(new FileOutputStream(file));
                        objectStream.writeObject(model.getShapeList());
                        objectStream.close();

                    }  catch (IOException ex) {

                        System.out.println("ERROR");

                    }
                }
            }
        });

        //load
        load_.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int ret = fileChoice.showOpenDialog(frame);

                if (ret == JFileChooser.APPROVE_OPTION) {

                    File file = fileChoice.getSelectedFile();
                    String name = file.getName();

                    if (name.endsWith(".sketch")) {

                        try {
                            ObjectInputStream objectStream = new ObjectInputStream(new FileInputStream(file));
                            ArrayList<aShape> result = (ArrayList<aShape>) objectStream.readObject();

                            model.loadShapes(result);
                            objectStream.close();

                        } catch (FileNotFoundException ex) {
                            System.out.println("ERROR: file not found");

                        } catch (ClassNotFoundException ex) {
                            System.out.println("ERROR: class not found");

                        }catch (IOException ex) {
                            System.out.println("ERROR");

                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "incorrect file format");
                    }
                }
            }
        });

        JRadioButtonMenuItem fitWinButton = new JRadioButtonMenuItem("Fit to Window");
        JRadioButtonMenuItem fullSizeButton = new JRadioButtonMenuItem("Full Size");

        fitWinButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout cardlayout = (CardLayout)(canvas.getLayout());
                cardlayout.show(canvas, "Fit to Window");
            }
        });

        fullSizeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout cardlayout = (CardLayout)(canvas.getLayout());
                cardlayout.show(canvas, "Full Size");
            }
        });

        ButtonGroup Butgroup = new ButtonGroup();
        fullSizeButton.setSelected(true);
        Butgroup.add(fullSizeButton);
        menuview.add(fullSizeButton);


        Butgroup.add(fitWinButton);
        menuview.add(fitWinButton);

        menubar.add(file);
        file.add(new_);
        file.add(load_);
        file.add(save_);


        menubar.add(menuview);

        // to set up core layout
        Container mainPane = frame.getContentPane();
        BoxLayout mainLayout = new BoxLayout(mainPane, BoxLayout.X_AXIS);

        mainPane.setLayout(mainLayout);
        mainPane.add(toolbar);
        mainPane.add(canvas);

        frame.setVisible(true);
        frame.setMinimumSize(new Dimension(800,650));
        frame.setPreferredSize(new Dimension(800,650));
    }
}