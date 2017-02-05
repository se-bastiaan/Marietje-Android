package eu.se_bastiaan.marietje.test.common;

import android.content.Context;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import eu.se_bastiaan.marietje.MarietjeApp;
import eu.se_bastiaan.marietje.data.DataManager;
import eu.se_bastiaan.marietje.data.local.PreferencesHelper;
import eu.se_bastiaan.marietje.test.common.injection.component.DaggerTestComponent;
import eu.se_bastiaan.marietje.test.common.injection.component.TestComponent;
import eu.se_bastiaan.marietje.test.common.injection.module.AppTestModule;

/**
 * Test rule that creates and sets a Dagger TestComponent into the application overriding the
 * existing application component.
 * Use this rule in your test case in order for the app to use mock dependencies.
 * It also exposes some of the dependencies so they can be easily accessed from the tests, e.g. to
 * stub mocks etc.
 */
public class TestComponentRule implements TestRule {

    private final TestComponent testComponent;
    private final Context context;

    public TestComponentRule(Context context) {
        this.context = context;
        MarietjeApp application = MarietjeApp.get(context);
        testComponent = DaggerTestComponent.builder()
                .appTestModule(new AppTestModule(application))
                .build();
    }

    public Context getContext() {
        return context;
    }

    public DataManager getMockDataManager() {
        return testComponent.dataManager();
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                MarietjeApp application = MarietjeApp.get(context);
                application.setComponent(testComponent);
                base.evaluate();
                application.setComponent(null);
            }
        };
    }
}
