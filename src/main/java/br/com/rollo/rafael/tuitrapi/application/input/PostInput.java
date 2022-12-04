package br.com.rollo.rafael.tuitrapi.application.input;

import br.com.rollo.rafael.tuitrapi.application.validators.ImageResourcePath;
import br.com.rollo.rafael.tuitrapi.domain.posts.Post;
import br.com.rollo.rafael.tuitrapi.domain.users.User;
import br.com.rollo.rafael.tuitrapi.domain.users.UserRepository;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class PostInput {

    @Length(max = 280)
    private String textContent;

    @ImageResourcePath
    private String imagePath;

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Post buildFor(User author) {
        return Post.createAsSinglePost(
                Post.Content.withTextAndImage(this.textContent, this.imagePath),
                author);
    }

}
