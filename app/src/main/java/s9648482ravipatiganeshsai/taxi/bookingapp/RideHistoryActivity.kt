package s9648482ravipatiganeshsai.taxi.bookingapp

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RideHistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RideHistoryScreen()
        }
    }
}

@Composable
fun RideHistoryScreen() {

    val context = LocalContext.current as Activity

    var rideBookings by remember { mutableStateOf(listOf<Booking>()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(context) {
        getRideBookingHistory(context) { orders ->
            rideBookings = orders
            isLoading = false
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.first_color))
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(36.dp)
                    .clickable { context.finish() },
                painter = painterResource(id = R.drawable.baseline_arrow_back_36),
                contentDescription = "Taxi Icon"
            )

            Text(
                modifier = Modifier.padding(12.dp),
                text = "Rides History",
                color = Color.Black,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
        ) {

            LazyColumn {
                items(rideBookings.size) { restaurant ->
                    RideHistoryItem(rideBookings[restaurant])
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

}


@Composable
fun RideHistoryItem(booking: Booking) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {

            },
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(modifier = Modifier.weight(1f)) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Booked By : ",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = booking.riderName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Booking Date : ",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = booking.bookingDate,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )

                    }
//                    Text(
//                        text = "Booking Date: ${booking.bookingDate}",
//                        fontSize = 14.sp,
//                        maxLines = 2
//                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_pickup), // Replace with actual car image
                            contentDescription = "Location",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "PickUp At : ${booking.pickupLocation}",
                            fontSize = 14.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_drop), // Replace with actual car image
                            contentDescription = "Location",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = "Drop At : ${booking.dropLocation}",
                            fontSize = 14.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                        .height(1.dp)
                        .background(Color.Black))

                    Text(text = booking.carName, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "Car Number: ${booking.carNumber}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(text = "Seating Capacity: ${booking.seatingCapacity}", fontSize = 14.sp)
                    Text(
                        text = "Fare: ${booking.fare}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Green
                    )
                }


            }

//            Spacer(modifier = Modifier.width(16.dp))
//
//            Image(
//                painter = painterResource(id = R.drawable.taxi_banner1), // Replace with actual car image
//                contentDescription = "Car",
//                modifier = Modifier.size(64.dp)
//            )


        }
    }
}

fun getRideBookingHistory(context: Context, onResult: (List<Booking>) -> Unit) {

    try {
        val userEmail = DriverAccountDetails.fetchUserMail(context).replace(".", ",")

        val databaseRef = FirebaseDatabase.getInstance().getReference("BookedRides/$userEmail")

        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bookingsList = mutableListOf<Booking>()
                for (donationSnapshot in snapshot.children) {
                    val donation = donationSnapshot.getValue(Booking::class.java)
                    donation?.let { bookingsList.add(it) }
                }

                Log.e("Test", "Bookings Fetched - ${bookingsList.size}")
                onResult(bookingsList)
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error: ${error.message}")
                onResult(emptyList())
            }
        })
    } catch (e: Exception) {
        Log.e("Test", "Error : ${e.message}")
    }
}


data class Booking(
    val carName: String = "",
    val carNumber: String = "",
    val seatingCapacity: Int = 0,
    val arrivingTime: String = "",
    val fare: String = "",
    val pickupLocation: String = "",
    val dropLocation: String = "",
    val riderName: String = "",
    val phoneNumber: String = "",
    val age: Int = 0,
    val noOfPassengers: Int = 0,
    val timestamp: Long = 0,
    val bookingDate: String = "No Date",
)
