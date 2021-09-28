package fi.paikalla.ticketguru.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import fi.paikalla.ticketguru.Repositories.TicketRepository;

@RestController
public class TicketController {
	@Autowired
	private TicketRepository ticketRepo;
}
