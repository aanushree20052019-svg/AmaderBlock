package in.amaderblock.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Green = androidx.compose.ui.graphics.Color(0xFF176B4D)
private val Pale = androidx.compose.ui.graphics.Color(0xFFEAF5EF)
private val Ink = androidx.compose.ui.graphics.Color(0xFF17211C)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AmaderBlockApp() }
    }
}

data class ServiceItem(val title: String, val subtitle: String, val icon: ImageVector)
data class Notice(val title: String, val date: String, val body: String)

private val services = listOf(
    ServiceItem("সরকারি প্রকল্প", "যোগ্যতা ও যাচাইকৃত তথ্য", Icons.Default.AccountBalance),
    ServiceItem("আবেদনপত্র", "ফর্ম ও নমুনা ডাউনলোড", Icons.Default.Description),
    ServiceItem("অভিযোগ জানান", "স্থানীয় সমস্যার বিবরণ জমা", Icons.Default.ReportProblem),
    ServiceItem("কৃষি পরিষেবা", "কৃষি ও কৃষক-সহায়তা তথ্য", Icons.Default.Agriculture),
    ServiceItem("শিক্ষা ও বৃত্তি", "শিক্ষা-সংক্রান্ত বিজ্ঞপ্তি", Icons.Default.School),
    ServiceItem("স্বাস্থ্য পরিষেবা", "স্বাস্থ্যকেন্দ্রের তথ্য", Icons.Default.LocalHospital),
    ServiceItem("পঞ্চায়েত তথ্য", "স্থানীয় অফিসের যোগাযোগ", Icons.Default.LocationCity),
    ServiceItem("জরুরি যোগাযোগ", "জরুরি নম্বর ও সহায়তা", Icons.Default.Call)
)

