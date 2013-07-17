package daverog.oldnews;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

public class NewsList extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        
        try {
        	List<NewsItem> list = new NewsLoadTask().execute().get();
        	
        	List<Map<String, String>> listData = Lists.transform(list, new Function<NewsItem, Map<String, String>>() {
        		public Map<String, String> apply(NewsItem item) {
        		    return ImmutableMap.<String, String>builder()
        		    		.put("title", item.getTitle())
		        		    .put("description", item.getDescription())
		        		    .put("modified", item.getModified())
		        		    .put("url", item.getUrl())
		        		    .build();
        		}
        	});
        	
			ListView lv = (ListView) findViewById(R.id.listView);
			final SimpleAdapter simpleAdpt = new SimpleAdapter(
					this, 
					listData, 
					R.layout.list_item,
					new String[] {"title", "description", "modified"}, 
					new int[] {android.R.id.text1, android.R.id.text2, R.id.text3});
			lv.setAdapter(simpleAdpt);
			
			lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> parentAdapter,
						View view, int position, long id) {
					@SuppressWarnings("unchecked")
					Map<String, String> itemData = (Map<String, String>) simpleAdpt.getItem(position);
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(itemData.get("url")));
				    startActivity(browserIntent);
				}
			});
		} catch (InterruptedException e) {
			Log.e("stuff", "interupted");
		} catch (ExecutionException e) {
			Log.e("stuff", "failed");
		}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.news_list, menu);
        return true;
    }
    
}
