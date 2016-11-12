package es.udc.pa.pa001.apuestas.model.userprofile;

import es.udc.pojo.modelutil.dao.GenericDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * The Interface UserProfileDao.
 */
public interface UserProfileDao extends GenericDao<UserProfile, Long> {

  /**
   * Returns an UserProfile by login name (not user identifier).
   *
   * @param loginName
   *          the user identifier
   * @return the UserProfile
   * @throws InstanceNotFoundException
   *           the instance not found exception
   */
  UserProfile findByLoginName(String loginName)
      throws InstanceNotFoundException;
}
