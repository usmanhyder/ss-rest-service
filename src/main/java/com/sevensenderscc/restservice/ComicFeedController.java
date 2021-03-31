package com.sevensenderscc.restservice;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.SyndFeedOutput;
import com.sun.syndication.io.XmlReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.json.*;


@RestController
@RequestMapping("/comicFeed")
public class ComicFeedController {

    public static final Logger logger = LoggerFactory.getLogger(RestServiceApplication.class);

    @GetMapping("/get10RecentEntries")
    public ResponseEntity<List<FeedObject>> getRecent10EntriesFromFeed() throws IOException {

        SyndFeedInput input = new SyndFeedInput();
        List<FeedObject> feedObjectList = new ArrayList<>();
        try {
            URL feedSource = new URL("http://feeds.feedburner.com/PoorlyDrawnLines");
            SyndFeed feed = input.build(new XmlReader(feedSource));
            logger.info("Success");
            String xml = new SyndFeedOutput().outputString(feed);

            try {
                JSONObject json = XML.toJSONObject(xml);
                /*String jsonString = json.toString(4);
                System.out.println(jsonString);*/
                JSONArray array = json.getJSONObject("rss").getJSONObject("channel").getJSONArray("item");
                for (int i = 0; i < array.length(); i++) {
                    FeedObject feedObject = new FeedObject();
                    feedObject.setTitle(array.getJSONObject(i).getString("title"));
                    feedObject.setWebURL(array.getJSONObject(i).getString("link"));

                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    Date date = dateFormat.parse(array.getJSONObject(i).getString("dc:date"));
                    feedObject.setPublishingDate(date);
                    feedObject.setPublishingDateString(date.toString());

                    Document doc = Jsoup.parse(array.getJSONObject(i).getString("content:encoded"));
                    feedObject.setPictureURL(doc.select("a").first().attr("href"));
                    feedObjectList.add(feedObject);
                }

                // Sort the resulting feed by publishing date from recent to older
                Collections.sort(feedObjectList, Collections.reverseOrder());
//                System.out.println(feedObjectList.get(0).getTitle());

            } catch (JSONException e) {
// TODO: handle exception
                System.out.println(e.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } catch (FeedException e) {
            e.printStackTrace();
        }

        // Return Json Response Object containing the Feed List
        return ResponseEntity.ok(feedObjectList);
    }
}
