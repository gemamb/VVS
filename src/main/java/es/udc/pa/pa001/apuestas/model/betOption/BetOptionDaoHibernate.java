package es.udc.pa.pa001.apuestas.model.betOption;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.pa.pa001.apuestas.model.betType.BetType;
import es.udc.pa.pa001.apuestas.model.category.Category;
import es.udc.pojo.modelutil.dao.GenericDaoHibernate;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Repository
public class BetOptionDaoHibernate extends GenericDaoHibernate<BetOption, Long>
		implements BetOptionDao {

}