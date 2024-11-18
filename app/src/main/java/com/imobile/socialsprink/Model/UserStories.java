package com.imobile.socialsprink.Model;

public class UserStories {
    private String image;

    public UserStories(String image, long storyAt) {
        this.image = image;
        this.storyAt = storyAt;
    }

    public UserStories() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getStoryAt() {
        return storyAt;
    }

    public void setStoryAt(long storyAt) {
        this.storyAt = storyAt;
    }

    private long storyAt;
}
