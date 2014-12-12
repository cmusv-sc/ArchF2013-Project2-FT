package models.dao;

import models.SearchDataset;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import java.util.List;

public class DatasetDaoImplemetation implements DatasetDao {
    private JdbcTemplate jdbcTemplate;

    public SimpleJdbcTemplate getSimpleJdbcTemplate() {
        return simpleJdbcTemplate;
    }

    public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
        this.simpleJdbcTemplate = simpleJdbcTemplate;
    }

    private SimpleJdbcTemplate simpleJdbcTemplate;

    @Override
    public List<SearchDataset> searchDatasetsByKeyword(String keyword) {
        String selectQuery = getSingleKeywordQuery(keyword);

        try {
            List<SearchDataset> searchResults = simpleJdbcTemplate.query(
                    selectQuery, ParameterizedBeanPropertyRowMapper
                            .newInstance(SearchDataset.class));
            return searchResults;
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public List<SearchDataset> searchDatasetsByKeywordList(String[] keywords) {
        String keywordListSQL = getMultiKeywordQuery(keywords);

        try {
            List<SearchDataset> searchResults = simpleJdbcTemplate.query(
                    keywordListSQL, ParameterizedBeanPropertyRowMapper
                            .newInstance(SearchDataset.class));
            return searchResults;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<SearchDataset> getDataSets(int limit) {
//        String selectQuery = "SELECT data_id, keyword FROM gcmd.datasets LIMIT "+limit;
		String selectQuery = "SELECT data_id, keyword, url, description,title FROM gcmd.datasets";

        try {
            List<SearchDataset> searchResults = simpleJdbcTemplate.query(
                    selectQuery, ParameterizedBeanPropertyRowMapper
                            .newInstance(SearchDataset.class));
            return searchResults;
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public List<String> searchDatasetIDByKeyword(String keyword) {
        String selectQuery = getSingleKeywordQuery(keyword);

        try {
            List<String> searchResults = (List<String>) jdbcTemplate.queryForList(selectQuery, String.class);
            return searchResults;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<String> searchDatasetIDByKeywordList(String[] keywords) {
        String keywordListSQL = getMultiKeywordQuery(keywords);

        try {
            List<String> searchResults = (List<String>) jdbcTemplate.queryForList(keywordListSQL, String.class);
            return searchResults;
        } catch (Exception e) {
            return null;
        }
    }

    private static String getMultiKeywordQuery(String[] keywords) {
//        StringBuilder keywordListSQL = new StringBuilder("SELECT distinct data_id FROM gcmd.datasets where ");
		StringBuilder keywordListSQL = new StringBuilder("SELECT data_id, keyword, url, description,title FROM gcmd.datasets where ");
        String keywordBefore = "keyword like ( '%' || '";
        String keywordAfter = "' || '%' ) ";
        boolean first = true;
        for (String word : keywords)
        {
            if (!first)
            {
                keywordListSQL.append("OR ");
            }
            first = false;
            keywordListSQL.append(keywordBefore);
            keywordListSQL.append(word);
            keywordListSQL.append(keywordAfter);
        }
        return keywordListSQL.toString();
    }

    private static String getSingleKeywordQuery(String keyword) {
//        return "SELECT distinct data_id FROM gcmd.datasets where keyword like ( '%' || '" + keyword + "' || '%' ) OR description like ( '%' || '" + keyword + "' || '%' )";
		return "SELECT data_id, keyword, url, description,title FROM gcmd.datasets " +
				"where keyword like ( '%' || '" + keyword + "' || '%' ) " +
				"OR description like ( '%' || '" + keyword + "' || '%' ) " +
				"OR title like ( '%' || '" + keyword + "' || '%' )";
    }


    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
