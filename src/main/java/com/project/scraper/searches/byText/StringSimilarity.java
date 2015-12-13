package com.project.scraper.searches.byText;

import com.project.scraper.models.ArticleSearchAgainst;
import com.project.scraper.models.ArticleSearchFrom;
import com.project.scraper.searches.RankingUtils;
import com.project.scraper.searches.SimilarityInterface;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Dice's Coefficient implementation to search for semantic sentence similitude between two strings
 * <p>
 * The algorithm consist in divide strings by adjacent pairs of letters and check the interception
 * of both strings agains the total possibilities between both strings, no white space are consider
 * to be able to alter the order of the words
 * <p>
 * Sydney-Australia / 9/12/2015.
 * Created by legramira
 */
public class StringSimilarity implements SimilarityInterface<ArticleSearchFrom, ArticleSearchAgainst> {

  @Override
  public Map<ArticleSearchAgainst, Double> search(ArticleSearchFrom objectToCompareFrom, final Map<ArticleSearchAgainst, Double> similarityToOthers) {

    List<Pair<String, Double>> listTupleReferenceSimilarity = new ArrayList<>();
    similarityToOthers.forEach((k, v) ->
            listTupleReferenceSimilarity.add(new Pair<>(k.getVisualDescriptor(), search(objectToCompareFrom.getName(), k.getName())))
    );

    List<Pair<String, Double>> listTupleNormalized = RankingUtils.getSortedAndRankedTupleList(listTupleReferenceSimilarity);
    listTupleNormalized.forEach(
            x -> {
              ArticleSearchAgainst keyToStore = new ArticleSearchAgainst("", BigDecimal.ZERO , x.getKey());
              if (similarityToOthers.get(keyToStore) != null) {
                Double oldValue = similarityToOthers.get(keyToStore);
                similarityToOthers.put(keyToStore, (oldValue + x.getValue()));
              }
              System.out.println(": \t" + keyToStore.getVisualDescriptor() + "\t ranking " + x.getValue());

            }
    );

    return similarityToOthers;
  }

  /**
   * @param toCompare:      parameter Object A to compare from
   * @param compareAgainst: parameter Object B to search similarities
   * @return: normalized value between 0 to 1, with 1 most similar and 0 less similar
   */
  public double search(String toCompare, String compareAgainst) {
//  getting the pairs of both sentences to uppercase to be case insensible similarity
    List<String> pairs1 = wordLetterPairs(toCompare.toUpperCase());
    List<String> pairs2 = wordLetterPairs(compareAgainst.toUpperCase());
    int intersection = 0;
    int union = pairs1.size() + pairs2.size();

    for (String pair1 : pairs1) {
      for (int j = 0; j < pairs2.size(); j++) {
        Object pair2 = pairs2.get(j);
        if (pair1.equals(pair2)) {
          intersection++;
          pairs2.remove(j);
          break;
        }
      }
    }

    return (2.0 * intersection) / union;
  }

  /**
   * @param str: parameter string to be divided by pairs of adjacent letters following the algorithm,
   *             the string must be white space free !!! (single word)
   * @return an array of adjacent letter pairs contained in the input string
   */
  private static String[] letterPairs(String str) {
    int numPairs = str.length() - 1;
    String[] pairs = new String[numPairs];

    for (int i = 0; i < numPairs; i++) {
      pairs[i] = str.substring(i, i + 2);
    }

    return pairs;
  }

  /**
   * @param str: parameter string to be tokenized and divided by pairs following the algorithm
   * @return an ArrayList of 2-character Strings.
   */
  private static List<String> wordLetterPairs(String str) {
//  Tokenize the string and put the tokens (words) into an array
    String[] words = str.split("\\s");

    return Arrays.stream(words)                 // For each word
            .map(StringSimilarity::letterPairs) // Find the pairs of characters
            .flatMap(Arrays::stream)            // And return a list of them
            .collect(Collectors.toList());
  }


}
