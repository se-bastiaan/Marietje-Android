package eu.se_bastiaan.marietje.util

import org.junit.Before
import org.junit.Rule
import org.junit.Test

import rx.observers.TestSubscriber

class EventBusTest {

    lateinit var eventBus: EventBus

    @get:Rule
    val overrideSchedulersRule = RxSchedulersOverrideRule()

    @Before
    fun setUp() {
        eventBus = EventBus()
    }

    @Test
    fun postedObjectsAreReceived() {
        val testSubscriber = TestSubscriber<Any>()
        eventBus.register(Any::class.java, testSubscriber)

        val event1 = Any()
        val event2 = Any()
        eventBus.post(event1)
        eventBus.post(event2)

        testSubscriber.assertValues(event1, event2)
    }

    @Test
    fun onlyReceivesSomeObjects() {
        val testSubscriber = TestSubscriber<String>()
        eventBus.register(String::class.java, testSubscriber)

        val stringEvent = "Event"
        val intEvent = 20
        eventBus.post(stringEvent)
        eventBus.post(intEvent)

        testSubscriber.assertValueCount(1)
        testSubscriber.assertValue(stringEvent)
    }
}