private val notices = listOf(
    Notice("স্বাগতম — আমাদের ব্লক", "প্রাথমিক নমুনা বিজ্ঞপ্তি", "এই অ্যাপটি ছাতনা ব্লকের মানুষের জন্য তথ্য ও পরিষেবা সহজে খুঁজে পেতে তৈরি করা হচ্ছে।"),
    Notice("তথ্য যাচাই করে নিন", "গুরুত্বপূর্ণ", "সরকারি প্রকল্প, শেষ তারিখ ও যোগ্যতার তথ্য সংশ্লিষ্ট সরকারি দপ্তরের সঙ্গে যাচাই করে নিন।")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmaderBlockApp() {
    var selectedTab by remember { mutableStateOf("হোম") }
    var selectedService by remember { mutableStateOf<ServiceItem?>(null) }
    var showComplaint by remember { mutableStateOf(false) }
    var showAdmin by remember { mutableStateOf(false) }

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Green,
            secondary = androidx.compose.ui.graphics.Color(0xFFF0B54A),
            background = androidx.compose.ui.graphics.Color(0xFFF7FAF8),
            surface = androidx.compose.ui.graphics.Color.White
        )
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("আমাদের ব্লক", fontWeight = FontWeight.Bold)
                            Text("ছাতনা • বাঁকুড়া, পশ্চিমবঙ্গ", fontSize = 12.sp)
                        }
                    },
                    actions = {
                        IconButton(onClick = { showAdmin = true }) {
                            Icon(Icons.Default.AdminPanelSettings, contentDescription = "অ্যাডমিন")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Green,
                        titleContentColor = androidx.compose.ui.graphics.Color.White,
                        actionIconContentColor = androidx.compose.ui.graphics.Color.White
                    )
                )
            },
            bottomBar = {
                NavigationBar {
                    listOf(
                        "হোম" to Icons.Default.Home,
                        "বিজ্ঞপ্তি" to Icons.Default.Notifications,
                        "অভিযোগ" to Icons.Default.Report,
                        "তথ্য" to Icons.Default.Info
                    ).forEach { (label, icon) ->
                        NavigationBarItem(
                            selected = selectedTab == label,
                            onClick = {
                                selectedTab = label
                                if (label == "অভিযোগ") showComplaint = true
                            },
                            icon = { Icon(icon, contentDescription = label) },
                            label = { Text(label) }
                        )
                    }
                }
            }
        ) { padding ->
            when {
                showComplaint -> ComplaintScreen(
                    modifier = Modifier.padding(padding),
                    onBack = { showComplaint = false; selectedTab = "হোম" }
                )
                showAdmin -> AdminScreen(
                    modifier = Modifier.padding(padding),
                    onBack = { showAdmin = false }
                )
                selectedService != null -> ServiceDetail(
                    item = selectedService!!,
                    modifier = Modifier.padding(padding),
                    onBack = { selectedService = null }
                )
                selectedTab == "বিজ্ঞপ্তি" -> NoticeScreen(Modifier.padding(padding))
                selectedTab == "তথ্য" -> AboutScreen(Modifier.padding(padding))
                else -> HomeScreen(
                    modifier = Modifier.padding(padding),
                    onService = { item ->
                        if (item.title == "অভিযোগ জানান") showComplaint = true
                        else selectedService = item
                    },
                    onNotice = { selectedTab = "বিজ্ঞপ্তি" }
                )
            }
        }
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onService: (ServiceItem) -> Unit,
    onNotice: () -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().background(Pale),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column(
                Modifier.fillMaxWidth()
                    .background(Green, RoundedCornerShape(22.dp))
                    .padding(20.dp)
            ) {
                Text("আপনার এলাকার তথ্য, এক জায়গায়", color = androidx.compose.ui.graphics.Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text("ছাতনা ব্লকের পরিষেবা ও বিজ্ঞপ্তি সহজে খুঁজুন।", color = androidx.compose.ui.graphics.Color.White)
                Spacer(Modifier.height(14.dp))
                Button(onClick = onNotice, colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color.White, contentColor = Green)) {
                    Text("সাম্প্রতিক বিজ্ঞপ্তি দেখুন")
                }
            }
        }
        item { Text("পরিষেবা সমূহ", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Ink) }
        items(services) { service ->
            ServiceCard(service = service, onClick = { onService(service) })
        }
        item {
            Text(
                "নোট: এটি একটি স্বাধীন তথ্য-পরিষেবা অ্যাপের প্রোটোটাইপ; সরকারি অ্যাপ নয়।",
                fontSize = 12.sp,
                color = androidx.compose.ui.graphics.Color(0xFF59665E),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
fun ServiceCard(service: ServiceItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier.size(48.dp).background(Pale, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) { Icon(service.icon, contentDescription = null, tint = Green) }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(service.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Ink)
                Text(service.subtitle, fontSize = 13.sp, color = androidx.compose.ui.graphics.Color(0xFF657169))
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Green)
        }
    }
}

