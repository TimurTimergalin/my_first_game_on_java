package java_code.entity.player.controls;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ControlsLoader {
    private static final ControlsLoader instance = new ControlsLoader();

    public static ControlsLoader get() {
        return instance;
    }


    private Map<String, Integer> controlsMap = new HashMap<>();

    private final Path controlsPath = Path.of("src/resources/config/controls.txt");

    private void setDefault() {
        controlsMap.put("a1", MouseEvent.BUTTON1 + 1000);
        controlsMap.put("a2", MouseEvent.BUTTON2 + 1000);
        controlsMap.put("s1", KeyEvent.VK_1);
        controlsMap.put("s2", KeyEvent.VK_2);
        controlsMap.put("s3", KeyEvent.VK_3);
        controlsMap.put("u", KeyEvent.VK_W);
        controlsMap.put("d", KeyEvent.VK_S);
        controlsMap.put("l", KeyEvent.VK_A);
        controlsMap.put("r", KeyEvent.VK_D);
        controlsMap.put("ds", KeyEvent.VK_SPACE);
        controlsMap.put("sp", KeyEvent.VK_SHIFT);
    }

    public boolean isAttack1(int id) {return controlsMap.get("a1").equals(id);}
    public boolean isAttack2(int id) {return controlsMap.get("a2").equals(id);}
    public boolean isSkill1(int id) {return controlsMap.get("s1").equals(id);}
    public boolean isSkill2(int id) {return controlsMap.get("s2").equals(id);}
    public boolean isSkill3(int id) {return controlsMap.get("s3").equals(id);}
    public boolean isUp(int id) {return controlsMap.get("u").equals(id);}
    public boolean isDown(int id) {return controlsMap.get("d").equals(id);}
    public boolean isLeft(int id) {return controlsMap.get("l").equals(id);}
    public boolean isRight(int id) {return controlsMap.get("r").equals(id);}
    public boolean isDash(int id) {return controlsMap.get("ds").equals(id);}
    public boolean isSprint(int id) {return controlsMap.get("sp").equals(id);}

    private ControlsLoader() {
        try {
            loadControls();
        } catch (IOException e) {
            setDefault();
            saveControls();
        }
    }

    private void loadControls() throws IOException {
        Stream<String> controls = Files.lines(controlsPath);
        controls.forEach(s -> {
            String[] splt = s.split(":");
            controlsMap.put(splt[0], Integer.parseInt(splt[1]));
        });
    }

    private void saveControls() {
        try {
            if (Files.exists(controlsPath)) {
                Files.delete(controlsPath);
            }

            Files.createFile(controlsPath);

            StringBuilder res = new StringBuilder();
            for (String name : controlsMap.keySet()) {
                res.append(name).append(":").append(controlsMap.get(name)).append("\n");
            }

            Files.write(
                    controlsPath,
                    res.toString().getBytes()
            );
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
