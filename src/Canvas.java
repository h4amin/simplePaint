import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Canvas extends JPanel implements Observer {

    private int x;
    private int y;

    private Model model;

    private double canvasW = 1200;
    private double canvasH = 900;

    public void updateObserver() {
        FullSize.repaint();
        FitWin.repaint();
    }

    private void paintShapes(Graphics2D g2d, JPanel canvas) {
        double scaleWidth = canvas.getWidth() / canvasW;
        double scaleHeight = canvas.getHeight() / canvasH;
        double scale = Math.min(scaleWidth, scaleHeight);
        g2d.scale(scale, scale);

        ArrayList<aShape> shapeList = model.getShapeList();
        for (aShape shape: shapeList) {

            int shapeThickness = (shape.thickLevel+1)*2;

            if (shape.fillColor != null) {
                g2d.setColor(shape.fillColor);
                g2d.fill(shape.shapeType);
            }

            g2d.setStroke(new BasicStroke(shapeThickness));
            if(shape == model.getSelectedShape()) {
                float dash1[] = {10.0f};
                g2d.setStroke(new BasicStroke(shapeThickness,BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dash1 , 0));
            }

            g2d.setColor(shape.lineColor);
            g2d.draw(shape.shapeType);
            g2d.setStroke(new BasicStroke(3));

        }
        if(model.getSketchingShape() != null) {

            int thick = model.getThickLevel();
            g2d.setStroke(new BasicStroke(2*(1+thick)));

            g2d.setColor(model.getSelectedColor());
            g2d.draw(model.getSketchingShape().shapeType);
        }

    }
    private	class JPanel_ extends JPanel{    // for different canvas sizes : full size and fit to window

        public void paintComponent(Graphics graphic){

            super.paintComponent(graphic);
            Graphics2D g2d = (Graphics2D) graphic;
            paintShapes(g2d,this);
        }
    };

    // The two types of views
    private	JPanel_ FullSize = new JPanel_();
    private	JPanel_ FitWin = new JPanel_();


    Canvas(Model aModel) {
        model = aModel;

        ArrayList<toolType> drawTools = new ArrayList<toolType>() {
            {
                add(toolType.CIRCLE);
                add(toolType.RECTANGLE);
                add(toolType.LINE);
            }
        };

        this.setLayout(new CardLayout());
        this.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        FullSize.setBackground(Color.WHITE);
        FitWin.setBackground(Color.WHITE);

        FullSize.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        FitWin.setPreferredSize(new Dimension((int)canvasW,(int)canvasH));


        JPanel canvas0 = new JPanel(new BorderLayout());

        canvas0.setMinimumSize(new Dimension((int)canvasW,(int)canvasH));
        canvas0.setPreferredSize(new Dimension((int)canvasW,(int)canvasH));
        canvas0.setMaximumSize(new Dimension((int)canvasW,(int)canvasH));

        canvas0.add(FullSize, BorderLayout.CENTER);


        JPanel canvas1 = new JPanel();
        JPanel canvas2 = new JPanel();

        JScrollPane scroller = new JScrollPane(canvas2);
        scroller.setSize(new Dimension(800,640));
        this.add(scroller, "Full Size");

        canvas1.setLayout(new BoxLayout(canvas1, BoxLayout.Y_AXIS));
        canvas2.setLayout(new BoxLayout(canvas2, BoxLayout.X_AXIS));
        canvas2.add(canvas1);
        canvas2.setBackground(Color.GRAY);
        canvas1.add(canvas0);


        // handle resizing
        JPanel fitWindow = new JPanel(new FlowLayout(FlowLayout.LEFT));

        fitWindow.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {

                double scale =  Math.min(fitWindow.getWidth() / canvasW, fitWindow.getHeight() / canvasH);

                FitWin.setPreferredSize(new Dimension((int)(scale*canvasW), (int)(scale*canvasH)));
                fitWindow.revalidate();
            }
        });

        fitWindow.setBackground(Color.GRAY);
        fitWindow.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        fitWindow.add(FitWin);
        this.add(fitWindow, "Fit to Window");

        FullSize.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                toolType toolSelected = model.getSelectedTool();

                if(toolSelected == toolType.CIRCLE || toolSelected == toolType.LINE || toolSelected == toolType.RECTANGLE) {
                    model.addShape();
                }
            }

            public void mousePressed(MouseEvent e) {

                x = e.getX();
                y = e.getY();

                int curx = x;
                int cury = y;

                toolType toolSelected = model.getSelectedTool();

                if (toolSelected == toolType.ERASE){
                    model.erase(curx,cury);
                }
                else if (toolSelected == toolType.SELECT){
                    model.selectShape(curx,cury);
                }
                else if (model.getSelectedTool() == toolType.FILL){
                    model.fill(curx,cury);
                }
                else if(drawTools.contains(toolSelected)) {
                    model.sketchingShape(0,0,0,0);
                }
            }

        });
        FullSize.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {

                int curx = e.getX();
                int cury = e.getY();

                toolType toolSelected = model.getSelectedTool();
                if(drawTools.contains(toolSelected))  {

                    model.sketchingShape(x, curx, y, cury);
                }
                else if (toolSelected == toolType.SELECT) {

                    model.moveShape(curx, cury);
                }
            }
        });

        // mouse events for Fit to Window
        FitWin.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {

                double scaleW = FitWin.getWidth() / canvasW;
                double scaleH = FitWin.getHeight() / canvasH;

                x = (int) (e.getX()/scaleW);
                y = (int) (e.getY()/scaleH);

                int curx = x;
                int cury = y;

                toolType toolSelected = model.getSelectedTool();

                if(drawTools.contains(toolSelected)) { // for Rectangle, circle or line
                    model.sketchingShape(0,0,0,0);
                }
                else if (toolSelected == toolType.SELECT){

                    model.selectShape(curx,cury);
                }
                else if (toolSelected == toolType.ERASE){

                    model.erase(curx,cury);
                }
                else if (toolSelected == toolType.FILL){

                    model.fill(curx,cury);
                }
            }
            public void mouseReleased(MouseEvent e) {

                toolType toolSelected = model.getSelectedTool();

                if(drawTools.contains(toolSelected)) {
                    model.addShape();
                }
            }
        });
        FitWin.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {

                // for resizing
                double heightScale = FitWin.getHeight() /canvasH;
                double widthScale = FitWin.getWidth() /canvasW;

                int curx = (int) (e.getX()/widthScale);
                int cury =(int) (e.getY()/heightScale);

                toolType toolSelected = model.getSelectedTool();

                if (toolSelected == toolType.SELECT){
                    model.moveShape(curx, cury);
                }
                else if(drawTools.contains(toolSelected)) {
                    model.sketchingShape(x, curx, y, cury);
                }
            }
        });
    }
}
