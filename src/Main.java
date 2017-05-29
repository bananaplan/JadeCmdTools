import com.zbaccp.bananaplan.Application;
import com.zbaccp.bananaplan.Config;

public class Main {
    public static void main(String[] args) {
        Config.init();

        Application app = new Application();
        app.run();
    }
}
