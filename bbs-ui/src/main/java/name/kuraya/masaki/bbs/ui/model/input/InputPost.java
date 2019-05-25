package name.kuraya.masaki.bbs.ui.model.input;

import javax.validation.constraints.NotEmpty;

public class InputPost {

    @NotEmpty
    private String posterId;
    @NotEmpty
    private String comment;

    public InputPost() {}

    public InputPost(String posterId, String comment) {
        this.posterId = posterId;
        this.comment = comment;
    }

    public String getPosterId() {
        return posterId;
    }

    public void setPosterId(String userId) {
        this.posterId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}