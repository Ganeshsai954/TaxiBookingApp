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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.database.FirebaseDatabase

class PassengerSignInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PassengerSignInScreen()
        }
    }
}

@Composable
fun PassengerSignInScreen() {

    var driverEmail by remember { mutableStateOf("") }
    var driverPassword by remember { mutableStateOf("") }

    val context = LocalContext.current as Activity


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

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "Taxi Booking App",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .background(
                        color = colorResource(id = R.color.white),
                    ),
                value = driverEmail,
                onValueChange = { driverEmail = it },
                placeholder = { Text(text = "Email") }
            )

            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .background(
                        color = colorResource(id = R.color.white),
                    ),
                value = driverPassword,
                onValueChange = { driverPassword = it },
                placeholder = { Text(text = "Password") }
            )

            Spacer(modifier = Modifier.height(45.dp))

            Text(
                modifier = Modifier
                    .clickable {
                        when{
                            driverEmail.isEmpty() -> {
                                Toast.makeText(context, "MailID missing", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            driverPassword.isEmpty() -> {
                                Toast.makeText(context, "Password missing", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            else -> {

                                val passengerData = PassengerData(
                                    "",
                                    driverEmail,
                                    "",
                                    driverPassword
                                )

                                loginPassenger(passengerData, context)

                            }
                        }
                    }
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .background(
                        color = colorResource(id = R.color.second_color),
                        shape = RoundedCornerShape(
                            10.dp
                        )
                    )
                    .border(
                        width = 2.dp,
                        color = colorResource(id = R.color.second_color),
                        shape = RoundedCornerShape(
                            10.dp
                        )
                    )
                    .padding(vertical = 12.dp, horizontal = 12.dp),
                text = "Login",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = colorResource(id = R.color.first_color),
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .clickable {
                        context.startActivity(Intent(context, PassengerSignUpActivity::class.java))
                        context.finish()
                    },
                text = "Take Me To Registration",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = colorResource(id = R.color.second_color),
                )
            )


        }

    }
}


fun loginPassenger(passengerData: PassengerData, context: Context) {


    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("TaxiBooking").child(passengerData.emailId.replace(".", ","))

    databaseReference.get().addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val taxiData = task.result?.getValue(PassengerData::class.java)
            if (taxiData != null) {
                if (taxiData.password == passengerData.password) {
                    Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                    DriverAccountDetails.persistLoginState(context, true)
                    DriverAccountDetails.persistUserMail(context, taxiData.emailId)
                    DriverAccountDetails.persistUserName(context, taxiData.userName)

                    context.startActivity(Intent(context, CustomerHomeActivity::class.java))
                    (context as Activity).finish()
                } else {
                    Toast.makeText(context, "Seems Incorrect Credentials", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Your account not found", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(
                context,
                "Something went wrong",
                Toast.LENGTH_SHORT
            ).show()
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PassengerSignInScreenPreview() {
    PassengerSignInScreen()
}