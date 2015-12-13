package com.project.scraper.searches.byImage;


import com.project.scraper.models.ArticleSearchAgainst;
import com.project.scraper.models.ArticleSearchFrom;
import com.project.scraper.searches.SimilarityInterface;
import net.semanticmetadata.lire.builders.DocumentBuilder;
import net.semanticmetadata.lire.searchers.ImageSearchHits;
import net.semanticmetadata.lire.searchers.ImageSearcher;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

/**
 * Algorithm to search for the similarity of two articles via images
 *
 * Sydney-Australia / 10/12/2015.
 * Created by legramira
 */
public class ImageSimilarity implements SimilarityInterface<ArticleSearchFrom, ArticleSearchAgainst> {

  final private String pathIndex;
  final private ImageSearcher imageSearcher;

  public ImageSimilarity(String pathIndex, ImageSearcher imageSearcher) {
    this.pathIndex = pathIndex;
    this.imageSearcher = imageSearcher;
  }

// having to compare two products by image give a value that describe the rank of the similarity of the object via image comparison
  @Override
  public Map<ArticleSearchAgainst, Double> search(ArticleSearchFrom objectToCompareFrom, Map<ArticleSearchAgainst, Double> similarityToOthers) {
    Set<String> pathImages = objectToCompareFrom.getVisualDescriptor();
    pathImages.forEach((singleImagePath) -> {
              File imageFile = new File(singleImagePath);
              try {
                BufferedImage bufferedImage = ImageIO.read(imageFile);
                IndexReader indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(pathIndex)));
                ImageSearchHits hits = imageSearcher.search(bufferedImage, indexReader);

                double auxPreviousEquality = hits.score(0);
                for (int i = 0, similarityRanking = 1; i < hits.length(); i++) {
                  String fileName = indexReader.document(hits.documentID(i)).getValues(DocumentBuilder.FIELD_NAME_IDENTIFIER)[0];
                  ArticleSearchAgainst keyToStore = new ArticleSearchAgainst("", BigDecimal.ZERO, fileName);
                  if (similarityToOthers.get(keyToStore) != null) {
                    Double oldValue = similarityToOthers.get(keyToStore);
                    if (hits.score(i) != auxPreviousEquality) {
                      similarityRanking = i + 1;
                      auxPreviousEquality = hits.score(i);
                    }
                    similarityToOthers.put(keyToStore, (oldValue + similarityRanking));
                  }
                  System.out.println(hits.score(i) + ": \t" + keyToStore + "\t ranking " + similarityRanking);
                }
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
    );
    return similarityToOthers;
  }


}
