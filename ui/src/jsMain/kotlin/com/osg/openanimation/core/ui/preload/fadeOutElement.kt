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
    transitionDuration: Duration = 1.seconds
) {
    val transitionMs = transitionDuration.inWholeMilliseconds
    val element = document.getElementById(elementId) as? HTMLElement ?: return

    element.style.transition = "opacity ${transitionMs}ms ease-out"
    element.style.opacity = "0"

    val options = AddEventListenerOptions(once = true)
    element.addEventListener("transitionend", {
        element.remove()
    }, options)
}