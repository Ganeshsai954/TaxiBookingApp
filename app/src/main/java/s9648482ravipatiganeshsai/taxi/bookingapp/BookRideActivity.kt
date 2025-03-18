package s9648482ravipatiganeshsai.taxi.bookingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.*

class BookRideActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RideBookingScreen()
        }
    }
}



@Composable
fun RideBookingScreen() {
    val context = LocalContext.current

    var pickupLocation by remember { mutableStateOf("") }
    var dropLocation by remember { mutableStateOf("") }
    var rideType by remember { mutableStateOf("") }
    var passengerCount by remember { mutableStateOf("") }

    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var bookingConfirmed by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()

    // Date Picker Dialog
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Time Picker Dialog
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            selectedTime = String.format("%02d:%02d", hourOfDay, minute)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        false
    )

    if (bookingConfirmed) {
        // Booking Successful Screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "🚖 Booking Successful!", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Your ride from $pickupLocation to $dropLocation is confirmed.")
            Text("Date: $selectedDate  |  Time: $selectedTime")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { bookingConfirmed = false }) {
                Text("Book Another Ride")
            }
        }
    } else {
        // Ride Booking Form
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "Book a Ride", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = pickupLocation,
                onValueChange = { pickupLocation = it },
                label = { Text("Pickup Location") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = dropLocation,
                onValueChange = { dropLocation = it },
                label = { Text("Drop Location") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = rideType,
                onValueChange = { rideType = it },
                label = { Text("Ride Type (Economy, Premium, etc.)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = passengerCount,
                onValueChange = { passengerCount = it },
                label = { Text("Number of Passengers") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { datePickerDialog.show() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (selectedDate.isNotEmpty()) "Date: $selectedDate" else "Select Date")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { timePickerDialog.show() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (selectedTime.isNotEmpty()) "Time: $selectedTime" else "Select Time")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (pickupLocation.isNotEmpty() && dropLocation.isNotEmpty() && selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
                        bookingConfirmed = true
                        Toast.makeText(context, "Ride Booked Successfully!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Book Now")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRideBookingScreen() {
    RideBookingScreen()
}
