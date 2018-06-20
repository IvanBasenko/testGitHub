import com.codeborne.selenide.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.junit.Test;
import org.openqa.selenium.By;
import pojo.SearchObject;
import pojo.SearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;
import static java.lang.System.setProperty;
import static org.junit.Assert.assertEquals;

public class Tests {
    private static final String BASE_URL_API = "https://api.github.com/search/repositories";
    private static final String BASE_URL = "https://github.com/";

    @Test
    public void firstTest() throws IOException {
        Response response = given()
                .param("q", "Selenide")
                .param("sort", "stars")
                .param("order", "desc")
                .when()
                .get(BASE_URL_API);

        ObjectMapper objectMapper = new ObjectMapper();
        SearchResponse searchResponse = objectMapper.readValue(response.getBody().asString(), SearchResponse.class);
        List<SearchObject> searchObjects;
        searchObjects = new ArrayList<>(searchResponse.getSearchObjects());
        Configuration.browser = "chrome";
        setProperty("selenide.browser", "chrome");
        open(BASE_URL);
        $(By.name("q")).setValue("Selenide").pressEnter();
        $(".pr-3 .v-align-middle").shouldHave(text(searchObjects.get(0).getFullName()));
        $(".pr-3 .d-flex .f6").shouldHave(text(searchObjects.get(0).getLicense().getName()));
        $(".d-table-cell").shouldHave(text(searchObjects.get(0).getLanguage()));
        $(".col-2 .muted-link").shouldHave(text(searchObjects.get(0).getStars()));
        $(".one-fourth .menu-item:first-child .ml-1").shouldHave(text(searchResponse.getTotalResult()));
    }

    @Test
    public void secondTest() throws IOException {
        Response response = given()
                .param("q", "google")
                .param("sort", "stars")
                .param("order", "desc")
                .when()
                .get(BASE_URL_API);

        ObjectMapper objectMapper = new ObjectMapper();
        SearchResponse searchResponse = objectMapper.readValue(response.getBody().asString(), SearchResponse.class);
        List<SearchObject> searchObjects;
        searchObjects = new ArrayList<>(searchResponse.getSearchObjects());
        Configuration.browser = "chrome";
        setProperty("selenide.browser", "chrome");
        open(BASE_URL);
        $(By.name("q")).setValue("google").pressEnter();
        $(".select-menu.select-menu-modal-right").click();
        $(".select-menu-item", 1).click();
        $(".pr-3 .v-align-middle").shouldHave(text(searchObjects.get(0).getFullName()));
        $(".pr-3 .d-flex .f6").shouldHave(text(searchObjects.get(0).getLicense().getName()));
        $(".d-table-cell").shouldHave(text(searchObjects.get(0).getLanguage()));
    }

    @Test
    public void thirdTest() throws IOException {
        String username = "ivanbasenkotest";
        String pass = "qwerty1";
        String repName = "Rofl";
        String json =
                "{\n" +
                        "  \"name\": \"" + repName + "\",\n" +
                        "  \"description\": \"This is your first repository\",\n" +
                        "  \"auto_init\" : true \n" +
                        "}";
        given()
                .auth().preemptive().basic(username, pass)
                .when()
                .contentType("application/json; charset=UTF-8")
                .body(json)
                .post("https://api.github.com/user/repos");
        Configuration.browser = "chrome";
        setProperty("selenide.browser", "chrome");
        open(BASE_URL + "/login");
        login(username, pass);
        $("#dashboard-repos-filter").val(username + "/" + repName);
        $(".list-style-none .width-full").click();
        $(".reponav .octicon-gear").click();
        $(".btn-danger", 4).click();
        $(".input-block", 1).val(repName).pressEnter();
        Response response = given()
                .param("q", username + "/" + repName)
                .when()
                .get(BASE_URL_API);
        ObjectMapper objectMapper = new ObjectMapper();
        SearchResponse searchResponse = objectMapper.readValue(response.getBody().asString(), SearchResponse.class);

        assertEquals(0, Integer.parseInt(searchResponse.getTotalResult()));
    }

    private static void login(String l, String p) {
        $("#login_field").val(l);
        $("#password").val(p).pressEnter();
    }
}