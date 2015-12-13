package com.project.scraper.models;

import java.math.BigDecimal;

/**
 * Data class that describe the product to search against, in this case is dexcribe for one single image (Store the path for the image)
 *
 * Sydney-Australia / 10/12/2015.
 * Created by legramira
 */
public class ArticleSearchAgainst extends ArticleAbstract<String> {
  private String pathImage;

  public ArticleSearchAgainst(String name, BigDecimal price, String pathImage) {
    this.setName(name);
    this.setPrice(price);
    this.setVisualDescriptor(pathImage);
  }


  @Override
  public String getVisualDescriptor() {
    return pathImage;
  }

  @Override
  public void setVisualDescriptor(String visualDescriptor) {
    this.pathImage = visualDescriptor;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ArticleSearchAgainst that = (ArticleSearchAgainst) o;

    return !(pathImage != null ? !pathImage.equals(that.pathImage) : that.pathImage != null);

  }

  @Override
  public int hashCode() {
    return pathImage != null ? pathImage.hashCode() : 0;
  }
}