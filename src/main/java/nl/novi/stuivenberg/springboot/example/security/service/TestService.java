package nl.novi.stuivenberg.springboot.example.security.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    public String generatePublicContent() {
        return "Public Content.";
    }


    public String generateUserContent() {
        return "User Content.";
    }


    public String generateModContent() {
        return "Moderator Board.";
    }


    public String generateAdminContent() {
        return "Admin Board.";
    }

}
