package es.udc.pa.pa001.apuestas.web.util;

import java.util.List;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

import es.udc.pa.pa001.apuestas.model.bet.Bet;
import es.udc.pa.pa001.apuestas.model.betservice.BetService;

/**
 * The Class MyBetsGridDataSource.
 */
public class MyBetsGridDataSource implements GridDataSource {

  /** The bet service. */
  private BetService betService;

  /** The user profile id. */
  private Long userProfileId;

  /** The start index. */
  private int startIndex;

  /** The bets. */
  private List<Bet> bets;

  /** The bet not found. */
  private boolean betNotFound;

  /**
   * Instantiates a new my bets grid data source.
   *
   * @param betService
   *          the bet service
   * @param userProfileId
   *          the user profile id
   */
  public MyBetsGridDataSource(BetService betService, Long userProfileId) {
    super();
    this.betService = betService;
    this.userProfileId = userProfileId;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.tapestry5.grid.GridDataSource#getAvailableRows()
   */
  @Override
  public int getAvailableRows() {

    return betService.findBetsByUserIdNumber(userProfileId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.tapestry5.grid.GridDataSource#prepare(int, int, java.util.List)
   */
  @Override
  public void prepare(int startIndex, int endIndex,
      List<SortConstraint> sortConstraints) {

    bets = betService
        .findBets(userProfileId, startIndex, endIndex - startIndex + 1)
        .getBets();
    this.startIndex = startIndex;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.tapestry5.grid.GridDataSource#getRowValue(int)
   */
  @Override
  public Object getRowValue(int index) {
    return bets.get(index - this.startIndex);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.tapestry5.grid.GridDataSource#getRowType()
   */
  @Override
  public Class<Bet> getRowType() {
    return Bet.class;
  }

  /**
   * Gets the bet not found.
   *
   * @return the bet not found
   */
  public boolean getBetNotFound() {
    return betNotFound;
  }
}
