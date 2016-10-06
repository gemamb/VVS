package es.udc.pa.pa001.apuestas.model.category;

import java.util.List;

import org.hibernate.annotations.BatchSize;
import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Repository

public class CategoryDaoHibernate extends GenericDaoHibernate<Category, Long>
		implements CategoryDao {

	@Override
	public List<Category> findCategories() {

		return getSession().createQuery("SELECT u FROM Category u").list();
	}
	
}
