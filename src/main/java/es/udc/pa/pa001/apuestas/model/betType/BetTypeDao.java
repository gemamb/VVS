package es.udc.pa.pa001.apuestas.model.betType;

import es.udc.pojo.modelutil.dao.GenericDao;

/**
 * The Interface BetTypeDao.
 */
public interface BetTypeDao extends GenericDao<BetType, Long> {

  /**
   * Find duplicates.
   *
   * @param eventId
   *          the event id
   * @param fullName
   *          the full name
   * @return true, if successful
   */
  boolean findDuplicates(Long eventId, String fullName);

}
