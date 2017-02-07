package eu.se_bastiaan.marietje.util;

import android.support.test.espresso.Espresso;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import rx.plugins.RxJavaHooks;
import rx.schedulers.Schedulers;

/**
 * This rule registers SchedulerHooks for RxJava to ensure that subscriptions
 * are detected as idling resources
 */
public class IdlingSchedulersOverrideRule implements TestRule {

    private final IdlingScheduler newThreadScheduler = new IdlingScheduler(Schedulers.newThread());
    private final IdlingScheduler ioThreadScheduler = new IdlingScheduler(Schedulers.io());
    private final IdlingScheduler compThreadScheduler = new IdlingScheduler(Schedulers.computation());

    public IdlingSchedulersOverrideRule() {
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                RxJavaHooks.reset();

                RxJavaHooks.setOnNewThreadScheduler(scheduler -> newThreadScheduler);
                RxJavaHooks.setOnIOScheduler(scheduler -> ioThreadScheduler);
                RxJavaHooks.setOnComputationScheduler(scheduler -> compThreadScheduler);

                Espresso.registerIdlingResources(newThreadScheduler.countingIdlingResource(),
                        ioThreadScheduler.countingIdlingResource(),
                        compThreadScheduler.countingIdlingResource());

                base.evaluate();

                Espresso.unregisterIdlingResources(newThreadScheduler.countingIdlingResource(),
                        ioThreadScheduler.countingIdlingResource(),
                        compThreadScheduler.countingIdlingResource());

                RxJavaHooks.reset();
            }
        };
    }
}