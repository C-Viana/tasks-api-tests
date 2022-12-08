package tasks.apitest;

import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class APITest {
	@BeforeClass
	public static void beforeAll() {
		RestAssured.baseURI = "http://localhost:8901/tasks-backend";
	}
	
	@Test
	public void deveRetornarTarefas() {
		RestAssured
			.given()
			.when()
				.get("/todo")
			.then()
				.statusCode(200);
	}
	
	@Test
	public void deveSalvarTarefaComSucesso() {
		String body = "{\"task\": \"Nova tarefa criada\", \"dueDate\": \"2022-12-30\"}";
		RestAssured
			.given()
				.contentType(ContentType.JSON)
				.body(body)
			.when()
				.post("/todo")
			.then()
				.statusCode(201);
	}
	
	@Test
	public void naoDeveAdicionarTarefaInvalida() {
		String body = "{\"task\": \"Nova tarefa criada\", \"dueDate\": \"2021-12-30\"}";
		RestAssured
			.given()
				.contentType(ContentType.JSON)
				.body(body)
			.when()
				.post("/todo")
			.then()
				.statusCode(400)
				.body("message", CoreMatchers.is("Due date must not be in past"));
	}
	
	@Test
	public void deveRemoverTarefaComSucesso() {
		String body = "{\"task\": \"Tarefa para remoção\", \"dueDate\": \"2022-12-30\"}";
		
		Integer taskId = RestAssured
		.given()
			.contentType(ContentType.JSON)
			.body(body)
		.when()
			.post("/todo")
		.then()
			.statusCode(201)
			.extract().path("id");
		
		RestAssured
			.given()
				.contentType(ContentType.JSON)
				.body(body)
			.when()
				.delete("/todo/"+taskId)
			.then()
				.statusCode(204);
	}
}
