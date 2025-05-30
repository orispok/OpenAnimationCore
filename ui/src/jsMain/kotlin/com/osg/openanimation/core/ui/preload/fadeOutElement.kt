import kotlinx.browser.document
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.w3c.dom.HTMLDivElement
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@OptIn(DelicateCoroutinesApi::class)
fun fadeOutElement(
    elementId: String = "splash",
    transitionDuration: Duration = 1.seconds,
    keepDuration: Duration = 100.milliseconds
) {
    val divElement = document.getElementById(elementId)
    val element = divElement as HTMLDivElement
    GlobalScope.launch {
        delay(keepDuration)
        val fadeOutMs = transitionDuration.inWholeMilliseconds
        element.style.transition = "opacity ${fadeOutMs}ms ease-out"
        element.style.opacity = "0"
        delay(transitionDuration)
        element.style.zIndex = "-1"
    }
}