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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.unit.sp
import com.google.firebase.database.FirebaseDatabase

class PassengerSignUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PassengerSignUpScreen()
        }
    }
}

@Composable
fun PassengerSignUpScreen() {

    var accountName by remember { mutableStateOf("") }
    var accountEmail by remember { mutableStateOf("") }
    var accountPassword by remember { mutableStateOf("") }

    var selectedGender by remember { mutableStateOf("Male") }

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
                .height(300.dp)
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
                value = accountName,
                onValueChange = { accountName = it },
                placeholder = { Text(text = "Name") }
            )

            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .background(
                        color = colorResource(id = R.color.white),
                    ),
                value = accountEmail,
                onValueChange = { accountEmail = it },
                placeholder = { Text(text = "Email") }
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "Select Gender", fontWeight = FontWeight.Bold, fontSize = 18.sp
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                RadioButton(
                    selected = selectedGender == "Male",
                    onClick = { selectedGender = "Male" }
                )
                Text(
                    text = "Male",
                    modifier = Modifier
                        .clickable { selectedGender = "Male" }
                        .padding(start = 4.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                RadioButton(
                    selected = selectedGender == "Female",
                    onClick = { selectedGender = "Female" }
                )
                Text(
                    text = "Female",
                    modifier = Modifier
                        .clickable { selectedGender = "Female" }
                        .padding(start = 4.dp)
                )
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .background(
                        color = colorResource(id = R.color.white),
                    ),
                value = accountPassword,
                onValueChange = { accountPassword = it },
                placeholder = { Text(text = "Password") }
            )

            Spacer(modifier = Modifier.height(45.dp))

            Text(
                modifier = Modifier
                    .clickable {
                        when {

                            accountName.isBlank() -> {
                                Toast.makeText(context, "UserName missing", Toast.LENGTH_SHORT)
                                    .show()

                            }

                            accountEmail.isBlank() -> {
                                Toast.makeText(context, "EmailId missing", Toast.LENGTH_SHORT)
                                    .show()
                            }

                            selectedGender.isBlank() -> {
                                Toast.makeText(context, "Gender missing", Toast.LENGTH_SHORT)
                                    .show()
                            }

                            accountPassword.isBlank() -> {
                                Toast.makeText(context, "Password missing", Toast.LENGTH_SHORT)
                                    .show()
                            }

                            else -> {

                                val passengerData = PassengerData(
                                    accountName,
                                    accountEmail,
                                    selectedGender,
                                    accountPassword

                                )

                                registerPassenger(passengerData, context)

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
                text = "Register",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = colorResource(id = R.color.first_color),
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .clickable {
                        context.startActivity(Intent(context, PassengerSignInActivity::class.java))
                        context.finish()
                    },
                text = "Take Me To Login",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = colorResource(id = R.color.second_color),
                )
            )


        }

    }
}

fun registerPassenger(passengerData: PassengerData, context: Context) {

    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("TaxiBooking")

    databaseReference.child(passengerData.emailId.replace(".", ","))
        .setValue(passengerData)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "You Registered Successfully", Toast.LENGTH_SHORT)
                    .show()
//                context.startActivity(Intent(context, CheckInActivity::class.java))
//                (context as Activity).finish()

            } else {
                Toast.makeText(
                    context,
                    "Registration Failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        .addOnFailureListener { _ ->
            Toast.makeText(
                context,
                "Something went wrong",
                Toast.LENGTH_SHORT
            ).show()
        }
}


@Preview(showBackground = true)
@Composable
fun PassengerSignUpScreenPreview() {
    PassengerSignUpScreen()
}

data class PassengerData(
    var userName : String = "",
    var emailId : String = "",
    var gender : String = "",
    var password: String = ""
)
