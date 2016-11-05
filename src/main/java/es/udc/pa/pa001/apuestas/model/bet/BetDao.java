package es.udc.pa.pa001.apuestas.model.bet;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;

public interface BetDao extends GenericDao<Bet, Long> {

	public List<Bet> findBetsByUserId(Long userId, int startIndex, int count);

	int findBetsByUserIdNumber(Long userId);
}