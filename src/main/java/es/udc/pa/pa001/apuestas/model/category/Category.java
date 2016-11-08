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
  private Long categoryId;

  /** The name. */
  private String name;

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
  public Category(String name) {
    this.name = name;
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
  public Long getCategoryId() {
    return categoryId;
  }

  /**
   * Sets the category id.
   *
   * @param categoryId
   *          the new category id
   */
  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  /**
   * Gets the name.
   *
   * @return the name
   */
  @Column(name = "name")
  public String getName() {
    return name;
  }

  /**
   * Sets the name.
   *
   * @param name
   *          the new name
   */
  public void setName(String name) {
    this.name = name;
  }

}
