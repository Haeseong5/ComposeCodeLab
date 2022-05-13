package com.example.compsecodelab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.compsecodelab.ui.theme.CompseCodeLabTheme

class DialogActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompseCodeLabTheme() {
                mainComp()
            }
        }
    }
}

data class Season(val d: Int = 0, val episodes: List<Int> = (0 until 20).toList())


fun Float.asPxtoDP(density: Float): Dp {
    return (this / (density)).dp
}

@Composable
fun mainComp() {

    val (selectedSeason, setSelectedSeason) = remember { mutableStateOf(0) }
    val seasonsList = mutableListOf(
        Season(2020), Season(2021), Season(2022)
    )
    val activeIndicatorTextWidth = remember { mutableStateOf(0f) }
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .background(Color.Magenta)
                ) {}
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .background(Color.Green),
                ) {}
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .background(Color.Blue),
                ) {}
                val density = LocalDensity.current
                ScrollableTabRow(
                    selectedTabIndex = selectedSeason,
                    backgroundColor = Color.White,
                    edgePadding = 0.dp,
                    modifier = Modifier
                        .padding(vertical = 24.dp)
                        .fillMaxWidth()
                        .height(40.dp),
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            color = Color.Red,
                            height = 4.dp,
                            modifier = Modifier
                                .ownTabIndicatorOffset(
                                    currentTabPosition = tabPositions[selectedSeason],
                                    currentTabWidth = activeIndicatorTextWidth.value.asPxtoDP(density.density)
                                )
                        )
                    }
                ) {
                    seasonsList.forEachIndexed { index, contentItem ->
                        Tab(
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .padding(bottom = 10.dp),
                            selected = index == selectedSeason,
                            onClick = {
                                setSelectedSeason.invoke(index)
                            }
                        )
                        {
                            val text = @Composable {
                                Text(
                                    "Season " + contentItem.d,
                                    color = Color.Black,
                                    //style = styles.seasonBarTextStyle(index == selectedSeason)
                                )
                            }
                            if (index == selectedSeason) {
                                MeasureWidthOf(setWidth = { activeIndicatorTextWidth.value = it.toFloat() }) {
                                    text()
                                }
                            } else {
                                text()
                            }
                        }
                    }

                }
            }
        },
        content = {
//            WithConstraints {
//                val density = AmbientDensity.current
//                val width = with(density) { constraints.maxWidth.toDp() - it.start - it.end }
//                val height = with(density) { constraints.maxHeight.toDp() - it.top - it.bottom }
//                    .takeIf { it.value >= 0 } ?: 0.dp


                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .animateContentSize()
                        .padding(horizontal = 8.dp)
                ) {
                    itemsIndexed(
                        items = seasonsList[selectedSeason].episodes,
                        itemContent = { index, contentItem ->
                            var expanded = true
                            Text(text = "Some Content of Season" + seasonsList[selectedSeason].d)
                            if (expanded == true) {
                                Text(
                                    text = contentItem.toString(),
                                    modifier = Modifier.padding(
                                        top = 4.dp,
                                        bottom = 4.dp
                                    ),
                                    style = MaterialTheme.typography.body1,
                                    color = Color.Gray
                                )
                            }
                            Divider(
                                color = Color.LightGray,
                                modifier = Modifier
                                    .padding(
                                        bottom = 4.dp,
                                        top = if (expanded == true) 0.dp else 4.dp
                                    )
                            )
                        }
                    )
                }
//            }
        }
    )

}

@Composable
fun MeasureWidthOf(setWidth: (Int) -> Unit, content: @Composable () -> Unit) {
    Layout(
        content = content
    ) { list: List<Measurable>, constraints: Constraints ->
        check(list.size == 1)
        val placeable = list.last().measure(constraints)
        layout(
            width = placeable.width.also(setWidth),
            height = placeable.height
        ) {
            placeable.placeRelative(x = 0, y = 0)
        }
    }
}

fun Modifier.ownTabIndicatorOffset(
    currentTabPosition: TabPosition,
    currentTabWidth: Dp = currentTabPosition.width
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "ownTabIndicatorOffset"
        value = currentTabPosition
    }
) {
    val indicatorOffset by animateAsState(
        targetValue = currentTabPosition.left,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )
    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset + ((currentTabPosition.width - currentTabWidth) / 2))
        .width(currentTabWidth)
}


// Cant find the import for functions below so i copied them

@Composable
fun animateAsState(
    targetValue: Dp,
    animationSpec: AnimationSpec<Dp> = remember {
        spring(visibilityThreshold = Dp.VisibilityThreshold)
    },
    finishedListener: ((Dp) -> Unit)? = null
): State<Dp> {
    return animateAsState(
        targetValue,
        Dp.VectorConverter,
        animationSpec,
        finishedListener = finishedListener
    )
}

@Composable
fun <T, V : AnimationVector> animateAsState(
    targetValue: T,
    typeConverter: TwoWayConverter<T, V>,
    animationSpec: AnimationSpec<T> = remember {
        spring(visibilityThreshold = visibilityThreshold)
    },
    visibilityThreshold: T? = null,
    finishedListener: ((T) -> Unit)? = null
): State<T> {
    val animationState: AnimationState<T, V> = remember(typeConverter) {
        AnimationState(initialValue = targetValue, typeConverter = typeConverter)
    }

    val listener by rememberUpdatedState(finishedListener)
    LaunchedEffect(targetValue, animationSpec) {
        animationState.animateTo(
            targetValue,
            animationSpec,
            // If the previous animation was interrupted (i.e. not finished), make it sequential.
            !animationState.isFinished
        )
        listener?.invoke(animationState.value)
    }
    return animationState
}