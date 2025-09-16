package com.osg.openanimation.core.ui.preload
import kotlinx.browser.document
import kotlinx.coroutines.DelicateCoroutinesApi
import org.w3c.dom.AddEventListenerOptions
import org.w3c.dom.HTMLElement
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@OptIn(DelicateCoroutinesApi::class)
fun fadeOutElement(
    elementId: String = "splash",
    transitionDuration: Duration = 250.milliseconds
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