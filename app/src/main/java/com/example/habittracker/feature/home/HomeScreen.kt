package com.example.habittracker.feature.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.habittracker.core.ui.theme.BrandBlue
import com.example.habittracker.core.ui.theme.BrandBlueGradient
import kotlinx.coroutines.launch

enum class HomeTab { TODAY, CLUBS }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RoutinerHomeScreen() {

    var tab by remember { mutableStateOf(HomeTab.TODAY) }
    val days = remember { listOf(3, 4, 5, 6, 7, 8, 9) }
    var selectedDay by remember { mutableIntStateOf(days.first()) }

    var habits by remember {
        mutableStateOf(
            listOf(
                HabitUi("1", "Drink the water", "500/2000 ML", 25, 12, false),
                HabitUi("2", "Walk", "0/10 000 STEPS", 0, 3, false),
                HabitUi("3", "Water Plants", "0/1 TIMES", 0, 7, true),
            )
        )
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var bottomTab by remember { mutableStateOf(BottomTab.HOME) }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = { HomeBottomBar(selected = bottomTab, onSelect = { bottomTab = it }) },
        floatingActionButton = {
            HomeFab(
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Added new habit")
                    }
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            stickyHeader {
                Column(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.background)
                        .fillMaxWidth()
                ) {
                    HomeHeader()
                    TodayClubsToggle(
                        selected = tab,
                        onSelect = { tab = it }
                    )
                    WeekCalendar(
                        days = days,
                        selectedDay = selectedDay,
                        onDaySelect = { selectedDay = it }
                    )
                    DailyGoalBanner()
                }
            }

            if (tab == HomeTab.TODAY) {
                item { ChallengesSection() }
                item {
                    HabitsSection(
                        habits = habits,
                        onHabitClick = { id ->
                            scope.launch { snackbarHostState.showSnackbar("Habit click: $id") }
                        },
                        onToggleDone = { id ->
                            habits = habits.map {
                                if (it.id == id) it.copy(isDoneToday = !it.isDoneToday)
                                else it
                            }
                        }
                    )
                }

            } else {
                item {
                    ClubsPlaceholder(
                        onFindClubs = {
                            scope.launch { snackbarHostState.showSnackbar("Clubs") }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Hi, Mert 👋",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Let's make habits together!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(0.6f)
            )
        }

        IconButton(onClick = { }) {
            Icon(Icons.Default.Notifications, contentDescription = null)
        }

        Spacer(Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
        )
    }
}

@Composable
private fun TodayClubsToggle(
    selected: HomeTab,
    onSelect: (HomeTab) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.onSurface.copy(0.05f))
            .padding(4.dp)
    ) {
        ToggleItem(
            text = "Today",
            selected = selected == HomeTab.TODAY,
            onClick = { onSelect(HomeTab.TODAY) },
            modifier = Modifier.weight(1f)
        )
        ToggleItem(
            text = "Clubs",
            selected = selected == HomeTab.CLUBS,
            onClick = { onSelect(HomeTab.CLUBS) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ToggleItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(
                if (selected) BrandBlue else Color.Transparent
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun WeekCalendar(
    days: List<Int>,
    selectedDay: Int,
    onDaySelect: (Int) -> Unit
) {

    LazyRow(
        modifier = Modifier.padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 20.dp)
    ) {
        items(days) { day ->
            DateItem(day = day, selected = day == selectedDay, onClick = { onDaySelect(day) })
        }
    }
}

@Composable
private fun DateItem(day: Int, selected: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(48.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (selected) BrandBlue.copy(0.1f)
                else MaterialTheme.colorScheme.surface
            )
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(day.toString(), style = MaterialTheme.typography.titleMedium, color = BrandBlue)
    }
}

@Composable
private fun DailyGoalBanner() {
    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(BrandBlueGradient)
            .padding(16.dp)
    ) {
        Text(
            text = "Your daily goals almost done 🔥",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

enum class BottomTab { HOME, STATS, PROFILE }

@Composable
private fun HomeBottomBar(
    selected: BottomTab,
    onSelect: (BottomTab) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = selected == BottomTab.HOME,
            onClick = { onSelect(BottomTab.HOME) },
            icon = { Icon(Icons.Default.Home, null) }
        )
        NavigationBarItem(
            selected = selected == BottomTab.STATS,
            onClick = { onSelect(BottomTab.STATS) },
            icon = { Icon(Icons.Outlined.BarChart, null) }
        )
        NavigationBarItem(
            selected = selected == BottomTab.PROFILE,
            onClick = { onSelect(BottomTab.PROFILE) },
            icon = { Icon(Icons.Default.Person, null) }
        )
    }
}

@Composable
fun HomeFab(
    onClick: () -> Unit = {}
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primary,
        shape = CircleShape
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Habit",
            tint = Color.White
        )
    }
}

@Composable
fun ChallengesSection() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {

        // Title row
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Challenges",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "View All",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Challenge Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    text = "Best Runners 🏃",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "5 days left",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
                )
            }
        }
    }
}

@Composable
fun HabitsSection(
    habits: List<HabitUi>,
    onHabitClick: (String) -> Unit,
    onToggleDone: (String) -> Unit,
    modifier: Modifier = Modifier
) {
//    val habits = listOf(
//        HabitUi("1", "Drink the water", "500/2000 ML", 25, 12, false),
//        HabitUi("2", "Walk", "0/10 000 STEPS", 0, 3, false),
//        HabitUi("3", "Water Plants", "0/1 TIMES", 0, 7, true),
//    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Habits",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            Text(
                "View All",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    //TODO view all habits
                })
        }

        Spacer(Modifier.height(12.dp))

        habits.forEach { habit ->
            HabitCard(
                habit = habit,
                onClick = { onHabitClick(habit.id) },
                onToggleDone = { onToggleDone(habit.id) }
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
fun HabitCard(
    habit: HabitUi,
    onClick: () -> Unit,
    onToggleDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left icon bubble
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.WaterDrop,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = habit.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = habit.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    maxLines = 1
                )

                Spacer(Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "${habit.streakDays} days streak",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(horizontalAlignment = Alignment.End) {
                PercentPill(percent = habit.percent)

                Spacer(Modifier.height(10.dp))

                DoneToggle(
                    checked = habit.isDoneToday,
                    onCheckedChange = { onToggleDone() }
                )
            }
        }
    }
}

@Composable
private fun PercentPill(percent: Int) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(99.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.10f))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = "%$percent",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun DoneToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .height(34.dp)
            .clip(RoundedCornerShape(99.dp))
            .background(
                if (checked) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f)
            )
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (checked) "Done" else "Mark",
            style = MaterialTheme.typography.bodyMedium,
            color = if (checked) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
        )
    }
}

@Composable
fun ClubsPlaceholder(onFindClubs: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Text("Clubs", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(6.dp))
        Text(
            "This section will show communities and habit buddies.",
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(Modifier.height(12.dp))
        Button(onClick = onFindClubs) { Text("Find clubs") }
    }
}

data class HabitUi(
    val id: String,
    val title: String,
    val subtitle: String,
    val percent: Int,
    val streakDays: Int,
    val isDoneToday: Boolean,
)