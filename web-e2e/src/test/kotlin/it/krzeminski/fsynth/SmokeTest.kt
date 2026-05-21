package it.krzeminski.fsynth

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer
import io.github.bonigarcia.wdm.WebDriverManager
import java.io.File
import java.net.InetSocketAddress
import java.nio.file.Files
import java.time.Duration
import org.junit.AfterClass
import org.junit.Assert.assertTrue
import org.junit.BeforeClass
import org.junit.Test
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.WebDriverWait

class SmokeTest {
    companion object {
        private lateinit var server: HttpServer
        private lateinit var driver: ChromeDriver
        private const val PORT = 8765

        @BeforeClass
        @JvmStatic
        fun setup() {
            val webDist = System.getProperty("web.dist")!!
            server = HttpServer.create(InetSocketAddress(PORT), 0).apply {
                createContext("/") { exchange: HttpExchange ->
                    val file = File(webDist, exchange.requestURI.path).normalize()
                    if (file.absolutePath.startsWith(File(webDist).absolutePath) && file.isFile) {
                        exchange.responseHeaders.add("Content-Type", when {
                            file.name.endsWith(".js") -> "application/javascript"
                            file.name.endsWith(".html") -> "text/html"
                            file.name.endsWith(".css") -> "text/css"
                            file.name.endsWith(".svg") -> "image/svg+xml"
                            else -> "application/octet-stream"
                        })
                        exchange.sendResponseHeaders(200, file.length())
                        Files.copy(file.toPath(), exchange.responseBody)
                    } else {
                        exchange.sendResponseHeaders(404, -1)
                    }
                    exchange.close()
                }
                executor = null
            }
            server.start()

            WebDriverManager.chromedriver().setup()
            driver = ChromeDriver(ChromeOptions().apply {
                addArguments("--headless")
                addArguments("--no-sandbox")
                addArguments("--disable-dev-shm-usage")
            })
        }

        @AfterClass
        @JvmStatic
        fun teardown() {
            driver.quit()
            server.stop(0)
        }
    }

    @Test
    fun titleAndSongListAreDisplayed() {
        driver.get("http://localhost:$PORT/index.html")

        var bodyText = ""
        WebDriverWait(driver, Duration.ofSeconds(10)).until {
            bodyText = driver.findElement(By.tagName("body")).text
            bodyText.contains("fsynth")
        }

        assertTrue("Page should contain app title", bodyText.contains("fsynth"))
        assertTrue("Page should contain at least one song name", bodyText.contains("Simple demo song"))
        assertTrue("Page should contain playback customization section", bodyText.contains("Playback customization"))
    }
}
