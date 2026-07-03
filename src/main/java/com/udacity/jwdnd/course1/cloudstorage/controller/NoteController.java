package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping("/note")
    public String addOrUpdateNote(@ModelAttribute Note note,
                                  Authentication authentication) {

        String username = authentication.getName();
        User user = userService.getUser(username);

        note.setUserid(user.getUserid());

        if (note.getNoteid() == null) {
            noteService.addNote(note);
            return "redirect:/home?tab=notes&success=noteCreated";
        } else {
            noteService.updateNote(note);
            return "redirect:/home?tab=notes&success=noteUpdated";
        }
    }

    @GetMapping("/note/delete/{noteid}")
    public String deleteNote(@PathVariable Integer noteid) {
        noteService.deleteNote(noteid);
        return "redirect:/home?tab=notes&success=noteDeleted";
    }
}