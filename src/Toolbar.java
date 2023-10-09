import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;



class Toolbar extends JPanel implements Observer {

    private Model model;

    // Observer method
    public void updateObserver() {
        //update selected  to selected button
        // use .getRGB to get the precise color.
        int thick_index = model.getThickLevel();

        if(model.getSelectedShape() != null) {

            thicknessList.setSelectedIndex(thick_index);
            int preciseCol = model.getSelectedColor().getRGB();

            if(preciseCol == Color.WHITE.getRGB())
                whiteButton.setSelected(true);
            else if(preciseCol == Color.BLACK.getRGB())
                blackButton.setSelected(true);
            else if(preciseCol == Color.BLUE.getRGB())
                blueButton.setSelected(true);
            else if(preciseCol == Color.MAGENTA.getRGB())
                magentaButton.setSelected(true);
            else if(preciseCol == Color.YELLOW.getRGB())
                yellowButton.setSelected(true);
            else if(preciseCol == Color.RED.getRGB())
                redButton.setSelected(true);

        }
    }

    // from : https://docs.oracle.com/javase/7/docs/api/javax/swing/ListCellRenderer.html
    class ListRenderer extends JLabel implements ListCellRenderer {
        public ListRenderer() {
            setOpaque(true);
            setBackground(Color.blue);
            setHorizontalAlignment(CENTER);
            setBorder(BorderFactory.createLineBorder(Color.lightGray,1));

        }


        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

            Integer intobj=  (Integer)value;
            int selectedIndex = intobj.intValue();

            String images[] = {"images/thick1.png","images/thick2.png", "images/thick3.png", "images/thick4.png"};

            if (isSelected) {
                setBackground(list.getSelectionBackground());

                setForeground(list.getSelectionForeground());
            } else {
                setBackground(Color.WHITE);
                setForeground(list.getSelectionForeground());
            }

