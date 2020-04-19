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

    public Summaries(Category category, Summary summaryForCurrentUser, Summary summaryForAllUsers) {
        this.category = category;
        this.summaryForCurrentUser = summaryForCurrentUser;
        this.summaryForAllUsers = summaryForAllUsers;
    }

    public Category getCategory() {
        return category;
    }

    public Summary getSummaryForCurrentUser() {
        return summaryForCurrentUser;
    }

    public Summary getSummaryForAllUsers() {
        return summaryForAllUsers;
    }
}
