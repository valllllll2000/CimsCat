package com.vaxapp.cims

import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vaxapp.cims.ui.theme.CimsCatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            CimsCatTheme {

                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    DisplayList(this@MainActivity)
                }
            }
        }
    }
}

@Composable
fun DisplayList(context: Context?) {
    val value = mutableListOf<DistancedMountainTop>()
    var tops by rememberSaveable { mutableStateOf(value) }

    //TODO: get location
    val location = Location(LocationManager.GPS_PROVIDER)
    location.latitude = "41.6219136".toDouble()
    location.longitude = "2.6760848".toDouble()
    val orderedTopsByLocation = Api().orderByDistance(context, location)
    Log.i("MainActivity", orderedTopsByLocation.toString())
    LazyColumn(contentPadding = PaddingValues(6.dp)) {
        for (top in tops) {
            item {
                TopItem(top, context)
                Row {
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopItem(top: DistancedMountainTop, context: Context?) {
    Card(
        modifier = Modifier
            .padding(all = 10.dp)
            .fillMaxWidth(), onClick = {onClick(top.url, context)}
    ) {
        Column(modifier = Modifier.padding(all = 10.dp)) {
            Text(top.name, fontSize = 25.sp, fontWeight = FontWeight.W700, modifier = Modifier.padding(10.dp))
            Text(top.url, modifier = Modifier.padding(10.dp))
            Text("${top.distanceFromUsInKm} km away", modifier = Modifier.padding(10.dp))
        }
    }
}

fun onClick(url: String, context: Context?) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context?.startActivity(intent)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CimsCatTheme {
        DisplayList(context = null)
    }
}