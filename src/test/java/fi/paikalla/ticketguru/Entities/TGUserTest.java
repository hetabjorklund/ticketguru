package fi.paikalla.ticketguru.Entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TGUserTest {

	// testaa että kun luo user-tasoisen käyttäjän, auth-taso on automaattisesti USER
	@Test
	void testTGUserStringStringStringString() {
		TGUser testuser = new TGUser("Testaaja", "Testinen", "testikäyttäjä", "password");		
		assertEquals("ROLE_USER", testuser.getAuth()); 			
	}

}

 