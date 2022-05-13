package com.example.compsecodelab

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.tooling.preview.Preview
import com.example.compsecodelab.ui.theme.CompseCodeLabTheme

class DynamicUiActivity : ComponentActivity() {
    private val dynamicUiType: String by lazy {
        intent?.getStringExtra(TYPE) ?: DynamicUiType.TABS.name
    }

    @OptIn(
        ExperimentalComposeUiApi::class,
        ExperimentalMaterialApi::class
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompseCodeLabTheme {
//                val state by viewModel.state.collectAsState(initial = MusicState())
                // A surface container using the 'background' color from the theme
                DynamicUiWrapper(dynamicUiType) {
                    onBackPressed()
                }
            }
        }
    }

    companion object {
        const val TYPE = "type"
        const val DARK_THEME = "darkTheme"
        fun newIntent(context: Context, dynamicUiType: String) =
            Intent(context, DynamicUiActivity::class.java).apply {
                putExtra(TYPE, dynamicUiType)
//                putExtra(DARK_THEME, isDarkTheme)
            }
    }
}


@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun DynamicUiWrapper(uiType: String, onback: () -> Unit) {
    Scaffold(
        content = {
            // We setup a base activity and we will change content depending upon ui type so
            // we don't have to create Activity for every feature showcase
            when (uiType) {
                DynamicUiType.TABS.name -> {
//                    TabLayout()
                }
                DynamicUiType.BOTTOMSHEET.name -> {
//                    BottomSheetLayouts()
                }
                DynamicUiType.LAYOUTS.name -> {
//                    Layouts()
                }
                DynamicUiType.CONSTRAINTLAYOUT.name -> {
//                    ConstraintLayoutDemos()
                }
                DynamicUiType.CAROUSELL.name -> {
//                    CarouselLayout()
                }
                DynamicUiType.MODIFIERS.name -> {
//                    HowToModifiers()
                }
//                DynamicUiType.ANDROIDVIEWS.name -> {
//                    AndroidViews()
//                }
                DynamicUiType.PULLRERESH.name -> {
//                    PullRefreshList(onPullRefresh = {})
                }
                DynamicUiType.MOTIONLAYOUT.name -> {
//                    MotionLayoutDemo()
                }
                DynamicUiType.TAB_VIEWPAGER_LIST_DIALOG.name -> {
//                    var selectedTabIndex by remember { mutableStateOf(0) }
//                    val tabs = listOf("Fruits", "Vegetables", "Meats", "Miscellaneous")
//                    CustomScrollableTabRow(
//                        tabs = tabs,
//                        selectedTabIndex = selectedTabIndex,
//                    ) { tabIndex ->
//                        selectedTabIndex = tabIndex
//                    }
                }
            }
        }
    )
}

@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalMaterialApi::class
)
@Preview
@Composable
fun PreviewDynamicUI() {
    DynamicUiWrapper(uiType = DynamicUiType.TABS.name, onback = {})
}


