package es.udc.pa.pa001.apuestas.model.betOption;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

/**
 * The Class BetOptionDaoHibernate.
 */
@Repository
public class BetOptionDaoHibernate extends GenericDaoHibernate<BetOption, Long>
    implements BetOptionDao {

}
