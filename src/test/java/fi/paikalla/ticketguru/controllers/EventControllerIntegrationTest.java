package fi.paikalla.ticketguru.controllers;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import fi.paikalla.ticketguru.Entities.Event;
import fi.paikalla.ticketguru.Repositories.EventRepository;
import net.bytebuddy.utility.RandomString;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", password = "password", roles = "ADMIN")
class EventControllerIntegrationTest {
	
	@Autowired
    private MockMvc mvc;
	@Autowired
	private EventRepository eventrepositorytester;
	
	private String local = "/events"; // paikallista testaamista varten
	private String heroku = "https://ticketguru-2021.herokuapp.com/events"; // herokun testaamista varten		
	
	// testaa, onko testirepository olemassa
	@Test
    public void testRepositoryInitializedCorrectly() {
        Assertions.assertThat(eventrepositorytester).isNotNull();
    }
	
	// testaa, löytyykö luotu testitapahtuma tietokannasta GETillä haettaessa
	@Test
	void testEventIsCreatedAndExists() throws Exception {
		 
		LocalDateTime start = LocalDateTime.of(2022,12,01,00,00);
		LocalDateTime end = LocalDateTime.of(2022,12,01,00,00); 
		LocalDateTime presaleend = LocalDateTime.of(2022,12,01,00,00);
		
		String randomEventName = RandomString.make(15);
		
		Event testevent = new Event(randomEventName, "Testimaailma", 10,  start, end, presaleend, "Tapahtuma testausta varten");
		eventrepositorytester.save(testevent);
		
		// tulostetaan varmuuden vuoksi jotta näkee missä indeksissä mennään
		String jsonlocation = "$[" + (eventrepositorytester.findAll().size()-1) + "].name";
		
		System.out.println(jsonlocation);
					
		mvc.perform(get(local)
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk())
			      .andExpect(content()
			      .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			      .andExpect(jsonPath(jsonlocation).value(testevent.getName()));	
		
	}
	
	// testi, jonka on tarkoitus feilata
	@Test
	void testShouldFail() throws Exception {
		 
		LocalDateTime start = LocalDateTime.of(2022,12,01,00,00);
		LocalDateTime end = LocalDateTime.of(2022,12,01,00,00); 
		LocalDateTime presaleend = LocalDateTime.of(2022,12,01,00,00);
		
		String randomEventName = RandomString.make(15);
		
		Event testevent = new Event(randomEventName, "Testimaailma", 10,  start, end, presaleend, "Tapahtuma testausta varten");
		eventrepositorytester.save(testevent);
		
		// tulostetaan varmuuden vuoksi jotta näkee missä indeksissä mennään
		String jsonlocation = "$[" + (eventrepositorytester.findAll().size()-1) + "].name";
		
		System.out.println(jsonlocation);
					
		mvc.perform(get(local)
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk())
			      .andExpect(content()
			      .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			      .andExpect(jsonPath(jsonlocation).value("Tätä nimeä ei ole olemassa"));	
		
	}

}
