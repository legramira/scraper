package com.project.scraper.searches;

import com.project.scraper.models.ArticleSearchAgainst;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utils that are going to be use for the search of similarities via different caracteristics
 * Sydney-Australia - 10/12/2015.
 * Created by legramira
 */
public class RankingUtils {

//  sort the ranked list to obtain the best base in a normalization rank base
  public static List<Pair<String, Double>> getSortedAndRankedTupleList(final List<Pair<String, Double>> listTupleReferenceSimilarity) {

    List<Pair<String, Double>> auxiliaryListRanking = listTupleReferenceSimilarity
            .stream()
            .sorted((x, y) -> x.getValue() > y.getValue() ? -1 : 1)
            .collect(Collectors.toList());
    double auxPreviousEquality = auxiliaryListRanking.get(0).getValue();
    for (int i = 0, similarityRanking = 1; i < auxiliaryListRanking.size(); i++) {
      if (auxPreviousEquality != auxiliaryListRanking.get(i).getValue()) {
        similarityRanking = i + 1;
        auxPreviousEquality = auxiliaryListRanking.get(i).getValue();
      }
      auxiliaryListRanking.set(i, new Pair<>(auxiliaryListRanking.get(i).getKey(), (double) similarityRanking));
    }

    return auxiliaryListRanking;
  }


//  sort the final map that will be the result at the end
  public static List<Map.Entry<ArticleSearchAgainst, Double>> sortFinalMap(Map<ArticleSearchAgainst, Double> similarityToOthers) {
    return similarityToOthers
            .entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue())
            .collect(Collectors.toList());
  }


}
