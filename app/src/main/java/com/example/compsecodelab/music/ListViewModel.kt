package com.example.compsecodelab.music

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ListViewModel(initialState: MusicState = MusicState()) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<MusicState> get() = _state

    private val _musicCategory = MutableStateFlow(getMusicCategory())
    val musicCategory: StateFlow<List<MusicCategory>> get() = _musicCategory

    init {
        _state.tryEmit(
            initialState.copy(
                sections = getMusicSections()
            )
        )
    }

    fun onSelectMusic(music: Music) {
        _state.tryEmit(
            state.value.copy(selectedMusic = if (music != state.value.selectedMusic) music else null)
        )
    }

    private fun getMusicSections(): List<MusicSection> {
        return listOf(
            MusicSection(
                title = "My",
                musics = musics("My"),
                isExpandable = true
            ),
            MusicSection(
                title = "Favorite",
                musics = musics("Favorite"),
                isExpandable = false
            )
        )
    }

    private fun musics(section: String): List<Music> {
        return listOf(
            Music(
                0, "붉은 노을", "빅뱅", "03:18", section,
                url = "https://storage.googleapis.com/exoplayer-test-media-0/play.mp3"
            ),
            Music(
                1, "소원을 말해봐", "소녀시대", "03:18", section,
                url = "http://tastyspleen.net/~quake2/baseq2/sound/world/ra.wav"
            ),
            Music(2, "붉은 노을", "빅뱅", "03:18", section),
            Music(3, "소원을 말해봐", "소녀시대", "03:18", section),
            Music(4, "붉은 노을", "빅뱅", "03:18", section),
            Music(5, "소원을 말해봐", "소녀시대", "03:18", section),
            Music(6, "붉은 노을", "빅뱅", "03:18", section),
        )
    }

    private fun getMusicCategory(): List<MusicCategory> {
        return listOf(
            MusicCategory("My"),
            MusicCategory("All"),
            MusicCategory("New"),
            MusicCategory("Test"),
            MusicCategory("Pop")
        )
    }
}


data class MusicState(
    val selectedMusic: Music? = null,
    val sections: List<MusicSection> = emptyList()
)

data class Music(
    val id: Int,
    val title: String,
    val singer: String,
    val time: String,
    val category: String,
    val url: String? = null
)

data class MusicSection(
    val title: String,
    val musics: List<Music>,
    val isExpandable: Boolean
)

data class MusicCategory(
    val text: String
)

sealed class SideEffect
