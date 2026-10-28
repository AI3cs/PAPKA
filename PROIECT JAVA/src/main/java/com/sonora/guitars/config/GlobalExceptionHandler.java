package com.sonora.guitars.config;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Void> handleNoResourceFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public String handleBusinessExceptions(RuntimeException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/products";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDenied() {
        return "redirect:/login?denied=true";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException() {
        return "error";
    }
}
