package stashpullrequestbuilder.stashpullrequestbuilder;

import static java.lang.String.format;

import stashpullrequestbuilder.stashpullrequestbuilder.stash.StashPullRequestResponseValue;
import hudson.model.AbstractProject;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Nathan McCarthy
 */
public class StashPullRequestsBuilder {
    private static final Logger logger = Logger.getLogger(StashBuildTrigger.class.getName());
    private AbstractProject<?, ?> project;
    private StashBuildTrigger trigger;
    private StashRepository repository;
    private StashBuilds builds;

    public static StashPullRequestsBuilder getBuilder() {
        return new StashPullRequestsBuilder();
    }

    public void stop() {
        // TODO?
    }

    public void run() {
        logger.info(format("Build Start (%s).", project.getName()));
        try {
            this.repository.init();
            Collection<StashPullRequestResponseValue> targetPullRequests = this.repository.getTargetPullRequests();
            this.repository.addFutureBuildTasks(targetPullRequests);
        } catch (Exception e) {
            logger.log(Level.SEVERE, format("Build Failed (%s).", project.getName()), e);
        }
    }

    public StashPullRequestsBuilder setupBuilder() {
        if (this.project == null || this.trigger == null) {
            throw new IllegalStateException();
        }
        this.repository = new StashRepository(this.trigger.getProjectPath(), this);
        this.builds = new StashBuilds(this.trigger, this.repository);
        return this;
    }

    public void setProject(AbstractProject<?, ?> project) {
        this.project = project;
    }

    public void setTrigger(StashBuildTrigger trigger) {
        this.trigger = trigger;
    }

    public AbstractProject<?, ?> getProject() {
        return this.project;
    }

    public StashBuildTrigger getTrigger() {
        return this.trigger;
    }

    public StashBuilds getBuilds() {
        return this.builds;
    }
}
