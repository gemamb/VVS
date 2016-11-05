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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class UserProfileDaoUnitTest {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private UserProfileDao userProfileDao;

	UserProfile userProfile;

	private void initializeUser() {
		userProfile = new UserProfile("pepe6", "XxXyYyZzZ", "Pepe", "García",
				"pepe6@gmail.com");

		sessionFactory.getCurrentSession().saveOrUpdate(userProfile);
	}

	/**
	 * PR-UN-039
	 *
	 */

	@Test
	public void testFindByLoginName() throws InstanceNotFoundException {

		/* SETUP */
		initializeUser();

		/* INVOCACION */
		UserProfile foundUser = userProfileDao.findByLoginName(userProfile
				.getLoginName());

		/* ASERCION */
		assertEquals(userProfile, foundUser);
	}

	/**
	 * PR-UN-040
	 *
	 */

	@Test(expected = InstanceNotFoundException.class)
	public void testFindByNonExistentLoginName()
			throws InstanceNotFoundException {

		/* SETUP */
		String nonExistentLoginName = "-";

		/* INVOCACION */
		userProfileDao.findByLoginName(nonExistentLoginName);

		/* ASERCION */
		/* InstanceNotFoundException expected */
	}

}
