package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException() {
        return "redirect:/home?tab=files&error=fileTooLarge";
    }
}