package eu.se_bastiaan.marietje.util;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.functions.Func1;
import rx.plugins.RxJavaHooks;
import rx.schedulers.Schedulers;

/**
 * This rule registers SchedulerHooks for RxJava and RxAndroid to ensure that subscriptions
 * always subscribeOn and observeOn Schedulers.immediate().
 * Warning, this rule will reset RxAndroidPlugins and RxJavaPlugins before and after each test so
 * if the application code uses RxJava plugins this may affect the behaviour of the testing method.
 */
public class RxSchedulersOverrideRule implements TestRule {

    private final Func1<Scheduler, Scheduler> newThreadSchedulerFunc;
    private final Func1<Scheduler, Scheduler> ioThreadSchedulerFunc;
    private final Func1<Scheduler, Scheduler> compThreadSchedulerFunc;

    public RxSchedulersOverrideRule() {
        newThreadSchedulerFunc = scheduler -> Schedulers.immediate();
        ioThreadSchedulerFunc = scheduler -> Schedulers.immediate();
        compThreadSchedulerFunc = scheduler -> Schedulers.immediate();
    }

    private final RxAndroidSchedulersHook rxAndroidSchedulersHook = new RxAndroidSchedulersHook() {
        @Override
        public Scheduler getMainThreadScheduler() {
            return Schedulers.immediate();
        }
    };

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                RxAndroidPlugins.getInstance().reset();
                RxAndroidPlugins.getInstance().registerSchedulersHook(rxAndroidSchedulersHook);

                RxJavaHooks.reset();
                RxJavaHooks.setOnNewThreadScheduler(newThreadSchedulerFunc);
                RxJavaHooks.setOnIOScheduler(ioThreadSchedulerFunc);
                RxJavaHooks.setOnComputationScheduler(compThreadSchedulerFunc);

                base.evaluate();

                RxAndroidPlugins.getInstance().reset();
                RxJavaHooks.reset();
            }
        };
    }
}