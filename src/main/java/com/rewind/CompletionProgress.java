package com.rewind;

import lombok.Value;

/** Snapshot of completion objectives available at the selected historical date. */
@Value
public class CompletionProgress
{
    int completedQuests;
    int availableQuests;
    int completedSkillMilestones;
    int availableSkillMilestones;
    int completedActivities;
    int availableActivities;

    public int getCompleted()
    {
        return completedQuests + completedSkillMilestones + completedActivities;
    }

    public int getAvailable()
    {
        return availableQuests + availableSkillMilestones + availableActivities;
    }

    public int getPercentage()
    {
        int available = getAvailable();
        return available == 0 ? 0 : (int) Math.round(getCompleted() * 100.0 / available);
    }
}
