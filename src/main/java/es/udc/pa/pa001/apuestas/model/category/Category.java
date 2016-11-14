package es.udc.pa.pa001.apuestas.model.category;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.BatchSize;

/**
 * The Class Category.
 */
@Entity
@BatchSize(size = 10)
public class Category {

  /** The category id. */
  private Long categoryID;

  /** The name. */
  private String categoryName;

  /**
   * Instantiates a new category.
   */
  public Category() {
  }

  /**
   * Instantiates a new category.
   *
   * @param name
   *          the name
   */
  public Category(final String name) {
    this.categoryName = name;
  }

  /**
   * Gets the category id.
   *
   * @return the category id
   */
  @Column(name = "categoryId")
  @SequenceGenerator(// It only takes effect for
      name = "CategoryIdGenerator", // databases providing identifier
      sequenceName = "CategorySeq")
  // generators.
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "CategoryIdGenerator")
  public final Long getCategoryId() {
    return categoryID;
  }

  /**
   * Sets the category id.
   *
   * @param categoryId
   *          the new category id
   */
  public final void setCategoryId(final Long categoryId) {
    this.categoryID = categoryId;
  }

  /**
   * Gets the name.
   *
   * @return the name
   */
  @Column(name = "name")
  public final String getName() {
    return categoryName;
  }

  /**
   * Sets the name.
   *
   * @param name
   *          the new name
   */
  public final void setName(final String name) {
    this.categoryName = name;
  }

}
