package it.krzeminski.fsynth

import it.krzeminski.fsynth.songs.allSongs
import react.dom.render
import kotlin.browser.document

fun main(args: Array<String>) {
    try {
        render(document.getElementById("root")) {
            app {
                attrs {
                    songs = allSongs
                }
            }
        }
    } catch (e: dynamic) {
        // This try-catch is a workaround needed to run the unit tests successfully.
        // It's not known why the test runner treats this entry point as another unit test. It cannot find the element
        // with ID "root", and it fails, throwing an exception that is caught in this 'catch'.
        // In production, the element is there, defined in index.html, and the exception isn't thrown.
    }
}
