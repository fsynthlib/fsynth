package it.krzeminski.fsynth

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.media.AudioTrack.WRITE_BLOCKING
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.plusAssign
import com.google.android.material.button.MaterialButton
import it.krzeminski.fsynth.songs.allSongs
import it.krzeminski.fsynth.types.Song
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        allSongs.forEach { song ->
            buttons += MaterialButton(this, null, R.attr.materialButtonOutlinedStyle).apply {
                text = song.name
                setOnClickListener {
                    song.playOnAndroid(sampleRate = 44100)
                }
            }
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
