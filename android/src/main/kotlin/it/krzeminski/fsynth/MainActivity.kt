package it.krzeminski.fsynth

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.media.AudioTrack.WRITE_BLOCKING
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.button.MaterialButton
import it.krzeminski.fsynth.songs.allSongs
import it.krzeminski.fsynth.types.Song

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttons = findViewById<LinearLayout>(R.id.buttons)

        allSongs.forEach { song ->
            buttons.addView(MaterialButton(this, null, com.google.android.material.R.attr.materialButtonOutlinedStyle).apply {
                text = song.name
                setOnClickListener {
                    song.playOnAndroid(sampleRate = 44100)
                }
            })
        }
    }
}

private fun Song.playOnAndroid(sampleRate: Int) {
    val samples = renderWithSampleRate(sampleRate).toList()
    val audioTrack = AudioTrack(
            AudioManager.STREAM_MUSIC,
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_FLOAT,
            samples.size*java.lang.Float.BYTES,
            AudioTrack.MODE_STATIC)
    with(audioTrack) {
        write(samples.toFloatArray(), 0, samples.size, WRITE_BLOCKING)
        play()
    }
}
