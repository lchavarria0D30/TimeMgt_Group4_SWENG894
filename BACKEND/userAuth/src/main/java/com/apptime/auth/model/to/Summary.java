package com.apptime.auth.model.to;

import com.apptime.auth.model.AllUserTaskSummary;
import com.apptime.auth.model.TaskCategory;
import com.apptime.auth.model.UserTaskSummary;
import com.apptime.auth.util.DurationUtil;

public class Summary {
    private SummaryType type;
    private Category category;
    private String averageDuration;

    public SummaryType getType() {
        return type;
    }

    public void setType(SummaryType type) {
        this.type = type;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getAverageDuration() {
        return averageDuration;
    }

    public void setAverageDuration(String averageDuration) {
        this.averageDuration = averageDuration;
    }

    public static Summary parse(UserTaskSummary userTaskSummary, TaskCategory category) {
        if (userTaskSummary == null || category == null || userTaskSummary.getCategoryId() != category.getId()) {
            return null;
        }
        Summary summary = new Summary();
        summary.setType(SummaryType.MINE);
        summary.setCategory(Category.parse(category));
        summary.setAverageDuration(DurationUtil.toString(userTaskSummary.getAverageDuration()));
        return summary;
    }

    public static Summary parse(AllUserTaskSummary allUserTaskSummary, TaskCategory category) {
        if (allUserTaskSummary == null || category == null || allUserTaskSummary.getCategoryId() != category.getId()) {
            return null;
        }
        Summary summary = new Summary();
        summary.setType(SummaryType.ALL_USERS);
        summary.setCategory(Category.parse(category));
        summary.setAverageDuration(DurationUtil.toString(allUserTaskSummary.getAverageDuration()));
        return summary;
    }

}
