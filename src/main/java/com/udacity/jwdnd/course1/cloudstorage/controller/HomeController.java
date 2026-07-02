package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final UserService userService;
    private final NoteService noteService;
    private final FileService fileService;
    private final CredentialService credentialService;

    public HomeController(UserService userService,
                          NoteService noteService,
                          FileService fileService,
                          CredentialService credentialService) {
        this.userService = userService;
        this.noteService = noteService;
        this.fileService = fileService;
        this.credentialService = credentialService;
    }

    @GetMapping("/home")
    public String homeView(Authentication authentication, Model model) {

        String username = authentication.getName();
        User user = userService.getUser(username);

        model.addAttribute("notes",
                noteService.getNotes(user.getUserid()));

        model.addAttribute("files",
                fileService.getFiles(user.getUserid()));

        model.addAttribute("credentials",
                credentialService.getCredentials(user.getUserid()));

        return "home";
    }
}