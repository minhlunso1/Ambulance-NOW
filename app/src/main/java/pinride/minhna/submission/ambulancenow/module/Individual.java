package pinride.minhna.submission.ambulancenow.module;

/**
 * Created by Minh on 3/26/2016.
 */
public class Individual {
    private String name;
    private String imgUrl;

    public Individual(String name, String imgUrl) {
        this.name = name;
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
