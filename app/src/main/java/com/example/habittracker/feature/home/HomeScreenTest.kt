package com.example.habittracker.feature.home

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.habittracker.core.ui.theme.BrandBlueGradient
import com.example.habittracker.core.ui.theme.RoutinerTheme

data class TestHabitUi(
    val id: String,
    val title: String,
    val progressLabel: String,   // "500/2000 ML"
    val percent: Int,            // 25
    val streakPlus: Int,         // +3
    val isDone: Boolean = false
)

@Composable
fun HomeScreenTest(
    habits: List<TestHabitUi>,
    onHabitClick: (String) -> Unit,
    onDone: (String) -> Unit,
    onSkip: (String) -> Unit,
    onFail: (String) -> Unit,
    onAddHabit: () -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddHabit) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
        ) {
            HomeTopBar()

            Spacer(Modifier.height(16.dp))

            // Quick stats / shortcuts area (figmada odatda yuqorida bo‘ladi)
            HomeHighlightsRow()

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Today",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(12.dp))

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(habits, key = { it.id }) { habit ->
                    TestHabitUiHabitCard(
                        habit = habit,
                        onClick = { onHabitClick(habit.id) },
                        onDone = { onDone(habit.id) },
                        onSkip = { onSkip(habit.id) },
                        onFail = { onFail(habit.id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Routiner",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Start changing your life for good",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f)
            )
        }

        IconButton(onClick = { /* notifications */ }) {
            Icon(Icons.Default.Notifications, contentDescription = "Notifications")
        }
    }
}

@Composable
private fun HomeHighlightsRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        GradientInfoCard(
            modifier = Modifier.weight(1f),
            title = "Streak",
            value = "12 days"
        )
        GradientInfoCard(
            modifier = Modifier.weight(1f),
            title = "Mood",
            value = "😊 Good"
        )
    }
}

@Composable
private fun GradientInfoCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String
) {
    Box(
        modifier = modifier
            .height(86.dp)
            .clip(MaterialTheme.shapes.large)
            .background(BrandBlueGradient) // avval theme’da qilgan gradient
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.85f)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
        }
    }
}

@Composable
private fun TestHabitUiHabitCard(
    habit: TestHabitUi,
    onClick: () -> Unit,
    onDone: () -> Unit,
    onSkip: () -> Unit,
    onFail: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // title row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = habit.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                PercentPill(percent = habit.percent)
            }

            Spacer(Modifier.height(10.dp))

            // progress
            LinearProgressIndicator(
                progress = (habit.percent / 100f).coerceIn(0f, 1f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(99.dp))
            )

            Spacer(Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = habit.progressLabel,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                    modifier = Modifier.weight(1f)
                )

                StreakPlusPill(plus = habit.streakPlus)
            }

            Spacer(Modifier.height(14.dp))

            // actions: Fail / Skip / Done
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ActionChip(text = "Fail", onClick = onFail, kind = ChipKind.Negative)
                ActionChip(text = "Skip", onClick = onSkip, kind = ChipKind.Neutral)
                ActionChip(text = if (habit.isDone) "Done" else "View Done", onClick = onDone, kind = ChipKind.Positive, modifier = Modifier.weight(1f))
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
private fun StreakPlusPill(plus: Int) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(99.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = "+$plus",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

private enum class ChipKind { Positive, Neutral, Negative }

@Composable
private fun ActionChip(
    text: String,
    onClick: () -> Unit,
    kind: ChipKind,
    modifier: Modifier = Modifier
) {
    val bg = when (kind) {
        ChipKind.Positive -> MaterialTheme.colorScheme.primary
        ChipKind.Neutral -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f)
        ChipKind.Negative -> MaterialTheme.colorScheme.error.copy(alpha = 0.12f)
    }
    val fg = when (kind) {
        ChipKind.Positive -> MaterialTheme.colorScheme.onPrimary
        ChipKind.Neutral -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
        ChipKind.Negative -> MaterialTheme.colorScheme.error
    }

    Box(
        modifier = modifier
            .height(36.dp)
            .clip(RoundedCornerShape(99.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, style = MaterialTheme.typography.bodyMedium, color = fg)
    }
}

@Preview(showBackground = true)
@Composable
private fun HomePreview() {
    RoutinerTheme {
        val habits = listOf(
            TestHabitUi("1", "Drink the water", "500/2000 ML", 25, 3),
            TestHabitUi("2", "Water Plants", "0/1 TIMES", 0, 1),
            TestHabitUi("3", "Read 20 pages", "10/20 PAGES", 50, 2, isDone = true),
        )

        HomeScreenTest(
            habits = habits,
            onHabitClick = {},
            onDone = {},
            onSkip = {},
            onFail = {},
            onAddHabit = {}
        )
    }
}