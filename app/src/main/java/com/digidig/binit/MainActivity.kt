package com.digidig.binit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.digidig.binit.ui.detection.TrashImageDetection
import com.digidig.binit.ui.profile.ProfileActivity
import com.digidig.binit.ui.profile.Transaction
import com.digidig.binit.ui.theme.BinITTheme
import com.digidig.binit.ui.transaction.TransactionActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BinITTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting(this)
                }
            }
        }
    }
}

@Composable
fun Greeting(context : Context?) {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

    }
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.main_bg),
                contentDescription = "Home Background",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
            .align(Alignment.BottomCenter)){
            Card (
                modifier = Modifier.fillMaxSize(),
                elevation = 8.dp,
                shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
                    ) {
                Row (modifier = Modifier.fillMaxSize(),
                Arrangement.Center) {
                    itemCards(icon = R.drawable.ic_recycle, text = "Recycle") {
                        var intent = Intent(context, TransactionActivity::class.java)
                        launcher.launch(intent)
                    }
                    itemCards(icon = R.drawable.ic_detection, text = "Trash") {
                        var intent = Intent(context, TrashImageDetection::class.java)
                        launcher.launch(intent)
                    }
                    itemCards(icon = R.drawable.ic_profile, text = "Profile") {
                        var intent = Intent(context, ProfileActivity::class.java)
                        launcher.launch(intent)
                    }
                }
            }
        }
    }
}

@Composable
fun itemCards(icon : Int, text : String,
              clickable : () -> Unit){
    Column (
        modifier = Modifier
            .padding(top = 50.dp, start = 20.dp)
            .width(IntrinsicSize.Max)
            .height(IntrinsicSize.Max)
            .clickable {
                clickable.invoke()
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Card {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(painter = painterResource(id = icon)
                    , contentDescription = "$text Icon")
                Text(text="$text")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BinITTheme {
        Greeting(context = LocalContext.current)
    }
}