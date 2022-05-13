package com.example.compsecodelab

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compsecodelab.music.ListActivity
import com.example.compsecodelab.ui.theme.CompseCodeLabTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompseCodeLabTheme {
                // A surface container using the 'background' color from the theme
                MenusCompose(menuList = listOf("List", "Text"))
            }
        }
    }
}

@Composable
private fun MenusCompose(menuList: List<String>) {
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = menuList) { menu ->
            MenuCompose(menu = menu)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MenuCompose(menu: String) {
    val context = LocalContext.current

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()

    Surface(
        color = Color.White,
        modifier = Modifier.padding(vertical = 10.dp, horizontal = 18.dp)
    ) {
        Text(text = menu, modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                val intent = when (menu) {
                    "List" -> {
                        ListActivity.newIntent(context)
                    }
                    "Text" -> {
                        Intent(context, DialogActivity::class.java)
                    }
                    else -> {
                        Intent(context, ListActivity::class.java)
                    }
                }
                context.startActivity(intent)
            })
    }
}

@ExperimentalMaterialApi
@Composable
fun BottomSheetDialog() {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Text(text = "Hello from sheet")
            }
        }, sheetPeekHeight = 0.dp
    ) {
//        Button(onClick = {
//            coroutineScope.launch {
//                if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
//                    bottomSheetScaffoldState.bottomSheetState.expand()
//                } else {
//                    bottomSheetScaffoldState.bottomSheetState.collapse()
//                }
//            }
//        }) {
//            Text(text = "Expand/Collapse Bottom Sheet")
//        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CompseCodeLabTheme {
        MenusCompose(menuList = listOf("List", "Text"))
    }
}