@Composable
fun ServiceDetail(item: ServiceItem, modifier: Modifier = Modifier, onBack: () -> Unit) {
    Column(
        modifier.fillMaxSize().background(Pale).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        TextButton(onClick = onBack) { Text("← ফিরে যান") }
        Icon(item.icon, contentDescription = null, tint = Green, modifier = Modifier.size(42.dp))
        Text(item.title, fontSize = 25.sp, fontWeight = FontWeight.Bold, color = Ink)
        Text(item.subtitle, color = Green, fontSize = 15.sp)
        Card(colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("এই বিভাগে তথ্য যোগ করা হবে", fontWeight = FontWeight.Bold)
                Text("পরবর্তী ধাপে যাচাইকৃত সরকারি লিংক, দপ্তরের যোগাযোগ, প্রয়োজনীয় নথি ও আবেদন প্রক্রিয়া যুক্ত করা যাবে।")
                Text("এই মুহূর্তে এখানে নমুনা তথ্য দেখানো হচ্ছে; কোনও আবেদন সরকারি দপ্তরে পাঠানো হচ্ছে না।", fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun NoticeScreen(modifier: Modifier = Modifier) {
    LazyColumn(modifier.fillMaxSize().background(Pale), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { Text("খবর ও বিজ্ঞপ্তি", fontSize = 24.sp, fontWeight = FontWeight.Bold) }
        items(notices) { notice ->
            Card(colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(notice.title, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    Text(notice.date, color = Green, fontSize = 12.sp)
                    Text(notice.body)
                }
            }
        }
    }
}

@Composable
fun ComplaintScreen(modifier: Modifier = Modifier, onBack: () -> Unit) {
    var category by remember { mutableStateOf("রাস্তা/নিকাশি") }
    var location by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf(false) }

    Column(modifier.fillMaxSize().background(Pale).padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        TextButton(onClick = onBack) { Text("← ফিরে যান") }
        Text("অভিযোগের খসড়া", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("এটি এখন নমুনা ফর্ম। জমা দিলে কোনও সরকারি দপ্তরে পাঠানো হবে না।")
        if (submitted) {
            Card(colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("খসড়া তৈরি হয়েছে", fontWeight = FontWeight.Bold, color = Green)
                    Text("বাস্তব জমা ও ট্র্যাকিং চালু করতে নিরাপদ সার্ভার ও অনুমোদিত প্রক্রিয়া যুক্ত করতে হবে।")
                    Button(onClick = { submitted = false }) { Text("আবার লিখুন") }
                }
            }
        } else {
            OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("অভিযোগের বিভাগ") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("গ্রাম / স্থান") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = details, onValueChange = { details = it }, label = { Text("সমস্যার বিবরণ") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
            OutlinedTextField(value = contact, onValueChange = { contact = it }, label = { Text("যোগাযোগ নম্বর (ঐচ্ছিক)") }, modifier = Modifier.fillMaxWidth())
            Button(onClick = { submitted = true }, modifier = Modifier.fillMaxWidth(), enabled = location.isNotBlank() && details.isNotBlank()) {
                Text("খসড়া তৈরি করুন")
            }
        }
    }
}

@Composable
fun AdminScreen(modifier: Modifier = Modifier, onBack: () -> Unit) {
    Column(modifier.fillMaxSize().background(Pale).padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        TextButton(onClick = onBack) { Text("← ফিরে যান") }
        Text("অ্যাডমিন প্যানেল", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("এই স্ক্রিনটি শুধু UI নমুনা। এখনও লগইন বা ডেটাবেস যুক্ত করা হয়নি।", color = androidx.compose.ui.graphics.Color(0xFF8A4B00))
        OutlinedTextField(value = "", onValueChange = {}, label = { Text("অ্যাডমিন ইমেল") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = "", onValueChange = {}, label = { Text("পাসওয়ার্ড") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = {}, modifier = Modifier.fillMaxWidth(), enabled = false) { Text("লগইন (পরবর্তী ধাপে যুক্ত হবে)") }
        Text("পরবর্তী ধাপে আলাদা ভূমিকা: প্রধান অ্যাডমিন, সম্পাদক এবং কেবল-পাঠক।")
    }
}

@Composable
fun AboutScreen(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().background(Pale).padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("অ্যাপ সম্পর্কে", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("আমাদের ব্লক — ছাতনা ব্লকের মানুষের জন্য একটি স্বাধীন কমিউনিটি তথ্য-পরিষেবা অ্যাপের প্রোটোটাইপ।")
        Text("সরকারি অনুমোদন বা সরকারি দপ্তরের সঙ্গে সংযোগ আছে বলে এই অ্যাপ দাবি করে না।")
        Text("তথ্য ব্যবহার করার আগে সংশ্লিষ্ট সরকারি উৎস থেকে যাচাই করুন। জরুরি অবস্থায় সরাসরি অনুমোদিত জরুরি পরিষেবার সঙ্গে যোগাযোগ করুন।")
    }
}
