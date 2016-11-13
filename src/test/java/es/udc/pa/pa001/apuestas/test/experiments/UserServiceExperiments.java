package es.udc.pa.pa001.apuestas.test.experiments;

import static es.udc.pa.pa001.apuestas.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa001.apuestas.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import es.udc.pa.pa001.apuestas.model.userprofile.UserProfile;
import es.udc.pa.pa001.apuestas.model.userservice.IncorrectPasswordException;
import es.udc.pa.pa001.apuestas.model.userservice.UserProfileDetails;
import es.udc.pa.pa001.apuestas.model.userservice.UserService;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * The Class UserServiceExperiments.
 */
public class UserServiceExperiments {

  /**
   * The main method.
   *
   * @param args
   *          the arguments
   */
  public static void main(final String[] args) {

    /* Get service object. */
    ApplicationContext ctx = new ClassPathXmlApplicationContext(
        new String[] { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE });
    UserService userService = ctx.getBean(UserService.class);

    try {
      // Register user.
      UserProfile userProfile = userService.registerUser("serviceUser",
          "userPassword",
          new UserProfileDetails("name", "lastName", "user@udc.es"));
      System.out.println("User with userId '" + userProfile.getUserProfileId()
          + "' has been created");
      System.out.println(userProfile);

      // Find user.
      userProfile = userService.login("serviceUser", "userPassword", false);
      System.out.println("User with userId '" + userProfile.getUserProfileId()
          + "' has been retrieved");
      System.out.println(userProfile);

      // ... proceed in the same way for other entities / methods / use
      // cases

    } catch (IncorrectPasswordException | InstanceNotFoundException
        | DuplicateInstanceException e) {
      e.printStackTrace();
    }

  }

}
