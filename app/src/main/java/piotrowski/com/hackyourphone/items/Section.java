package piotrowski.com.hackyourphone.items;

public class Section implements Titled {
    private String mTitle;

    public Section(String title) {
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }
}
