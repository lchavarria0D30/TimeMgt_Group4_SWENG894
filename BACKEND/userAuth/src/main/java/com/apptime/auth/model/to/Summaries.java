package com.apptime.auth.model.to;

/**
 * @author Qi Zhang
 * TO for summaries
 * Use Case: TMGP4-319, TMGP4-320
 */
public class Summaries {
    private Category category;
    private Summary summaryForCurrentUser;
    private Summary summaryForAllUsers;

    public Summaries(Category category, Summary summaryForCurrentUser) {
        this.category = category;
        this.summaryForCurrentUser = summaryForCurrentUser;
    }

    public Summaries(Category category, Summary summaryForCurrentUser, Summary summaryForAllUsers) {
        this.category = category;
        this.summaryForCurrentUser = summaryForCurrentUser;
        this.summaryForAllUsers = summaryForAllUsers;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Summary getSummaryForCurrentUser() {
        return summaryForCurrentUser;
    }

    public void setSummaryForCurrentUser(Summary summaryForCurrentUser) {
        this.summaryForCurrentUser = summaryForCurrentUser;
    }

    public Summary getSummaryForAllUsers() {
        return summaryForAllUsers;
    }

    public void setSummaryForAllUsers(Summary summaryForAllUsers) {
        this.summaryForAllUsers = summaryForAllUsers;
    }
}
