package name.kuraya.masaki.bbs.ui.model.output;

import java.util.List;

public class OutputPosts {

    private List<OutputPost> posts;
    private int total;

    public OutputPosts() {
    }

    public OutputPosts(List<OutputPost> posts, int total) {
        this.posts = posts;
        this.total = total;
    }

    public List<OutputPost> getPosts() {
        return posts;
    }

    public void setPosts(List<OutputPost> posts) {
        this.posts = posts;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}