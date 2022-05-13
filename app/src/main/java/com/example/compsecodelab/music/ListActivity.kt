package com.example.compsecodelab.music

import BottomSheetLayouts
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compsecodelab.MeasureWidthOf
import com.example.compsecodelab.R
import com.example.compsecodelab.asPxtoDP
import com.example.compsecodelab.composepager.customTabIndicatorOffset
import com.example.compsecodelab.ownTabIndicatorOffset
import com.example.compsecodelab.ui.theme.CompseCodeLabTheme
import com.google.accompanist.pager.*
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.Util
import kotlinx.coroutines.launch

class ListActivity : ComponentActivity() {

    private val viewModel: ListViewModel by viewModels()

    private var player: SimpleExoPlayer? = null

    private fun initializePlayer() {
        player = SimpleExoPlayer.Builder(this)
            .build().also { exoPlayer ->
                val mediaItem =
                    MediaItem.fromUri("https://storage.googleapis.com/exoplayer-test-media-0/play.mp3")
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(currentWindow, playbackPosition)
                exoPlayer.prepare()
            }

    }

    public override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    public override fun onResume() {
        super.onResume()
        if ((Util.SDK_INT < 24 || player == null)) {
            initializePlayer()
        }
    }

    public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    public override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L
    private fun releasePlayer() {
        player?.run {
            playbackPosition = this.currentPosition
            currentWindow = this.currentWindowIndex
            playWhenReady = this.playWhenReady
            release()
        }
        player = null
    }

    companion object {
        const val TYPE = "type"
        const val DARK_THEME = "darkTheme"
        fun newIntent(context: Context) = Intent(context, ListActivity::class.java)

        val tabData = listOf(
            "MUSIC",
            "MARKET",
            "FILMS",
            "BOOKS",
            "MUSIC",
            "MARKET",
            "FILMS",
            "BOOKS",
        )
    }

    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompseCodeLabTheme {
                val state by viewModel.state.collectAsState(initial = MusicState())
                Scaffold {
                    BottomSheetLayouts(state)
//                    (state, categoryTabs = tabData, viewModel)
                }
            }
        }
    }
}

@Composable
fun MusicList(state: MusicState) {
    Column {
        Box(Modifier.fillMaxHeight()) {
            Column() {
                Greetings(
                    musicSections = state.sections,
                    selectedMusic = state.selectedMusic,
                    onSelect = {
//                        viewModel.onSelectMusic(it)
                    }
                )
            }
        }
    }
}

@ExperimentalPagerApi
@OptIn(ExperimentalMaterialApi::class, dev.chrisbanes.snapper.ExperimentalSnapperApi::class)
@Composable
fun MusicListContent(state: MusicState, categoryTabs: List<String>) {
    val pagerState = rememberPagerState()

    Column {
        CustomScrollableRow(
            tabs = categoryTabs,
            pagerState = pagerState,
        )
        MySoundButton {}

        HorizontalPager(
            count = categoryTabs.size,
            state = pagerState,
            modifier = Modifier.weight(1f),
        ) { index ->
            MusicList(state)
        }
        if (state.selectedMusic != null) {
            MusicController(
                modifier = Modifier,
                selectedMusic = state.selectedMusic
            )
        }
    }
}

//https://medium.com/@sukhdip_sandhu/jetpack-compose-scrollabletabrow-indicator-matches-width-of-text-e79c0e5826fe
@ExperimentalPagerApi
@Composable
fun CustomScrollableRow(
    tabs: List<String>,
    pagerState: PagerState,
) {
    val coroutineScope = rememberCoroutineScope()
    val selectedTabIndex = pagerState.currentPage
    val activeIndicatorTextWidth = remember { mutableStateOf(0f) }

    val density = LocalDensity.current

    val tabWidths = remember {
        val tabWidthStateList = mutableStateListOf<Dp>()
        repeat(tabs.size) {
            tabWidthStateList.add(0.dp)
        }
        tabWidthStateList
    }

    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = Modifier.height(40.dp),
//        backgroundColor = Color.Transparent,
        contentColor = Color.Black,
        edgePadding = 0.dp,
        divider = { TabRowDefaults.Divider(color = Color.Transparent) },
        indicator = { tabPositions ->
//            TabRowDefaults.Indicator(
//                color = Color.Red,
//                height = 4.dp,
//                modifier = Modifier
//                    .ownTabIndicatorOffset(
//                        currentTabPosition = tabPositions[selectedTabIndex],
//                        currentTabWidth = activeIndicatorTextWidth.value.asPxtoDP(density.density)
//                    )
//            )
            TabRowDefaults.Indicator(
                modifier = Modifier.customTabIndicatorOffset(
                    currentTabPosition = tabPositions[selectedTabIndex],
                    tabWidth = tabWidths[selectedTabIndex]// ToDo - Explained next.
                )
            )
        }
    ) {
        tabs.forEachIndexed { tabIndex, tab ->
            Text(
                text = tab,
                modifier = Modifier.wrapContentSize(),
                onTextLayout = { textLayoutResult ->
                    tabWidths[tabIndex] =
                        with(density) { textLayoutResult.size.width.toDp() }
                }
            )
//            Tab(
//                modifier = Modifier.wrapContentSize().background(Color.White),
//                selectedContentColor = Color.Black,
//                unselectedContentColor = Color.Gray,
//                selected = selectedTabIndex == tabIndex, onClick = {
//                    coroutineScope.launch {
//                        pagerState.animateScrollToPage(tabIndex)
//                    }
//                },
//                text = {
//                    Text(
//                        text = tab,
//                        modifier = Modifier.wrapContentSize(),
//                        onTextLayout = { textLayoutResult ->
//                            tabWidths[tabIndex] =
//                                with(density) { textLayoutResult.size.width.toDp() }
//                        })
//                }
//            )
//                content = {
//                    val text = @Composable {
//                        Text(
//                            tab,
//                            color = Color.Black,
//                            //style = styles.seasonBarTextStyle(index == selectedSeason)
//                        )
//                    }
//                    if (selectedTabIndex == tabIndex) {
//                        MeasureWidthOf(setWidth = { activeIndicatorTextWidth.value = it.toFloat() }) {
//                            text()
//                        }
//                    } else {
//                        text()
//                    }
//                })
        }
    }
}

