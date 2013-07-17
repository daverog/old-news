package daverog.oldnews;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import android.os.AsyncTask;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class NewsLoadTask extends AsyncTask<String, Integer, List<NewsItem>> {

	@Override
	protected List<NewsItem> doInBackground(String... params) {
		try {
			String date = new DateTime().minusYears(1).toString("yyyy-MM-dd");
			URL url = new URL("http://ec2-54-216-52-215.eu-west-1.compute.amazonaws.com/ldp-core/creative-works?type=cwork:NewsArticle&to=" + date );
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.addRequestProperty("Accept", "application/json-ld");
			try {
		     	InputStream in = new BufferedInputStream(urlConnection.getInputStream());
		     	return readNewsStream(in);
			} finally {
				urlConnection.disconnect();
			}
		} catch(IOException e) {
			return Lists.newArrayList(new NewsItem(e.getMessage()));
		}
	}
	
	private List<NewsItem> readNewsStream(InputStream in) throws IOException {
		StringWriter writer = new StringWriter();
		IOUtils.copy(in, writer, "UTF-8");
		ArrayList<NewsItem> items = new ArrayList<NewsItem>();
		
		JsonArray results = new JsonParser().parse(writer.toString()).getAsJsonObject().getAsJsonArray("results");
		for (JsonElement item: Lists.newArrayList(results.iterator())) {
			items.add(new NewsItem(
					item.getAsJsonObject().getAsJsonPrimitive("title").getAsString(),
					item.getAsJsonObject().getAsJsonPrimitive("description").getAsString(),
					item.getAsJsonObject().getAsJsonObject("primaryContentOf").getAsJsonPrimitive("@id").getAsString(),
					ISODateTimeFormat.dateTimeParser().parseDateTime(item.getAsJsonObject().getAsJsonPrimitive("dateModified").getAsString()).toString("EEE, dd MMMM yyyy 'at' KK:mm aa")
				));
		}

		return items;
	}

}