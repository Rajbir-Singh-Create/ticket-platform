package org.rajcreate.java.spring.ticketplatform.controller;

import org.rajcreate.java.spring.ticketplatform.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    // Dashboard
    @GetMapping
    public String index(@RequestParam(name="keyword", required=false) String title, Model model){
        model.addAttribute("list", ticketService.findTicketList(title));

        return "/tickets/index";
    }
}
