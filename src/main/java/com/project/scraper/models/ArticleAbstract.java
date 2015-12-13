package com.project.scraper.models;

import java.math.BigDecimal;

/**
 * Base data model to be use for the parsing visual descriptor is dependant of the store
 *
 * Sydney-Australia / 10/12/2015.
 * Created by legramira
 */
public abstract class ArticleAbstract<T> {

  private String name;
  private BigDecimal price;


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public abstract T getVisualDescriptor();

  public abstract void setVisualDescriptor(T visualDescriptor);





}
