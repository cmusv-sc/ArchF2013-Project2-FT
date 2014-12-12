package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import models.SearchDataset;
import models.dao.DatasetDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class KeywordSearchController extends Controller {
    private static ApplicationContext context;
    private static DatasetDao datasetDao;

    private static void checkDao() {
        if (context == null) {
            context = new ClassPathXmlApplicationContext("application-context.xml");
        }
        if (datasetDao == null) {
            datasetDao = (DatasetDao) context.getBean("datasetDaoImplementation");
        }
    }


    public static Result getDatasetByKeyword(String format, String keyword) {
        response().setHeader("Access-Control-Allow-Origin", "*");
        checkDao();
//        List<String> resultData = datasetDao.searchDatasetIDByKeyword(keyword);
        List<SearchDataset> resultData = datasetDao.searchDatasetsByKeyword(keyword);

        if (resultData == null || resultData.isEmpty()) {
            return notFound("no data sets found");
        }

        String ret = new Gson().toJson(resultData);
//        StringBuilder retStr = new StringBuilder();
//        for (String str : resultData) {
//            retStr.append(str);
//            retStr.append("\t");
//        }
//
//        return ok(retStr.toString());
		return ok(ret);
    }

    /**
     * Takes in a tab separated list of keywords, and output a set of restults
     * @return
     */
    public static Result getDatasetByKeywordList() {
        response().setHeader("Access-Control-Allow-Origin", "*");
        checkDao();
        String[] keywords = request().body().asText().split("\t");

        List<SearchDataset> resultData = datasetDao.searchDatasetsByKeywordList(keywords);
//        List<String> resultData = datasetDao.searchDatasetIDByKeywordList(keywords);

        if (resultData == null || resultData.isEmpty()) {
            return notFound("no data sets found");
        }

        String ret = new Gson().toJson(resultData);
//        StringBuilder retStr = new StringBuilder();
//        for (String str : resultData) {
//            retStr.append(str);
//            retStr.append("\t");
//        }

//        return ok(retStr.toString());
		return ok(ret);
    }

    public static Result getKeywordTree() {
        JsonNode root = new ObjectNode(JsonNodeFactory.instance);
        try {
            root = Json.parse(new FileInputStream("./public/data/keywords.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ok(root);
    }

    public static Result getDatasetList() {

        response().setHeader("Access-Control-Allow-Origin", "*");
        checkDao();

        List<SearchDataset> resultData = datasetDao.getDataSets(100);

        if (resultData == null || resultData.isEmpty()) {
            return notFound("no data sets found");
        }

        String ret = new Gson().toJson(resultData);

        return ok(ret);
    }
}
