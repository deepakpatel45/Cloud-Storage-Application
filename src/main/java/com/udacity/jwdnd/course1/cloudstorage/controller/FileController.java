package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class FileController {

    private final FileService fileService;
    private final UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping("/file/upload")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile multipartFile,
                             Authentication authentication) {

        try {
            String username = authentication.getName();
            User user = userService.getUser(username);

            // Empty file check
            if (multipartFile.isEmpty()) {
                return "redirect:/home?error=emptyFile";
            }

            // Duplicate filename check
            if (fileService.isFileExists(
                    multipartFile.getOriginalFilename(),
                    user.getUserid())) {
                return "redirect:/home?error=fileExists";
            }

            File file = new File(
                    null,
                    multipartFile.getOriginalFilename(),
                    multipartFile.getContentType(),
                    String.valueOf(multipartFile.getSize()),
                    user.getUserid(),
                    multipartFile.getBytes()
            );

            fileService.addFile(file);

            return "redirect:/home?success=fileUploaded";

        } catch (IOException e) {
            return "redirect:/home?error=fileUploadFailed";
        }
    }

    @GetMapping("/file/view/{fileId}")
    public ResponseEntity<ByteArrayResource> viewFile(@PathVariable Integer fileId,
                                                      Authentication authentication) {

        String username = authentication.getName();
        User user = userService.getUser(username);

        File file = fileService.getFile(fileId);

        if (file != null && file.getUserid().equals(user.getUserid())) {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(file.getContenttype()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(new ByteArrayResource(file.getFiledata()));
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/file/delete/{fileId}")
    public String deleteFile(@PathVariable Integer fileId,
                             Authentication authentication) {

        String username = authentication.getName();
        User user = userService.getUser(username);

        File file = fileService.getFile(fileId);

        if (file != null && file.getUserid().equals(user.getUserid())) {
            fileService.deleteFile(fileId);
            return "redirect:/home?success=fileDeleted";
        }

        return "redirect:/home?error=unauthorized";
    }
}