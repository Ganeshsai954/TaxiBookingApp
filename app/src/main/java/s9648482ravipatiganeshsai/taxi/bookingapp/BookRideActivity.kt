package s9648482ravipatiganeshsai.taxi.bookingapp

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import java.util.Calendar
import java.util.Locale

class BookRideActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RideBookingScreen()
        }
    }
}

fun getCurrentLocation(context: Context, fusedLocationClient: FusedLocationProviderClient, cameraPositionState: CameraPositionState, onLocationFetched: (LatLng) -> Unit) {
    if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED
    ) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val latLng = LatLng(it.latitude, it.longitude)
                onLocationFetched(latLng)  // Update State
                Log.e("Location", "Lat: ${it.latitude}, Lng: ${it.longitude}")

                // Move camera to current location
                cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 15f)
            } ?: Toast.makeText(
                context,
                "Please Turn On Location and Try Again",
                Toast.LENGTH_SHORT
            ).show()
        }
    } else {
        Toast.makeText(context, "Location Permission Not Allowed", Toast.LENGTH_SHORT).show()
        Log.e("Location", "Permission not granted")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RideBookingScreen() {
    val context = LocalContext.current as Activity
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(17.3850, 78.4867), 12f)
    }

    var pickupLocation by remember { mutableStateOf<LatLng?>(null) }
    var dropLocation by remember { mutableStateOf<LatLng?>(null) }
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }

    var pickupAddress by remember { mutableStateOf("Tap on Map to Select Pickup Location") }
    var dropAddress by remember { mutableStateOf("Tap on Map to Select Drop Location") }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // Initialize Maps SDK
    LaunchedEffect(Unit) {
        MapsInitializer.initialize(context.applicationContext)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getCurrentLocation(context, fusedLocationClient, cameraPositionState) { location ->
                currentLocation = location
            }
        } else {
            Log.e("Location", "Permission Denied")
        }
    }

    fun getAddressFromLatLng(context: Context, latLng: LatLng): String {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            addresses?.get(0)?.getAddressLine(0) ?: "Unknown Location"
        } catch (e: Exception) {
            "Unknown Location"
        }
    }

    fun checkPermissionAndRequest() {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            getCurrentLocation(context, fusedLocationClient, cameraPositionState) { location ->
                currentLocation = location
            }
        }
    }

    val markerIcon = rememberBitmapDescriptor(R.drawable.iv_location, 100, 100) // Adjust size

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
                text = "Book Ride",
                color = Color.Black,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // PickUp & Drop Fields
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            TextField(
                value = pickupAddress,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("PickUp Location") },
                trailingIcon = {
                    Row {
                        Icon(Icons.Default.LocationOn, contentDescription = "PickUp Location", tint = Color.Green)
                        if (pickupLocation != null) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear PickUp Location",
                                modifier = Modifier.clickable {
                                    pickupLocation = null
                                    pickupAddress = "Tap on Map to Select Pickup Location"
                                }.padding(8.dp)
                            )
                        }
                    }
                },
                colors = TextFieldDefaults.textFieldColors(containerColor = Color.White)
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = dropAddress,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Drop Location") },
                trailingIcon = {
                    Row {
                        Icon(Icons.Default.LocationOn, contentDescription = "Drop Location", tint = Color.Red)
                        if (dropLocation != null) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear Drop Location",
                                modifier = Modifier.clickable {
                                    dropLocation = null
                                    dropAddress = "Tap on Map to Select Drop Location"
                                }.padding(8.dp)
                            )
                        }
                    }
                },
                colors = TextFieldDefaults.textFieldColors(containerColor = Color.White)
            )
        }

        // Google Maps
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapClick = { latLng ->
                    if (pickupLocation == null) {
                        pickupLocation = latLng
                        pickupAddress = getAddressFromLatLng(context, latLng)
                        RideBookingDetails.pickUpLocation=pickupAddress
                    } else {
                        dropLocation = latLng
                        dropAddress = getAddressFromLatLng(context, latLng)
                        RideBookingDetails.dropLocation=dropAddress
                    }
                }
            ) {
                pickupLocation?.let {
                    Marker(
                        state = MarkerState(position = it),
                        title = "PickUp Location",
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                    )
                }
                dropLocation?.let {
                    Marker(
                        state = MarkerState(position = it),
                        title = "Drop Location",
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                    )
                }
                currentLocation?.let {
                    Marker(
                        state = MarkerState(position = it),
                        title = "Current Location",
                        icon = markerIcon
                    )
                }
            }

            Image(
                modifier = Modifier
                    .size(64.dp)
                    .padding(12.dp)
                    .clickable { checkPermissionAndRequest() }
                    .align(Alignment.TopEnd),
                painter = painterResource(id = R.drawable.get_location),
                contentDescription = "Get Location"
            )
        }

        // Book Ride Button
        Button(
            onClick = {
                context.startActivity(Intent(context, ConfirmBookingActivity::class.java))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            enabled = pickupLocation != null && dropLocation != null
        ) {
            Text(text = "Book Ride")
        }
    }
}


@Composable
fun rememberBitmapDescriptor(@DrawableRes resId: Int, width: Int, height: Int): BitmapDescriptor? {
    val context = LocalContext.current
    return remember {
        try {
            val bitmap = BitmapFactory.decodeResource(context.resources, resId)
            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
            BitmapDescriptorFactory.fromBitmap(scaledBitmap)
        } catch (e: Exception) {
            Log.e("MapError", "Error creating BitmapDescriptor: ${e.message}")
            null
        }
    }
}

object RideBookingDetails{
    var pickUpLocation = ""
    var dropLocation = ""
}

@Preview(showBackground = true)
@Composable
fun PreviewRideBookingScreen() {
//    RideBookingScreen()
}
