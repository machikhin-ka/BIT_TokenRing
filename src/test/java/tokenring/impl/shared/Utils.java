package tokenring.impl.shared;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static tokenring.impl.shared.Load.LoadLevel.*;

public class Utils {
    public static void createFile(StringBuilder sb, Path path) {
        try {
            Files.write(path, sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Load[] getRelativeLoads(int size) {
        if (size == 2) {
            return new Load[]{
                    new Load(NORMAL, 1),
                    new Load(HIGH, 2)
            };
        } else if (size == 3) {
            return new Load[]{
                    new Load(LOW, 1),
                    new Load(NORMAL, 2),
                    new Load(HIGH, 3)
            };
        } else {
            int low = (int) ((size / 5.) + 0.5);
            int middle = (int) ((size / 2.) + 0.5);
            return new Load[]{
                    new Load(LOW, low),
                    new Load(NORMAL, middle),
                    new Load(HIGH, size)
            };
        }
    }
}
