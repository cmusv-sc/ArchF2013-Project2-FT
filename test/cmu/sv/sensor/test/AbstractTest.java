package cmu.sv.sensor.test;


import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

public class AbstractTest {

	private static EmbeddedDatabase database;
	protected static SimpleJdbcTemplate jdbcTemplate;

	@BeforeClass
	public static void setup() {
		database = new EmbeddedDatabaseBuilder().addDefaultScripts().build();

		jdbcTemplate = new SimpleJdbcTemplate(database);
		
	}

	@AfterClass
	public static void tearDown() {
		database.shutdown();
	}

}
