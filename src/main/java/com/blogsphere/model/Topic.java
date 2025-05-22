package com.blogsphere.model;

public enum Topic {
    LIFE("Life"),
    FAMILY("Family"),
    HEALTH("Health"),
    RELATIONSHIPS("Relationships"),
    SELF_IMPROVEMENT("Self Improvement"),
    MENTAL_HEALTH("Mental Health"),
    PRODUCTIVITY("Productivity"),
    MINDFULNESS("Mindfulness"),
    WORK("Work"),
    BUSINESS("Business"),
    MARKETING("Marketing"),
    LEADERSHIP("Leadership"),
    TECHNOLOGY("Technology"),
    ARTIFICIAL_INTELLIGENCE("Artificial Intelligence"),
    BLOCKCHAIN("Blockchain"),
    DATA_SCIENCE("Data Science"),
    SOFTWARE_DEVELOPMENT("Software Development"),
    PROGRAMMING("Programming"),
    MEDIA("Media"),
    WRITING("Writing"),
    ART("Art"),
    GAMING("Gaming"),
    SOCIETY("Society"),
    ECONOMICS("Economics"),
    EDUCATION("Education"),
    EQUALITY("Equality"),
    CULTURE("Culture"),
    PHILOSOPHY("Philosophy"),
    RELIGION("Religion"),
    SPIRITUALITY("Spirituality"),
    WORLD("World"),
    CITIES("Cities"),
    NATURE("Nature"),
    TRAVEL("Travel");

    private final String displayName;

    Topic(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}