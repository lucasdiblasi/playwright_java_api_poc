package playwrightApiPoc;

import com.microsoft.playwright.options.AriaRole;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.microsoft.playwright.*;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class playwrightApiValidations {
    public static void main(String[] args) {

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            String jsonFilePath = "src/main/java/playwrightApiPoc/token.json";
            String authTokenPropertyName = "authTokenApi";

            // Defina o token de autorização
            String authToken = readAuthTokenFromJson(jsonFilePath, authTokenPropertyName);
            String apiHost = "api-football-beta.p.rapidapi.com";

            // Defina a URL da API que você deseja validar
            String apiUrl = "https://api-football-beta.p.rapidapi.com/timezone";

            // Configure o cabeçalho de autorização
            Map<String, String> headers = new HashMap<>();
            headers.put("X-RapidAPI-Key", authToken);
            headers.put("X-RapidAPI-Host", apiHost);
            page.setExtraHTTPHeaders(headers);

            // Execute uma solicitação GET para a API
            Response response = page.navigate(apiUrl);

//////////////////////Essa verificação abaixo não é necessária /////////////////////////////////////////
            // Verifique o código de status da resposta
            int statusCode = response.status();
            System.out.println("Status code: " + statusCode);

            // Verifique o corpo da resposta
            String responseBody = response.text();
            System.out.println("Response body: " + responseBody);
/////////////////////////////////////////////////////////////////////////////////////////////////////////

            // Realize as validações necessárias com playwright
            if (statusCode == 200) {
                System.out.println("API request was successful.");
            } else {
                System.out.println("API request failed.");
            }

            if (responseBody.contains("Africa")) {
                System.out.println("Success message found in response.");
            } else {
                System.out.println("Success message not found in response.");
            }

            // Feche o navegador
            browser.close();
        } catch (PlaywrightException e) {
            e.printStackTrace();
        }
    }
    private static String readAuthTokenFromJson(String filePath, String propertyName) {
        JSONParser parser = new JSONParser();
        try {
            // Faz o parsing do arquivo JSON
            Object obj = parser.parse(new FileReader(filePath));
            JSONObject jsonObject = (JSONObject) obj;

            // Obtém o valor do token de autorização do JSON
            return (String) jsonObject.get(propertyName);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}