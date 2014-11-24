package models.dao;


import models.SearchDataset;

import java.util.List;

public interface DatasetDao {

    public List<SearchDataset> searchDatasetsByKeyword(String keyword);

    public List<SearchDataset> searchDatasetsByKeywordList(String[] keywords);

    public List<String> searchDatasetIDByKeyword(String keyword);

    public List<String> searchDatasetIDByKeywordList(String[] keywords);

    public List<SearchDataset> getDataSets(int limit);
}
