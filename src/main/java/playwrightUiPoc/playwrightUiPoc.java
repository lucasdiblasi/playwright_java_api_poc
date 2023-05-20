package playwrightUiPoc;

import com.microsoft.playwright.*;
import java.nio.file.Paths;

public class playwrightUiPoc {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.webkit().launch();
            Page page = browser.newPage();
            page.navigate("http://google.com/");
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("example.png")));
        }
    }
}
