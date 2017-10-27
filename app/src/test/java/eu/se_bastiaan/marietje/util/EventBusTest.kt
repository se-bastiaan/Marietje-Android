package eu.se_bastiaan.marietje.util

import org.junit.Before
import org.junit.Rule
import org.junit.Test

import rx.observers.TestSubscriber

class EventBusTest {

    private var eventBus: EventBus? = null

    @Rule
            // Must be added to every test class that targets app code that uses RxJava
    val overrideSchedulersRule = RxSchedulersOverrideRule()

    @Before
    fun setUp() {
        eventBus = EventBus()
    }

    @Test
    fun postedObjectsAreReceived() {
        val testSubscriber = TestSubscriber<Any>()
        eventBus!!.register(Any::class.java, testSubscriber)

        val event1 = Any()
        val event2 = Any()
        eventBus!!.post(event1)
        eventBus!!.post(event2)

        testSubscriber.assertValues(event1, event2)
    }

    @Test
    fun onlyReceivesSomeObjects() {
        val testSubscriber = TestSubscriber<String>()
        eventBus!!.register(String::class.java, testSubscriber)

        val stringEvent = "Event"
        val intEvent = 20
        eventBus!!.post(stringEvent)
        eventBus!!.post(intEvent)

        testSubscriber.assertValueCount(1)
        testSubscriber.assertValue(stringEvent)
    }
}