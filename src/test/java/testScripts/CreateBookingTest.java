package testScripts;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import constants.Status_Code;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import pojo.request.createbooking.Bookingdates;
import pojo.request.createbooking.CreateBookingRequest;

public class CreateBookingTest {
	String token;
	CreateBookingRequest payload;
	int bookingId;

	@BeforeMethod
	public void generateToken() {
		RestAssured.baseURI = "https://restful-booker.herokuapp.com";

		Response res = RestAssured.given().log().all().headers("Content-Type", "application/json")
				.body("{\r\n" + "    \"username\" : \"admin\",\r\n" + "    \"password\" : \"password123\"\r\n" + "}")
				.when().post("/auth")

		;
		Assert.assertEquals(res.statusCode(), 200);
		token = res.jsonPath().getString("token");
		System.out.println(token);
	}

	@Test
	public void createBookingTest1() {
		// Given: all input details -> URL, Headers, path/ query paramerters, payload,
		// When -> submit the API[headerType,endpoint]
		// Then -> validate the API

		/*
		 * Response res = RestAssured.given().log().all().headers("Content-Type",
		 * "application/json") .body("{\r\n" + "    \"username\" : \"admin\",\r\n" +
		 * "    \"password\" : \"password123\"\r\n" + "}")
		 * .when().post("/auth").then().assertThat().statusCode(200).log().all().extract
		 * ().response()
		 * 
		 * ;
		 */
		// System.out.println(res.statusCode());

		// System.out.println(res.asPrettyString());

	}

	@Test
	public void createBookingTest() {
		Response res = RestAssured.given().headers("Content-Type", "application/json")
				.headers("Accept", "application/json")
				.body("{\r\n" + "    \"firstname\" : \"James\",\r\n" + "    \"lastname\" : \"Brown\",\r\n"
						+ "    \"totalprice\" : 111,\r\n" + "    \"depositpaid\" : true,\r\n"
						+ "    \"bookingdates\" : {\r\n" + "        \"checkin\" : \"2018-01-01\",\r\n"
						+ "        \"checkout\" : \"2019-01-01\"\r\n" + "    },\r\n"
						+ "    \"additionalneeds\" : \"Breakfast\"\r\n" + "}")
				.when().post("/booking");
		System.out.println(res.getStatusCode());
		System.out.println(res.getStatusLine());
		Assert.assertEquals(res.getStatusCode(), Status_Code.OK);
	}

	public void createBookingTestNormalMode() {
	}

	@Test
	public void createBookingTestWithPOJO() {

		Bookingdates bookingDate = new Bookingdates();
		bookingDate.setCheckin("2023-05-02");
		bookingDate.setCheckout("2023-05-05");

		payload = new CreateBookingRequest();
		payload.setFirstname("Sandhya");
		payload.setLastname("Kale");
		payload.setTotalprice(123);
		payload.setDepositpaid(true);
		payload.setAdditionalneeds("breakfast");
		payload.setBookingdates(bookingDate);

		Response res = RestAssured.given().log().all().headers("Content-Type", "application/json")
				.headers("Accept", "application/json").body(payload).log().all().when().post("/booking");
		Assert.assertEquals(res.getStatusCode(), 200);
		int bookingId = res.jsonPath().getInt("bookingid");
		Assert.assertTrue(bookingId > 0);
		//validateResponse(res, payload, "booking.");
		// Assert.assertTrue(Integer.valueOf(res.jsonPath().getInt("bookingid"))
		// instanceof Integer);

	}

	private void validateResponse(Response res, CreateBookingRequest payload, String object) {
		Assert.assertEquals(res.jsonPath().getString(object + "firstname"), payload.getFirstname());
		Assert.assertEquals(res.jsonPath().getString(object + "lastname"), payload.getLastname());
		Assert.assertEquals(res.jsonPath().getInt(object + "totalprice"), payload.getTotalprice());
		Assert.assertEquals(res.jsonPath().getBoolean(object + "depositepaid"), payload.isDepositpaid());
		Assert.assertEquals(res.jsonPath().getString(object + "bookingdates.checkin"),
				payload.getBookingdates().getCheckin());
		Assert.assertEquals(res.jsonPath().getString(object + "bookingdates.checkout"),
				payload.getBookingdates().getCheckout());
		Assert.assertEquals(res.jsonPath().getString(object + "additionalneeds"), payload.getAdditionalneeds());
	}

	@Test(priority = 1)
	public void getAllBookingTest() {
		RestAssured.baseURI = "https://restful-booker.herokuapp.com";
		int bookingid = 2522;
		Response res = RestAssured.given().log().all().headers("Content-Type", "application/json")
				.headers("Accept", "application/json").log().all().when().get("/booking/" + bookingid);
		List<Integer> listOfBookingIds = res.jsonPath().getList("bookingid");
		System.out.println(listOfBookingIds.size());
		Assert.assertTrue(listOfBookingIds.contains(bookingid));
		validateResponse(res, payload, "");
	}

	@Test(priority = 2)
	public void getBookingIdDeserializedTest() {
		RestAssured.baseURI = "https://restful-booker.herokuapp.com";
		int bookingid = 2522;
		Response res = RestAssured.given().log().all().headers("Content-Type", "application/json")
				.headers("Accept", "application/json").log().all().when().get("/booking/" + bookingid);
		CreateBookingRequest responseBody = res.as(CreateBookingRequest.class);
		// deserialized response
Assert.assertTrue(responseBody.equals(payload));
	}
@Test(priority = 0)
	public void updateBookingIdTest() {
		payload.setFirstname("SandhyaK");
		//bookingId = 2522;
		Response res =RestAssured.given().headers("Accept", "application/json").log().all()
		.headers("Content-Type", "application/json")
		.headers("Cookieoptional","token="+token)
		.body(payload).log().all()
		.when().put("/booking/" + bookingId);
		Assert.assertEquals(res.getStatusCode(), Status_Code.OK);
System.out.println(res.asPrettyString());
CreateBookingRequest responseBody = res.as(CreateBookingRequest.class);
	}
}
