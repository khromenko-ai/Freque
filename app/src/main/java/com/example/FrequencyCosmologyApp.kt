package com.example

import android.content.Intent
import android.net.Uri
import android.speech.tts.TextToSpeech
import androidx.compose.animation.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@Composable
fun FrequencyCosmologyApp(tts: TextToSpeech?) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var isPlayingGlobal by remember { mutableStateOf(false) }
    var runningTtsId by remember { mutableStateOf("") }
    var showGlobalMapDialog by remember { mutableStateOf(false) }

    // Helper function to handle TTS playback cleanly
    val playSpeech: (String, String) -> Unit = { id, text ->
        if (isPlayingGlobal && runningTtsId == id) {
            tts?.stop()
            isPlayingGlobal = false
            runningTtsId = ""
        } else {
            tts?.stop()
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, id)
            isPlayingGlobal = true
            runningTtsId = id
        }
    }

    // Stop speech when tab changes
    LaunchedEffect(selectedTab) {
        tts?.stop()
        isPlayingGlobal = false
        runningTtsId = ""
    }

    if (showGlobalMapDialog) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = { showGlobalMapDialog = false },
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .fillMaxHeight(0.85f)
                    .border(2.dp, Color(0xFF00BCD4), RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A))
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    MapFlow(
                        playSpeech = playSpeech,
                        isPlayingGlobal = isPlayingGlobal,
                        runningTtsId = runningTtsId
                    )
                    
                    IconButton(
                        onClick = { showGlobalMapDialog = false },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Закрыть", tint = Color.White)
                    }
                }
            }
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isWideScreen = maxWidth >= 800.dp

        if (isWideScreen) {
            Row(modifier = Modifier.fillMaxSize().background(Color(0xFF0F172A))) {
                NavigationRail(
                    containerColor = Color(0xFF1E293B),
                    contentColor = Color.White,
                    header = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)) {
                            FloatingActionButton(
                                onClick = { showGlobalMapDialog = true },
                                containerColor = Color(0xFF00BCD4),
                                contentColor = Color.White,
                                modifier = Modifier.padding(bottom = 16.dp)
                            ) {
                                Icon(Icons.Default.Map, contentDescription = "Открыть карту")
                            }
                            val context = LocalContext.current
                            FloatingActionButton(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://khromenko-ai.github.io/Freque/"))
                                    context.startActivity(intent)
                                },
                                containerColor = Color(0xFFE040FB),
                                contentColor = Color.White
                            ) {
                                Icon(Icons.Default.Language, contentDescription = "Web App")
                            }
                        }
                    }
                ) {
                    NavigationRailItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = "Базовый обзор") },
                        label = { Text("Базовый", style = MaterialTheme.typography.labelSmall) },
                        colors = NavigationRailItemDefaults.colors(
                            selectedIconColor = Color(0xFF4CAF50),
                            selectedTextColor = Color(0xFF4CAF50),
                            unselectedIconColor = Color.LightGray,
                            unselectedTextColor = Color.LightGray
                        )
                    )
                    NavigationRailItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = { Icon(Icons.Default.Explore, contentDescription = "Основной раздел") },
                        label = { Text("Основной", style = MaterialTheme.typography.labelSmall) },
                        colors = NavigationRailItemDefaults.colors(
                            selectedIconColor = Color(0xFF00BCD4),
                            selectedTextColor = Color(0xFF00BCD4),
                            unselectedIconColor = Color.LightGray,
                            unselectedTextColor = Color.LightGray
                        )
                    )
                    NavigationRailItem(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        icon = { Icon(Icons.AutoMirrored.Filled.LibraryBooks, contentDescription = "Манифест и Контекст") },
                        label = { Text("Контекст", style = MaterialTheme.typography.labelSmall) },
                        colors = NavigationRailItemDefaults.colors(
                            selectedIconColor = Color(0xFFE040FB),
                            selectedTextColor = Color(0xFFE040FB),
                            unselectedIconColor = Color.LightGray,
                            unselectedTextColor = Color.LightGray
                        )
                    )
                    NavigationRailItem(
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        icon = { Icon(Icons.Default.Gavel, contentDescription = "Научная критика") },
                        label = { Text("Критика", style = MaterialTheme.typography.labelSmall) },
                        colors = NavigationRailItemDefaults.colors(
                            selectedIconColor = Color(0xFFFF9800),
                            selectedTextColor = Color(0xFFFF9800),
                            unselectedIconColor = Color.LightGray,
                            unselectedTextColor = Color.LightGray
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    when (selectedTab) {
                        0 -> FirstSourceReader(playSpeech = playSpeech, isPlayingGlobal = isPlayingGlobal, runningTtsId = runningTtsId)
                        1 -> MainTabWorkspace(playSpeech = playSpeech, isPlayingGlobal = isPlayingGlobal, runningTtsId = runningTtsId)
                        2 -> WhitePaperReader(playSpeech = playSpeech, isPlayingGlobal = isPlayingGlobal, runningTtsId = runningTtsId)
                        3 -> ScientificCriticismScreen(playSpeech = playSpeech, isPlayingGlobal = isPlayingGlobal, runningTtsId = runningTtsId)
                    }
                }
            }
        } else {
            Scaffold(
                containerColor = Color(0xFF0F172A),
                floatingActionButton = {
                    val context = LocalContext.current
                    Column(horizontalAlignment = Alignment.End) {
                        FloatingActionButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://khromenko-ai.github.io/Freque/"))
                                context.startActivity(intent)
                            },
                            containerColor = Color(0xFFE040FB),
                            contentColor = Color.White,
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            Icon(Icons.Default.Language, contentDescription = "Web App")
                        }
                        ExtendedFloatingActionButton(
                            onClick = { showGlobalMapDialog = true },
                            containerColor = Color(0xFF00BCD4),
                            contentColor = Color.White,
                            icon = { Icon(Icons.Default.Map, contentDescription = "Открыть карту") },
                            text = { Text("Карта смыслов", fontWeight = FontWeight.Bold) }
                        )
                    }
                },
                bottomBar = {
                    NavigationBar(
                        containerColor = Color(0xFF1E293B),
                        contentColor = Color.White
                    ) {
                        NavigationBarItem(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            icon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = "Базовый обзор") },
                            label = { Text("Базовый", style = MaterialTheme.typography.labelSmall) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF4CAF50),
                                selectedTextColor = Color(0xFF4CAF50),
                                unselectedIconColor = Color.LightGray,
                                unselectedTextColor = Color.LightGray
                            )
                        )
                        NavigationBarItem(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            icon = { Icon(Icons.Default.Explore, contentDescription = "Основной раздел") },
                            label = { Text("Основной", style = MaterialTheme.typography.labelSmall) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF00BCD4),
                                selectedTextColor = Color(0xFF00BCD4),
                                unselectedIconColor = Color.LightGray,
                                unselectedTextColor = Color.LightGray
                            )
                        )
                        NavigationBarItem(
                            selected = selectedTab == 2,
                            onClick = { selectedTab = 2 },
                            icon = { Icon(Icons.AutoMirrored.Filled.LibraryBooks, contentDescription = "Манифест и Контекст") },
                            label = { Text("Контекст", style = MaterialTheme.typography.labelSmall) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFFE040FB),
                                selectedTextColor = Color(0xFFE040FB),
                                unselectedIconColor = Color.LightGray,
                                unselectedTextColor = Color.LightGray
                            )
                        )
                        NavigationBarItem(
                            selected = selectedTab == 3,
                            onClick = { selectedTab = 3 },
                            icon = { Icon(Icons.Default.Gavel, contentDescription = "Научная критика") },
                            label = { Text("Критика", style = MaterialTheme.typography.labelSmall) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFFFF9800),
                                selectedTextColor = Color(0xFFFF9800),
                                unselectedIconColor = Color.LightGray,
                                unselectedTextColor = Color.LightGray
                            )
                        )
                    }
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    when (selectedTab) {
                        0 -> FirstSourceReader(playSpeech = playSpeech, isPlayingGlobal = isPlayingGlobal, runningTtsId = runningTtsId)
                        1 -> MainTabWorkspace(playSpeech = playSpeech, isPlayingGlobal = isPlayingGlobal, runningTtsId = runningTtsId)
                        2 -> WhitePaperReader(playSpeech = playSpeech, isPlayingGlobal = isPlayingGlobal, runningTtsId = runningTtsId)
                        3 -> ScientificCriticismScreen(playSpeech = playSpeech, isPlayingGlobal = isPlayingGlobal, runningTtsId = runningTtsId)
                    }
                }
            }
        }
    }
}

