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
    public ResponseEntity<ByteArrayResource> viewFile(@PathVariable Integer fileId) {

        File file = fileService.getFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContenttype()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFilename() + "\"")
                .body(new ByteArrayResource(file.getFiledata()));
    }

    @GetMapping("/file/delete/{fileId}")
    public String deleteFile(@PathVariable Integer fileId) {
        fileService.deleteFile(fileId);
        return "redirect:/home?success=fileDeleted";
    }
}