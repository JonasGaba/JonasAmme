package com.JonasAmme.website.controller;


import com.JonasAmme.website.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@ControllerAdvice
public class HomePageController {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileUploadSize;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/")
    public String showHomePage() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        if (userRepository.findByUsername(currentPrincipalName) == null) {
            return "login";
        }


        return "frontpage";
    }

    /*
        @ExceptionHandler(org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException.class)
        public String handleTooLargeFileUpload(HttpServletRequest request, HttpServletResponse response, Model model){
            model.addAttribute("error", "Filesize is too large must be less than "+maxFileUploadSize);
            if (response.isCommitted()) {
                //log.debug("Response has already been committed. Unable to redirect to " + request.getRequestURI());
                return "/";
            }
            return request.getRequestURI();

        }

    */
    @ExceptionHandler(org.springframework.web.multipart.MaxUploadSizeExceededException.class)
    public ModelAndView handleTooLargeFileUpload(HttpServletRequest request, HttpServletResponse response, Model model, Exception ex) {
        model.addAttribute("error", "Filesize is too large must be less than " + maxFileUploadSize);
        if (response.isCommitted()) {
            //log.debug("Response has already been committed. Unable to redirect to " + request.getRequestURI());
            return new ModelAndView("redirect:/");
        }
        return new ModelAndView("redirect:" + request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));

    }
}
