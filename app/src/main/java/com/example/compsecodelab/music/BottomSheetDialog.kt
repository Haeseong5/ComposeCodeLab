import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.compsecodelab.music.ListActivity.Companion.tabData
import com.example.compsecodelab.music.MusicListContent
import com.example.compsecodelab.music.MusicState
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetLayouts(viewModel: MusicState) {
    BottomSheetDrawer(viewModel)
}

@ExperimentalPagerApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetDrawer(
    state: MusicState
) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    BottomSheetScaffold(
        drawerBackgroundColor = Color.White,
        sheetBackgroundColor = Color.White,
        backgroundColor = Color.White,
        content = {
            Box {
                ScafoldContent(
                    state = state,
                    coroutineScope, bottomSheetScaffoldState, sheetState
                )
            }
        },
        sheetContent = {
//            PlayerBottomSheet(state)
        },
        drawerContent = {
//            DrawerContent(state)
        },
        scaffoldState = bottomSheetScaffoldState,
//        sheetPeekHeight = if (sheetState.isAnimationRunning || sheetState.isVisible) 0.dp else 65
//            .dp,
    )
}

@ExperimentalPagerApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ScafoldContent(
    state: MusicState,
    coroutineScope: CoroutineScope,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    sheetState: ModalBottomSheetState
) {
    var showFullBottomSheet = remember { mutableStateOf(false) }
    ModalBottomSheetLayout(
        modifier = Modifier.fillMaxSize(),
        sheetState = sheetState,
        sheetBackgroundColor = Color.White,
        sheetContent = {
            if (showFullBottomSheet.value) {
                FullBottomSheetContent(
                    state = state
                ) {
                    coroutineScope.launch {
                        sheetState.hide()
                    }
                }
            } else {
                BottomSheetContent(state)
            }
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(55.dp),
                onClick = {
                    coroutineScope.launch {
                        bottomSheetScaffoldState.drawerState.open()
                    }
                }) {
                Text(text = "Navigation Drawer")
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(55.dp),
                onClick = {
                    coroutineScope.launch {
                        bottomSheetScaffoldState.bottomSheetState.expand()
                    }
                }) {
                Text(text = "Bottom Sheet")
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(55.dp),
                onClick = {
                    showFullBottomSheet.value = false
                    coroutineScope.launch {
                        sheetState.show()
                    }
                }) {
                Text(text = "Modal Bottom sheet")
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(55.dp),
                onClick = {
                    showFullBottomSheet.value = true
                    coroutineScope.launch {
                        // sheetState.snapTo(ModalBottomSheetValue.Expanded)
                        sheetState.show()
                    }
                }) {
                Text(text = "Modal Bottom sheet Full")
            }
        }
    }
}


@ExperimentalPagerApi
@Composable
fun BottomSheetContent(state: MusicState) {
    DrawerContent(state)
}

@ExperimentalPagerApi
@Composable
fun FullBottomSheetContent(state: MusicState, onClose: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxHeight()
            .background(Color.Transparent)
    ) {
        MusicListContent(state, tabData)
    }
}

@ExperimentalPagerApi
@Composable
fun DrawerContent(state: MusicState) {
    MusicListContent(state, tabData)
}

@ExperimentalPagerApi
@Composable
fun PlayerBottomSheet(state: MusicState) {
    MusicListContent(state, tabData)
}