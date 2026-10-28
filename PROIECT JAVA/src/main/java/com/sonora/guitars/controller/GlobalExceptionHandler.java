package com.sonora.guitars.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

// @ControllerAdvice disabled due to conflict with config.GlobalExceptionHandler
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundError(NoHandlerFoundException ex, Model model) {
        log.warn("Eroare 404: Pagina nu a fost găsită - {}", ex.getRequestURL());
        model.addAttribute("errorCode", "404");
        model.addAttribute("errorMessage", "Pagina pe care o cauți nu a putut fi găsită. Poate a fost mutată sau ștearsă.");
        return "error";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgument(IllegalArgumentException ex, Model model) {
        log.warn("Argument invalid: {}", ex.getMessage());
        model.addAttribute("errorCode", "400");
        model.addAttribute("errorMessage", "Datele introduse nu sunt valide: " + ex.getMessage());
        return "error";
    }
    
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalState(IllegalStateException ex, Model model) {
        log.warn("Stare invalidă a aplicației: {}", ex.getMessage());
        model.addAttribute("errorCode", "400");
        model.addAttribute("errorMessage", "Acțiune nepermisă: " + ex.getMessage());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneralError(Exception ex, Model model) {
        log.error("Eroare internă a serverului", ex);
        model.addAttribute("errorCode", "500");
        model.addAttribute("errorMessage", "Ne pare rău, a apărut o eroare internă. Echipa noastră a fost notificată și lucrăm la remediere.");
        return "error";
    }
}
