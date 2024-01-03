package com.cookingapp.cookingapp.util;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery.Builder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.util.ObjectBuilder;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;
import org.springframework.data.elasticsearch.client.elc.Queries;

@UtilityClass
public class ESUtil {

  /*
  public static Supplier<Query> createBoolQuery(SearchRequestDto dto) {
    return () -> Query.of(q -> q.bool(boolQuery(dto.getFieldName().get(0).toString(), dto.getSearchValue().get(0),
        dto.getFieldName().get(1).toString(), dto.getSearchValue().get(1))));
  }

   */

  public static BoolQuery boolQuery(String key1, String value1, String key2, String value2) {
    return new BoolQuery.Builder()
        // todo filtermap ve recipename alsın, filtera filtermaptekileri ekle
        .filter(termQuery(key1.toString(), value1), termQuery(key2.toString(), value2))
        .must(matchQuery(key2.toString(), value2))
        .build();
  }

  public static Query termQuery(String field, String value) {
    return Query.of(q -> q.term(new TermQuery.Builder()
        .field(field)
        .value(value)
        .build()));
  }

  public static Query matchQuery(String field, String value) {
    return Query.of(q -> q.match(new MatchQuery.Builder()
        .field(field)
        .query(value)
        .build()));
  }
  public static Supplier<Query> buildQueryForFieldAndValue(String fieldName, String searchValue, boolean fuzziness) {
    if(fuzziness){
      return () -> Query.of(q -> q.match(buildMatchQueryForFieldAndValueWithFuzziness(fieldName, searchValue)));
    }
    return () -> Query.of(q -> q.match(buildMatchQueryForFieldAndValue(fieldName, searchValue)));
  }

  private static MatchQuery buildMatchQueryForFieldAndValue(String fieldName, String searchValue) {
    return new MatchQuery.Builder()
        .field(fieldName)
        .query(searchValue)
        .build();
  }

  private static MatchQuery buildMatchQueryForFieldAndValueWithFuzziness(String fieldName, String searchValue) {
    return new MatchQuery.Builder()
        .field(fieldName)
        .query(searchValue)
        .fuzziness("AUTO")
        .analyzer("custom_search")
        .build();
  }

}
