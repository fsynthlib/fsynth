package it.krzeminski.fsynth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.view.plusAssign
import it.krzeminski.fsynth.songs.allSongs
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        allSongs.forEach { song ->
            buttons += Button(this).apply {
                text = song.name
                setOnClickListener {
                    println("TODO: synthesize ${song.name}")
                }
            }
        }
    }
}
