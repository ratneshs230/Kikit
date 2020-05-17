package com.example.kikit;

import android.net.Uri;

public class Story_model {


        private String story_Name;
        private String story_desc;
        private String story_date;
        private String story_host;
        private String story_category;
        private String UID;
        private String story_image;
        private String storyKey;

    public Story_model(String story_Name, String story_desc, String story_date, String story_host, String story_category, String UID, String story_image, String storyKey, String story_key) {
        this.story_Name = story_Name;
        this.story_desc = story_desc;
        this.story_date = story_date;
        this.story_host = story_host;
        this.story_category = story_category;
        this.UID = UID;
        this.story_image = story_image;
        this.storyKey = storyKey;
        this.story_key = story_key;
    }

    public String getStoryKey() {
        return storyKey;
    }

    public void setStoryKey(String storyKey) {
        this.storyKey = storyKey;
    }

    public String getStory_image() {
        return story_image;
    }

    public void setStory_image(String story_image) {
        this.story_image = story_image;
    }

    public Story_model(String story_Name, String story_desc, String story_date, String story_host, String story_category, String UID, String story_image, String story_key) {
        this.story_Name = story_Name;
        this.story_desc = story_desc;
        this.story_date = story_date;
        this.story_host = story_host;
        this.story_category = story_category;
        this.UID = UID;
        this.story_image = story_image;
        this.story_key = story_key;
    }

    public Story_model(String story_Name, String story_desc, String story_date) {
        this.story_Name = story_Name;
        this.story_desc = story_desc;
        this.story_date = story_date;
    }

    public Story_model(String story_Name, String story_desc, String story_date, String story_host, String story_category) {
        this.story_Name = story_Name;
        this.story_desc = story_desc;
        this.story_date = story_date;
        this.story_host = story_host;
        this.story_category = story_category;
    }

    public Story_model(String story_Name, String story_desc, String story_date, String story_host, String story_category, String UID) {
        this.story_Name = story_Name;
        this.story_desc = story_desc;
        this.story_date = story_date;
        this.story_host = story_host;
        this.story_category = story_category;
        this.UID = UID;
    }

    public String getStory_key() {
        return story_key;
    }

    public void setStory_key(String story_key) {
        this.story_key = story_key;
    }

    private String story_key;

        public String getUID() {
            return UID;
        }

        public Story_model(String story_Name, String story_desc, String story_date, String story_category) {
            this.story_Name = story_Name;
            this.story_desc = story_desc;
            this.story_date = story_date;
            this.story_category = story_category;
        }

        public void setUID(String UID) {
            this.UID = UID;
        }

        public Story_model(){}


        public String getStory_category() {
            return story_category;
        }

        public void setStory_category(String story_category) {
            this.story_category = story_category;
        }

        public String getStory_Name() {
            return story_Name;
        }

        public void setStory_Name(String story_Name) {
            this.story_Name = story_Name;
        }

        public void setStory_desc(String story_desc) {
            this.story_desc = story_desc;
        }

        public void setStory_date(String story_date) {
            this.story_date = story_date;
        }

        public void setStory_host(String story_host) {
            this.story_host = story_host;
        }

        public String getStory_desc() {
            return story_desc;
        }

        public String getStory_date() {
            return story_date;
        }

        public String getStory_host() {
            return story_host;
        }

        public Story_model(String story_category) {
            this.story_category = story_category;
        }
    }


