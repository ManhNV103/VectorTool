package Testing;

import PaintTool.SquarePadDrawing;
import PaintTool.VecFileException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
    public void readFile2() throws VecFileException {
        String line = "FILL OOO";
        Assertions.assertThrows(VecFileException.class, () -> {SquarePadDrawing.checkLine(line);});
    }






}
