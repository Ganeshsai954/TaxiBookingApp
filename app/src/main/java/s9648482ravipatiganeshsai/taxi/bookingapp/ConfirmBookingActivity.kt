package s9648482ravipatiganeshsai.taxi.bookingapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ConfirmBookingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CabListScreen()
        }
    }
}

@Composable
fun CabListScreen() {
    val context = LocalContext.current as Activity

    var pickupLocation by remember { mutableStateOf(RideBookingDetails.pickUpLocation) }
    var dropLocation by remember { mutableStateOf(RideBookingDetails.dropLocation) }
    var cabs by remember { mutableStateOf(emptyList<Cab>()) }
    var selectedCab by remember { mutableStateOf<Cab?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    val allCabs = listOf(
        Cab("Toyota Etios", "AP16AB1234", 4, "5 min", "₹150"),
        Cab("Swift Dzire", "KA03XY9876", 4, "3 min", "₹120"),
        Cab("Honda City", "MH12YZ6543", 4, "7 min", "₹180"),
        Cab("Hyundai Verna", "TN09AB7654", 4, "6 min", "₹160"),
        Cab("Maruti Ciaz", "RJ14CD5678", 4, "8 min", "₹170"),
        Cab("Innova Crysta", "TS09CD5678", 7, "8 min", "₹250"),
        Cab("Mahindra Xylo", "MH12ZX6543", 7, "6 min", "₹230"),
        Cab("Toyota Fortuner", "DL04XY5678", 7, "10 min", "₹350"),
        Cab("Maruti Ertiga", "GJ01YZ9876", 7, "5 min", "₹220"),
        Cab("Tata Hexa", "WB08MN5432", 7, "9 min", "₹280"),
        Cab("Tempo Traveller", "UP32MN4321", 12, "12 min", "₹500"),
        Cab("Force Urbania", "KL07JK8765", 12, "15 min", "₹550"),
        Cab("Mahindra Supro", "TN22GH5432", 12, "14 min", "₹480")
    )

    // Shuffle cabs randomly on every recomposition
    LaunchedEffect(Unit) {
        cabs = allCabs.shuffled().take(5) // Show 5 random cabs every time
    }

    Column(modifier = Modifier.fillMaxSize()) {
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
                text = "Select Cab",
                color = Color.Black,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
        // Pickup & Drop Location Input
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Ride Details", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = pickupLocation,
                    readOnly = true,
                    onValueChange = { pickupLocation = it },
                    label = { Text("Pickup Location") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = dropLocation,
                    readOnly = true,
                    onValueChange = { dropLocation = it },
                    label = { Text("Drop Location") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // List of Cabs
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(cabs.size) { cab ->
                CabCard(cabs[cab]) {
                    if (pickupLocation.isNotEmpty() && dropLocation.isNotEmpty()) {
                        selectedCab = cabs[cab]
                        showDialog = true
                    } else {
                        Toast.makeText(
                            context,
                            "Please enter Pickup and Drop locations!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    if (showDialog && selectedCab != null) {
        RiderDetailsDialog(
            cab = selectedCab!!,
            pickupLocation = pickupLocation,
            dropLocation = dropLocation,
            onDismiss = { showDialog = false },
            onConfirm = { riderDetails ->
                saveBookingToFirebase(selectedCab!!,riderDetails,RideBookingDetails.pickUpLocation,RideBookingDetails.dropLocation,context)
//                saveBookingToFirebase(firestore, selectedCab!!, riderDetails, pickupLocation, dropLocation)
                Toast.makeText(context, "Ride Booked Successfully!", Toast.LENGTH_SHORT).show()
                showDialog = false
            }
        )
    }
}

@Composable
fun CabCard(cab: Cab, onBookNow: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onBookNow() },
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
                    Text(text = cab.carName, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "Car Number: ${cab.carNumber}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(text = "Seating Capacity: ${cab.seatingCapacity}", fontSize = 14.sp)
                    Text(
                        text = "Arriving in: ${cab.arrivingTime}",
                        fontSize = 14.sp,
                        color = Color.Blue
                    )
                    Text(
                        text = "Fare: ${cab.fare}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Green
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Image(
                    painter = painterResource(id = R.drawable.taxi_banner1), // Replace with actual car image
                    contentDescription = "Car",
                    modifier = Modifier.size(64.dp)
                )

            }

            Button(
                onClick = onBookNow,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Book Now")
            }
        }
    }
}

@Composable
fun RiderDetailsDialog(
    cab: Cab,
    pickupLocation: String,
    dropLocation: String,
    onDismiss: () -> Unit,
    onConfirm: (RiderDetails) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var passengers by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Enter Rider Details") },
        text = {
            Column {
                Text("Pickup: $pickupLocation", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text("Drop: $dropLocation", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") })
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
                )
                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    label = { Text("Age") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = passengers,
                    onValueChange = { passengers = it },
                    label = { Text("No. of Passengers") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotEmpty() && phoneNumber.isNotEmpty() && age.isNotEmpty() && passengers.isNotEmpty()) {
                        val riderDetails =
                            RiderDetails(name, phoneNumber, age.toInt(), passengers.toInt())
                        onConfirm(riderDetails)
                    }
                }
            ) {
                Text("Book Ride")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

data class Cab(
    val carName: String,
    val carNumber: String,
    val seatingCapacity: Int,
    val arrivingTime: String,
    val fare: String
)

data class RiderDetails(
    val name: String,
    val phoneNumber: String,
    val age: Int,
    val passengers: Int
)


fun saveBookingToFirebase(cab: Cab, riderDetails: RiderDetails, pickup: String, drop: String,context: Context) {
    val booking = hashMapOf(
        "carName" to cab.carName,
        "carNumber" to cab.carNumber,
        "seatingCapacity" to cab.seatingCapacity,
        "arrivingTime" to cab.arrivingTime,
        "fare" to cab.fare,
        "pickupLocation" to pickup,
        "dropLocation" to drop,
        "riderName" to riderDetails.name,
        "phoneNumber" to riderDetails.phoneNumber,
        "age" to riderDetails.age,
        "noOfPassengers" to riderDetails.passengers,
        "timestamp" to System.currentTimeMillis(),
        "bookingDate" to getCurrentDateTime()
    )

    val fireDB = FirebaseDatabase.getInstance()
    val databaseRef = fireDB.getReference("BookedRides")

    val userEmail = DriverAccountDetails.fetchUserMail(context)

    val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
    val orderId = dateFormat.format(Date())

    databaseRef.child(userEmail.replace(".", ",")).child(orderId).setValue(booking)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Ride Booked Successfully", Toast.LENGTH_SHORT)
                    .show()

                val intent = Intent(context, CustomerHomeActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                context.startActivity(intent)
//                (context as Activity).finish()
            } else {
                Toast.makeText(
                    context,
                    "Failed To Book Ride: ${task.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        .addOnFailureListener { exception ->
            Toast.makeText(
                context,
                "Donor Registration Failed: ${exception.message}",
                Toast.LENGTH_SHORT
            ).show()
        }

}

fun getCurrentDateTime(): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy 'at' hh:mm a", Locale.getDefault())
    return dateFormat.format(Date())
}

