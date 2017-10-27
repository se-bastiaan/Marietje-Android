package eu.se_bastiaan.marietje.util

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

import rx.Scheduler
import rx.android.plugins.RxAndroidPlugins
import rx.android.plugins.RxAndroidSchedulersHook
import rx.functions.Func1
import rx.plugins.RxJavaHooks
import rx.schedulers.Schedulers

/**
 * This rule registers SchedulerHooks for RxJava and RxAndroid to ensure that subscriptions
 * always subscribeOn and observeOn Schedulers.immediate().
 * Warning, this rule will reset RxAndroidPlugins and RxJavaPlugins before and after each test so
 * if the application code uses RxJava plugins this may affect the behaviour of the testing method.
 */
class RxSchedulersOverrideRule : TestRule {

    private val newThreadSchedulerFunc: Func1<Scheduler, Scheduler>
    private val ioThreadSchedulerFunc: Func1<Scheduler, Scheduler>
    private val compThreadSchedulerFunc: Func1<Scheduler, Scheduler>

    private val rxAndroidSchedulersHook = object : RxAndroidSchedulersHook() {
        override fun getMainThreadScheduler(): Scheduler {
            return Schedulers.immediate()
        }
    }

    init {
        newThreadSchedulerFunc = { scheduler -> Schedulers.immediate() }
        ioThreadSchedulerFunc = { scheduler -> Schedulers.immediate() }
        compThreadSchedulerFunc = { scheduler -> Schedulers.immediate() }
    }

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                RxAndroidPlugins.getInstance().reset()
                RxAndroidPlugins.getInstance().registerSchedulersHook(rxAndroidSchedulersHook)

                RxJavaHooks.reset()
                RxJavaHooks.setOnNewThreadScheduler(newThreadSchedulerFunc)
                RxJavaHooks.setOnIOScheduler(ioThreadSchedulerFunc)
                RxJavaHooks.setOnComputationScheduler(compThreadSchedulerFunc)

                base.evaluate()

                RxAndroidPlugins.getInstance().reset()
                RxJavaHooks.reset()
            }
        }
    }
}