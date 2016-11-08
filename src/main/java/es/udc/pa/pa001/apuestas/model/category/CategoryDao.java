package es.udc.pa.pa001.apuestas.model.category;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;

/**
 * The Interface CategoryDao.
 */
public interface CategoryDao extends GenericDao<Category, Long> {

  /**
   * Find categories.
   *
   * @return the list
   */
  public List<Category> findCategories();

}
