package com.digidig.binit.ui.profile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.digidig.binit.R
import com.digidig.binit.ui.profile.ui.theme.BinITTheme
import java.text.DecimalFormat

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        setContent {
            BinITTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ProfileScreen("1.000.000", transactions = transactions)
                }
            }
        }
    }
}

data class Transaction(val id : String,
                       val date : String,
                       val amount: Int,
                       val tipe_sampah: String,
                       val keterangan: String
                       )
fun formatter(n: Int) =
    DecimalFormat("#,###")
        .format(n)
        .replace(",", ".")
val primary_green = android.graphics.Color.parseColor("#2DCC70")

val transactions = listOf(
    Transaction("1", "June 1, 2023",200_000,"Plastik","berhasil"),
    Transaction("2", "June 1, 2023",200_000,"Plastik","berhasil"),
    Transaction("3", "June 1, 2023",200_000,"Plastik","berhasil"),
    Transaction("4", "June 1, 2023",200_000,"Plastik","berhasil"),
    Transaction("5", "June 1, 2023",200_000,"Plastik","berhasil"),
    Transaction("6", "June 1, 2023",200_000,"Plastik","berhasil"),
    Transaction("7", "June 1, 2023",200_000,"Plastik","berhasil"),
    Transaction("8", "June 1, 2023",200_000,"Plastik","berhasil"),
    Transaction("9", "June 1, 2023",200_000,"Plastik","berhasil"),
    Transaction("10", "June 1, 2023",200_000,"Plastik","berhasil"),
    Transaction("11", "June 1, 2023",200_000,"Plastik","berhasil"),
    Transaction("12", "June 1, 2023",200_000,"Plastik","berhasil"),
    Transaction("13", "June 1, 2023",200_000,"Plastik","berhasil"),
    Transaction("14", "June 1, 2023",200_000,"Plastik","berhasil"),
    Transaction("15", "June 1, 2023",200_000,"Plastik","berhasil"),
    Transaction("16", "June 1, 2023",200_000,"Plastik","berhasil"),
    Transaction("17", "June 1, 2023",200_000,"Plastik","berhasil"),
    Transaction("18", "June 1, 2023",200_000,"Plastik","berhasil"),
    Transaction("19", "June 1, 2023",200_000,"Plastik","berhasil"),
    Transaction("20", "June 1, 2023",200_000,"Plastik","berhasil"),
    Transaction("22", "June 1, 2023",200_000,"Plastik","berhasil"),
    Transaction("23", "June 1, 2023",200_000,"Plastik","berhasil"),
    // Add more transactions as needed
)

@Composable
fun ProfileScreen(amount: String, transactions: List<Transaction>) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_profile),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp),
            contentScale = ContentScale.Crop
        )
        Text(text = "John Doe", style = MaterialTheme.typography.h4)
        Text(text = "Software Engineer", style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.size(200.dp,20.dp))
        Row(modifier = Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically){
            Image(painter = painterResource(id = R.drawable.ic_coin),
                contentDescription = "BinCoins Logo")
            Text(text = amount, modifier = Modifier.padding(start = 8.dp),
                style = MaterialTheme.typography.body1)
        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .padding(horizontal = 20.dp, vertical = 5.dp)
            .background(color = Color(primary_green)))
        LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
            itemsIndexed(transactions) { _, transaction ->
                TransactionItem(transaction)
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 5.dp)
        .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(size = 5.dp))) {
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max)) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Transaction ${transaction.id}",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.h6 // Heading style
                )

                Text(
                    text = transaction.keterangan,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.subtitle1 // Subtitle style
                )
            }

            Column(
                modifier = Modifier
                    .background(color = Color(primary_green))
                    .padding(10.dp)
                    .align(Alignment.CenterVertically),
                ) {
                Text(
                    text = "Rp${formatter(transaction.amount)}",
                    modifier = Modifier
                        .fillMaxHeight()
                        .wrapContentSize(Alignment.Center)
                        .padding(horizontal = 3.dp, vertical = 1.dp)
                )
            }
        }
    }
    // Composable for displaying individual transaction item
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    BinITTheme {
        ProfileScreen("1.000.000", transactions = transactions)
    }
}