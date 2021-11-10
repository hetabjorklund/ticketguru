package fi.paikalla.ticketguru.controllers;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.json.JSONObject;
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
	
	// helposti vaihdettavat osoitteet muuttujina paikallista ja herokun testaamista varten
	private String local = "/events";
	private String heroku = "https://ticketguru-2021.herokuapp.com/events";		
	
	// testaa, onko testirepository olemassa
	@Test
    public void testRepositoryInitializedCorrectly() {
        Assertions.assertThat(eventrepositorytester).isNotNull();
    }
	
	// testaa, löytyykö luotu testitapahtuma tietokannasta GETillä haettaessa
	@Test
	void testEventIsCreatedAndExists() throws Exception {
		 	
		String randomEventName = RandomString.make(15);	

		JSONObject testevent = new JSONObject();		 
		testevent.put("name", randomEventName);
		testevent.put("address", "Testimaailma");
		testevent.put("maxCapacity", 10);
		testevent.put("startTime", "2022-12-01T00:00:00");
		testevent.put("endTime", "2022-12-01T00:00:00");
		testevent.put("endOfPresale", "2022-12-01T00:00:00");
		testevent.put("status", null);
		testevent.put("description", "Testin luoma tapahtuma");
		testevent.put("ticketTypes", null);
		
		mvc.perform(post(local)
				.contentType(MediaType.APPLICATION_JSON)
		        .content(testevent.toString()) 
		        .accept(MediaType.APPLICATION_JSON))
		        .andExpect(status().isCreated())
		        .andExpect(content().contentType(MediaType.APPLICATION_JSON)); 			
		
		String jsonlocation = "$[" + (eventrepositorytester.findAll().size()-1) + "].name";

		// tulostetaan varmuuden vuoksi jotta näkee missä indeksissä mennään
		System.out.println(jsonlocation);
					
		mvc.perform(get(local)
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk())
			      .andExpect(content()
			      .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			      .andExpect(jsonPath(jsonlocation).value(testevent.get("name")));	
		
	}
	
	// testi, jonka on tarkoitus feilata
	@Test
	void testShouldFail() throws Exception {
		 	
		String randomEventName = RandomString.make(15);
		
		JSONObject testevent = new JSONObject();		 
		testevent.put("name", randomEventName);
		testevent.put("address", "Testimaailma");
		testevent.put("maxCapacity", 10);
		testevent.put("startTime", "2022-12-01T00:00:00");
		testevent.put("endTime", "2022-12-01T00:00:00");
		testevent.put("endOfPresale", "2022-12-01T00:00:00");
		testevent.put("status", null);
		testevent.put("description", "Testin luoma tapahtuma");
		testevent.put("ticketTypes", null);
		
		mvc.perform(post(local)
				.contentType(MediaType.APPLICATION_JSON)
		        .content(testevent.toString()) 
		        .accept(MediaType.APPLICATION_JSON))
		        .andExpect(status().isCreated())
		        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
				
		String jsonlocation = "$[" + (eventrepositorytester.findAll().size()-1) + "].name";
		
		// tulostetaan varmuuden vuoksi jotta näkee missä indeksissä mennään
		System.out.println(jsonlocation);
					
		mvc.perform(get(local)
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk())
			      .andExpect(content()
			      .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			      .andExpect(jsonPath(jsonlocation).value("Tätä nimeä ei ole olemassa"));	
		
	}

}
