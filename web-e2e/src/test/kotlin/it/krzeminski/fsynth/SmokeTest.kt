package it.krzeminski.fsynth

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer
import io.github.bonigarcia.wdm.WebDriverManager
import java.awt.image.BufferedImage
import java.io.File
import java.net.InetSocketAddress
import java.nio.file.Files
import java.time.Duration
import javax.imageio.ImageIO
import org.junit.AfterClass
import org.junit.Assert.assertTrue
import org.junit.BeforeClass
import org.junit.Test
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import ru.yandex.qatools.ashot.AShot
import ru.yandex.qatools.ashot.Screenshot

class SmokeTest {
    companion object {
        private lateinit var server: HttpServer
        private lateinit var driver: ChromeDriver
        private const val PORT = 8765
        private val baselineDir = File("src/test/resources/screenshots")

        @BeforeClass
        @JvmStatic
        fun setup() {
            val webDist = System.getProperty("web.dist")!!
            server = HttpServer.create(InetSocketAddress(PORT), 0).apply {
                createContext("/") { exchange: HttpExchange ->
                    val file = File(webDist, exchange.requestURI.path).normalize()
                    if (file.absolutePath.startsWith(File(webDist).absolutePath) && file.isFile) {
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
            println("HTTP server started on port $PORT, serving $webDist")

            WebDriverManager.chromedriver().setup()
            val options = ChromeOptions().apply {
                addArguments("--headless")
                addArguments("--no-sandbox")
                addArguments("--disable-dev-shm-usage")
                addArguments("--window-size=1280,720")
            }
            driver = ChromeDriver(options)
            println("ChromeDriver started")
        }

        @AfterClass
        @JvmStatic
        fun teardown() {
            driver.quit()
            server.stop(0)
            println("Cleanup done")
        }

        private fun assertScreenshot(name: String, tolerance: Double = 0.0) {
            baselineDir.mkdirs()
            val screenshot: Screenshot = AShot().takeScreenshot(driver)
            val baseline = File(baselineDir, "$name.png")
            if (!baseline.exists()) {
                ImageIO.write(screenshot.image, "PNG", baseline)
                return
            }
            val expected: BufferedImage = ImageIO.read(baseline)
            val diff = imageDiffPercent(expected, screenshot.image)
            if (diff > tolerance) {
                val diffFile = File(baselineDir, "${name}_diff.png")
                ImageIO.write(screenshot.image, "PNG", diffFile)
                org.junit.Assert.fail(
                    "Screenshot mismatch: $name (%.2f%% pixels differ, tolerance %.2f%%)".format(diff * 100, tolerance * 100)
                )
            }
        }

        private fun imageDiffPercent(expected: BufferedImage, actual: BufferedImage): Double {
            val w = minOf(expected.width, actual.width)
            val h = minOf(expected.height, actual.height)
            var diffCount = 0
            val total = w * h
            for (y in 0 until h) {
                for (x in 0 until w) {
                    if (expected.getRGB(x, y) != actual.getRGB(x, y)) {
                        diffCount++
                    }
                }
            }
            return diffCount.toDouble() / total
        }
    }

    @Test
    fun titleAndSongListAreDisplayed() {
        driver.get("http://localhost:$PORT/index.html")

        val bodyText = driver.findElement(By.tagName("body")).text
        assertTrue("Page should contain app title", bodyText.contains("fsynth"))
        assertTrue("Page should contain at least one song name", bodyText.contains("Simple demo song"))
        assertTrue("Page should contain playback customization section", bodyText.contains("Playback customization"))

        assertScreenshot("title-and-song-list")
    }

    @Test
    fun waveformIsDisplayedAfterSynthesizingSimpleDemoSong() {
        driver.get("http://localhost:$PORT/index.html")

        val playButton = driver.findElement(
            By.xpath("//*[contains(text(), 'Simple demo song')]/ancestor::li//button")
        )
        assertScreenshot("before-click")

        playButton.click()

        val wait = WebDriverWait(driver, Duration.ofSeconds(15))
        val waveform = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("waveform"))
        )

        assertTrue("Waveform should be visible after synthesis", waveform.isDisplayed)
        assertScreenshot("after-waveform", tolerance = 0.01)
    }
}