@Composable
private fun Header(
    title: String,
    isExpandable: Boolean,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(modifier = Modifier.weight(1f), text = title)
        if (isExpandable) {
            Image(
                painter = if (isExpanded) painterResource(id = R.drawable.ic_launcher_background)
                else painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)
                    .clickable {
                        onClick.invoke()
                    }
            )
        }
    }
}

@Composable
private fun MySoundButton(
    openGallery: () -> Unit,
) {
    Button(
        onClick = { openGallery.invoke() },
        modifier = Modifier
            .padding(horizontal = 18.dp, vertical = 14.dp)
            .fillMaxWidth()
            .height(40.dp),

        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
    ) {
        Text(
            text = "Get The My Sound",
            style = MaterialTheme.typography.body1,
            color = Color.White
        )
    }
}

@Composable
private fun MusicController(
    modifier: Modifier,
    selectedMusic: Music?,
) {
    selectedMusic ?: return
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.White),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = R.drawable.heart_red),
            modifier = Modifier.size(65.dp),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Text(
            text = selectedMusic.title,
            style = typography.h6.copy(fontSize = 14.sp),
            modifier = Modifier
                .padding(8.dp)
                .weight(1f),
        )
        Icon(
            imageVector = Icons.Default.FavoriteBorder, modifier = Modifier.padding(8.dp),
            contentDescription = null
        )
        Icon(
            imageVector = Icons.Default.PlayArrow, modifier = Modifier.padding(8.dp),
            contentDescription = null
        )
    }

}

@Composable
private fun Greetings(
    musicSections: List<MusicSection>,
    selectedMusic: Music?,
    onSelect: (Music) -> Unit,
) {
    val expandedState = remember(musicSections) {
        musicSections.map { it.isExpandable }.toMutableStateList()
    }

    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        musicSections.forEachIndexed { i, section ->
            val expanded = expandedState[i]
            item(key = i) {
                if (section.musics.isNotEmpty()) {
                    Header(
                        title = section.title,
                        isExpandable = section.isExpandable,
                        isExpanded = expanded,
                        onClick = { expandedState[i] = !expanded }
                    )
                    if (section.title == "My") {
                        Greeting(
                            music = section.musics.first(),
                            selectedMusic = selectedMusic,
                            onSelect = {
                                onSelect.invoke(it)
                            }
                        )
                    }
                }
            }
            if (!expanded) {
                items(section.musics) { music ->
                    Greeting(music = music,
                        selectedMusic = selectedMusic,
                        onSelect = {
                            onSelect.invoke(it)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun Greeting(music: Music, selectedMusic: Music?, onSelect: (Music) -> Unit) {
    Surface(
        color = Color.White,
        modifier = Modifier.padding(vertical = 10.dp, horizontal = 18.dp)
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight(Alignment.CenterVertically)
                .clickable(onClick = {
                    onSelect.invoke(music)
                }),
            verticalArrangement = Arrangement.Center,
        ) {
            Row() {
                Box(
                    modifier = Modifier
                        .wrapContentHeight(Alignment.CenterVertically)
                ) {
                    Image(
//                        painter = rememberImagePainter("https://www.example.com/image.jpg"),
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(10.dp)),
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = music.title, fontSize = 15.sp)
                    Text(text = music.singer, fontSize = 13.sp)
                    Text(text = music.time, fontSize = 11.sp)
                }
                Row(
                    modifier = Modifier
                        .fillMaxHeight(),
                ) {
                    if (selectedMusic != music) {
                        //favorite button
                        Image(
                            painter = painterResource(R.drawable.ic_launcher_background),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(20.dp)
                                .fillMaxHeight()
                        )
                    } else { //applied music
                        //cut button
                        Image(
                            painter = painterResource(R.drawable.ic_launcher_background),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(20.dp)
                                .fillMaxHeight()
                        )
                        //more button
                        Image(
                            painter = painterResource(R.drawable.ic_launcher_background),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(20.dp)
                                .fillMaxHeight()
                        )
                        //
                        Image(
                            painter = painterResource(R.drawable.ic_launcher_background),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(20.dp)
                                .fillMaxHeight()
                        )
                    }
                }
            }
            if (selectedMusic == music) {
                Text(
                    "Hello",
                    Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    val dataSource by lazy { ListViewModel() }

    CompseCodeLabTheme {
//        Greeting(dataSource.getMusics()[0])
    }
}