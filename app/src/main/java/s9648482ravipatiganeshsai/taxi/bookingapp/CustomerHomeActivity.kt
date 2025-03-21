package s9648482ravipatiganeshsai.taxi.bookingapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CustomerHomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CustomerHomeActivityScreen()
        }
    }
}


@Composable
fun CustomerHomeActivityScreen() {

    val context = LocalContext.current as Activity

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = colorResource(id = R.color.first_color)
                )
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(36.dp)
                    .clickable {
                        context.finish()
                    },
                painter = painterResource(id = R.drawable.ic_taxi),
                contentDescription = "Taxi Icon"
            )

            Text(
                modifier = Modifier
                    .padding(12.dp),
                text = "Taxi Booking App",
                color = Color.Black,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

//            TaxiOptions()

            EventCard(
                title = "Book Ride",
                caption = "Select pickUp and destination and book a ride",
                imageRes = R.drawable.taxi_banner1,
                "Book Now"
            ) {
                context.startActivity(Intent(context, BookRideActivity::class.java))
            }

            EventCard(
                title = "Rides History",
                caption = "See the rides history along with other details",
                imageRes = R.drawable.ic_booked_rides,
                "View History"
            ) {
                context.startActivity(Intent(context, RideHistoryActivity::class.java))
            }

            EventCard(
                title = "Driver Profile",
                caption = "See my account details and manage account",
                imageRes = R.drawable.taxi_profile,
                "View Profile"
            ) {

            }


        }


    }
}

@Composable
fun EventCard(
    title: String,
    caption: String,
    imageRes: Int, // Pass a drawable resource ID
    buttonText: String,
    onBookNowClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp)
        ) {
            // Title (Top-Left)
            Column(modifier = Modifier.align(Alignment.TopStart)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = caption,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            // Image (Bottom-Right)
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Event Image",
                modifier = Modifier
                    .width(150.dp)
                    .height(100.dp)
                    .align(Alignment.BottomEnd)
            )

            // Book Now Button (Bottom-Left)
            Button(
                onClick = onBookNowClick,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(top = 8.dp)
            ) {
                Text(text = buttonText)
            }
        }
    }
}


@Composable
fun TaxiOptions() {
    val context = LocalContext.current as Activity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TaxiButtons(text = "Book a Ride") {
            context.startActivity(Intent(context, BookRideActivity::class.java))
            context.finish()
        }
        TaxiButtons(text = "See Booking Details") { /* Handle click */ }
        TaxiButtons(text = "My Profile") { /* Handle click */ }
    }
}

@Composable
fun TaxiButtons(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(60.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text = text, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}


@Preview(showBackground = true)
@Composable
fun CustomerHomeActivityScreenPreview() {
    CustomerHomeActivityScreen()
}