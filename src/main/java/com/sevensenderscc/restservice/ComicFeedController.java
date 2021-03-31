package com.sevensenderscc.restservice;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.SyndFeedOutput;
import com.sun.syndication.io.XmlReader;
import org.joda.time.LocalDate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.json.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/comicFeed")
public class ComicFeedController {

    public static final Logger logger = LoggerFactory.getLogger(RestServiceApplication.class);

    @GetMapping("/get20RecentEntries")
    public ResponseEntity<List<FeedObject>> getRecent20EntriesFromFeed() throws IOException {

        List<FeedObject> feedObjectList = getEntriesFromPDLFeed();
        feedObjectList = getEntriesFromWebComicAPI(feedObjectList);

        // Sort the resulting feed by publishing date from recent to older
        Collections.sort(feedObjectList, Collections.reverseOrder());

        // Return Json Response Object containing the Feed List
        return ResponseEntity.ok(feedObjectList);
    }

    public List<FeedObject> getEntriesFromWebComicAPI(List<FeedObject> feedObjectList) {
        Integer lastComicNum = getLastComicNumberFromWebComicAPI();
        String comicAPIUrl;
        RestTemplate restTemplate = new RestTemplate();
        for (int i = 0; i < 10; i++) {
            comicAPIUrl = "https://xkcd.com/" + lastComicNum + "/info.0.json";
            WebComicAPIObject webComicAPIObject = restTemplate.getForObject(comicAPIUrl, WebComicAPIObject.class);
//            logger.info(webComicAPIObject.toString());

            //Decrement Comic Number
            lastComicNum--;

            FeedObject feedObject = new FeedObject();
            feedObject.setTitle(webComicAPIObject.getTitle());
            feedObject.setPictureURL(webComicAPIObject.getImg());
            feedObject.setWebURL(comicAPIUrl);

            LocalDate localDate = new LocalDate(webComicAPIObject.getYear(), webComicAPIObject.getMonth(), webComicAPIObject.getDay());     //Create Date object from year,month,day values received from WebComic API
            feedObject.setPublishingDate(localDate.toDate());
            feedObject.setPublishingDateString(localDate.toDate().toString());
            feedObjectList.add(feedObject);
        }
        return feedObjectList;
    }

    private Integer getLastComicNumberFromWebComicAPI() {
        String currentComicURL = "https://xkcd.com/info.0.json";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> call = restTemplate.getForEntity(currentComicURL, String.class);
        JsonObject jsonObj = new Gson().fromJson(call.getBody(), JsonObject.class);
        return jsonObj.get("num").getAsInt();
    }

    public List<FeedObject> getEntriesFromPDLFeed() throws IOException {
        SyndFeedInput input = new SyndFeedInput();
        List<FeedObject> feedObjectList = new ArrayList<>();
        try {
            URL feedSource = new URL("http://feeds.feedburner.com/PoorlyDrawnLines");
            SyndFeed feed = input.build(new XmlReader(feedSource));
            logger.info("Success");
            String xml = new SyndFeedOutput().outputString(feed);
            try {
                JSONObject json = XML.toJSONObject(xml);
                JSONArray array = json.getJSONObject("rss").getJSONObject("channel").getJSONArray("item");
                for (int i = 0; i < array.length(); i++) {
                    FeedObject feedObject = new FeedObject();
                    feedObject.setTitle(array.getJSONObject(i).getString("title"));
                    feedObject.setWebURL(array.getJSONObject(i).getJSONObject("feedburner:origLink").get("content").toString());

                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    Date date = dateFormat.parse(array.getJSONObject(i).getString("dc:date"));
                    feedObject.setPublishingDate(date);
                    feedObject.setPublishingDateString(date.toString());

                    Document doc = Jsoup.parse(array.getJSONObject(i).getString("content:encoded"));
                    feedObject.setPictureURL(doc.select("a").first().attr("href"));
                    feedObjectList.add(feedObject);
                }
            } catch (JSONException e) {
                System.out.println(e.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } catch (FeedException e) {
            e.printStackTrace();
        }
        return feedObjectList;
    }
}
