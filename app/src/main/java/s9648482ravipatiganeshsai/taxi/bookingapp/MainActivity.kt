package s9648482ravipatiganeshsai.taxi.bookingapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.window.SplashScreen
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaxiStartingValidation()
        }
    }
}


@Composable
fun TaxiStartingValidation() {
    var shouldShowSplash by remember { mutableStateOf(true) }

    val appContext = LocalContext.current as Activity

    LaunchedEffect(Unit) {
        delay(3000) // 3 seconds delay
        shouldShowSplash = false
    }
    if (shouldShowSplash) {
        TaxiStartingScreen()
    } else {
        val driverCheckStatus = DriverAccountDetails.fetchLoginState(appContext)

        if (driverCheckStatus) {
            appContext.startActivity(Intent(appContext, CustomerHomeActivity::class.java))
            appContext.finish()
        }
        else {
            appContext.startActivity(Intent(appContext, PassengerSignInActivity::class.java))
            appContext.finish()
        }
    }
}


@Composable
fun TaxiStartingScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = colorResource(id = R.color.first_color),
            ),
    ) {

        Image(
            painter = painterResource(id = R.drawable.taxi_img1), // Replace with your actual SVG drawable
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .offset(y = (-20).dp)
                .background(
                    color = colorResource(id = R.color.first_color),
                    shape = RoundedCornerShape(
                        topStart = 40.dp,
                        topEnd = 40.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                )
                .border(
                    width = 2.dp,
                    color = colorResource(id = R.color.first_color),
                    shape = RoundedCornerShape(
                        topStart = 40.dp,
                        topEnd = 40.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )

                )
        ) {

            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "Taxi Booking App",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            )

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "A Project By",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            )

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "Ravipati Ganesh Sai",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            )

            Spacer(modifier = Modifier.weight(1f))


        }

    }
}

@Preview(showBackground = true)
@Composable
fun TaxiStartingScreenPreview() {
    TaxiStartingScreen()
}