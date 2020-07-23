package com.twilio.starter.domain.sessionStart;

public class User {
    private String id;
    private String name;
    private String avatarURL;
    private String biography;
    private String followCount;
    private String followerCount;
    private String variantCount;
    private String viewCount;
    private boolean confirmedInterests;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getFollowCount() {
        return followCount;
    }

    public void setFollowCount(String followCount) {
        this.followCount = followCount;
    }

    public String getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(String followerCount) {
        this.followerCount = followerCount;
    }

    public String getVariantCount() {
        return variantCount;
    }

    public void setVariantCount(String variantCount) {
        this.variantCount = variantCount;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public boolean isConfirmedInterests() {
        return confirmedInterests;
    }

    public void setConfirmedInterests(boolean confirmedInterests) {
        this.confirmedInterests = confirmedInterests;
    }
}