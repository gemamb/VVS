package es.udc.pa.pa001.apuestas.model.betType;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

/**
 * The Class BetTypeDaoHibernate.
 */
@Repository
public class BetTypeDaoHibernate extends GenericDaoHibernate<BetType, Long>
    implements BetTypeDao {

  /*
   * (non-Javadoc)
   * 
   * @see es.udc.pa.pa001.apuestas.model.betType.BetTypeDao#findDuplicates(java.lang.Long,
   * java.lang.String)
   */
  @Override
  public boolean findDuplicates(Long eventId, String fullName) {
    return !getSession()
        .createQuery("Select e from BetType e where e.question = :fullName and"
            + " e.event.eventId =:eventId")
        .setParameter("eventId", eventId).setParameter("fullName", fullName)
        .list().isEmpty();
  }

}
