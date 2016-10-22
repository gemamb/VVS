package es.udc.pa.pa001.apuestas.test.model.category;

import static es.udc.pa.pa001.apuestas.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa001.apuestas.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa001.apuestas.model.category.Category;
import es.udc.pa.pa001.apuestas.model.category.CategoryDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class CategoryDaoUnitTest {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private CategoryDao categoryDao;

	Category category1, category2;

	private void initializeCategories() {
		category1 = new Category("Baloncesto");
		category2 = new Category("Futbol");

		sessionFactory.getCurrentSession().saveOrUpdate(category1);
		sessionFactory.getCurrentSession().saveOrUpdate(category2);
	}

	/**
	 * PR-UN-029
	 */

	@Test
	public void testFindCategories() {

		/* SETUP */

		initializeCategories();
		List<Category> listcategories = new ArrayList<>();
		listcategories.add(category1);
		listcategories.add(category2);

		/* INVOCACION */

		List<Category> listFindCategories = categoryDao.findCategories();

		/* ASERCION */

		assertEquals(listFindCategories, listcategories);
	}

	/**
	 * PR-UN-030
	 */

	@Test
	public void testFindNoCategories() {

		/* SETUP */

		List<Category> listcategories = new ArrayList<>();

		/* INVOCACION */

		List<Category> listFindCategories = categoryDao.findCategories();

		/* ASERCION */

		assertEquals(listFindCategories, listcategories);
	}

}