            try {
                Image img = ImageIO.read(getClass().getResource(images[selectedIndex]));
                ImageIcon icon = new ImageIcon(img);
                setIcon(icon);
            } catch (Exception e) {
                System.out.println(e);
            }
//
            return this;
        }
    }

    private  JToggleButton createToolButton(toolType tool, Icon icon, boolean selected) {
        JToggleButton button = new JToggleButton(icon, selected);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.clearSelectedShape();
                model.setSelectedTool(tool);
            }
        });
        return button;
    }

    private  JToggleButton createColorButton(Color color) {
        JToggleButton button = new JToggleButton("", false);

        if (color == color.BLACK) {
            button.setSelected(true);
        }
        button.setBackground(color);

        String addr = "images/";

        String defImages[] = {
                "blue.png",
                "red.png",
                "white.png",
                "yellow.png",
                "magenta.png",
                "black.png",
                };

        String selImages[] = {
                "sel_blue.png",
                "sel_red.png",
                "sel_white.png",
                "sel_yellow.png",
                "sel_magenta.png",
                "sel_black.png"
        };

        if(color == Color.BLUE) {
            try {
                Image imgIcon = ImageIO.read(getClass().getResource(addr+defImages[0]));
                Image imgSel = ImageIO.read(getClass().getResource(addr+selImages[0]));
                button.setIcon(new ImageIcon(imgIcon));
                button.setSelectedIcon(new ImageIcon(imgSel));

            } catch (Exception e) {
                System.out.println(e);
            }

        }
        else if(color == Color.RED) {
            try {
                Image imgIcon = ImageIO.read(getClass().getResource(addr+defImages[1]));
                Image imgSel = ImageIO.read(getClass().getResource(addr+selImages[1]));
                button.setIcon(new ImageIcon(imgIcon));
                button.setSelectedIcon(new ImageIcon(imgSel));

            } catch (Exception e) {
                System.out.println(e);
            }
        }
        else if(color == Color.WHITE) {
            try {
                Image imgIcon = ImageIO.read(getClass().getResource(addr+defImages[2]));
                Image imgSel = ImageIO.read(getClass().getResource(addr+selImages[2]));
                button.setIcon(new ImageIcon(imgIcon));
                button.setSelectedIcon(new ImageIcon(imgSel));

            } catch (Exception e) {
                System.out.println(e);
            }
        }
        else if(color == Color.YELLOW) {
            try {
                Image imgIcon = ImageIO.read(getClass().getResource(addr+defImages[3]));
                Image imgSel = ImageIO.read(getClass().getResource(addr+selImages[3]));
                button.setIcon(new ImageIcon(imgIcon));
                button.setSelectedIcon(new ImageIcon(imgSel));

            } catch (Exception e) {
                System.out.println(e);
            }
        }
        else if(color == Color.MAGENTA) {
            try {
                Image imgIcon = ImageIO.read(getClass().getResource(addr+defImages[4]));
                Image imgSel = ImageIO.read(getClass().getResource(addr+selImages[4]));
                button.setIcon(new ImageIcon(imgIcon));
                button.setSelectedIcon(new ImageIcon(imgSel));

            } catch (Exception e) {
                System.out.println(e);
            }
        }
        else if(color == Color.BLACK) {
            try {
                Image imgIcon = ImageIO.read(getClass().getResource(addr+defImages[5]));
                Image imgSel = ImageIO.read(getClass().getResource(addr+selImages[5]));
                button.setIcon(new ImageIcon(imgIcon));
                button.setSelectedIcon(new ImageIcon(imgSel));

            } catch (Exception e) {
                System.out.println(e);
            }
        }

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.setSelectedColor(button.getBackground());
                if(model.getSelectedTool() == toolType.SELECT) {
                    model.setSelectedShapeColor(model.getSelectedColor());
                }
            }
        });
       return button;
    }
    /////////////////// declare Color buttons and thickness buttons/////////////////////////////////////////////////////
    private JToggleButton whiteButton = createColorButton(Color.WHITE); //default

    private JToggleButton blackButton = createColorButton(Color.BLACK);

    private JToggleButton blueButton = createColorButton(Color.BLUE);

    private JToggleButton magentaButton = createColorButton(Color.MAGENTA);

    private JToggleButton yellowButton = createColorButton(Color.YELLOW);

    private JToggleButton redButton = createColorButton(Color.RED);

    private JList<Integer> thicknessList;
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /////Toolbar class /////////////////////////////////////////////////////////////////////////////////////////////////
    Toolbar(Model model_) {
        model = model_;

        ///// ToolBar General Outline Widgets///////////////////////////////////////////////////
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        MatteBorder innerBorder = BorderFactory.createMatteBorder(0,0,0,0,Color.GRAY);
        Border outerBorder = BorderFactory.createLineBorder(Color.GRAY,2);
        CompoundBorder widgetsBorder = BorderFactory.createCompoundBorder(innerBorder,outerBorder);
        this.setBorder(widgetsBorder);

        this.setPreferredSize(new Dimension(150,100));

        JToolBar toolsToolBar = new JToolBar(JToolBar.VERTICAL);
        this.add(toolsToolBar);

        JToolBar colorToolBar = new JToolBar(JToolBar.VERTICAL);
        this.add(colorToolBar);

        JToolBar thicknessToolBar = new JToolBar(JToolBar.VERTICAL);
        thicknessToolBar.setPreferredSize(new Dimension(100, 170));
        thicknessToolBar.setMinimumSize(new Dimension(100, 170));
        this.add(thicknessToolBar);

        JPanel tools = new JPanel(new GridLayout(3,2,4,4));
        toolsToolBar.add(tools);

        tools.setPreferredSize(new Dimension(100, 150));
        ButtonGroup toolButtons = new ButtonGroup();
        ///////////////////////////////////////////////////////////////////////////////////////////



        //// Creating tools for Tools widget////////////////////////////////////////////////////////
        JToggleButton selectTool;
        try {
            Image img = ImageIO.read(getClass().getResource("images/arrow.png"));
            selectTool = createToolButton(toolType.SELECT, new ImageIcon(img), true);
            toolButtons.add(selectTool);
            tools.add(selectTool);

        } catch (Exception ex) {
            System.out.println(ex);
        }

        JToggleButton eraseTool;
        try {
            Image img = ImageIO.read(getClass().getResource("images/eraser0.png"));
            eraseTool = createToolButton(toolType.ERASE, new ImageIcon(img), false);
            toolButtons.add(eraseTool);
            tools.add(eraseTool);

        } catch (Exception ex) {
            System.out.println(ex);
        }
        JToggleButton lineTool;
        try {
            Image img = ImageIO.read(getClass().getResource("images/line0.png"));
            lineTool = createToolButton(toolType.LINE, new ImageIcon(img), false);
            toolButtons.add(lineTool);
            tools.add(lineTool);

        } catch (Exception ex) {
            System.out.println(ex);
        }
        JToggleButton circleTool;
        try {
            Image img = ImageIO.read(getClass().getResource("images/circle0.png"));
            circleTool = createToolButton(toolType.CIRCLE,new ImageIcon(img), false);
            toolButtons.add(circleTool);
            tools.add(circleTool);

        } catch (Exception ex) {
            System.out.println(ex);
        }
        JToggleButton fillTool;
        try {
            Image img = ImageIO.read(getClass().getResource("images/paintbucket.png"));
            fillTool = createToolButton(toolType.FILL,new ImageIcon(img), false);
            toolButtons.add(fillTool);
            tools.add(fillTool);

        } catch (Exception ex) {
            System.out.println(ex);
        }
        JToggleButton rectangleTool;
        try {
            Image img = ImageIO.read(getClass().getResource("images/square.png"));
            rectangleTool = createToolButton(toolType.RECTANGLE,new ImageIcon(img), false);
            toolButtons.add(rectangleTool);
            tools.add(rectangleTool);

        } catch (Exception ex) {
            System.out.println(ex);
        }
        /////////////////////////////////////////////////////////////////////////////////////////////////

        /// setup Color Palette//////////////////////////////////////////////////////////////////////////
        GridLayout colorsGrid = new GridLayout(3,2,4,4);
        JPanel colors = new JPanel(colorsGrid);

        colors.setBorder(BorderFactory.createMatteBorder(0,0,0,0,Color.BLACK));
        colors.setPreferredSize(new Dimension(100, 150));
        ButtonGroup colorButtons = new ButtonGroup();


        colors.add(whiteButton);
        colorButtons.add(whiteButton);

        colors.add(blackButton);
        colorButtons.add(blackButton);


        colors.add(blueButton);
        colorButtons.add(blueButton);


        colors.add(magentaButton);
        colorButtons.add(magentaButton);

        colors.add(yellowButton);
        colorButtons.add(yellowButton);

        colors.add(redButton);
        colorButtons.add(redButton);


        colorToolBar.add(colors);

        JButton chooserButton = new JButton("Color Palette");
        chooserButton.setFont(new Font("Century", Font.PLAIN, 15));
        chooserButton.setAlignmentX(0.55F);

        chooserButton.setMaximumSize(new Dimension(155, 35));
        chooserButton.setMinimumSize(new Dimension(155, 35));


        chooserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setSelectedColor(JColorChooser.showDialog(null, "Pick a Color", Color.BLACK));
                if(model.getSelectedTool() == toolType.SELECT) {
                    model.setSelectedShapeColor(model.getSelectedColor());
                }
            }
        });
        colorToolBar.add(Box.createVerticalStrut(5));
        colorToolBar.add(chooserButton);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //// Setup line-thickness Widget///////////////////////////////////////////////////////////////////////////////

        Integer[] intArray = {0,1,2,3};
        thicknessList = new JList<Integer>(intArray);

        ListRenderer renderer = new ListRenderer();
        renderer.setPreferredSize(new Dimension(125, 35));

        thicknessList.setCellRenderer(renderer);
        thicknessList.setSelectionBackground(Color.lightGray);
        thicknessList.setSelectedIndex(0);

        thicknessList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {

                Object event = e.getSource();
                JList list =(JList)event;

                model.setThickLevel(list.getSelectedIndex());

                if(toolType.SELECT == model.getSelectedTool()) {

                    model.setSelectedShapeThickLevel(list.getSelectedIndex());
                }
            }
        });
        thicknessToolBar.add(thicknessList);
    }
    ///////////////////// end of Toolbar ///////////////////////////////////////////////////////////////////////////////
}
