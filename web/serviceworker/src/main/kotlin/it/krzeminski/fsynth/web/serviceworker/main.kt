import org.w3c.workers.ServiceWorkerGlobalScope

external val self: ServiceWorkerGlobalScope

fun main() {
    // The below implementation is mostly to satisfy validation performed by the browser,
    // in order to allow installing the app as a PWA.
    self.addEventListener("install", { event ->
        console.log("Service Worker installed! $event")
    })
    self.addEventListener("activate", { event ->
        console.log("Service Worker is now active! $event")
    })
    self.addEventListener("fetch", { event ->
        console.log("fetch event: $event")
    })
}
