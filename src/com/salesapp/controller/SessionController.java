package com.salesapp.controller;

import com.salesapp.business.UserBusinessInterface;
import com.salesapp.model.Principal;
import com.salesapp.model.UserModel;
import com.salesapp.utility.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;


@Controller
@RequestMapping("/login")
public class SessionController {

    //Class scoped business service to handle back-end logic
    private UserBusinessInterface<UserModel> userService;

    /**
     * This function injects a user business service at runtime for use in this particular class
     * @param userService A business service which handles any functions related to login
     */
    @Autowired
    public void setUserBusinessService(UserBusinessInterface<UserModel> userService)
    {
        this.userService=userService;
    }

    /**
     * Method for displaying login form page
     * @return ModelAndView A response page that displays the login portal with the relevant objects
     */
    @RequestMapping(path = "/portal", method = RequestMethod.GET)
    public ModelAndView displayPage(){
        //Change these view names as you see fit I just tried to make it obvious what they led to
        return new ModelAndView("loginPortal", "credentials", new UserModel());
    }

    /**
     * Method to validate user input as well as checking their input information
     * @param credentials The user's attempted login credentials
     * @param result The binding result containing validation
     * @param session The current HTTP session
     * @return ModelAndView A page dependent on the success or failure of the attempted login
     */
    @RequestMapping(path = "doLogin", method = RequestMethod.POST)
    public ModelAndView authenticate(@Valid @ModelAttribute("credentials") UserModel credentials, BindingResult result, HttpSession session){

        //Returning to the login portal with any validation errors
        if(result.hasErrors())
        {
            return new ModelAndView("loginPortal", "credentials", credentials);
        }

        //Attempts to authenticate and log on this user
        try
        {
            //Attempts to authenticate user
            UserModel user = userService.authenticate(credentials);

            //Adds a new principal model to the session to keep track of the user's login
            session.setAttribute("principal", new Principal(user.getUsername(), user.getID(), user.getRole(), user.getStatus()));

            //Redirecting user to main page upon success (Currently returns login credentials. Later will place a user in the session)
            return new ModelAndView("main", "credentials", credentials);
        }
        //Handles the event that a user is not found in the database
        catch(ItemNotFoundException e)
        {
            //Populating a response with relevant error information
            ModelAndView response = new ModelAndView();
            response.setViewName("loginPortal");
            response.addObject("credentials", credentials);
            response.addObject("failed", "Incorrect Username or Password");
            return response;
        }
        //Handling unknown errors
        catch(Exception e)
        {
            //Populating a response with relevant error information
            e.printStackTrace();
            ModelAndView response = new ModelAndView();
            response.setViewName("loginPortal");
            response.addObject("credentials", credentials);
            response.addObject("failed", "An unknown error has occurred");
            return response;
        }

    }

    /**
     * This method handles the log out functionality of the application
     * @param session The current session to be modified
     * @return The main view to redirect to after logging out
     */
    @RequestMapping(path = "signOut", method = RequestMethod.GET)
    public String logout(HttpSession session)
    {
        //Removes the user from the session without invalidating the session (And by extension, causing redirection to fail)
        session.removeAttribute("principal");

        //Return to response page
        return "main";
    }
}