@Composable
fun MainTabWorkspace(
    playSpeech: (String, String) -> Unit,
    isPlayingGlobal: Boolean,
    runningTtsId: String
) {
    var subTabSelected by remember { mutableIntStateOf(0) } // 0 -> Map, 1 -> Essay book, 2 -> Synthesis & Summary

    Column(modifier = Modifier.fillMaxSize()) {
        ScrollableTabRow(
            selectedTabIndex = subTabSelected,
            containerColor = Color(0xFF1E293B),
            contentColor = Color.White,
            edgePadding = 8.dp,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[subTabSelected]),
                    color = Color(0xFF00BCD4)
                )
            }
        ) {
            Tab(
                selected = subTabSelected == 0,
                onClick = { subTabSelected = 0 },
                text = { Text("Карта смыслов", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = subTabSelected == 1,
                onClick = { subTabSelected = 1 },
                text = { Text("Книга-Эссе (15 глав)", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = subTabSelected == 2,
                onClick = { subTabSelected = 2 },
                text = { Text("Новый синтез & Саммэри", fontWeight = FontWeight.Bold) }
            )
        }

        Box(modifier = Modifier.weight(1f)) {
            when (subTabSelected) {
                0 -> MapFlow(playSpeech = playSpeech, isPlayingGlobal = isPlayingGlobal, runningTtsId = runningTtsId)
                1 -> EssayReader(playSpeech = playSpeech, isPlayingGlobal = isPlayingGlobal, runningTtsId = runningTtsId)
                2 -> NewSynthesisScreen(playSpeech = playSpeech, isPlayingGlobal = isPlayingGlobal, runningTtsId = runningTtsId)
            }
        }
    }
}

@Composable
fun MapFlow(
    playSpeech: (String, String) -> Unit,
    isPlayingGlobal: Boolean,
    runningTtsId: String
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "map_root"
    ) {
        composable("map_root") {
            MapScreen(
                onNodeClick = { nodeId ->
                    navController.navigate("node_detail/$nodeId")
                }
            )
        }
        composable("node_detail/{nodeId}") { backStackEntry ->
            val nodeId = backStackEntry.arguments?.getString("nodeId") ?: "intro"
            val node = cosmologyNodes.find { it.id == nodeId } ?: cosmologyNodes.first()

            val isCurrentPlaying = isPlayingGlobal && runningTtsId == "MAP_$nodeId"

            NodeDetailScreen(
                node = node,
                isPlaying = isCurrentPlaying,
                onBack = { navController.popBackStack() },
                onPlayToggle = {
                    val speechText = "Раздел: ${node.title}. Смысл: ${node.description}. Цитата из исследования: ${node.quote}"
                    playSpeech("MAP_$nodeId", speechText)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(onNodeClick: (String) -> Unit) {
    val infiniteTransition = rememberInfiniteTransition("map_flow")
    val pulseProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(3500, easing = LinearEasing)),
        label = "pulseProgress"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("ЧАСТОТНАЯ КОСМОЛОГИЯ", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color.White))
                        Text("Интерактивная карта смыслов и связей", style = MaterialTheme.typography.labelSmall.copy(color = Color.LightGray))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0F172A)
                )
            )
        },
        containerColor = Color(0xFF0F172A)
    ) { padding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            val width = maxWidth
            val height = maxHeight

            // Draw connecting flow lines with flowing pulses
            Canvas(modifier = Modifier.fillMaxSize()) {
                val connections = listOf(
                    "intro" to "time_reality",
                    "intro" to "spectral",
                    "time_reality" to "observer",
                    "spectral" to "observer",
                    "observer" to "hierarchy",
                    "observer" to "holography",
                    "hierarchy" to "ring",
                    "holography" to "ring"
                )

                for ((fromId, toId) in connections) {
                    val fromNode = cosmologyNodes.find { it.id == fromId }
                    val toNode = cosmologyNodes.find { it.id == toId }
                    if (fromNode != null && toNode != null) {
                        val startOffset = Offset(fromNode.xOffset * size.width, fromNode.yOffset * size.height)
                        val endOffset = Offset(toNode.xOffset * size.width, toNode.yOffset * size.height)
                        drawLine(
                            color = fromNode.accentColor.copy(alpha = 0.35f),
                            start = startOffset,
                            end = endOffset,
                            strokeWidth = 3f
                        )

                        // Draw traveling wavelets/pulses along the connections
                        for (p in 0..1) {
                            val t = (pulseProgress + p * 0.5f) % 1f
                            val px = startOffset.x + (endOffset.x - startOffset.x) * t
                            val py = startOffset.y + (endOffset.y - startOffset.y) * t

                            drawCircle(
                                color = fromNode.accentColor,
                                radius = 5f,
                                center = Offset(px, py)
                            )
                            drawCircle(
                                color = fromNode.accentColor.copy(alpha = 0.25f),
                                radius = 11f,
                                center = Offset(px, py)
                            )
                        }
                    }
                }
            }

            // Draw node interactive buttons
            for (node in cosmologyNodes) {
                val nodeX = width * node.xOffset - 40.dp
                val nodeY = height * node.yOffset - 40.dp

                Box(
                    modifier = Modifier
                        .offset(x = nodeX, y = nodeY)
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(node.accentColor.copy(alpha = 0.5f), Color.Transparent)
                            )
                        )
                        .border(1.5.dp, node.accentColor.copy(alpha = 0.8f), CircleShape)
                        .clickable { onNodeClick(node.id) },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(node.accentColor),
                        contentAlignment = Alignment.Center
                    ) {
                        // Icon matching is extremely intuitive
                        val icon = when(node.id) {
                            "intro" -> Icons.Default.Adjust
                            "time_reality" -> Icons.Default.AccessTime
                            "spectral" -> Icons.Default.Waves
                            "observer" -> Icons.Default.Visibility
                            "hierarchy" -> Icons.Default.BarChart
                            "holography" -> Icons.Default.AllInclusive
                            "ring" -> Icons.Default.Camera
                            else -> Icons.Default.Info
                        }
                        Icon(icon, contentDescription = node.title, tint = Color.White, modifier = Modifier.size(24.dp))
                    }
                }

                // Label on map
                Text(
                    text = node.title,
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .offset(x = width * node.xOffset - 75.dp, y = height * node.yOffset + 44.dp)
                        .width(150.dp)
                        .background(Color(0xFF0F172A).copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
fun NodeDetailScreen(
    node: CosmologyNode,
    isPlaying: Boolean,
    onBack: () -> Unit,
    onPlayToggle: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
    ) {
        // Aesthetic visualization top component
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.42f)
                .background(Color(0xFF0B0F19))
        ) {
            NodeVisualization(nodeId = node.id, accentColor = node.accentColor)

            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад к карте", tint = Color.White)
            }
        }

        // Info details sheet
        Column(
            modifier = Modifier
                .weight(0.58f)
                .fillMaxWidth()
                .background(Color(0xFF1E293B), shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = node.title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 24.sp
                    ),
                    modifier = Modifier.weight(1f)
                )

                Button(
                    onClick = onPlayToggle,
                    colors = ButtonDefaults.buttonColors(containerColor = node.accentColor),
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                        contentDescription = "Озвучка"
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = if (isPlaying) "Стоп" else "Озвучить", style = MaterialTheme.typography.labelLarge)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quotation Area
            Surface(
                color = Color.Black.copy(alpha = 0.25f),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ЦИТАТА",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = node.accentColor,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "«${node.quote}»",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontStyle = FontStyle.Italic,
                            color = Color.LightGray,
                            lineHeight = 20.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "СМЫСЛОВОЙ АНАЛИЗ",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = node.description,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White,
                    lineHeight = 26.sp
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "ИСТОЧНИКИ В ТЕКСТЕ: ",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                )
                Text(
                    text = node.refs,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = node.accentColor,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirstSourceReader(
    playSpeech: (String, String) -> Unit,
    isPlayingGlobal: Boolean,
    runningTtsId: String
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ОРИГИНАЛЬНЫЙ ТЕКСТ", color = Color.White, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))
            )
        },
        containerColor = Color(0xFF0F172A)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = firstSourceTitle,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00BCD4)
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        val speechId = "FS_INTRO"
                        val isPlaying = isPlayingGlobal && runningTtsId == speechId
                        IconButton(
                            onClick = { playSpeech(speechId, firstSourceIntroduction) },
                            modifier = Modifier
                                .background(Color(0xFF00BCD4).copy(alpha = 0.2f), CircleShape)
                                .size(36.dp)
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                                contentDescription = "Слушать введение",
                                tint = Color(0xFF00BCD4),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = firstSourceIntroduction,
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.White, lineHeight = 22.sp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0B0F19))
                            .border(0.5.dp, Color(0xFF00BCD4).copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                    ) {
                        CosmicVacuumIntroViz(color = Color(0xFF00BCD4))
                    }
                }
            }

            Text(
                text = "ОСНОВНЫЕ ИДЕИ КОНЦЕПЦИИ:",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.LightGray
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            firstSourceSections.forEachIndexed { index, section ->
                val cardColor = when(index % 6) {
                    0 -> Color(0xFF311B92).copy(alpha = 0.15f)
                    1 -> Color(0xFF0D47A1).copy(alpha = 0.15f)
                    2 -> Color(0xFF006064).copy(alpha = 0.15f)
                    3 -> Color(0xFF1B5E20).copy(alpha = 0.15f)
                    4 -> Color(0xFFE65100).copy(alpha = 0.15f)
                    else -> Color(0xFF880E4F).copy(alpha = 0.15f)
                }
                
                val accentColor = when(index % 6) {
                    0 -> Color(0xFFAB47BC)
                    1 -> Color(0xFF42A5F5)
                    2 -> Color(0xFF26C6DA)
                    3 -> Color(0xFF66BB6A)
                    4 -> Color(0xFFFFA726)
                    else -> Color(0xFFEC407A)
                }

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .border(1.dp, accentColor.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .background(cardColor)
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "■ ${section.title}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = accentColor
                                ),
                                modifier = Modifier.weight(1f)
                            )

                            val speechId = "FS_SEC_$index"
                            val isPlaying = isPlayingGlobal && runningTtsId == speechId
                            IconButton(
                                onClick = { playSpeech(speechId, "Тема: ${section.title}. Описание: ${section.text}") },
                                modifier = Modifier
                                    .background(accentColor.copy(alpha = 0.2f), CircleShape)
                                    .size(36.dp)
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                                    contentDescription = "Слушать тему",
                                    tint = accentColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = section.text,
                            style = MaterialTheme.typography.bodyLarge.copy(color = Color.White, lineHeight = 24.sp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            color = Color.Black.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = section.quote,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontStyle = FontStyle.Italic,
                                    color = Color.LightGray,
                                    lineHeight = 20.sp
                                ),
                                modifier = Modifier.padding(12.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF0B0F19))
                                .border(0.5.dp, accentColor.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        ) {
                            val vizNodeId = when(index) {
                                0 -> "time_reality"
                                1 -> "observer"
                                2 -> "hierarchy"
                                3 -> "holography"
                                4 -> "ring"
                                else -> "intro"
                            }
                            NodeVisualization(nodeId = vizNodeId, accentColor = accentColor)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EssayReader(
    playSpeech: (String, String) -> Unit,
    isPlayingGlobal: Boolean,
    runningTtsId: String
) {
    var selectedChapterIndex by remember { mutableIntStateOf(0) } // 0 is Introduction, 1..15 are chapters, 16 is Conclusion

    val chaptersList = remember {
        mutableListOf<EssayChapter>().apply {
            add(essayIntroduction)
            addAll(essayChapters)
            add(essayConclusion)
        }
    }

    val currentChapter = chaptersList[selectedChapterIndex]

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("РАЗВЕРНУТОЕ ЭССЕ", color = Color.White, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        Text("15 концептуальных глав космологии", style = MaterialTheme.typography.labelSmall.copy(color = Color.LightGray))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))
            )
        },
        containerColor = Color(0xFF0F172A)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Chapter selector horizontal scrollable bar
            ScrollableTabRow(
                selectedTabIndex = selectedChapterIndex,
                containerColor = Color(0xFF1E293B),
                contentColor = Color.White,
                edgePadding = 16.dp,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedChapterIndex]),
                        color = Color(0xFFFF9800)
                    )
                }
            ) {
                chaptersList.forEachIndexed { index, chapter ->
                    Tab(
                        selected = selectedChapterIndex == index,
                        onClick = { selectedChapterIndex = index },
                        text = { 
                            Text(
                                text = chapter.title,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = if (selectedChapterIndex == index) FontWeight.Bold else FontWeight.Medium
                                )
                            ) 
                        }
                    )
                }
            }

            // Main reading workspace
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header of current Chapter
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = currentChapter.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFF9800)
                                )
                            )
                            if (currentChapter.subtitle.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = currentChapter.subtitle,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 18.sp
                                    )
                                )
                            }
                        }

                        val speechId = "ESSAY_CHAP_$selectedChapterIndex"
                        val fullChapterText = currentChapter.title + ". " + (if (currentChapter.subtitle.isNotEmpty()) currentChapter.subtitle + ". " else "") + currentChapter.paragraphs.joinToString(". ")
                        val isPlaying = isPlayingGlobal && runningTtsId == speechId

                        IconButton(
                            onClick = { playSpeech(speechId, fullChapterText) },
                            modifier = Modifier
                                .background(Color(0xFFFF9800).copy(alpha = 0.2f), CircleShape)
                                .size(44.dp)
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                                contentDescription = "Слушать главу",
                                tint = Color(0xFFFF9800),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Interactive banner displaying corresponding cosmology visualizer
                val chapterVizId = when(selectedChapterIndex) {
                    0, 1, 2, 3, 16 -> "intro"
                    4, 5 -> "time_reality"
                    6 -> "spectral"
                    7, 8, 9 -> "observer"
                    10, 11 -> "hierarchy"
                    12, 13 -> "holography"
                    14, 15 -> "ring"
                    else -> "intro"
                }

                val chapterVizColor = when(chapterVizId) {
                    "intro" -> Color(0xFFAB47BC)
                    "time_reality" -> Color(0xFF42A5F5)
                    "spectral" -> Color(0xFF26C6DA)
                    "observer" -> Color(0xFF00BCD4)
                    "hierarchy" -> Color(0xFF66BB6A)
                    "holography" -> Color(0xFFFF9800)
                    "ring" -> Color(0xFFEC407A)
                    else -> Color(0xFFAB47BC)
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF0B0F19))
                        .border(1.dp, chapterVizColor.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                ) {
                    NodeVisualization(nodeId = chapterVizId, accentColor = chapterVizColor)
                    
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "СПЕКТРАЛЬНЫЙ СИМУЛЯТОР",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = chapterVizColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Paragraph fields
                currentChapter.paragraphs.forEachIndexed { pIndex, paragraph ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF151F32)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = paragraph,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = Color.White,
                                        lineHeight = 26.sp,
                                        fontSize = 16.sp
                                    ),
                                    modifier = Modifier.weight(1f)
                                )

                                val rowSpeechId = "ESSAY_CHAP_${selectedChapterIndex}_P_$pIndex"
                                val isRowPlaying = isPlayingGlobal && runningTtsId == rowSpeechId

                                IconButton(
                                    onClick = { playSpeech(rowSpeechId, paragraph) },
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isRowPlaying) Icons.Default.Stop else Icons.Default.PlayCircle,
                                        contentDescription = "Слушать абзац",
                                        tint = if (isRowPlaying) Color(0xFFFF9800) else Color.Gray,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Interactive Pagination buttons
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { if (selectedChapterIndex > 0) selectedChapterIndex-- },
                        enabled = selectedChapterIndex > 0,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Назад", color = if (selectedChapterIndex > 0) Color.White else Color.Gray)
                    }

                    Button(
                        onClick = { if (selectedChapterIndex < chaptersList.size - 1) selectedChapterIndex++ },
                        enabled = selectedChapterIndex < chaptersList.size - 1,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Далее", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(64.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhitePaperReader(
    playSpeech: (String, String) -> Unit,
    isPlayingGlobal: Boolean,
    runningTtsId: String
) {
    var selectedWhitePaperIndex by remember { mutableIntStateOf(0) } // 0 is Annotation/Index, 1..11 are chapters, 12 is References bibliography
    
    val sectionsList = remember {
        mutableListOf<WhitePaperSection>().apply {
            addAll(whitePaperSections)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("МАНИФЕСТ & КОНТЕКСТ", color = Color.White, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        Text("Белая книга космологии & источники", style = MaterialTheme.typography.labelSmall.copy(color = Color.LightGray))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))
            )
        },
        containerColor = Color(0xFF0F172A)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Horizontal scrollable TabRow for Parts of White Paper
            ScrollableTabRow(
                selectedTabIndex = selectedWhitePaperIndex,
                containerColor = Color(0xFF1E293B),
                contentColor = Color.White,
                edgePadding = 16.dp,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedWhitePaperIndex]),
                        color = Color(0xFFE040FB)
                    )
                }
            ) {
                Tab(
                    selected = selectedWhitePaperIndex == 0,
                    onClick = { selectedWhitePaperIndex = 0 },
                    text = { Text("Аннотация", fontWeight = if (selectedWhitePaperIndex == 0) FontWeight.Bold else FontWeight.Medium) }
                )
                sectionsList.forEachIndexed { index, section ->
                    val partNum = index + 1
                    Tab(
                        selected = selectedWhitePaperIndex == partNum,
                        onClick = { selectedWhitePaperIndex = partNum },
                        text = { 
                            Text(
                                text = if (index == sectionsList.size - 1) "Заключение" else "Часть $partNum",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = if (selectedWhitePaperIndex == partNum) FontWeight.Bold else FontWeight.Medium
                                )
                            ) 
                        }
                    )
                }
                Tab(
                    selected = selectedWhitePaperIndex == sectionsList.size + 1,
                    onClick = { selectedWhitePaperIndex = sectionsList.size + 1 },
                    text = { Text("Литература", fontWeight = if (selectedWhitePaperIndex == sectionsList.size + 1) FontWeight.Bold else FontWeight.Medium) }
                )
            }

            // main workspace
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                if (selectedWhitePaperIndex == 0) {
                    // Annotation / Introduction screen
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = whitePaperTitle,
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFE040FB)
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = whitePaperSubtitle,
                                        style = MaterialTheme.typography.labelMedium.copy(color = Color.LightGray)
                                    )
                                }

                                val speechId = "WP_INTRO"
                                val isPlaying = isPlayingGlobal && runningTtsId == speechId
                                IconButton(
                                    onClick = { playSpeech(speechId, "$whitePaperTitle. $whitePaperSubtitle. $whitePaperAnnotation") },
                                    modifier = Modifier
                                        .background(Color(0xFFE040FB).copy(alpha = 0.2f), CircleShape)
                                        .size(44.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                                        contentDescription = "Слушать введение",
                                        tint = Color(0xFFE040FB),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = whitePaperAnnotation,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = Color.White,
                                    lineHeight = 24.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF0B0F19))
                                    .border(0.5.dp, Color(0xFFE040FB).copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            ) {
                                HolographicHorizonViz(color = Color(0xFFE040FB))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "ОСНОВНЫЕ РАЗДЕЛЫ МАНИФЕСТА:",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color.Gray)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    sectionsList.forEachIndexed { index, section ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF151F32)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { selectedWhitePaperIndex = index + 1 }
                        ) {
                            ListItem(
                                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                                headlineContent = { Text(section.title, color = Color.White, fontWeight = FontWeight.Bold) },
                                leadingContent = { 
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .background(Color(0xFFE040FB).copy(alpha = 0.15f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${index + 1}",
                                            color = Color(0xFFE040FB),
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    }
                                },
                                trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = "Открыть", tint = Color.Gray) }
                            )
                        }
                    }
                } else if (selectedWhitePaperIndex <= sectionsList.size) {
                    val section = sectionsList[selectedWhitePaperIndex - 1]
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = section.title,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFE040FB)
                                ),
                                modifier = Modifier.weight(1f)
                            )

                            val speechId = "WP_SECTION_TTS_$selectedWhitePaperIndex"
                            val fullSectionText = section.title + ". " + section.paragraphs.joinToString(". ")
                            val isPlaying = isPlayingGlobal && runningTtsId == speechId

                            IconButton(
                                onClick = { playSpeech(speechId, fullSectionText) },
                                modifier = Modifier
                                    .background(Color(0xFFE040FB).copy(alpha = 0.2f), CircleShape)
                                    .size(44.dp)
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                                    contentDescription = "Слушать раздел",
                                    tint = Color(0xFFE040FB),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Embedded animated visualizer matching whitepaper section focus
                    val wpVizId = when(selectedWhitePaperIndex) {
                        1 -> "intro"
                        2 -> "time_reality"
                        3 -> "observer"
                        4 -> "ring"
                        5 -> "hierarchy"
                        6 -> "holography"
                        7 -> "spectral"
                        8 -> "observer"
                        9 -> "holography"
                        10 -> "intro"
                        11 -> "ring"
                        else -> "intro"
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF0B0F19))
                            .border(1.dp, Color(0xFFE040FB).copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                    ) {
                        NodeVisualization(nodeId = wpVizId, accentColor = Color(0xFFE040FB))
                        
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp)
                                .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "СПЕКТРАЛЬНЫЕ КОРРЕЛЯЦИИ",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color(0xFFE040FB),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    section.paragraphs.forEachIndexed { pIndex, paragraph ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF151F32)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text(
                                        text = paragraph,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            color = Color.White,
                                            lineHeight = 26.sp,
                                            fontSize = 16.sp
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )

                                    val itemSpeechId = "WP_SEC_${selectedWhitePaperIndex}_P_$pIndex"
                                    val isItemPlaying = isPlayingGlobal && runningTtsId == itemSpeechId

                                    IconButton(
                                        onClick = { playSpeech(itemSpeechId, paragraph) },
                                        modifier = Modifier
                                            .padding(start = 8.dp)
                                            .size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isItemPlaying) Icons.Default.Stop else Icons.Default.PlayCircle,
                                            contentDescription = "Слушать абзац",
                                            tint = if (isItemPlaying) Color(0xFFE040FB) else Color.Gray,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { selectedWhitePaperIndex-- },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Назад", color = Color.White)
                        }

                        Button(
                            onClick = { selectedWhitePaperIndex++ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE040FB)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Далее", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    // References list screen
                    Text(
                        text = "НАУЧНЫЙ КОНТЕКСТ & ИСТОЧНИКИ",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00BCD4)
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Для более глубокого академического контекста мы подобрали и верифицировали ключевые препринты, статьи по теории волн плотности, теории автоматов и оптимизации:",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.LightGray, lineHeight = 20.sp),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    val context = androidx.compose.ui.platform.LocalContext.current

                    researchReferences.forEach { ref ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = ref.title,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = ref.description,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = Color.LightGray,
                                        lineHeight = 20.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = ref.url,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color(0xFF00BCD4),
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        modifier = Modifier.weight(1f),
                                        maxLines = 1
                                    )

                                    Button(
                                        onClick = {
                                            try {
                                                val intent = android.content.Intent(
                                                    android.content.Intent.ACTION_VIEW,
                                                    android.net.Uri.parse(ref.url)
                                                )
                                                context.startActivity(intent)
                                            } catch (e: Exception) {
                                                // safe fallback
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4)),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text("Открыть", style = MaterialTheme.typography.labelSmall.copy(color = Color.Black))
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { selectedWhitePaperIndex = 0 },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Вернуться в начало манифеста", color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(64.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScientificCriticismScreen(
    playSpeech: (String, String) -> Unit,
    isPlayingGlobal: Boolean,
    runningTtsId: String
) {
    var expandedIndex by remember { mutableIntStateOf(-1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("НАУЧНЫЙ РЕЦЕНЗЕНТ", color = Color.White, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        Text("Критика JSPPC и ответы авторов", style = MaterialTheme.typography.labelSmall.copy(color = Color.LightGray))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))
            )
        },
        containerColor = Color(0xFF0F172A)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Prestigious Peer Review Header Info Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .border(1.5.dp, Color(0xFFFF9800).copy(alpha = 0.4f), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color(0xFFFF9800).copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.School, contentDescription = "Академическая экспертиза", tint = Color(0xFFFF9800), modifier = Modifier.size(28.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = journalName,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Статус статьи: Ревизия / Пересмотр",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFFA726)
                                )
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = criticismIntro,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.LightGray,
                            lineHeight = 20.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Для просмотра математической симуляции ответа выберите тему ниже:",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                    )
                }
            }

            // List of Criticisms
            criticismItems.forEachIndexed { index, item ->
                val isExpanded = expandedIndex == index
                val itemAccentColor = item.accentColor
                
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .border(
                            width = if (isExpanded) 1.5.dp else 1.dp,
                            color = if (isExpanded) itemAccentColor else itemAccentColor.copy(alpha = 0.25f),
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .clickable { expandedIndex = if (isExpanded) -1 else index }
                            .padding(16.dp)
                    ) {
                        // Title of item
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isExpanded) itemAccentColor else Color.White
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = if (isExpanded) "Свернуть" else "Развернуть",
                                tint = Color.Gray,
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        }

                        if (isExpanded) {
                            Spacer(modifier = Modifier.height(16.dp))

                            // 1. Reviewer opinion (criticism)
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.25f)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, Color(0xFFEF4444).copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.ErrorOutline,
                                                contentDescription = "Критика",
                                                tint = Color(0xFFEF4444),
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "МНЕНИЕ РЕЦЕНЗЕНТА #2",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFFEF4444)
                                                )
                                            )
                                        }

                                        val revSpeechId = "CRIT_REV_$index"
                                        val isRevPlaying = isPlayingGlobal && runningTtsId == revSpeechId
                                        IconButton(
                                            onClick = { playSpeech(revSpeechId, "Мнение рецензента. " + item.reviewerOpinion) },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (isRevPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                                                contentDescription = "Слушать рецензента",
                                                tint = Color(0xFFEF4444),
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                    
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = item.reviewerOpinion,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = Color(0xFFFCA5A5),
                                            lineHeight = 20.sp
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // 2. Author's rigorous response
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.2f)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, Color(0xFF10B981).copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircleOutline,
                                                contentDescription = "Ответ",
                                                tint = Color(0xFF10B981),
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "ОТВЕТ АВТОРОВ КОНЦЕПЦИИ",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF10B981)
                                                )
                                            )
                                        }

                                        val authSpeechId = "CRIT_AUTH_$index"
                                        val isAuthPlaying = isPlayingGlobal && runningTtsId == authSpeechId
                                        IconButton(
                                            onClick = { playSpeech(authSpeechId, "Ответ авторов. " + item.authorResponse) },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (isAuthPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                                                contentDescription = "Слушать ответ авторов",
                                                tint = Color(0xFF10B981),
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                    
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = item.authorResponse,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = Color.White,
                                            lineHeight = 20.sp
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // 3. Custom-tailored animated physics mathematical simulation
                            Text(
                                text = "ИНТЕРАКТИВНОЕ РЕШЕНИЕ ПАРАДОКСА В РЕАЛЬНОМ ВРЕМЕНИ:",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = itemAccentColor
                                ),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF0B0F19))
                                    .border(1.dp, itemAccentColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                            ) {
                                NodeVisualization(nodeId = item.id, accentColor = itemAccentColor)
                                
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(10.dp)
                                        .background(Color.Black.copy(alpha = 0.65f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = when(item.id) {
                                            "crit_1_lagrangian" -> "Манифестация Квантового Волнового Пакета"
                                            "crit_2_gabor" -> "Соотношение Неопределенности Габора Δt·Δω"
                                            "crit_3_unitarity" -> "Фрактальная Унитарность на Кольце Состояний"
                                            "crit_4_entropy" -> "Голографическая Фильтрация Масс-Энтропии"
                                            else -> "Динамическая Спектральная Матрица"
                                        },
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = itemAccentColor,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewSynthesisScreen(
    playSpeech: (String, String) -> Unit,
    isPlayingGlobal: Boolean,
    runningTtsId: String
) {
    val introductionText = """
        Отказываемся от скрытого предположения, которое мы всё время делали: что вейвлет — это связный локальный участок спектра.
        Если убрать это ограничение, то вейвлет становится ближе не к окну, а к мере на спектре.
        То есть вместо W(x) != 0 только на непрерывном интервале, мы допускаем W(x) != 0 на множестве разрозненных точек:
        x x x x x x x x x x x
        1 0 0 1 0 1 0 0 0 1
        или даже
        1 0 0 1 0 1 0 0 1 0 0 0 1 ...
        Тогда вейвлет уже не "занимает место". Он становится шаблоном выборки.
        
        Тут появляется очень сильная аналогия с квантовой механикой.
        Электрон не обязан быть локализованным в одной точке.
        Его волновая функция может иметь поддержку сразу в нескольких областях пространства.
        Точно так же твой вейвлет может одновременно содержать кванты, принадлежащие очень удалённым областям окружности.
        Тогда несколько вейвлетов действительно могут пересекаться на одном и том же кванте.
        В этом смысле они больше похожи на волновые моды или бозонные состояния, чем на частицы.
        
        Теперь про количество таких вейвлетов. Тут есть три режима:
        1. Конечное число: Если окружность содержит N квантов, число возможных вейвлетов 2^N.
        2. Счётно бесконечное: Если квантов бесконечно много, но комбинации конечны.
        3. Континуум: Если разрешить произвольное распределение амплитуд W(x) в вещественных числах.
        
        Теперь рассмотрим путь вейвлета. Его "путь" можно задавать функцией W(x,t). Множество путей - {W_i(x,t)}.
        Что будет, если сложить все возможные пути? Фейнмановский интеграл S(x,t) = sum_i W_i(x,t) стремится к нулю из-за взаимного уничтожения противоположных вкладов f(x) + (-f(x)) = 0.
        Сумма всего пространства симметричных возможностей стремится к нулю.
        
        И тут возникает связь с фразой: "ноль — это бесконечность, а бесконечность — это ноль".
        Среднее значение полного спектра гармоник, вакуум в квантовой теории, белый шум - все они указывают на то, что Ноль означает полную симметрию структуры.
        Наблюдаемая история — это локальное нарушение симметрии пространства всех возможных путей.
    """.trimIndent()

    val scaleSpaceText = """
        Одной из скрытых проблем многих моделей времени является то, что они пытаются объяснить время, уже предполагая существование времени. Именно с этой трудностью мы сталкиваемся, когда говорим о «расширяющемся кольце состояний». Если между квантами окружности постоянно появляются новые кванты, возникает вопрос: относительно чего происходит это появление? Само понятие роста уже предполагает внешнюю временную ось, а значит, мы незаметно вводим то, что пытаемся объяснить.

        Более последовательным оказывается другой подход. Вместо того чтобы рассматривать кольцо как объект, который расширяется со временем, можно считать, что существует сразу целое семейство колец различных размеров. Тогда радиус кольца становится не функцией времени, а самостоятельной координатой. Мы имеем дело не с процессом расширения, а со структурой масштабов.

        В такой картине кольца различных размеров существуют одновременно. Нет необходимости говорить, что одно кольцо возникло раньше другого или что одно расширилось в другое. Между ними существуют лишь отношения отображения. Каждое кольцо может проецироваться на соседние кольца, содержать информацию о них или, наоборот, представлять их в более грубом масштабе. То, что раньше выглядело как динамика, превращается в геометрию связей между уровнями описания.

        Здесь появляется важная аналогия с вейвлетным анализом. Вейвлеты позволяют описывать сигнал сразу на множестве масштабов. При этом разные масштабы существуют одновременно и не требуют отдельного времени для своего возникновения. Сигнал уже содержит всю иерархию масштабов, а наблюдатель лишь выбирает, на каком уровне его рассматривать. Возможно, нечто подобное происходит и с реальностью.

        Особенно интересной становится идея «выворачивания» колец. Если структура самоподобна, становится трудно определить, какое кольцо является внутренним, а какое внешним. Каждое кольцо может быть представлено через другие кольца. В пределе возникает ситуация, в которой понятия центра и периферии теряют абсолютный смысл. Любой масштаб может рассматриваться как центральный относительно собственного уровня описания.

        В таком случае бесконечная прямая, возникающая после мысленного разрезания кольца, приобретает новое значение. Она перестаёт быть просто геометрической линией. Скорее, она становится способом кодирования всей иерархии масштабов в одномерную последовательность. Каждая точка на этой линии соответствует определённому паттерну состояний, а каждый участок — определённому уровню организации структуры.

        Это приводит к радикальному пересмотру понятия времени. Если различные масштабы существуют одновременно, то наблюдаемое течение времени может оказаться не фундаментальным процессом, а локальной интерпретацией переходов между масштабами. Наблюдатель воспринимает часть многомасштабной структуры и интерпретирует её как последовательность событий. Время становится не самостоятельной сущностью, а способом чтения отношений между уровнями организации.

        С этой точки зрения кольца, прямая и вейвлеты оказываются тремя различными представлениями одной и той же реальности. Кольца описывают её как систему взаимосвязанных масштабов. Прямая представляет ту же структуру в сериализованном виде. Вейвлеты выступают локальными окнами наблюдения, через которые осуществляется чтение этой структуры. Тогда расширение превращается в смену масштаба, а динамика — в проекцию более глубокой многомасштабной геометрии.

        Возможно, именно здесь находится мост между идеями времени, наблюдения и голографии. То, что кажется нам историей событий, может быть не движением мира через время, а последовательным декодированием уже существующей структуры масштабов. В этом случае реальность оказывается ближе не к разворачивающемуся процессу, а к сложной спектральной архитектуре, внутри которой наблюдатель прокладывает собственную траекторию чтения.
    """.trimIndent()

    val synthesisSummaryText = """
        ОБЩИЙ СИНТЕЗ ПЯТИ ФУНДАМЕНТАЛЬНЫХ РАЗДЕЛОВ КОСМОЛОГИИ СЕМЕЙСТВА КОЛЕЦ:
        
        1. БАЗОВЫЙ ОБЗОР: Время не течёт. Оно возникает как последовательное считывание сознанием статичной многомерной спектральной информационной структуры.
        
        2. РАЗВЕРНУТОЕ ЭССЕ: Наблюдатель — это вейвлет (частотно-временное окно памяти), фрактально вложенный в шкалу вселенских надвейвлетов-гармоник.
        
        3. КОНТЕКСТ & МАНИФЕСТ (White Paper): Математическое и физическое обоснование. Голографический спектральный предел Бекенштейна и сохранение унитарности вероятностей на Кольце Состояний.
        
        4. КВАНТОВЫЙ РАСПРЕДЕЛЕННЫЙ ВЕЙВЛЕТ (Нулевой Фон & Симметрия): Вейвлет не привязан к локальному интервалу, а распределен по спектру как волна вероятности в КМ. Вклады всех симметричных траекторий взаимокомпенсируются в ноль. Мир и время — локальная асимметрия нулевого вакуумного фона.
        
        5. ОТ РАСШИРЕНИЯ К ПРОСТРАНСТВУ МАСШТАБОВ: Снимает парадокс «динамически расширяющегося» кольца. Кольца всех масштабов существуют одновременно как спектральная геометрия, не требуя внешней временной оси. Радиус — это координата масштаба. Динамика расширения в реальности является геометрической проекцией переходов наблюдателя между соразмерными масштабами.
    """.trimIndent()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("НОВЫЙ СИНТЕЗ & САММЭРИ", color = Color.White, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        Text("Квантовые вейвлеты, Нулевой Фон и Пространство Масштабов", style = MaterialTheme.typography.labelSmall.copy(color = Color.LightGray))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))
            )
        },
        containerColor = Color(0xFF0F172A)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            
            // --- SECTION 1: QUANTUM WAVELET ---
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .border(2.dp, Color(0xFF00BCD4).copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color(0xFF00BCD4).copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Waves, contentDescription = "Квантовые волны", tint = Color(0xFF00BCD4), modifier = Modifier.size(28.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "СПЕКТРАЛЬНАЯ СУПЕРПОЗИЦИЯ",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Квантовые вейвлеты и темпоральная интерференция",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF00BCD4)
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF0B0F19))
                            .border(1.dp, Color(0xFF00BCD4).copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                    ) {
                        NodeVisualization(nodeId = "zero_symmetry", accentColor = Color(0xFF00BCD4))
                        
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp)
                                .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "S(x,t) = Σ W(x,t) -> 0 (СИММЕТРИЯ)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color(0xFF00BCD4),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    
                    val isConceptPlaying = isPlayingGlobal && runningTtsId == "CONCEPT_SPEECH"
                    Button(
                        onClick = { 
                            playSpeech("CONCEPT_SPEECH", "Спектральная суперпозиция и квантовые вейвлеты. " + introductionText)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4)),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = if (isConceptPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                            contentDescription = "Озвучить концепт"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isConceptPlaying) "ОСТАНОВИТЬ ОЗВУЧКУ" else "СЛУШАТЬ КВАНТОВЫЙ ВЕЙВЛЕТ",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }

            // Paragraphs for Quantum Wavelet
            listOf(
                "Отказываемся от скрытого предположения, которое мы всё время делали: что вейвлет — это связный локальный участок спектра.\n\nЕсли убрать это ограничение, то вейвлет становится ближе не к окну, а к мере на спектре.",
                "То есть вместо:\n\nS(x) != 0 только на непрерывном интервале,\n\nмы допускаем:\n\nS(x) != 0 на множестве разрозненных точек:\n\nx x x x x x x x x x x\n1 0 0 1 0 1 0 0 0 1\n\nили даже:\n\n1 0 0 1 0 1 0 0 1 0 0 0 1 ...\n\nТогда вейвлет уже не «занимает место». Он становится шаблоном выборки.",
                "Тут появляется очень сильная аналогия с квантовой механикой.\n\nЭлектрон не обязан быть локализованным в одной точке. Его волновая функция может иметь поддержку сразу в нескольких областях пространства.\n\nТочно так же твой вейвлет может одновременно содержать кванты, принадлежащие очень удалённым областям окружности. Тогда несколько вейвлетов действительно могут пересекаться на одном и том же кванте. В этом смысле они больше похожи на волновые моды или бозонные состояния, чем на локализованные частицы.",
                "Теперь про количество таких вейвлетов. Реальность предлагает три режима:\n\n1. Конечное число:\nЕсли окружность содержит N квантов и каждый вейвлет задаётся выбором подмножества, число возможных вейвлетов 2^N. Оно огромно, но конечно.\n\n2. Счётно бесконечное:\nЕсли квантов бесконечно много, но допускаются только их конечные комбинации.\n\n3. Континуум:\nЕсли разрешить произвольные распределения амплитуд W(x) in R, то число возможных вейвлетов становится несчётным, по мощности равным множеству всех функций.",
                "Теперь рассмотрим путь вейвлета.\n\nМы раньше представляли его как движение объекта. Но если вейвлет — это распределённый шаблон квантов, то его «путь» задаётся пространственно-временной функцией W(x,t).\n\nКаждая возможная эволюция становится элементом пространства вещественных функций {W_i(x,t)}.",
                "Что будет, если сложить абсолютно все возможные пути?\n\nS(x,t) = sum_i W_i(x,t)\n\nЕсли пространство путей симметрично, то многие вклады взаимно уничтожатся. Это в точности повторяет интеграл по траекториям Фейнмана. Отдельная траектория не наблюдается — наблюдается сумма всех траекторий. Взаимно компенсируясь, противоположные функции f(x) и -f(x) дают в сумме ноль. Сумма всего пространства симметричных возможностей действительно стремится к абсолютному нулю.",
                "И вот тут возникает неожиданная связь с фразой:\n\n«Ноль — это бесконечность, а бесконечность — это ноль».\n\nВ обычной абстрактной математике это неверно, но как структурная идея — встречается повсеместно. Среднее значение полного гармонического спектра равно нулю, сумма противоположных мод даёт ноль, квантовый вакуум выглядит как пустота, хотя вмещает бесконечное число мод, а белый шум имеет нулевое среднее, несмотря на бесконечную полноту состояний. Ноль — это не пустое отсутствие структуры, а её совершенная, полная симметрия.",
                "В терминах частотной космологии можно сформулировать почти афоризм:\n\n«Наблюдаемая история — это не отдельный путь по спектру. Она является локальным нарушением симметрии пространства всех возможных путей.»\n\nВсе возможные пути вместе создают нулевой компенсированный квантовый фон; отдельный же наблюдатель выделяет небольшую несбалансированную область этого фона. Именно эта асимметрия переживается как стрела времени, история и причинность. Бесконечный компенсирующийся спектр выглядит как «ничто», а наблюдаемый мир рождается как локальный резонанс внутри этого компенсированного пространства."
            ).forEach { txt ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF151F32)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Text(
                        text = txt,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White.copy(alpha = 0.9f),
                            lineHeight = 22.sp,
                            fontSize = 14.sp
                        ),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- SECTION 2: SCALE SPACE (NEW PARADIGM SHIFT) ---
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .border(2.dp, Color(0xFFFF9800).copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color(0xFFFF9800).copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Waves, contentDescription = "Пространство масштабов", tint = Color(0xFFFF9800), modifier = Modifier.size(28.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "ПРОСТРАНСТВО МАСШТАБОВ",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "От расширения окружности к геометрии уровней",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFF9800)
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF0B0F19))
                            .border(1.dp, Color(0xFFFF9800).copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                    ) {
                        // Rendering our brand new custom ScaleSpaceViz
                        NodeVisualization(nodeId = "scale_space", accentColor = Color(0xFFFF9800))
                        
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp)
                                .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "СЕМЕЙСТВО КОЛЕЦ СОВМЕСТНОГО БЫТИЯ",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color(0xFFFF9800),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    
                    val isScaleSpacePlaying = isPlayingGlobal && runningTtsId == "SCALE_SPACE_SPEECH"
                    Button(
                        onClick = { 
                            playSpeech("SCALE_SPACE_SPEECH", "От расширяющейся окружности к пространству масштабов. " + scaleSpaceText)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = if (isScaleSpacePlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                            contentDescription = "Озвучить Пространство Масштабов"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isScaleSpacePlaying) "ОСТАНОВИТЬ ОЗВУЧКУ" else "СЛУШАТЬ ПРОСТРАНСТВО МАСШТАБОВ",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }

            // Paragraphs for Scale Space
            listOf(
                "Одной из скрытых проблем многих моделей времени является то, что они пытаются объяснить время, уже предполагая существование времени.\n\nИменно с этой трудностью мы сталкиваемся, когда говорим о «расширяющемся кольце состояний». Если между квантами окружности постоянно появляются новые кванты, возникает вопрос: относительно чего происходит это появление? Само понятие роста уже предполагает внешнюю временную ось, а значит, мы незаметно вводим то, что пытаемся объяснить.",
                "Более последовательным оказывается другой подход.\n\nВместо того чтобы рассматривать кольцо как объект, который расширяется со временем, можно считать, что существует сразу целое семейство колец различных размеров. Тогда радиус кольца становится не функцией времени, а самостоятельной координатой. Мы имеем дело не с процессом расширения, а со структурой масштабов.",
                "В такой картине кольца различных размеров существуют одновременно.\n\nНет необходимости говорить, что одно кольцо возникло раньше другого или что одно расширилось в другое. Между ними существуют лишь отношения отображения.\n\nКаждое кольцо может проецироваться на соседние кольца, содержать информацию о них или, наоборот, представлявать их в более грубом масштабе. То, что раньше выглядело как динамика, превращается в геометрию связей между уровнями описания.",
                "Здесь появляется важная аналогия с вейвлетным анализом. Вейвлеты позволяют описывать сигнал сразу на множестве масштабов.\n\nПри этом разные масштабы существуют одновременно и не требуют отдельного времени для своего возникновения. Сигнал уже содержит всю иерархию масштабов, а наблюдатель лишь выбирает, на каком уровне его рассматривать. Возможно, нечто подобное происходит и с реальностью.",
                "Особенно интересной становится идея «выворачивания» колец.\n\nЕсли структура самоподобна, становится трудно определить, какое кольцо является внутренним, а какое внешним. Каждое кольцо может быть представлено через другие кольца. В пределе возникает ситуация, в которой понятия центра и периферии теряют абсолютный смысл. Любой масштаб может рассматриваться как центральный относительно собственного уровня описания.",
                "В таком случае бесконечная прямая, возникающая после мысленного разрезания кольца, приобретает новое значение.\n\nОна перестаёт быть просто геометрической линией. Скорее, она становится способом кодирования всей иерархии масштабов в одномерную последовательность. Каждая точка на этой линии соответствует определённому паттерну состояний, а каждый участок — определённому уровню организации структуры.",
                "Это приводит к радикальному пересмотру понятия времени.\n\nЕсли различные масштабы существуют одновременно, то наблюдаемое течение времени может оказаться не фундаментальным процессом, а локальной интерпретацией переходов между масштабами.\n\nНаблюдатель воспринимает часть многомасштабной структуры и интерпретирует её как последовательность событий. Время становится не самостоятельной сущностью, а способом чтения отношений между уровнями организации.",
                "С этой точки зрения кольца, прямая и вейвлеты оказываются тремя различными представлениями одной и той же реальности.\n\nКольца описывают её как систему взаимосвязанных масштабов. Прямая представляет ту же структуру в сериализованном виде. Вейвлеты выступают локальными окнами наблюдения, через которые осуществляется чтение этой структуры. Тогда расширение превращается в смену масштаба, а динамика — в проекцию более глубокой многомасштабной геометрии.",
                "Возможно, именно здесь находится мост между идеями времени, наблюдения и голографии. То, что кажется нам историей событий, может быть не движением мира через время, а последовательным декодированием уже существующей структуры масштабов. В этом случае реальность оказывается ближе не к разворачивающемуся процессу, а к сложной спектральной архитектуре, внутри которой наблюдатель прокладывает собственную траекторию чтения."
            ).forEach { txt ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1F1F35)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Text(
                        text = txt,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White.copy(alpha = 0.9f),
                            lineHeight = 22.sp,
                            fontSize = 14.sp
                        ),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- SECTION 3: THE ULTIMATE SUMMARY ---
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2D37)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .border(2.dp, Color(0xFFE040FB), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Интегральный синтез пяти сфер",
                            tint = Color(0xFFE040FB),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "ОБЩЕЕ ИНТЕГРАЛЬНОЕ САММЭРИ",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    
                    Text(
                        text = "СИНТЕЗ ПЯТИ СТОЛПОВ ЧАСТОТНОЙ КОСМОЛОГИИ:",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFFE040FB),
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "1. **Базовый обзор (Время как Сериализация)**: Раскрывает фундаментальный сдвиг парадигмы. Пространственное существование многомерно и статично; время возникает не само по себе, а как побочный продукт последовательного считывания (сериализации) этой спектральной структуры сознанием наблюдателя.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.LightGray, lineHeight = 20.sp, fontSize = 13.sp),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text(
                        text = "2. **Развернутое Эссе (Наблюдатель-Вейвлет и Гармоники)**: Уточняет конструкцию наблюдателя. Он не точка, а локальный вейвлет — частотно-временное фильтрующее окно памяти. Это окно вложено в космическую иерархию гармоник (надвейвлеты), образуя фрактальное самоподобное дерево масштабов реальности.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.LightGray, lineHeight = 20.sp, fontSize = 13.sp),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text(
                        text = "3. **Манифест и Контекст (Физика и Унитарность)**: Дает строгую физическую прописку модели. Голографический спектральный предел энтропии Бекенштейна ограничивает плотность упаковки информации в вейвлете, а фрактальная унитарность переходов на расширяющемся Кольце Состояний удерживает вероятности от коллапса.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.LightGray, lineHeight = 20.sp, fontSize = 13.sp),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text(
                        text = "4. **Квантовый Свободный Вейвлет & Нулевой Фон (Темпоральная Интерференция)**: Завершает интеграцию. Снимает принудительную локальность. Вейвлет — это пространственно-распределенный квантовый шаблон. Путь вейвлета — это бесконечное поле путей Фейнмана, которое при сложении дает абсолютный симметричный ноль. Наш материальный мир, история и течение времени — не первичная ткань бытия, а микроскопическая локальная асимметрия, благословенная флуктуация ума на фоне бесконечной симметричной пустоты вакуума.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.LightGray, lineHeight = 20.sp, fontSize = 13.sp),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text(
                        text = "5. **Пространство масштабов (Переход от Расширяющейся Окружности)**: Разрешает главный парадокс времени. Расширение кольца состояний не происходит во внешнем времени. Вместо этого все масштабы и размеры колец сосуществуют одновременно как статичная геометрия связей и отображений между уровнями описания. Время оказывается локальной интерпретацией переходов наблюдателя между этими масштабами. Динамика превращается в геометрию спектральной архитектуры.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, lineHeight = 20.sp, fontSize = 13.sp),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val isSummaryPlaying = isPlayingGlobal && runningTtsId == "SUMMARY_SPEECH"
                    Button(
                        onClick = { 
                            playSpeech("SUMMARY_SPEECH", "Синтез пяти столпов частотной космологии. " + synthesisSummaryText)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE040FB)),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = if (isSummaryPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                            contentDescription = "Слушать Саммэри"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isSummaryPlaying) "ОСТАНОВИТЬ САММЭРИ" else "СЛУШАТЬ ОБЩИЙ СИНТЕЗ ПЯТИ НАПРАВЛЕНИЙ",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(84.dp))
        }
    }
}
