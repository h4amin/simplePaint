import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.*;


enum toolType {
    ERASE, SELECT, CIRCLE, RECTANGLE, LINE, FILL
}

class aShape implements Serializable {

    Shape shapeType;

    Color fillColor = null;
    Color lineColor;
    Integer thickLevel;



    aShape(Shape aNewShape, Integer newThickLevel, Color newlineColor) { // constructor

        shapeType = aNewShape;
        thickLevel = newThickLevel;
        lineColor = newlineColor;
    };
}

public class Model {

    // holds are Shape objects
    private ArrayList<aShape> listofShapes = new ArrayList<aShape>();
    // holds Toobar and Canvas views
    private ArrayList<Observer> observers = new ArrayList<Observer>();

    private Color selectedColor = Color.BLACK; // default starting color
    private toolType selectedTool = toolType.SELECT;

    private Integer selectedThickLevel = 0;
    private aShape selectedShape = null;
    private aShape curSketchShape = null;


    public void addObserver(Observer obs) {
        observers.add(obs);
        obs.updateObserver();
    }
    private void notifyObservers() {
        for (Observer obs : observers) {

            obs.updateObserver();
        }
    }

    public void setSelectedTool(toolType tool) {
        selectedTool = tool;
        notifyObservers();
    }


    public void setThickLevel(int thickLevel) {
        selectedThickLevel = thickLevel;
        notifyObservers();
    }

    public void setSelectedShapeColor(Color color) {
        if(selectedShape != null) {
            selectedShape.lineColor = color;
        }
        notifyObservers();
    }

    public void setSelectedShapeThickLevel(int thickLevel) {
        if(selectedShape != null) {
            selectedShape.thickLevel = thickLevel;
        }
        notifyObservers();
    }

    public void setSelectedColor(Color color) {
        selectedColor = color;
        notifyObservers();
    }

    public aShape getSelectedShape() {
        return selectedShape;
    }

    public ArrayList<aShape> getShapeList() {
        return listofShapes;
    }

    public toolType getSelectedTool() {
        return selectedTool;
    }
    public Integer getThickLevel() {
        return selectedThickLevel;
    }

    public aShape getSketchingShape() {
        return curSketchShape;
    }

    public Color getSelectedColor() {
        return selectedColor;
    }


    public void newSketch() {
        listofShapes = new ArrayList<aShape>();
        notifyObservers();
    }

    public void loadShapes(ArrayList<aShape> loadedListofShapes) {

        listofShapes = loadedListofShapes;

        notifyObservers();
    }

    public void sketchingShape(int x1, int x2, int y1, int y2) {

        int y = Math.min(y2, y1);
        int x = Math.min(x1, x2);

        int width = Math.abs(x2 - x1);
        int height = Math.abs(y1 - y2);

        if(selectedTool == toolType.LINE) {

            curSketchShape = new aShape(new Line2D.Float(x1,y1,x2,y2), selectedThickLevel, selectedColor);
        }

        else if(selectedTool == toolType.RECTANGLE) {

            curSketchShape = new aShape(new Rectangle2D.Float(x,y,width,height), selectedThickLevel, selectedColor);
        }
        else if(selectedTool == toolType.CIRCLE) {

            int size = Math.max(width, height);
            curSketchShape = new aShape(new Ellipse2D.Float(x,y,size,size), selectedThickLevel, selectedColor);
        }

        notifyObservers();
    }

    public void addShape() {

        listofShapes.add(curSketchShape);
        curSketchShape = null;

        notifyObservers();
    }

    // for relative shape movements from where user clicked
    double Xoffset;
    double Yoffset;

    public void clearSelectedShape() {

        selectedShape = null;
        notifyObservers();

    }

    public void fill(int x, int y) {

        int lenshapeList = listofShapes.size();

        for(int i = 0; i < lenshapeList ; i++) {

            boolean containsCoord = listofShapes.get(i).shapeType.contains(x, y);

            if (containsCoord) {

                listofShapes.get(i).fillColor = selectedColor;
                break;
            }
        }
        notifyObservers();
    }

    public void moveShape(int x, int y) {

        if(selectedShape != null) {

            AffineTransform trans = new AffineTransform();
            Rectangle hitbox = selectedShape.shapeType.getBounds();

            double prevX = hitbox.getX();
            double prevY = hitbox.getY();

            trans.translate(Xoffset - prevX + x, Yoffset + y - prevY);

            selectedShape.shapeType = trans.createTransformedShape(selectedShape.shapeType);
        }
        notifyObservers();
    }

    public void selectShape(int x, int y) {
        int lenShapeList = listofShapes.size() -1;

        for(int i = lenShapeList; i > -1; i--) {

            aShape theShape = listofShapes.get(i);

            if (theShape.shapeType.intersects(x-3,y-3,6,6)) {

                clearSelectedShape();

                selectedShape = theShape;
                selectedThickLevel = theShape.thickLevel;
                selectedColor = theShape.lineColor;

                break;
            }
        }

        if(selectedShape != null) {

            Rectangle hitbox = selectedShape.shapeType.getBounds();

            Xoffset = hitbox.getX() - x;
            Yoffset = hitbox.getY() - y;
        }

        notifyObservers();
    }

    public void erase(int x, int y) {

        for(int i = listofShapes.size()-1; i >= 0; i--) {

            aShape tmp = listofShapes.get(i);

            if (tmp.shapeType.intersects(x-3,y-3,6,6)) {

                listofShapes.remove(i);
                break;
            }
        }
        notifyObservers();
    }


}
