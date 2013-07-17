package daverog.oldnews;

public class NewsItem {
	
	private String title;
	private String description;
	private String url;
	private String modified;
	
	public NewsItem(String message) {
		this.title = message;
	}
	
	public NewsItem(String title, String description, String url, String modified) {
		this.title = title;
		this.description = description;
		this.url = url;
		this.modified = modified;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getUrl() {
		return url;
	}

	public String getModified() {
		return modified;
	}

}
