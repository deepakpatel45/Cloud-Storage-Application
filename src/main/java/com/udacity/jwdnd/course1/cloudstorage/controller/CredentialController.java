package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CredentialController {

    private final CredentialService credentialService;
    private final UserService userService;

    public CredentialController(CredentialService credentialService,
                                UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @PostMapping("/credential")
    public String addOrUpdateCredential(Credential credential,
                                        Authentication authentication) {

        String username = authentication.getName();
        User user = userService.getUser(username);

        credential.setUserid(user.getUserid());

        if (credential.getCredentialid() == null) {
            credentialService.addCredential(credential);
            return "redirect:/home?tab=credentials&success=credentialCreated";
        } else {
            credentialService.updateCredential(credential);
            return "redirect:/home?tab=credentials&success=credentialUpdated";
        }
    }

    @GetMapping("/credential/delete/{credentialid}")
    public String deleteCredential(@PathVariable Integer credentialid) {
        credentialService.deleteCredential(credentialid);
        return "redirect:/home?tab=credentials&success=credentialDeleted";
    }
}