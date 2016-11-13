package es.udc.pa.pa001.apuestas.model.event;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

/**
 * The Class EventDaoHibernate.
 */
@Repository
public class EventDaoHibernate extends GenericDaoHibernate<Event, Long>
    implements EventDao {

  @SuppressWarnings("unchecked")
  @Override
  public final List<Event> findEvents(final String keyWords,
      final Long categoryId,
      final int startIndex, final int count, final boolean admin) {

    String[] words = keyWords != null ? keyWords.split(" ") : null;

    String hqlQuery = "SELECT e FROM Event e";

    if (categoryId != null) {
      hqlQuery += " WHERE e.category.categoryId = :categoryId";
      if (words != null) {
        hqlQuery += " AND ";
      }
    }

    String w;
    if (words != null && words.length > 0) {
      if (categoryId == null) {
        hqlQuery += " WHERE ";
      }
      for (int i = 0; i < words.length; i++) {
        if (i > 0) {
          hqlQuery += " AND";
        }
        w = words[i];
        hqlQuery += " LOWER(e.name) LIKE LOWER(:name" + i + ")";
      }
    }

    Calendar date = Calendar.getInstance();
    if ((words == null) && (categoryId == null)) {
      if (!admin) {
        hqlQuery += " WHERE e.eventStart >= :date";
      }
    } else {
      if (!admin) {
        hqlQuery += " AND e.eventStart >= :date";
      }
    }

    Query queryHql = getSession()
        .createQuery(hqlQuery + " ORDER BY e.eventStart, e.name");

    if (categoryId != null) {
      queryHql.setParameter("categoryId", categoryId);
    }

    if (words != null && words.length > 0) {
      for (int i = 0; i < words.length; i++) {
        queryHql.setString("name" + i + "", "%" + words[i] + "%");
      }
    }

    if (!admin) {
      queryHql.setParameter("date", date);
    }

    return queryHql.setFirstResult(startIndex).setMaxResults(count).list();
  }

  @Override
  public final boolean findDuplicates(final String fullName) {
    return !getSession()
        .createQuery("Select e from Event e where e.name = :fullName")
        .setParameter("fullName", fullName).list().isEmpty();
  }

  @Override
  public final int getNumberOfEvents(final String keyWords,
      final Long categoryId,
      final boolean admin) {
    String[] words = keyWords != null ? keyWords.split(" ") : null;

    String hqlQuery = "SELECT count(e) FROM Event e";

    if (categoryId != null) {
      hqlQuery += " WHERE e.category.categoryId = :categoryId";
      if (words != null) {
        hqlQuery += " AND ";
      }
    }

    String w;
    if (words != null && words.length > 0) {
      if (categoryId == null) {
        hqlQuery += " WHERE ";
      }
      for (int i = 0; i < words.length; i++) {
        if (i > 0) {
          hqlQuery += " AND";
        }
        w = words[i];
        hqlQuery += " LOWER(e.name) LIKE LOWER(:name" + i + ")";
      }
    }

    Calendar date = Calendar.getInstance();
    if ((words == null) && (categoryId == null)) {
      if (!admin) {
        hqlQuery += " WHERE e.eventStart >= :date";
      }
    } else {
      if (!admin) {
        hqlQuery += " AND e.eventStart >= :date";
      }
    }

    Query queryHql = getSession()
        .createQuery(hqlQuery + " ORDER BY e.eventStart, e.name");

    if (categoryId != null) {
      queryHql.setParameter("categoryId", categoryId);
    }

    if (words != null && words.length > 0) {
      for (int i = 0; i < words.length; i++) {
        queryHql.setString("name" + i + "", "%" + words[i] + "%");
      }
    }

    if (!admin) {
      queryHql.setParameter("date", date);
    }

    long numberOfOperations = (Long) queryHql.uniqueResult();

    return (int) numberOfOperations;
  }
}
