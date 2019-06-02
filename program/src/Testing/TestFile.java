package Testing;


import PaintTool.SquarePadDrawing;
import PaintTool.VecFileException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestFile {

    @Test
    public void TestShape(){

    }

    @Test
    public void readFileRelativePath() {
        File file = new File("src/Testing/vecFile/check.txt");
        assertTrue(file.exists());
    }

    @Test
    public void invalidShape(){
        String line = "MANH 0.6 0.8";
        Assertions.assertThrows(VecFileException.class, () -> {SquarePadDrawing.checkLine(line);});
    }

    @Test
    public void invalidShapeMessage(){
        String line = "MANH 0.6 0.8";
        VecFileException thrown =
                Assertions.assertThrows(VecFileException.class,
                        () -> SquarePadDrawing.checkLine(line));
        Assertions.assertEquals("Invalid vec format: Wrong shapes", thrown.getMessage());
    }

    @Test
    public void fill_invalidColor() {
        String line = "FILL uWu";
        Assertions.assertThrows(VecFileException.class, () -> {SquarePadDrawing.checkLine(line);});
    }

    @Test
    public void fill_invalidLength() {
        String line = "FILL #FFFFFF YES";
        Assertions.assertThrows(VecFileException.class, () -> {SquarePadDrawing.checkLine(line);});
    }

    @Test
    public void pen_invalidColor() {
        String line = "PEN uWu";
        Assertions.assertThrows(VecFileException.class, () -> {SquarePadDrawing.checkLine(line);});
    }

    @Test
    public void pen_invalidLength() {
        String line = "PEN #FFFFFF YES";
        Assertions.assertThrows(VecFileException.class, () -> {SquarePadDrawing.checkLine(line);});
    }

    @Test
    public void plot_invalidLength() {
        String line = "PLOT 0.8 0.1 0.9";
        Assertions.assertThrows(VecFileException.class, () -> {SquarePadDrawing.checkLine(line);});
    }

    @Test
    public void plot_invalidCoordiatesNumber() {
        String line = "PLOT 0.1 0.3 0.4 6";
        Assertions.assertThrows(VecFileException.class, () -> {SquarePadDrawing.checkLine(line);});
    }

    @Test
    public void plot_invalidCoordiatesString() {
        String line = "PLOT 0.1 0.3 0.4 uWu";
        Assertions.assertThrows(VecFileException.class, () -> {SquarePadDrawing.checkLine(line);});
    }

    @Test
    public void line_invalidLength() {
        String line = "LINE 0.8 0.1 0.9";
        Assertions.assertThrows(VecFileException.class, () -> {SquarePadDrawing.checkLine(line);});
    }

    @Test
    public void line_invalidCoordiatesNumber() {
        String line = "LINE 0.1 0.3 0.4 6";
        Assertions.assertThrows(VecFileException.class, () -> {SquarePadDrawing.checkLine(line);});
    }

    @Test
    public void line_invalidCoordiatesString() {
        String line = "LINE 0.1 0.3 0.4 uWu";
        Assertions.assertThrows(VecFileException.class, () -> {SquarePadDrawing.checkLine(line);});
    }



    @Test
    public void line_invalidLengthMessage() {
        String line = "LINE 0.8 0.1 0.9";
        VecFileException thrown =
                Assertions.assertThrows(VecFileException.class,
                        () -> SquarePadDrawing.checkLine(line));
        Assertions.assertEquals("Invalid vec format for LINE. Correct format: LINE [X0] [Y0] [X1] [Y1].", thrown.getMessage());
    }

    @Test
    public void line_invalidCoordinatesMessage() {
        String line = "LINE 0.8 0.9 0.3 2";
        VecFileException thrown =
                Assertions.assertThrows(VecFileException.class,
                        () -> SquarePadDrawing.checkLine(line));
        Assertions.assertEquals("Invalid vec format: Coordinates have to be numeric and in the range [0, 1].", thrown.getMessage());
    }


    @Test
    public void rectangle_invalidLength() {
        String line = "RECTANGLE 0.8 0.1 0.9 0.3 0.6";
        Assertions.assertThrows(VecFileException.class, () -> {SquarePadDrawing.checkLine(line);});
    }

    @Test
    public void rectangle_invalidCoordiatesNumber() {
        String line = "RECTANGLE 0.1 0.3 0.4 6";
        Assertions.assertThrows(VecFileException.class, () -> {SquarePadDrawing.checkLine(line);});
    }

    @Test
    public void rectangle_invalidCoordiatesString() {
        String line = "RECTANGLE 0.1 0.3 0.4 uWu";
        Assertions.assertThrows(VecFileException.class, () -> {SquarePadDrawing.checkLine(line);});
    }



    @Test
    public void rectangle_invalidLengthMessage() {
        String line = "RECTANGLE 0.8 0.1 0.9";
        VecFileException thrown =
                Assertions.assertThrows(VecFileException.class,
                        () -> SquarePadDrawing.checkLine(line));
        Assertions.assertEquals("Invalid vec format for RECTANGLE. Correct format: RECTANGLE [X0] [Y0] [X1] [Y1].", thrown.getMessage());
    }

    @Test
    public void rectangle_invalidCoordinatesMessage() {
        String line = "RECTANGLE 0.8 0.9 0.3 2";
        VecFileException thrown =
                Assertions.assertThrows(VecFileException.class,
                        () -> SquarePadDrawing.checkLine(line));
        Assertions.assertEquals("Invalid vec format: Coordinates have to be numeric and in the range [0, 1].", thrown.getMessage());
    }


    @Test
    public void ellipse_invalidLength() {
        String line = "ELLIPSE 0.8 0.1 0.9";
        Assertions.assertThrows(VecFileException.class, () -> {SquarePadDrawing.checkLine(line);});
    }

    @Test
    public void ellipse_invalidCoordiatesNumber() {
        String line = "ELLIPSE 0.1 0.3 0.4 6";
        Assertions.assertThrows(VecFileException.class, () -> {SquarePadDrawing.checkLine(line);});
    }

    @Test
    public void ellipse_invalidCoordiatesString() {
        String line = "ELLIPSE 0.1 0.3 0.4 uWu";
        Assertions.assertThrows(VecFileException.class, () -> {SquarePadDrawing.checkLine(line);});
    }



    @Test
    public void ellipse_invalidLengthMessage() {
        String line = "ELLIPSE 0.8 0.1 0.9";
        VecFileException thrown =
                Assertions.assertThrows(VecFileException.class,
                        () -> SquarePadDrawing.checkLine(line));
        Assertions.assertEquals("Invalid vec format for ELLIPSE. Correct format: ELLIPSE [X0] [Y0] [X1] [Y1].", thrown.getMessage());
    }

    @Test
    public void ellipse_invalidCoordinatesMessage() {
        String line = "ELLIPSE 0.8 0.9 0.3 2";
        VecFileException thrown =
                Assertions.assertThrows(VecFileException.class,
                        () -> SquarePadDrawing.checkLine(line));
        Assertions.assertEquals("Invalid vec format: Coordinates have to be numeric and in the range [0, 1].", thrown.getMessage());
    }


    @Test
    public void polygon_invalidLength() {
        String line = "POLYGON 0.8 0.1 0.9 0.5 0.6 0.8 0.9";
        Assertions.assertThrows(VecFileException.class, () -> {SquarePadDrawing.checkLine(line);});
    }

    @Test
    public void polygon_invalidShortLength() {
        String line = "POLYGON 0.8";
        Assertions.assertThrows(VecFileException.class, () -> {SquarePadDrawing.checkLine(line);});
    }

    @Test
    public void polygon_invalidCoordiatesNumber() {
        String line = "POLYGON 0.1 0.3 0.4 6 0.7 0.8";
        Assertions.assertThrows(VecFileException.class, () -> {SquarePadDrawing.checkLine(line);});
    }

    @Test
    public void polygon_invalidCoordiatesString() {
        String line = "POLYGON 0.1 0.3 0.4 uWu 0.8 uWu";
        Assertions.assertThrows(VecFileException.class, () -> {SquarePadDrawing.checkLine(line);});
    }



    @Test
    public void polygon_invalidLengthMessage() {
        String line = "POLYGON 0.8 0.1 0.9";
        VecFileException thrown =
                Assertions.assertThrows(VecFileException.class,
                        () -> SquarePadDrawing.checkLine(line));
        Assertions.assertEquals("Invalid vec format for POLYGON. Correct format: POLYGON [X0] [Y0] [X1] [Y1] ... X[2n] Y[2n].", thrown.getMessage());
    }

    @Test
    public void polygon_invalidCoordinatesMessage() {
        String line = "POLYGON 0.8 0.9 0.3 2";
        VecFileException thrown =
                Assertions.assertThrows(VecFileException.class,
                        () -> SquarePadDrawing.checkLine(line));
        Assertions.assertEquals("Invalid vec format: Coordinates have to be numeric and in the range [0, 1].", thrown.getMessage());
    }



    @Test
    public void plot_invalidLengthMessage() {
        String line = "PLOT 0.8 0.1 0.9";
        VecFileException thrown =
                Assertions.assertThrows(VecFileException.class,
                        () -> SquarePadDrawing.checkLine(line));
        Assertions.assertEquals("Invalid vec format for PLOT. Correct format: PLOT [X] [Y].", thrown.getMessage());
    }

    @Test
    public void plot_invalidCoordinatesMessage() {
        String line = "PLOT 0.8 2";
        VecFileException thrown =
                Assertions.assertThrows(VecFileException.class,
                        () -> SquarePadDrawing.checkLine(line));
        Assertions.assertEquals("Invalid vec format: Coordinates have to be numeric and in the range [0, 1].", thrown.getMessage());
    }


    @Test
    public void fill_invalidLengthMessage() {
        String line = "FILL #FFFFFF YES";
        VecFileException thrown =
                Assertions.assertThrows(VecFileException.class,
                        () -> SquarePadDrawing.checkLine(line));
        Assertions.assertEquals("Invalid format for FILL. Correct format: FILL [color].", thrown.getMessage());
    }

    @Test
    public void fill_invalidColorMessage() {
        String line = "FILL uWu";
        VecFileException thrown =
                Assertions.assertThrows(VecFileException.class,
                        () -> SquarePadDrawing.checkLine(line));
        Assertions.assertEquals("Invalid vec format: Color is not valid.", thrown.getMessage());
    }

    @Test
    public void pen_invalidLengthMessage() {
        String line = "PEN #FFFFFF YES";
        VecFileException thrown =
                Assertions.assertThrows(VecFileException.class,
                        () -> SquarePadDrawing.checkLine(line));
        Assertions.assertEquals("Invalid format for PEN. Correct format: PEN [color].", thrown.getMessage());
    }

    @Test
    public void pen_invalidColorMessage() {
        String line = "PEN uWu";
        VecFileException thrown =
                Assertions.assertThrows(VecFileException.class,
                        () -> SquarePadDrawing.checkLine(line));
        Assertions.assertEquals("Invalid vec format: Color is not valid.", thrown.getMessage());
    }

    @Test
    public void test_setPenColor(){
        SquarePadDrawing Pad= new SquarePadDrawing();
        Color c = Color.BLACK;
        Pad.setCurrentPenColor(c);
        Assertions.assertEquals(c, Pad.currentToolDetails.getColor());
    }

    @Test
    public void test_setFillColor(){
        SquarePadDrawing Pad= new SquarePadDrawing();
        Color c = Color.GREEN;
        Pad.setCurrentFillColor(c);
        Assertions.assertEquals(c, Pad.fillColor);
    }




}
