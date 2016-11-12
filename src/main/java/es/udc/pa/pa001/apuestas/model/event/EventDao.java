package es.udc.pa.pa001.apuestas.model.event;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;

/**
 * The Interface EventDao.
 */
public interface EventDao extends GenericDao<Event, Long> {

  /**
   * Gets the number of events.
   *
   * @param keyWords
   *          the key words
   * @param categoryId
   *          the category id
   * @param admin
   *          the admin
   * @return the number of events
   */
  int getNumberOfEvents(String keyWords, Long categoryId, boolean admin);

  /**
   * Find events.
   *
   * @param keyWords
   *          the key words
   * @param categoryId
   *          the category id
   * @param startIndex
   *          the start index
   * @param count
   *          the count
   * @param admin
   *          the admin
   * @return the list
   */
  List<Event> findEvents(String keyWords, Long categoryId,
      int startIndex, int count, boolean admin);

  /**
   * Find duplicates.
   *
   * @param fullName
   *          the full name
   * @return true, if successful
   */
  boolean findDuplicates(String fullName);
}
