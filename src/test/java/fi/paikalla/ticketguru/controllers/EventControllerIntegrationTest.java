package fi.paikalla.ticketguru.controllers;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import fi.paikalla.ticketguru.Entities.Event;
import fi.paikalla.ticketguru.Repositories.EventRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class EventControllerIntegrationTest {
	
	@Autowired
    private MockMvc mvc;
	@Autowired
	private EventRepository eventrepositorytester;

	@Test
	@WithMockUser(username = "admin", password = "password", roles = "ADMIN")
	void test() throws Exception {
		 
		LocalDateTime start = LocalDateTime.of(2022,12,03,9,00);
		LocalDateTime end = LocalDateTime.of(2022,12,03,16,00); 
		LocalDateTime presaleend = LocalDateTime.of(2022,11,27,16,00);
		
		String testEventName = "Testitapahtuma11";
		Event testevent = new Event(testEventName, "Testimaailma", 10,  start, end, presaleend, "Tapahtuma testausta varten");
		eventrepositorytester.save(testevent);
		
		int size = eventrepositorytester.findAll().size();
		String jsonlocation = "$[" + (size-1) + "].name";
		
		System.out.println(jsonlocation);
					
		mvc.perform(get("https://ticketguru-2021.herokuapp.com/events")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk())
			      .andExpect(content()
			      .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			      .andExpect(jsonPath(jsonlocation).value(testEventName));	
		
	}

}
