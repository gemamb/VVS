package es.udc.pa.pa001.apuestas.test.model.userprofile;

import static es.udc.pa.pa001.apuestas.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa001.apuestas.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa001.apuestas.model.userprofile.UserProfile;
import es.udc.pa.pa001.apuestas.model.userprofile.UserProfileDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * The Class UserProfileDaoUnitTest.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE,
    SPRING_CONFIG_TEST_FILE })
@Transactional
public class UserProfileDaoUnitTest {

  /** The session factory. */
  @Autowired
  private SessionFactory sessionFactory;

  /** The user profile dao. */
  @Autowired
  private UserProfileDao userProfileDao;

  /** The user profile. */
  UserProfile userProfile;

  /**
   * Initialize user.
   */
  private void initializeUser() {
    userProfile = new UserProfile("pepe6", "XxXyYyZzZ", "Pepe", "García",
        "pepe6@gmail.com");

    sessionFactory.getCurrentSession().saveOrUpdate(userProfile);
  }

  /**
   * PR-UN-039.
   *
   * @throws InstanceNotFoundException
   *           the instance not found exception
   */

  @Test
  public final void testFindByLoginName() throws InstanceNotFoundException {

    /* SETUP */
    initializeUser();

    /* INVOCACION */
    UserProfile foundUser = userProfileDao
        .findByLoginName(userProfile.getLoginName());

    /* ASERCION */
    assertEquals(userProfile, foundUser);
  }

  /**
   * PR-UN-040.
   *
   * @throws InstanceNotFoundException
   *           the instance not found exception
   */

  @Test(expected = InstanceNotFoundException.class)
  public final void testFindByNonExistentLoginName()
      throws InstanceNotFoundException {

    /* SETUP */
    String nonExistentLoginName = "-";

    /* INVOCACION */
    userProfileDao.findByLoginName(nonExistentLoginName);

    /* ASERCION */
    /* InstanceNotFoundException expected */
  }

}
