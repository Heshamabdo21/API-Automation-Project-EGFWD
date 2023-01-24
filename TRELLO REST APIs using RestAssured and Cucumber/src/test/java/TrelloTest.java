import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class TrelloTest {
    String baseURL = "https://api.trello.com";
    String apiKey = "aaffd7806a18f9ce31c9bd9bef04f884";
    String apiToken = "ATTA05699a737ffa0dc24985f1f3f7305fb1df19342603f958b017758ea8209e5941769CC76F";
    String organizationId, memberId, boardId, listId;

    @Test(priority=1, description = "Create a new organization")
    public void createOrganization(){
        Response response = RestAssured.given().baseUri(baseURL).basePath("/1/organizations")
                .queryParam("displayName","TrelloTestOrganization")
                .queryParam("desc","Org 02 description")
                .queryParam("key", apiKey).queryParam("token", apiToken)
                .contentType("application/json").when().post();
        response.prettyPrint();
        response.then().statusCode(200);

        // First get the JsonPath object instance from the Response interface
        JsonPath jsonPathEvaluator = response.jsonPath();
        organizationId = jsonPathEvaluator.get("id");

        // Let us print the Organization ID variable to see what we got
        System.out.println("Organization ID from Response " + organizationId);
    }

    @Test(priority=2, description = "Get member id")
    public void getMemberID(){
        Response response = RestAssured.given().baseUri(baseURL).basePath("/1/members/me")
                .queryParam("key", apiKey).queryParam("token", apiToken).when().get();
        response.prettyPrint();
        response.then().statusCode(200);

        // First get the JsonPath object instance from the Response interface
        JsonPath jsonPathEvaluator = response.jsonPath();
        memberId = jsonPathEvaluator.get("id");

        // Let us print the Member ID variable to see what we got
        System.out.println("Member ID from Response " + memberId);
    }

    @Test(priority=3, description = "Get Member's Organizations", dependsOnMethods={"getMemberID"})
    public void getMemberOrganizations(){
        Response response = RestAssured.given().baseUri(baseURL).basePath("/1/members/"+memberId+"/organizations")
                .queryParam("key", apiKey).queryParam("token", apiToken).when().get();
        response.prettyPrint();
        response.then().statusCode(200);
    }

    @Test(priority=4, description = "Create a board inside an organization", dependsOnMethods={"createOrganization"})
    public void createBoard(){
        Response response = RestAssured.given().baseUri(baseURL).basePath("/1/boards")
                .queryParam("name","TrelloTestBoard")
                .queryParam("desc","Board 02 description")
                .queryParam("idOrganization",organizationId)
                .queryParam("key", apiKey).queryParam("token", apiToken)
                .contentType("application/json").when().post();
        response.prettyPrint();
        response.then().statusCode(200);

        // First get the JsonPath object instance from the Response interface
        JsonPath jsonPathEvaluator = response.jsonPath();
        boardId = jsonPathEvaluator.get("id");

        // Let us print the Board ID variable to see what we got
        System.out.println("Board ID from Response " + boardId);
    }

    @Test(priority=5, description = "Get boards in an organization", dependsOnMethods={"createOrganization"})
    public void getBoardsInOrganization(){
        Response response = RestAssured.given().baseUri(baseURL).basePath("/1/organizations/"+organizationId+"/boards")
                .queryParam("key", apiKey).queryParam("token", apiToken).when().get();
        response.prettyPrint();
        response.then().statusCode(200);
    }

    @Test(priority=6, description = "Create a new list on the board", dependsOnMethods={"createBoard"})
    public void createList(){
        Response response = RestAssured.given().baseUri(baseURL).basePath("/1/lists")
                .queryParam("name","AshoList02")
                .queryParam("idBoard",boardId)
                .queryParam("key", apiKey).queryParam("token", apiToken)
                .contentType("application/json").when().post();
        response.prettyPrint();
        response.then().statusCode(200);

        // First get the JsonPath object instance from the Response interface
        JsonPath jsonPathEvaluator = response.jsonPath();
        listId = jsonPathEvaluator.get("id");

        // Let us print the List ID variable to see what we got
        System.out.println("List ID from Response " + listId);
    }

    @Test(priority=7, description = "Get all lists on a board", dependsOnMethods={"createBoard"})
    public void getAllListsOnBoard(){
        Response response = RestAssured.given().baseUri(baseURL).basePath("/1/boards/"+boardId+"/lists")
                .queryParam("key", apiKey).queryParam("token", apiToken).when().get();
        response.prettyPrint();
        response.then().statusCode(200);
    }

    @Test(priority=8, description = "Archive a list", dependsOnMethods={"createList"})
    public void archiveList(){
        Response response = RestAssured.given().baseUri(baseURL).basePath("/1/lists/"+listId+"/closed")
                .queryParam("value",true)
                .queryParam("key", apiKey).queryParam("token", apiToken)
                .contentType("application/json").when().put();
        response.prettyPrint();
        response.then().statusCode(200);
    }

    @Test(priority=9, description = "Delete a board", dependsOnMethods={"createBoard"})
    public void deleteBoard(){
        Response response = RestAssured.given().baseUri(baseURL).basePath("/1/boards/"+boardId)
                .queryParam("key", apiKey).queryParam("token", apiToken)
                .contentType("application/json").when().delete();
        response.prettyPrint();
        response.then().statusCode(200);
    }

    @Test(priority=10, description = "Delete an organization", dependsOnMethods={"createOrganization"})
    public void deleteOrganization(){
        Response response = RestAssured.given().baseUri(baseURL).basePath("/1/organizations/"+organizationId)
                .queryParam("key", apiKey).queryParam("token", apiToken)
                .contentType("application/json").when().delete();
        response.prettyPrint();
        response.then().statusCode(200);
    }
}
